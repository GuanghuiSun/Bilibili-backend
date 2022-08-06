package com.bilibili.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * MD5加密
 * 单向加密算法
 * 特点：加密速度快，不需要秘钥，但是安全性不高，需要搭配随机盐值使用
 */
public class MD5Util {

    public static String sign(String content, String salt, String charset) {
        content = content + salt;
        return DigestUtils.md5Hex(getContentBytes(content, charset));
    }

    public static boolean verify(String content, String sign, String salt, String charset) {
        content = content + salt;
        String mysign = DigestUtils.md5Hex(getContentBytes(content, charset));
        return mysign.equals(sign);
    }

    private static byte[] getContentBytes(String content, String charset) {
        if (!"".equals(charset)) {
            try {
                return content.getBytes(charset);
            } catch (UnsupportedEncodingException var3) {
                throw new RuntimeException("MD5签名过程中出现错误,指定的编码集错误");
            }
        } else {
            return content.getBytes();
        }
    }

    /**
     * 获取文件的md5
     *
     * @param file 文件
     * @return md5
     */
    public static String getFileMd5(MultipartFile file) {
        InputStream fis = null;
        ByteArrayOutputStream outputStream = null;
        try {
            fis = file.getInputStream();
            outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                outputStream.write(buffer, 0, read);
            }
            return DigestUtils.md5Hex(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}