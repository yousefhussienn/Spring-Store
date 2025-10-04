package com.yh.springstore.exception;

public class ResourceNotFoundException extends RuntimeException{
    String resourceName;
    String fieldName;
    Long fieldLongValue;
    String fieldStringValue;
    
    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldLongValue) {
        super("ResourceNotFoundException :: " + resourceName + " with " + fieldName + ": " + fieldLongValue + " is not Found!");
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldLongValue = fieldLongValue;
    }
    public ResourceNotFoundException(String resourceName, String fieldName, String fieldStringValue) {
        super("ResourceNotFoundException :: " + resourceName + " with " + fieldName + ": " + fieldStringValue + " is not Found!");
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldStringValue = fieldStringValue;
    }

}
