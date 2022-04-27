package org.project.models;

import org.apache.http.client.fluent.Response;

import java.io.Serializable;

public record MyResponse(Response response, double executionTime) implements Serializable {
}
