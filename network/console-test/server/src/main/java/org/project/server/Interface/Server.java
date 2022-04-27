package org.project.server.Interface;

import java.rmi.Remote;

public interface Server extends Remote, ServerEnroll, ServerSupervisor, ServerSensors {
    String NAME = "SERVER";
    int PORT = 8000;
}
