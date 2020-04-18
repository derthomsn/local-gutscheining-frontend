package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.rest;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Coupon;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.pdf.CouponHtmlGenerator;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.pdf.HtmlToPdfTransformer;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository.CouponRepository;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.rest.dto.CouponDto;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/backoffice")
public class CouponRestController {

    @Autowired
    private HtmlToPdfTransformer htmlToPdfTransformer;

    @Autowired
    private CouponHtmlGenerator couponHtmlGenerator;

    @Autowired
    private CouponRepository couponRepository;

    @RequestMapping(path = "/coupon/{couponId}/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable String couponId) throws IOException {

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new IllegalArgumentException("Unable to locate coupon with id" + couponId));

        Binary generatedPdf = coupon.getGeneratedPdf();
        ByteArrayResource resource = new ByteArrayResource(generatedPdf.getData());

        return ResponseEntity.ok()
                .header("Content-Disposition”, “attachment; filename=Coupon.pdf")
                .contentLength(generatedPdf.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @RequestMapping(path = "/coupon/{couponId}/preview", method = RequestMethod.GET)
    public ResponseEntity<Resource> preview(@PathVariable String couponId) throws IOException {

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new IllegalArgumentException("Unable to locate coupon with id" + couponId));

        Binary generatedPdf = generateCouponPdf(coupon);
        ByteArrayResource resource = new ByteArrayResource(generatedPdf.getData());

        return ResponseEntity.ok()
                .header("Content-Disposition”, “attachment; filename=Coupon.pdf")
                .contentLength(generatedPdf.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @RequestMapping(path = "/coupon/{couponId}/previewHtml", method = RequestMethod.GET)
    public ResponseEntity<String> previewHtml(@PathVariable String couponId) throws IOException {

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new IllegalArgumentException("Unable to locate coupon with id" + couponId));

        String couponHtml = couponHtmlGenerator.generate(coupon);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(couponHtml);
    }

    private Binary generateCouponPdf(Coupon coupon) {
        try {
            String couponHtml = couponHtmlGenerator.generate(coupon);

            return htmlToPdfTransformer.transformToPdf(couponHtml);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/coupon/{couponId}")
    public CouponDto getCouponById(@PathVariable String couponId) {
        return couponRepository.findById(couponId)
                .map(CouponDto::fromCouponTemplate)
                .orElseThrow(() -> new IllegalArgumentException("Unable to locate coupon with id" + couponId));
    }

}
