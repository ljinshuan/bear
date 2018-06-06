package com.taobao.brand.bear.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jinshuan.li 2017/12/22 08:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dog2 implements Serializable {

    private String name;

    private Integer age;

    private Integer status;

}
