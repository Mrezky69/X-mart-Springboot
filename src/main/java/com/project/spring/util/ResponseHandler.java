package com.project.spring.util;

import java.util.*;
import org.springframework.http.*;
import static com.project.spring.util.ResponseMessage.*;

public class ResponseHandler {
    private ResponseHandler() {
        throw new IllegalStateException(UTILITY);
    }

    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj,
                                                          HttpHeaders headers) {
        Map<String, Object> map = new HashMap<>();
        map.put(MESSAGE, message);
        map.put(DATA, responseObj);
        return new ResponseEntity<>(map, headers, status);
    }

    public static ResponseEntity<Object> generateResponse(String message,
                                                          HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<>();
        map.put(MESSAGE, message);
        map.put(DATA, responseObj);
        return new ResponseEntity<>(map, status);
    }

    public static ResponseEntity<Object> generateResponse(String message,
                                                          HttpStatus status) {
        Map<String, Object> map = new HashMap<>();
        map.put(MESSAGE, message);
        return new ResponseEntity<>(map, status);
    }

}
