package com.smartosc.training.exception;

import java.util.function.Supplier;

public class MyFileNotFoundException extends RuntimeException {

    public MyFileNotFoundException(String message) {
        super(message);
    }

    public static Supplier<MyFileNotFoundException> supplier(final String fileName){
        return () -> new MyFileNotFoundException(String.format("File '%s' could not be read", fileName));
    }
}
