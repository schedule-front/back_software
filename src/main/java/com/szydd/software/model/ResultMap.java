package com.szydd.software.model;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ResultMap extends HashMap<String, Object> {
    public ResultMap() {
    }

    public ResultMap success() {
        this.put("result", "success");
        return this;
    }

    public ResultMap fail() {
        this.put("result", "fail");
        return this;
    }

    public ResultMap code(int code) {
        this.put("code", code);
        return this;
    }

    public ResultMap message(Object message) {
        this.put("message", message);
        return this;
    }
    public ResultMap token(Object token) {
        this.put("token", token);
        return this;
    }


    public ResultMap data(Object message) {
        this.put("data", message);
        return this;
    }
}

