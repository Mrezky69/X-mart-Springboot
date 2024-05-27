package com.project.spring.util;

import java.util.*;
import org.springframework.http.*;

public class ResponseHandler {
    private ResponseHandler() {
        throw new IllegalStateException("Utility class");
    }

    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj,
                                                          HttpHeaders headers) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("data", responseObj);
        return new ResponseEntity<>(map, headers, status);
    }

    public static ResponseEntity<Object> generateResponse(String message,
                                                          HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("data", responseObj);
        return new ResponseEntity<>(map, status);
    }

    public static ResponseEntity<Object> generateResponse(String message,
                                                          HttpStatus status) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        return new ResponseEntity<>(map, status);
    }

}
