package com.example.myapplication.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtils {

    public static void test() throws ClassNotFoundException, SQLException {

        //1、导入驱动jar包
        //2、注册驱动
        Class.forName("com.mysql.jdbc.Driver");

        //3、获取数据库的连接对象
        Connection con = DriverManager.getConnection("jdbc:mysql://106.15.94.206:3306/tumeng_db?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false", "root", "Mb886699!");

        //4、定义sql语句
        String sql = "SELECT * FROM users";

        //5、获取执行sql语句的对象
        Statement statement = con.createStatement();

        //6、执行sql并接收返回结果
        ResultSet set = statement.executeQuery(sql);

        //5、取出结果集的数据
        while (set.next()) {
            System.out.println("id = " + set.getObject("id"));
            System.out.println("name = " + set.getObject("nickname"));
            System.out.println("password = " + set.getObject("password"));
            System.out.println("email = " + set.getObject("phone"));
            System.out.println("birthday = " + set.getObject("pin"));
        }
        //6、关闭连接，释放资源
        set.close();
        statement.close();
        con.close();
    }
}
