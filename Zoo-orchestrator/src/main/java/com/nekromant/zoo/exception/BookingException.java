package com.nekromant.zoo.exception;

import com.nekromant.zoo.exception.enums.ErrorTextConstant;

public class BookingException extends RuntimeException {
    public BookingException(){
        super(ErrorTextConstant.BOOKING_EXCEPTION);
    }
}
