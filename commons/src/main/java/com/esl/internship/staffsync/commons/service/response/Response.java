package com.esl.internship.staffsync.commons.service.response;

import play.libs.Json;

/**
 * @author WARITH
 * @dateCreated 09/08/2023
 * @description A custom Response class for passing data from services to controllers
 */
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

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Returns true if request has errors
     *
     * @return boolean
     */
    public boolean requestHasErrors() {
        return this.error.isPresent();
    }

    public String getErrorsAsJsonString() {
        return Json.newObject().put("error", this.error.toJsonString()).toString();
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Add an error message for an attribute
     *
     * @param attribute Name of the attribute having error
     * @param message The error message
     *
     * @return Response<T>
     */
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
