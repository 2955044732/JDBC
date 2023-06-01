package com.qwh;

import com.qwh.utils.JDBCUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @auther qwh
 * @create 2023-06-2023/6/1 15:41
 */
public class QueryCustomers {
    @Test
    public void test(){
        String sql = "select id,name,email,birth from customers where id = ?";
        Customer select = select(sql, 3);
        System.out.println(select);
    }

    public Customer select(String sql,Object ...args) {

        Connection conn =null;
        PreparedStatement ps = null;
        ResultSet rs=null;
       try {
           conn = JDBCUtil.getConnect();
           ps = conn.prepareStatement(sql);
                //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }

            //返回结果集
            rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();
            if (rs.next()){
                Customer cust = new Customer();
                for (int i = 0; i < columnCount; i++) {
                    //列值
                    Object columValue = rs.getObject(i + 1);

                    //列名
                    String columnLabel = rsmd.getColumnName(i + 1);

                    //利用反射
                    Field field = Customer.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(cust,columValue);
                }
                return cust;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            JDBCUtil.closeSource(conn,ps,rs);
        }
        return null;
    }

}
