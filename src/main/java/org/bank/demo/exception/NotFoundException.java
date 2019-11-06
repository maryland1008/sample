package org.bank.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Data Not Found")
public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 3935230281455340039L;
}

