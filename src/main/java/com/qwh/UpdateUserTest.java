package com.qwh;

import com.qwh.utils.JDBCUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @auther qwh
 * @create 2023-06-2023/6/1 14:52
 */
public class UpdateUserTest {
    @Test
    public void update(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtil.getConnect();
            String sql = "update customers  set name = ? where id = ? ";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,"小白");
            ps.setObject(2,"10");
            //执行
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.closeSource(conn,ps);
        }
    }
}
