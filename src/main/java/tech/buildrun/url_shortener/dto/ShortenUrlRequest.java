package tech.buildrun.url_shortener.dto;

import jakarta.validation.constraints.NotBlank;

public record ShortenUrlRequest(@NotBlank(message = "URL cannot be empty") String url) {

}
