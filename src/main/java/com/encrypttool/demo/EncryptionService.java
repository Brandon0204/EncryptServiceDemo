package com.encrypttool.demo;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Service
public class EncryptionService {
    private static byte[] keyByte;
//    private final ConfigurationService configurationService;
//ConfigurationService configurationService
    @Autowired
    public EncryptionService() {
//        this.configurationService = configurationService;
    }

    @PostConstruct
    void init() {
//        String keyStr = configurationService.get("encryptionKey", "Bar12345Bar12345");
        String keyStr = "Bar12345Bar12345";
        keyByte = keyStr.getBytes();
    }

    @SneakyThrows
    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    @SneakyThrows
    public static Object deserialize(byte[] data) {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    @SneakyThrows
    public static byte[] encrypt(byte[] content) {
        byte[] encrypted;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyByte, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = new byte[cipher.getBlockSize()];
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParams);
            encrypted = cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return encrypted;
    }

    @SneakyThrows
    public static byte[] decrypt(byte[] content) {
        byte[] decrypted;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyByte, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] ivByte = new byte[cipher.getBlockSize()];
            IvParameterSpec ivParamsSpec = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParamsSpec);
            decrypted = cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return decrypted;
    }
}
