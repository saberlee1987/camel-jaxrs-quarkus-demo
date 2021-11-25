package com.example.dto;

import java.util.Objects;

public class HelloDto {
    private String message;
    private String text;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HelloDto helloDto = (HelloDto) o;
        return Objects.equals(message, helloDto.message) && Objects.equals(text, helloDto.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, text);
    }

    @Override
    public String toString() {
        return "HelloDto{" +
                "message='" + message + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
