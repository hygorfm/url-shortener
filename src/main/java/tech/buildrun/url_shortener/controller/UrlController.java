package tech.buildrun.url_shortener.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.buildrun.url_shortener.dto.ShortenUrlRequest;
import tech.buildrun.url_shortener.dto.ShortenUrlResponse;
import tech.buildrun.url_shortener.entity.UrlEntity;
import tech.buildrun.url_shortener.service.UrlService;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(final UrlService urlService) {
        this.urlService = urlService;
    }


    @PostMapping(value = "/shorten-url")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request,
                                                         HttpServletRequest servletRequest) {

        String id = urlService.generateUniqueCode(System.currentTimeMillis()); // Not ideal approach for multithreading (risk of collision)

        urlService.save(new UrlEntity(id, request.url(), LocalDateTime.now().plusMinutes(60)));

        var redirectUrl = servletRequest.getRequestURL().toString().replace("shorten-url", id);

        return ResponseEntity.ok(new ShortenUrlResponse(redirectUrl));
    }


    @GetMapping(value = "{id}")
    public ResponseEntity<Void> redirect(@PathVariable("id") String id) {

        var url = urlService.findById(id);

        if(url.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.get().getFullUrl()));

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

}
