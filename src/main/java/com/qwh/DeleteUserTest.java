package com.qwh;

import com.qwh.utils.JDBCUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @auther qwh
 * @create 2023-06-2023/6/1 14:42
 */
public class DeleteUserTest {
    @Test
    public void delete(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtil.getConnect();
            String sql = "delete from customers where id = ?";

            ps = conn.prepareStatement(sql);
            //填充占位符ximg
            ps.setObject(1,18);
            //执行
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            //关闭资源
            JDBCUtil.closeSource(conn,ps);
        }

    }
}
