package com.ruhan.accounts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by ruhandosreis on 20/11/17.
 */
public class AbstractControllerWithErrorHandling {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity handleException(Exception ex ) {
        return new ResponseEntity( "Ocorreu um erro inesperado", HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
