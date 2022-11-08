package com.sd2022.club.errors;

public class BadRequestException extends Exception{
    public BadRequestException(String message){
        super(message);
    }
}
