package com.qwh.utils;


import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

                    Field field =clazz.getDeclaredField(columnLabel);
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

    public static  <T> List<T> queryForList(Class<T> clazz,String sql, Object... args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnect();

            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            // 获取结果集的元数据 :ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            //创建集合对象
            ArrayList<T> list = new ArrayList<T>();
            while (rs.next()) {
                T t = clazz.newInstance();
                // 处理结果集一行数据中的每一个列:给t对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columValue = rs.getObject(i + 1);

                    // 获取每个列的列名
                    // String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 给t对象指定的columnName属性，赋值为columValue：通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columValue);
                }
                list.add(t);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeSource(conn, ps, rs);
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
