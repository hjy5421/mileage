package com.triple.mileage.dto;

public class Result {
    enum ResultCode {SUCCESS, CLIENTERROR, SERVERERROR}

    private ResultCode resultCode;
    private String message;
    private Object data;

    private Result(ResultCode resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public Result(ResultCode resultCode, String message, Object data) {
        this.resultCode = resultCode;
        this.message = message;
        this.data = data;
    }

    public static Result success() {
        return new Result(ResultCode.SUCCESS, "OK");
    }

    public static Result success(Object data) {
        return new Result(ResultCode.SUCCESS, "OK", data);
    }

    public static Result clientError(String message) {
        return new Result(ResultCode.CLIENTERROR, message);
    }

    public static Result serverError(String message) {
        return new Result(ResultCode.SERVERERROR, message);
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
