package com.esl.internship.staffsync.employee.management.service.response;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

public class Error {
    private ObjectNode node;

    public Error() {
        node = Json.newObject();
    }

    public Error put(String attribute, String message) {
        node.put(attribute, message);
        return this;
    }

    public boolean isPresent() {
        return node.isEmpty();
    }

    public String toJsonString() {
        return node.toString();
    }
}
