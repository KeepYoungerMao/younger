package com.mao.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * 加密工具类
 * @author mao by 18:32 2019/5/14
 */
public class SecretUtils {

    private static final String ALGORITHM = "SHA1PRNG";

    /**
     * 加密
     * @param data 需要加密的数据
     * @param key 密钥
     * @param secretEnum 加密类型
     * @return 加密后的数据
     */
    public static String encrypt(String data, String key, SecretEnum secretEnum) {
        try {
            if (SU.isEmpty(data) || SU.isEmpty(key)) return null;
            byte[] content = data.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = getCipher(key,secretEnum,Cipher.ENCRYPT_MODE);
            byte[] result = cipher.doFinal(content);
            return parseByte2HexStr(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param data 需要解密的数据
     * @param key 密钥
     * @param secretEnum 加密类型
     * @return 解密数据
     */
    public static String decrypt(String data, String key, SecretEnum secretEnum) {
        try {
            if (SU.isEmpty(data) || SU.isEmpty(key)) return null;
            byte[] content = parseHexStr2Byte(data);
            if (null == content) return null;
            Cipher cipher = getCipher(key, secretEnum,Cipher.DECRYPT_MODE);
            byte[] result = cipher.doFinal(content);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Cipher getCipher(String key, SecretEnum secretEnum, int mode) throws Exception{
        KeyGenerator k_gen = KeyGenerator.getInstance(secretEnum.getType());
        SecureRandom secureRandom = SecureRandom.getInstance(ALGORITHM);
        secureRandom.setSeed(key.getBytes());
        k_gen.init(secretEnum.getStrong(), secureRandom);
        SecretKey secretKey = k_gen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, secretEnum.getType());
        Cipher cipher = Cipher.getInstance(secretEnum.getType());// 创建密码器
        cipher.init(mode, keySpec);// 初始化
        return cipher;
    }

    /**
     * 将二进制转换成16进制
     * @param buf 二进制数据
     * @return String
     */
    private static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte aBuf : buf) {
            String hex = Integer.toHexString(aBuf & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 16进制转二进制
     * @param hexStr 16进制数据
     * @return 二进制数组
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] args) {
        String a = "hello world";
        String key = "123456";
        String en_a = encrypt(a,key,SecretEnum.AES);
        System.out.println(en_a);
        String de_a = decrypt(en_a,key,SecretEnum.AES);
        System.out.println(de_a);
    }

}