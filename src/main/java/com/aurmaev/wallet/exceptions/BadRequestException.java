package com.aurmaev.wallet.exceptions;

public class BadRequestException extends RuntimeException {
    public static final String BALANCE_NEGATIVE_VALUE = "Rejected balance reduction. Balance got a negative value. "
            + "Account ID: {0}. Current Balance: {1}. Reduction amount: {2}";

    public BadRequestException(String message) {
        super(message);
    }
}
