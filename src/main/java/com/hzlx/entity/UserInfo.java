package com.hzlx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements Serializable {
    private Integer id;
    private String nickName;
    private String userName;
    private String password;
    private String tel;
    private String address;
    private Integer sex;
    private String avatar;
    private Date create;
    private Integer status;


}
