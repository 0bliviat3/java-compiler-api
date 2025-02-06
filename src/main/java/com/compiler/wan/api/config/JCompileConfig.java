package com.compiler.wan.api.config;

public enum JCompileConfig {
    DEFAULT_PATH("tmp"),
    DICT_PREFIX("tmp_"),
    MAIN_FILE("Main.java"),
    JAVA("java"),
    CP("-cp"),
    MAIN("Main");

    private final String value;

    JCompileConfig(final String value) {
        this.value = value;
    }

    public String getVal() {
        return value;
    }
}
