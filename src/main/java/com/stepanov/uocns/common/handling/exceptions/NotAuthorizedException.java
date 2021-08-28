package com.stepanov.uocns.common.handling.exceptions;

public class NotAuthorizedException extends CommonException {

    public NotAuthorizedException() {
        super("");
    }

    @Override
    public String staticMessage(){
        return getMessage() == null || getMessage().isEmpty() ? "internal_error" : getMessage();
    }
}
