package com.qwh;

import com.qwh.bean.User;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * @auther qwh
 * @create 2023-06-2023/6/1 11:10
 */
public class StatementTest {


    //存在sql注入问题
    public static void main(String []args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入用户名");
        String user = scanner.nextLine();
        System.out.println("输入密码");
        String password = scanner.nextLine();
        String sql = "select user,password from user_table where user = '"+user+"'and password ='"+password+"'";
        User isUser = get(sql, User.class);
        if (isUser!=null){
            System.out.println(true);
        }
        else {
            System.out.println(false);
        }
    }

    // 使用Statement实现对数据表的查询操作
    public static  <T> T get(String sql, Class<T> clazz) {
        T t = null;

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            // 1.加载配置文件
            InputStream is = StatementTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties pros = new Properties();
            pros.load(is);

            // 2.读取配置信息
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            // 3.加载驱动
            Class.forName(driverClass);

            // 4.获取连接
            conn = DriverManager.getConnection(url, user, password);

            st = conn.createStatement();

            rs = st.executeQuery(sql);

            // 获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();

            // 获取结果集的列数
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {

                t = clazz.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    // //1. 获取列的名称
                    // String columnName = rsmd.getColumnName(i+1);

                    // 1. 获取列的别名
                    String columnName = rsmd.getColumnLabel(i + 1);

                    // 2. 根据列名获取对应数据表中的数据
                    Object columnVal = rs.getObject(columnName);

                    // 3. 将数据表中得到的数据，封装进对象
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnVal);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
