package Test;

import java.util.Base64;

public class Decode {

	public static String encrypt(String data) {

        byte[] encodedByte = Base64.getEncoder().encode(data.getBytes());

        String encodedData = new String(encodedByte);

        System.out.println(encodedData);

        return encodedData;

    }

    

    public static String decrypt(String data) {

        byte[] decodedByte = Base64.getDecoder().decode(data.getBytes());

        String decodedData = new String(decodedByte);

        System.out.println(decodedData);

        return decodedData;

    }
    
    public static void main(String[] args) {
		Decode d = new Decode();
		d.encrypt("Mysql1234$");
		d.decrypt("TXlzcWwxMjM0JA==");
	
	}
}