package cn.armory.common.utils;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密相关的工具类
 * <p>Created by Fenghj on 2018/5/29.</p>
 */

public class EncryptUtils {
    private EncryptUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * MD5加密
     *
     * @param data 明文字符串
     * @return 16进制密文
     */
    public static String encryptMD5ToString(final String data) {
        if (data == null || data.length() == 0) return "";
        return bytes2HexString(encryptMD5(data.getBytes()));
    }

    /**
     * MD5加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */
    private static byte[] encryptMD5(final byte[] data) {
        return hashTemplate(data);
    }

    ///////////////////////////////////////////////////////////////////////////
    // AES加密相关
    ///////////////////////////////////////////////////////////////////////////
    /**
     * AES转变
     * <p>法算法名称/加密模式/填充方式</p>
     * <p>加密模式有：电子密码本模式ECB、加密块链模式CBC、加密反馈模式CFB、输出反馈模式OFB</p>
     * <p>填充方式有：NoPadding、ZerosPadding、PKCS5Padding</p>
     */
    private static String AES_Transformation = "AES/ECB/PKCS5Padding";
    private static final String AES_Algorithm = "AES";

    /**
     * 不足16位，补/000至16位
     *
     * @param key 原密钥
     * @return 新密钥
     */
    private static String secureBytes(String key) {
        if (key.length() < 16) {
            StringBuilder keyBuilder = new StringBuilder(key);
            for (int i = (keyBuilder.length() - 1); i < 15; i++) {
                keyBuilder.append("\000");
            }
            key = keyBuilder.toString();
        }
        return key;
    }

    /**
     * AES加密后转为Base64编码
     *
     * @param data 明文
     * @param key  16、24、32字节秘钥
     * @param iv   向量
     * @return String Base64加密后
     */
    public static String encryptAES2Base64(final String data, final String key, final String iv) {
        byte[] bytes = encryptAES(data, key, iv);
        if (bytes != null) return Base64.encodeToString(bytes, Base64.DEFAULT);
        return null;
    }

    /**
     * AES加密后转为16进制
     *
     * @param data 明文
     * @param key  16、24、32字节秘钥
     * @param iv   向量
     * @return 16进制密文
     */
    public static String encryptAES2HexString(final String data, final String key, final String iv) {
        return bytes2HexString(encryptAES(data, key, iv));
    }

    /**
     * AES加密
     *
     * @param data 明文
     * @param key  16、24、32字节秘钥
     * @param iv   向量
     * @return 密文
     */
    private static byte[] encryptAES(final String data, final String key, final String iv) {
        String sKey = secureBytes(key);
        return aesTemplate(data.getBytes(), sKey.getBytes(), iv.getBytes(), true);
    }

    /**
     * AES解密Base64编码密文
     *
     * @param data Base64编码密文
     * @param key  16、24、32字节秘钥
     * @param iv   向量
     * @return 明文
     */
    public static String decryptBase64AES(final String data, final String key, final String iv) {
        if (data == null || "".equals(data)) return null;
        return new String(decryptAES(Base64.decode(data, Base64.DEFAULT), key, iv));
    }

    /**
     * AES解密16进制密文
     *
     * @param data 16进制密文
     * @param key  16、24、32字节秘钥
     * @param iv   向量
     * @return 明文
     */
    public static String decryptHexStringAES(final String data, final String key, final String iv) {
        byte[] bytes = decryptAES(hexString2Bytes(data), key, iv);
        if (bytes == null || bytes.length == 0) return null;
        return new String(bytes);
    }

    /**
     * AES解密
     *
     * @param data 密文
     * @param key  16、24、32字节秘钥
     * @param iv   向量
     * @return 明文
     */
    private static byte[] decryptAES(final byte[] data, final String key, final String iv) {
        String sKey = secureBytes(key);
        return aesTemplate(data, sKey.getBytes(), iv.getBytes(), false);
    }

    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return "";
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    private static byte[] hexString2Bytes(String hexString) {
        if (hexString == null || "".equals(hexString)) return null;
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
        }
        return ret;
    }

    private static int hex2Dec(final char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * hash加密模板
     *
     * @param data 数据
     * @return 密文字节数组
     */
    private static byte[] hashTemplate(final byte[] data) {
        if (data == null || data.length <= 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES加密模板
     *
     * @param data      数据
     * @param key       秘钥
     * @param iv        向量
     * @param isEncrypt {@code true}: 加密 {@code false}: 解密
     * @return 密文或者明文
     */
    private static byte[] aesTemplate(final byte[] data, final byte[] key, final byte[] iv, final boolean isEncrypt) {
        if (data == null || data.length == 0 || key == null || key.length == 0)
            return null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, AES_Algorithm);
            Cipher cipher = Cipher.getInstance(AES_Transformation);
            if (iv == null || iv.length == 0) {
                cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec);
            } else {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            }
            return cipher.doFinal(data);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // RSA加密相关
    ///////////////////////////////////////////////////////////////////////////
    /**
     * RSA转变
     * <p>法算法名称/加密模式/填充方式</p>
     * <p>加密模式有：电子密码本模式ECB、加密块链模式CBC、加密反馈模式CFB、输出反馈模式OFB</p>
     * <p>填充方式有：NoPadding、ZerosPadding、PKCS1Padding</p>
     */
    private static String RSA_Transformation = "RSA/ECB/PKCS1Padding";
    private static final String RSA_Algorithm = "RSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 将base64编码后的公钥字符串转成PublicKey实例
     *
     * @param publicKey 公钥字符
     * @return publicKEY
     * @throws Exception exception
     */
    private static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey, Base64.NO_WRAP);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_Algorithm);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将base64编码后的私钥字符串转成PrivateKey实例
     *
     * @param privateKey 私钥字符串
     * @return 私钥对象
     * @throws Exception exception
     */
    private static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey, Base64.NO_WRAP);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_Algorithm);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * RSA加密
     *
     * @param content   待加密文本
     * @param publicKey 公钥
     * @return 密文
     */
    public static String encryptRSA(String content, String publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_Transformation);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
            byte[] data = content.getBytes();
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return new String(Base64.encode(encryptedData, Base64.NO_WRAP));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * RSA解密
     *
     * @param content    密文
     * @param privateKey 私钥
     * @return 明文
     */
    public static String decryptRSA(String content, String privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_Transformation);
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
            byte[] encryptedData = Base64.decode(content, Base64.NO_WRAP);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String decrypt(String key, String data) throws Exception {
        int l = data.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = Integer.valueOf(data.substring(i * 2, i * 2 + 2), 16).byteValue();
        }

        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        @SuppressLint("GetInstance")
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] original = cipher.doFinal(ret);
        return new String(original, StandardCharsets.UTF_8);
    }
}
