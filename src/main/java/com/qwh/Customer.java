package com.qwh;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

/**
 * @auther qwh
 * @create 2023-06-2023/6/1 12:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Customer {
    private int id;
    private String name;
    private String email;
    private Date brith;

}
