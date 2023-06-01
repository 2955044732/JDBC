package com.qwh;

import com.qwh.bean.Order;
import com.qwh.utils.JDBCUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @auther qwh
 * @create 2023-06-2023/6/1 16:31
 */
public class QueryOrder {
    @Test
    public void test(){
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order query = query(sql, 1);
        System.out.println(query);
    }
    public Order query(String sql, Object... args){
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
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    Object columValue = rs.getObject(i + 1);

                    String columnName = rsmd.getColumnLabel(i + 1);

                    Field field = Order.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(order, columValue);
                }
                return order;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.closeSource(conn, ps, rs);
        }
        return null;
    }
}
