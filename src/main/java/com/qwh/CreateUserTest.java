package com.qwh;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Properties;

/**
 * @auther qwh
 * @create 2023-06-2023/6/1 11:57
 */
public class CreateUserTest {
    @Test
    public void add(){

        Connection connection = null;
        PreparedStatement ps = null;
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
            Properties pros = new Properties();
            pros.load(is);
            String driver = pros.getProperty("driverClass");
            String url = pros.getProperty("url");
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");

            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

            String sql = "insert into customers(name,email,birth) values (?,?,?)";
            ps = connection.prepareStatement(sql);
            //填充占位符
            ps.setString(1,"嘿嘿");
            ps.setString(2,"heihei@qq.com");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1234-02-03");

            ps.setDate(3,new Date(date.getTime()));
            //执行
            ps.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {

            try {
                if (ps!=null){
                    ps.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (connection!=null)
                {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
