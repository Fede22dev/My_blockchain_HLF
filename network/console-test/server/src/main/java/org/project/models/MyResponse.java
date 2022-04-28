package org.project.models;

import java.io.Serializable;

public record MyResponse(String response, double executionTime) implements Serializable {
}
