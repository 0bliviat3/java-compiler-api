package com.compiler.wan.api.config;

public enum JCompileConfig {
    DEFAULT_PATH("tmp"),
    DICT_PREFIX("tmp_"),
    MAIN_FILE("Main.java"),
    JAVA("java"),
    XMX("-Xmx%dm"),
    DEFAULT_MEMORY("256"),
    CP("-cp"),
    MAIN("Main"),
    DEFAULT_TIME("3");

    private final String value;

    JCompileConfig(final String value) {
        this.value = value;
    }

    public String getVal() {
        return value;
    }
}
