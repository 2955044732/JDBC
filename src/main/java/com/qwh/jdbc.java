package com.qwh;


import com.mysql.cj.jdbc.Driver;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * @auther qwh
 * @create 2023-06-2023/6/1 10:11
 */

public class jdbc {
    @Test
    public void getConnect1() throws Exception {
        Driver driver = new Driver();
        String url = "jdbc:mysql://localhost:3306/test";
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","123123");
        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }
    @Test
    public void getConnect2() throws Exception {
        //反射获取驱动对象
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        //反射创建驱动
        Driver driver = (Driver) clazz.newInstance();
        String url = "jdbc:mysql://localhost:3306/test";
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","123123");
        Connection connect = driver.connect(url, info);
        System.out.println(connect);

    }
    @Test
    public void getConnect3() throws Exception {
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123123";

        //注册驱动
//        DriverManager.registerDriver(driver);-->
//        static {
//            try {
//                DriverManager.registerDriver(new Driver());
//            } catch (SQLException var1) {
//                throw new RuntimeException("Can't register driver!");
//            }
//        }
        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println(connection);

    }

    @Test
    public void getConnect4() throws Exception {
        //获取系统配置文件流
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        //加载配置流
        pros.load(is);
        String diver = pros.getProperty("driverClass");
        String url = pros.getProperty("url");
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        //加载驱动
        Class.forName(diver);
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

}
