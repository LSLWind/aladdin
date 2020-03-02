package org.phoenix.aladdin.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MD5UtilTest {

    @Test
    void getMD5Str() {
        try {
            System.out.println(MD5Util.getMD5Str("123456"));
            System.out.println(MD5Util.getMD5Str("654321"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}