package com.jm.demo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnVO {
    @ApiModelProperty(example = "success")
    private String result;
    @ApiModelProperty(example = "성공")
    private String message;
}
