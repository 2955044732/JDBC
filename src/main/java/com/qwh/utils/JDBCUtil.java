package com.qwh.utils;


import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Properties;

/**
 * @auther qwh
 * @create 2023-06-2023/6/1 12:24
 */
public class JDBCUtil {
    /**
     * 获取链接
     * @return
     */
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
     * 增删改
     * @param sql
     * @param args
     */
    public static void update(String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnect();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeSource(conn,ps);
        }

    }

    public static <T> T query(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnect();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (rs.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    Field field = t.getClass().getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }  finally {
            JDBCUtil.closeSource(conn,ps,rs);
        }
        return null;
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



    /**
     * 关闭资源
     * @param conn
     * @param ps
     * @param rs
     */
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
