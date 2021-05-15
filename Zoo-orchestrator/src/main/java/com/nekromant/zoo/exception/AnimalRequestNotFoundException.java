package com.nekromant.zoo.exception;

public class AnimalRequestNotFoundException extends RuntimeException{
    public AnimalRequestNotFoundException(String animalRequestId) {
        super("Не найдена заявка c id = " + animalRequestId);
    }
}
