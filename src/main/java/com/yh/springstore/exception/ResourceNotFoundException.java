package com.yh.springstore.exception;

public class ResourceNotFoundException extends RuntimeException{
    String resource;
    String field;
    Long fieldId;
    String fieldName;
    
    public ResourceNotFoundException(String resource, String field, Long fieldId) {
        super("ResourceNotFoundException :: " + resource + " with " + field + ": " + fieldId + " is not Found!");
        this.resource = resource;
        this.field = field;
        this.fieldId = fieldId;
    }
    public ResourceNotFoundException(String resource, String field, String fieldName) {
        super("ResourceNotFoundException :: " + resource + " with " + field + ": " + fieldName + " is not Found!");
        this.resource = resource;
        this.field = field;
        this.fieldName = fieldName;
    }

}
