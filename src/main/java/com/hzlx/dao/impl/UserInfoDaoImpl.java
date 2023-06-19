package com.hzlx.dao.impl;

import com.hzlx.dao.UserInfoDao;
import com.hzlx.entity.UserInfo;
import com.hzlx.utils.BaseDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserInfoDaoImpl extends BaseDao<UserInfo> implements UserInfoDao {

    @Override
    public List<UserInfo> getUserInfoAll() {
/*        String sql="select * from t_user_info";
        return selectListForObject(sql,UserInfo.class);*/
        List<UserInfo> userInfos=new ArrayList<UserInfo>();
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            Connection connection=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/bgms?usrSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true;characterEncoding=UTF-8",
                    "root",
                    "YY5211314yy.");
            PreparedStatement preparedStatement = connection.prepareStatement("select * from t_user_info");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                UserInfo userInfo=new UserInfo();
                userInfo.setId(resultSet.getInt(1));
                userInfo.setNickName(resultSet.getString(2));
                userInfo.setUserName(resultSet.getString(3));
                userInfo.setPassword(resultSet.getString(4));
                userInfo.setTel(resultSet.getString(5));
                userInfo.setAddress(resultSet.getString(6));
                userInfo.setSex(resultSet.getInt(7));
                userInfo.setAvatar(resultSet.getString(8));
                userInfo.setCreate(resultSet.getDate(9));
                userInfo.setStatus(resultSet.getInt(10));
                userInfos.add(userInfo);
            }
            return userInfos;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
