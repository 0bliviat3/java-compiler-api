package com.compiler.wan.api.config;

public enum ExecutionStatus {
    SUCCESS,
    COMPILE_ERROR,
    SERVER_ERROR,
    EXCEPTION;

    public static boolean isERROR(ExecutionStatus status) {
        return status != null && (status.equals(COMPILE_ERROR) || status.equals(SERVER_ERROR));
    }
}
