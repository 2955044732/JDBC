package com.qwh.utils;


import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @auther qwh
 * @create 2023-06-2023/6/1 12:24
 */
public class JDBCUtil {
    public static Connection getConnect(){
        Connection connection = null;
        try {
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
            connection = DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    /**
     * 关闭资源
     * @param conn
     * @param ps
     */
    public static void closeSource(Connection conn, Statement ps){

        try {
            if (ps!=null){
                ps.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            if (conn!=null)
            {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void closeSource(Connection conn, Statement ps,ResultSet rs){

        try {
            if (ps!=null){
                ps.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            if (conn!=null)
            {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            if (rs!=null)
            {
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
