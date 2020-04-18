package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.pdf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@Service
public class HtmlToPdfTransformer {

    private static final Logger logger = LoggerFactory.getLogger(HtmlToPdfTransformer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient;

    @Value("${transformHtmlToPdf.engine}")
    private String engine;
    @Value("${transformHtmlToPdf.baseUri}")
    private String baseUri;
    @Value("${transformHtmlToPdf.userName}")
    private String userName;
    @Value("${transformHtmlToPdf.password}")
    private String password;


    public HtmlToPdfTransformer() {
        this.httpClient = buildHttpClient();
    }

    protected HttpClient buildHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password.toCharArray());
                    }
                })
                .build();
    }

    public Binary transformToPdf(String html) {
        try {
            PdfGenerationConfig pdfGenerationConfig = new PdfGenerationConfig();
            pdfGenerationConfig.setFileName(UUID.randomUUID().toString());
            pdfGenerationConfig.setHtml(html);
            pdfGenerationConfig.setZoom(1.2f);
            pdfGenerationConfig.setDesiredCapabilities(new PdfGenerationConfig.DesiredCapabilities(engine));

            String requestBody = toJson(pdfGenerationConfig);

            HttpRequest request = buildBaseRequest()
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                    .thenApply(logResponse())
                    .thenApply(failIfNotSuccessFull())
                    .thenApply(httpResponse -> new Binary(httpResponse.body()))
                    .get();

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected HttpRequest.Builder buildBaseRequest() {
        System.out.println(baseUri);
        return HttpRequest.newBuilder(URI.create(baseUri + "pdf"));
    }

    private <T> Function<HttpResponse<T>, HttpResponse<T>> logResponse() {
        return response -> {
            if (logger.isTraceEnabled() && response.sslSession().isPresent()) {
                logger.trace("Version {}", response.version());
                logger.trace("SSL Version {}", response.sslSession().get().getProtocol());
            }
            logger.debug("Received response with status code {} from {}", response.statusCode(), response.uri());
            return response;
        };
    }


    private <T> Function<HttpResponse<T>, HttpResponse<T>> failIfNotSuccessFull() {
        return response -> {
            int statusCode = response.statusCode();
            if (statusCode >= 200 && statusCode < 300) {
                return response;
            } else {
                throw new CompletionException(new IllegalStateException("Received non success status code " + statusCode + " with message " + response.body()));
            }
        };
    }
    
    private String toJson(Object object) {
        try {
            return this.objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    
}