package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.pdf;

import freemarker.template.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FreemarkerTemplate implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(FreemarkerTemplate.class);

    private final String templateSrc;
    private final Configuration configuration;


    protected static final Version freemarkerVersion = new Version(2, 3, 22);
    private static final Configuration defaultFreemarkerConfiguration = createFreemarkerConfiguration();

    public FreemarkerTemplate(String templateSrc) {
        this(templateSrc, defaultFreemarkerConfiguration);
    }

    public FreemarkerTemplate(String templateSrc, Configuration configuration) {
        this.templateSrc = templateSrc;
        this.configuration = configuration;
    }

    protected static Configuration createFreemarkerConfiguration() {
        Configuration freemarkerConfiguration = new Configuration(freemarkerVersion);

        // use class based file loading
        freemarkerConfiguration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        freemarkerConfiguration.setObjectWrapper(new DefaultObjectWrapper(freemarkerVersion));
        return freemarkerConfiguration;
    }

    /**
     * Renders the template using the given parameters as variables.
     *
     * @param parameters variables for template rendering
     * @return rendered template
     */
    public String render(Map<String, Object> parameters) {
        logger.debug("rendering templateSrc {} with parameters {}", templateSrc, parameters);
        try (StringWriter stringWriter = new StringWriter()) {
            Template freemarkerTemplate = new Template("template", templateSrc, configuration);

            // render
            freemarkerTemplate.process(parameters, stringWriter);
            stringWriter.flush();
            String renderedStr = stringWriter.toString();
            logger.debug("Rendered result: {}", renderedStr);
            return renderedStr;
        } catch (TemplateException | IOException e) {
            throw new FreemarkerTemplateException(e);
        }
    }

    public String getTemplateSrc() {
        return templateSrc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FreemarkerTemplate that = (FreemarkerTemplate) o;

        return templateSrc != null ? templateSrc.equals(that.templateSrc) : that.templateSrc == null;
    }

    @Override
    public int hashCode() {
        return templateSrc != null ? templateSrc.hashCode() : 0;
    }


    public static FreemarkerTemplate from(InputStream sourceStream, Charset charset) {
        StringWriter sw = new StringWriter();
        try {
            IOUtils.copy(sourceStream, sw, charset);
        } catch (IOException e) {
            throw new FreemarkerTemplateException(e);
        }

        return new FreemarkerTemplate(sw.toString());
    }

    public static FreemarkerTemplate from(InputStream sourceStream) {
        return from(sourceStream, StandardCharsets.UTF_8);
    }


    public static FreemarkerTemplate from(URL url) {
        try (InputStream is = url.openStream()) {
            return FreemarkerTemplate.from(is);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not find requested template " + url, e);
        }
    }

    public static class FreemarkerTemplateException extends RuntimeException {
        public FreemarkerTemplateException(Throwable e) {
            super(e);
        }
    }

}
