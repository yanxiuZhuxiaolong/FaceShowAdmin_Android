package com.yanxiu.gphone.faceshowadmin_android.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * DES、AES 加密解密工具类
 */
public class SysEncryptUtil {
    private static final String EN_DE_CODE_PWD = "ws3edaw4";
    public static Key getKey(byte[] arrBTmp, String alg) {
        if (!("DES".equalsIgnoreCase(alg) || "DESede".equalsIgnoreCase(alg) || "AES".equalsIgnoreCase(alg))) {
            System.out.println("alg type not find: " + alg);
            return null;
        }
        byte[] arrB;
        if ("DES".equalsIgnoreCase(alg)) {
            arrB = new byte[8];
        } else if ("DESede".equalsIgnoreCase(alg)) {
            arrB = new byte[24];
        } else {
            arrB = new byte[16];
        }
        int i = 0;
        int j = 0;
        while (i < arrB.length) {
            if (j > arrBTmp.length - 1) {
                j = 0;
            }
            arrB[i] = arrBTmp[j];
            i++;
            j++;
        }
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, alg);
        return key;
    }

    public static byte[] encrypt(String s, String strKey, String alg) {
        if (!("DES".equalsIgnoreCase(alg) || "DESede".equalsIgnoreCase(alg) || "AES".equalsIgnoreCase(alg))) {
            System.out.println("alg type not find: " + alg);
            return null;
        }
        byte[] r = null;
        try {
            Key key = getKey(strKey.getBytes(), alg);
            Cipher c;
            c = Cipher.getInstance(alg);
            c.init(Cipher.ENCRYPT_MODE, key);
            r = c.doFinal(s.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return r;
    }

    public static String decrypt(byte[] code, String strKey, String alg) {
        if (!("DES".equalsIgnoreCase(alg) || "DESede".equalsIgnoreCase(alg) || "AES".equalsIgnoreCase(alg))) {
            System.out.println("alg type not find: " + alg);
            return null;
        }
        String r = null;
        try {
            Key key = getKey(strKey.getBytes(), alg);
            Cipher c;
            c = Cipher.getInstance(alg);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] clearByte = c.doFinal(code);
            r = new String(clearByte);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.out.println("not padding");
            r = null;
        }
        return r;
    }
    public static String getMD5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] byteArray = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    sb.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    sb.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("no device Id");
    }

    public static String encryptDES(String encryptMsg , String encryptType) {
        if(!TextUtils.isEmpty(encryptMsg)) {
            String ret = Base64.encodeToString(encrypt(encryptMsg, EN_DE_CODE_PWD, encryptType), Base64.NO_WRAP);//Base64、HEX等编解码
            return ret;
        }
        return null;
    }

    public static String decryptDES(String deCipMsg, String decryptType) {
        try{
            if(!TextUtils.isEmpty(deCipMsg)) {
                String textDeCipher = deCipMsg;   //从服务器返回的加密字符串，需要解密的字符串
                byte[] byteStr = Base64.decode(textDeCipher, Base64.NO_WRAP);//先用Base64解码
                return decrypt(byteStr, EN_DE_CODE_PWD, decryptType);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getMd5_32(String md5Str) {
        String result = "";
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(md5Str.getBytes("utf-8"));

            byte[] bytes=algorithm.digest();
            StringBuilder hexString = new StringBuilder();

            for (int b : bytes) {
                if (b < 0) {
                    b += 256;
                }
                if (b < 16) {
                    hexString.append("0");
                }
                hexString.append(Integer.toHexString(b));
            }
            result=hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
