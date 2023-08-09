package com.esl.internship.staffsync.employee.management.service.response;

public class Response<T> {
    private T value;
    private Error error;

    public Response(T value) {
        this.value = value;
        this.error = new Error();
    }

    public Response() {
        this.value = null;
        this.error = new Error();
    }

    public boolean requestHasErrors() {
        return this.error.isPresent();
    }

    public String getErrorsHasJsonString() {
        return this.error.toJsonString();
    }

    public Response<T> putError(String attribute, String message) {
        this.error.put(attribute, message);
        return this;
    }

    public T getValue() {
        return  this.value;
    }

    public Response<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public boolean hasValue() {
        return this.value == null;
    }

}
