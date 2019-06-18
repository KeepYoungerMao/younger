package com.mao.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mao by 14:38 2019/6/18
 */
@Getter
@AllArgsConstructor
public enum SecretEnum {

    AES("AES",128),
    DES("DES",56);

    private String type;
    private int strong;

}