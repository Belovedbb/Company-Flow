package com.company.go.exceptions;

public class PurchaseOrderNotStoredException extends Exception {
    public PurchaseOrderNotStoredException(String message){
        super(message);
    }
}
