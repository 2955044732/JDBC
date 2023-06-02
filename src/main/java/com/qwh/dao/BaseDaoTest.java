package com.qwh.dao;

import com.qwh.bean.Goods;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.qwh.dao.BaseDao.*;

/**
 * @Auther: QWH
 * @Date: 2023-06-02-18:16
 */
public class BaseDaoTest {
    @Test
    public void test() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = getConnection();
        System.out.println(conn);
    }
    @Test
    public void test1() throws SQLException, IOException, ClassNotFoundException {

        String sql = "insert into Goods(name) values(?)";
        Connection conn = getConnection();
        update(conn,sql,"qwe123");

    }

    @Test
    public void test2() throws SQLException, IOException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String sql = "select * from Goods where id < ?";
        Connection conn = getConnection();
        List<Goods> goods = queryForList(conn, sql, Goods.class,2);
        for (Goods good : goods) {
            System.out.println(good);
        }
    }
}
