package com.stepanov.uocns.common.handling.exceptions;

public class NotAllowedException extends CommonException {

    public NotAllowedException() {
        super("Запрещено");
    }

    @Override
    public String staticMessage(){
        return "not_allowed_error";
    }
}
