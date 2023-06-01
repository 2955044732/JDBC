package com.qwh;

import com.qwh.bean.Customer;
import com.qwh.utils.JDBCUtil;
import org.junit.Test;

import java.io.*;
import java.sql.*;

/**
 * @auther qwh
 * @create 2023-06-2023/6/1 20:14
 */
public class BlobTest {
    @Test
    public void test() throws SQLException, FileNotFoundException {
        Connection conn = JDBCUtil.getConnect();
        String sql = "insert into customers(name,email,birth,photo) values(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1,"heih");
        ps.setObject(2,"wer.com");
        ps.setObject(3,"1231-02-13");
        FileInputStream is = new FileInputStream("13.jpg");
        ps.setObject(4,is);

        ps.execute();

    }

    @Test
    public void test2(){
        Connection conn = null;
        InputStream is = null;
        FileOutputStream fos = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnect();
            is = null;
            fos = null;
            String sql = "select * from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,24);

            rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String email = rs.getString(3);
                Date birth = rs.getDate(4);
                Customer customer = new Customer(id, name, email, birth);

                Blob blob = rs.getBlob(5);


                is = blob.getBinaryStream();
                fos = new FileOutputStream("heihei.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1){
                    fos.write(buffer,0,len);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.closeSource(conn,ps,rs);
            try {
                if (is!=null){
                    is.close();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                if (fos!=null)
                {
                    fos.close();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



    }
}
