package com.app.rent_manager.ResponseBody;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    public static ResponseEntity<Map<String,Object>> generateResponse(String message, org.springframework.http.HttpStatus status,
            Object responseObj) {
        Map<String, Object> response = Map.of(
                "message", message,
                "status", status.value(),
                "data", responseObj
        );


        return new ResponseEntity<Map<String,Object>>(response, status);
    }
    
    public static ResponseEntity<Map<String, Object>> generateResponse(String message, HttpStatus status, Map<String, String> errors) {
        Map<String, Object> response = Map.of(
                "message", message,
                "status", status.value(),
                "errors", errors
        );

        return new ResponseEntity<>(response, status);
    }

}
