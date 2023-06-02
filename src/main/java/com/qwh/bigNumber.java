package com.qwh;

import com.qwh.utils.JDBCUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Auther: QWH
 * @Date: 2023-06-02-8:28
 */
public class bigNumber {
    @Test
    public void test1() throws SQLException {
        Connection conn = JDBCUtil.getConnect();
        Statement st = conn.createStatement();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 20000; i++) {
            String sql = "insert into goods(name) values(name_"+i+")";
            st.execute(sql);
        }
        long end = System.currentTimeMillis();
        System.out.println(start-end);
    }
    @Test
    public void test2(){//43856
        Connection conn = null;
        PreparedStatement ps = null;
        long start = 0;
        long end = 0;
        try {
            conn = JDBCUtil.getConnect();
            String sql = "insert into goods(name) values (?)";
            ps = conn.prepareStatement(sql);
            start = System.currentTimeMillis();
            for (int i = 0; i < 20000; i++) {
                ps.setObject(1,"name_"+i);
                ps.execute();
            }
            end = System.currentTimeMillis();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.closeSource(conn,ps);
        }


        System.out.println(end-start);

    }

    @Test
    public void test3(){
        Connection conn = null;
        PreparedStatement ps = null;
        long start = 0;
        long end = 0;
        try {
            conn = JDBCUtil.getConnect();
            String sql = "insert into goods(name) values (?)";
            ps = conn.prepareStatement(sql);
            start = System.currentTimeMillis();
            for (int i = 0; i < 20000; i++) {
                ps.setObject(1,"name_"+i);
                ps.addBatch();
                if (i%500==0){
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            end = System.currentTimeMillis();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.closeSource(conn,ps);
        }


        System.out.println(end-start);

    }
    @Test
    public void test4(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {

            long start = System.currentTimeMillis();

            conn = JDBCUtil.getConnect();

            //设置不允许自动提交数据
            conn.setAutoCommit(false);

            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for(int i = 1;i <= 20000;i++){
                ps.setObject(1, "name_" + i);

                //1."攒"sql
                ps.addBatch();

                if(i % 500 == 0){
                    //2.执行batch
                    ps.executeBatch();

                    //3.清空batch
                    ps.clearBatch();
                }

            }

            //提交数据
            conn.commit();

            long end = System.currentTimeMillis();

            System.out.println("花费的时间为：" + (end - start));//20000:83065 -- 565
        } catch (Exception e) {								//1000000:16086 -- 5114
            e.printStackTrace();
        }finally{
            JDBCUtil.closeSource(conn, ps);

        }
    }
}
