package com.jm.demo.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommonResponse<T> extends BasicResponse {
    private int count;
    private T data;
    private String message;

    public CommonResponse(T data, String message) {
        this.data = data;
        this.message = message;
        if(data instanceof List) {
            this.count = ((List<?>) data).size();
        }else {
            this.count = 1;
        }
    }

    public CommonResponse(T data) {
        this.data = data;
        this.message = "성공.";
        if(data instanceof List) {
            this.count = ((List<?>) data).size();
        }else {
            this.count = 1;
        }
    }

    public CommonResponse(String message) {
        this.message = message;
    }
}
