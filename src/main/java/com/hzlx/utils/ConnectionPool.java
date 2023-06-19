package com.hzlx.utils;

import java.sql.*;
import java.util.LinkedList;

public class ConnectionPool {
    //最小连接数
    private static int MIN_POOL_NUM;
    //最大连接数
    private static int MAX_POOL_NUM;
    private static String URL;
    private static String USER_NAME;
    private static String PASS_WORD;
    private static String DRIVER;
    //装链接的容器
    private static LinkedList<Connection> connectionpool=new LinkedList<>();
    static {
        init();
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("加载数据库驱动异常");
            e.printStackTrace();
        }
        //给连接池填充连接(参数最小连接数)
        initConnection();
    }

    /**
     * 初始化方法，给属性赋值
     */
    private static void init(){
        //先把jdbc配置文件加载到程序里
        PropertiesUtil.load("jdbc");
        //根据配置文件里的key给我们的连接池里的属性赋值
        DRIVER=PropertiesUtil.getValue("jdbc.driver");
        URL=PropertiesUtil.getValue("jdbc.url");
        USER_NAME=PropertiesUtil.getValue("jdbc.userName");
        PASS_WORD=PropertiesUtil.getValue("jdbc.password");
        //连接池相关的属性赋值
        MIN_POOL_NUM=Integer.parseInt(PropertiesUtil.getValue("jdbc.minPoolNum"));
        MAX_POOL_NUM=Integer.parseInt(PropertiesUtil.getValue("jdbc.maxPoolNum"));
    }

    /**
     * 给连接池初始化
     */
    private static void initConnection(){
        for (int rows = 0; rows < MIN_POOL_NUM; rows++) {
            try {
                //每次创建一个新的连接都往集合的尾部追加
               connectionpool.addLast(DriverManager.getConnection(URL, USER_NAME, PASS_WORD));
            } catch (SQLException e) {
                System.out.println("获取连接异常");
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取连接对象
     * @return
     */
    public static Connection getConnection(){
        //判断连接池中是否有可用连接，如果有去除最顶上的连接，如果没有创建一个返回
        if (connectionpool.size()>0){
            return connectionpool.removeFirst();
        }
        try {
            return DriverManager.getConnection(URL, USER_NAME, PASS_WORD);
        } catch (SQLException e) {
            System.out.println("获取连接异常");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 归还连接的方法
     * @param connection 还回来的连接对象
     * @return true 归还成功 false 归还失败
     */
    private static boolean returnConnection(Connection connection){
        //当前连接池集合的size大于等于配置好的最大连接数的话 说明池子满了，这个连接需要丢弃
        if (connectionpool.size()>=MAX_POOL_NUM){
            try {
                connection.close();
                return  true;
            } catch (SQLException e) {
                System.out.println("关闭连接异常");
                e.printStackTrace();
            }
        }else {
            connectionpool.addLast(connection);
            return true;
        }
        return false;
    }
    /**
     * 关闭连接，释放资源的方法
     */
    public static boolean closeAll(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet){
        if (resultSet!=null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.out.println("关闭结果集失败");
                e.printStackTrace();
            }
        }
        if (preparedStatement!=null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                System.out.println("关闭preparedStatement失败");
                e.printStackTrace();
            }
        }
        return returnConnection(connection);

    }
}
