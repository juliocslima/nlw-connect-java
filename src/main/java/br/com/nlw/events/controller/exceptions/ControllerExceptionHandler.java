package br.com.nlw.events.controller.exceptions;

import br.com.nlw.events.service.exceptions.EventNotFoundException;
import br.com.nlw.events.service.exceptions.IndicatorNotFoundException;
import br.com.nlw.events.service.exceptions.ObjectNotFoundException;
import br.com.nlw.events.service.exceptions.SubscriptionConflictException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> ObjectNotFound(ObjectNotFoundException e,
                                                        HttpServletRequest request) {
        StandardError standardError = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.NOT_FOUND.value(),
                "Não encontrado",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardError);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<StandardError> EventNotFound(EventNotFoundException e,
                                                       HttpServletRequest request) {
        StandardError standardError = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Evento não encontrado",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
    }

    @ExceptionHandler(SubscriptionConflictException.class)
    public ResponseEntity<StandardError> EventNotFound(SubscriptionConflictException e,
                                                       HttpServletRequest request) {
        StandardError standardError = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.CONFLICT.value(),
                "Usuário já cadastrado neste evento",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(standardError);
    }

    @ExceptionHandler(IndicatorNotFoundException.class)
    public ResponseEntity<StandardError> EventNotFound(IndicatorNotFoundException e,
                                                       HttpServletRequest request) {
        StandardError standardError = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.NOT_FOUND.value(),
                "Usuário indicador não encontrado",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardError);
    }
}
