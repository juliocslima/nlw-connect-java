package br.com.nlw.events.service.exceptions;

public class IndicatorNotFoundException extends RuntimeException {
    public IndicatorNotFoundException(String message) {
        super(message);
    }

    public IndicatorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
