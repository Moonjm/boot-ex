package com.jm.demo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmptyResponse extends BasicResponse {
    @ApiModelProperty(example = "성공")
    private String message;

    public EmptyResponse(String message) {
        this.message = message;
    }
}
