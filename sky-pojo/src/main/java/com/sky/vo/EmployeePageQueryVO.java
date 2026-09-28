package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePageQueryVO {
    private String username;
    private String name;
    private String phone;
    private Integer status;
    private LocalDateTime updateTime;
}
