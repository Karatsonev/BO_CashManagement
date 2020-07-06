package com.example.cashmanagement.comm;


import android.util.Base64;

import com.example.cashmanagement.App;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Клас за архивиране и криптиране на данни към и от .NET
 * Created by christo.christov on 4.8.2016 г..
 */
public class SisPack {

    private static final String salt = "/6yZw9sT8f2HiA3FJvjwvmGhXKdcM1w7";

    /**
     * Декриптира по AES от .NET криптирани съобщения
     * @param encryptedBuffer криптиран буфер
     * @return
     */
    public static byte[] Decrypt(byte[] encryptedBuffer, String word){
        byte[] decryptedBuffer = null;
        try {
            byte[] saltValueBytes = salt.getBytes("UTF-8");

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec pbeKeySpec = new PBEKeySpec( word.toCharArray() , saltValueBytes, 1000, 512);
            Key secretKey  = factory.generateSecret(pbeKeySpec);
            byte[] key = new byte[32];
            byte[] iv = new byte[16];
            System.arraycopy(secretKey.getEncoded(), 0, key, 0, 32);
            System.arraycopy(secretKey.getEncoded(), 32, iv, 0, 16);

            SecretKeySpec secret = new SecretKeySpec(key, "AES");
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, ivSpec);
            decryptedBuffer = cipher.doFinal(encryptedBuffer);
        }catch (Exception ex){
            if(ex != null && ex.getMessage()!=null)
                App.writeToLog("decrypt:"+ex.getMessage());
        }
        return decryptedBuffer;
    }

    /**
     * Криптира съобщения, които да се разкриптират в .NET
     * @param uncryptedBuffer некриптиран буфер
     * @return
     */
    public static byte[] Crypt(byte[] uncryptedBuffer, String word){
        byte[] cryptedBuffer = null;
        try {
            byte[] saltValueBytes = salt.getBytes("UTF-8");

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec pbeKeySpec = new PBEKeySpec( word.toCharArray() , saltValueBytes, 1000, 512);
            Key secretKey  = factory.generateSecret(pbeKeySpec);
            byte[] key = new byte[32];
            byte[] iv = new byte[16];
            System.arraycopy(secretKey.getEncoded(), 0, key, 0, 32);
            System.arraycopy(secretKey.getEncoded(), 32, iv, 0, 16);

            SecretKeySpec secret = new SecretKeySpec(key, "AES");
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
            cryptedBuffer = cipher.doFinal(uncryptedBuffer);
        }catch (Exception ex){
            if(ex != null && ex.getMessage()!=null)
                App.writeToLog("crypt:"+ex.getMessage());
        }
        return cryptedBuffer;
    }

    /**
     * Декомпресира съобщения от .NET (тази функция не работи еднакво с всички версии на .NET)
     * @param compressed компресиран буфер
     * @return
     * @throws IOException
     */
    public static byte [] Decompress(byte[] compressed) throws IOException {
        if(compressed.length == 0)
            return  new byte[0];
        final int BUFFER_SIZE = 32;
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = gis.read(data)) != -1) {
            out.write(data,0,bytesRead);
        }
        gis.close();
        is.close();
        return out.toByteArray();
    }

    /**
     * Компресира съощения, които .NET да разкомпресира (тази функция не работи еднакво с всички версии на .NET)
     * @param uncompressed некомпресиран буфер
     * @return
     * @throws IOException
     */
    public static byte[] Compress(byte[] uncompressed) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(uncompressed.length);
        GZIPOutputStream gos = new GZIPOutputStream(os);
        gos.write(uncompressed);
        gos.close();
        byte[] compressed = os.toByteArray();
        os.close();
        return compressed;
    }

    /**
     * Компресира и криптира стринг, за изпращане към сървъра.
     * @param in данни
     * @param key публичен ключ на сървъра
     * @return
     */
    public static DuplexClientMessage CompressAndCrypt(String in, PublicKey key){
        try {
            DuplexClientMessage message = new DuplexClientMessage();
            String word = makeWord();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte [] toencrypt = cipher.doFinal(word.getBytes("UTF-8"));
            message.salt = Base64.encodeToString(toencrypt, Base64.NO_WRAP);
            message.pepper = Base64.encodeToString(Crypt(Compress(in.getBytes("UTF-8")), word), Base64.DEFAULT);
            return message;
        }catch (Exception ex){
            if(ex != null && ex.getMessage()!=null)
                App.writeToLog("compressAndCrypt:"+ex.getMessage());
            return null;
        }
    }

    /**
     * Разкриптира и разархивира съобщението от сървъра
     * @param message данни
     * @param key частен ключ на клиента
     * @return
     */
    public static String DecryptAndDecompress(DuplexClientMessage message, PrivateKey key){
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            String word = new String(cipher.doFinal(Base64.decode(message.salt, Base64.DEFAULT)), "UTF-8");
            return new String( Decompress(Decrypt(Base64.decode(message.pepper, Base64.DEFAULT), word)) ,"UTF-8");
        }catch (Exception ex){
            if(ex != null && ex.getMessage()!=null)
                App.writeToLog("decompressAndDecrypt:"+ex.getMessage());
            return null;
        }
    }

    /**
     * 32-символен произволно генериран симетричен ключ.
     * @return
     */
    public static String makeWord(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
