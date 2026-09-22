package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
//为了和前端所传递过来的数据对其
@ApiModel(description = "员" +
        "工登录时候传递的数据模型")
public class EmployeeDTO implements Serializable {
    @ApiModelProperty("主键值")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("XX")
    private String idNumber;

}
