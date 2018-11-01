package com.malsolo.pact.test.boot.consumer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Produced not found")
public class NotFoundException extends RuntimeException {
}
