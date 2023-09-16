package com.dochub.common;

public class Result<T> {
    private T data;
    private boolean success;
    private int code;
    private String[] messages;

    public Result(String[] messages) {
        this.messages = messages;
    }

    public T getData() { return data; }
    public void setData(T value) { this.data = value; }

    public boolean getSuccess() { return success; }
    public void setSuccess(boolean value) { this.success = value; }

    public long getCode() { return code; }
    public void setCode(int value) { this.code = value; }

    public String[] getMessages() { return messages; }
    public void setMessages(String[] value) { this.messages = value; }
}
