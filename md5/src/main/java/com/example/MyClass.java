package com.example;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;


public class MyClass {

    public static void main(String[] args) {
        String string = "123456";
        String md5String = null;
        try {
            MessageDigest msInst = MessageDigest.getInstance("MD5");
            msInst.update(string.getBytes());
            md5String =  new BigInteger(1, msInst.digest()).toString(16);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        System.out.println(md5String);
    }

    public void Xor(){
        String s = "hello";
        int n = 100;
        String out = "";
        byte[] array = s.getBytes();
        for(int i = 0; i < array.length; i++){
            System.out.println(array[i]);
            array[i] =(byte) (array[i] ^ n);
            System.out.println(array[i]);
        }
        out = new String(array);
        System.out.println(out + "\n");
    }
    private void md5(){
        String string = "123456";
        String md5String = null;
        try {
            MessageDigest msInst = MessageDigest.getInstance("MD5");
            msInst.update(string.getBytes());
            md5String =  new BigInteger(1, msInst.digest()).toString(16);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        System.out.println(md5String);
    }
}
