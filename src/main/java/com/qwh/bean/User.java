package com.qwh.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @auther qwh
 * @create 2023-06-2023/6/1 11:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private String user;
    private String password;

}
