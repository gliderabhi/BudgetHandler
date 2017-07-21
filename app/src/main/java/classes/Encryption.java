package classes;

import android.util.Log;

import java.security.MessageDigest;

/**
 * Created by MunnaSharma on 7/19/2017.
 */

public class Encryption {
    public static String main(String mail)throws Exception
    {        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(mail.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<byteData.length;i++) {
            String hex=Integer.toHexString(0xff & byteData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        return String.valueOf(hexString);
    }
}
