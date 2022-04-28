package org.project.models;

import java.io.Serializable;

public record MyRequest(String request, String body, String port) implements Serializable {
}
