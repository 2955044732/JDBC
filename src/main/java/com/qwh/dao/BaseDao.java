package com.qwh.dao;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Auther: QWH
 * @Date: 2023-06-02-17:51
 */

public class BaseDao {
    /**
     * 获取连接
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties pos = new Properties();
        pos.load(is);
        String driver = pos.getProperty("driverClass");
        String url = pos.getProperty("url");
        String user = pos.getProperty("user");
        String password = pos.getProperty("password");

        Class.forName(driver);

        Connection conn = DriverManager.getConnection(url, user, password);

        return conn;
    }

    /**
     * 增删改
     * @param conn
     * @param sql
     * @param args
     * @throws SQLException
     */

    public static int update(Connection conn,String sql,Object ...args){
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResource(null,ps);
        }
        return 0;
    }

    /**
     * 查询一条
     * @param conn
     * @param sql
     * @param clazz
     * @param args
     * @return
     * @param <T>
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public static <T> T query(Connection conn,String sql,Class<T> clazz,Object ...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Object columnValue = rs.getObject(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeResource(null,ps,rs);
        }
        return null;
    }

    /**
     * 查询多条数据
     * @param conn
     * @param sql
     * @param clazz
     * @param args
     * @return
     * @param <T>
     */
    public static <T> List<T> queryForList(Connection conn, String sql, Class<T> clazz, Object ...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            ArrayList<T> list  = new ArrayList<>();
            if (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Object columnValue = rs.getObject(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }

                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(null,ps,rs);
        }
        return null;
    }

/**@Description
 * @param
 * @return
 * @version v1.0
 * @author qwh
 * @date 2023/6/2 19:15
 */
    public static <E> E getValue(Connection conn,String sql,Object ...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                return (E) rs.getObject(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResource(null,ps,rs);
        }
        return null;
    }

    /**
     * 关闭连接
     * @param conn
     * @param ps
     * @param rs
     */
    public static void closeResource(Connection conn,PreparedStatement ps,ResultSet rs){
        try {
            if (conn!=null)
                conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if (ps!=null)
                ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if (rs!=null)
                rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 关闭连接
     * @param conn
     * @param ps
     */
    public static void closeResource(Connection conn,PreparedStatement ps){
        try {
            if (conn!=null)
                conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if (ps!=null)
                ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
