package com.stepanov.uocns.common.handling.exceptions;

public class InternalErrorException extends CommonException {

    public InternalErrorException(String message) {
        super(message);
    }

    @Override
    public String staticMessage(){
        return getMessage() == null || getMessage().isEmpty() ? "internal_error" : getMessage();
    }
}
