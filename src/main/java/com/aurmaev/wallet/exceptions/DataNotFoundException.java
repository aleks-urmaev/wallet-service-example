package com.aurmaev.wallet.exceptions;

public class DataNotFoundException extends RuntimeException {
    public static final String ACCOUNT_NOT_FOUND = "Account not found. Account ID: {0}.";

    public DataNotFoundException(String message) {
        super(message);
    }
}
