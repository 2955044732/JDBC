package com.qwh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @auther qwh
 * @create 2023-06-2023/6/1 16:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    private int orderId;
    private String orderName;
    private Date orderDate;
}
