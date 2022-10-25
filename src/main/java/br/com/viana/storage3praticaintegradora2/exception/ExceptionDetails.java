package br.com.viana.storage3praticaintegradora2.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ExceptionDetails {
    private String title;
    private String message;
    private int status;
    private LocalDateTime timestamp;
}
