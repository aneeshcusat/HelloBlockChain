package hellobc.stage3;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAEncryptionDemo {

	public static void main(String[] args) {
		RSAEncryption rsaEncryption = new RSAEncryption();
		// generate public and private keys
        KeyPair keyPair = rsaEncryption.buildKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        
        
        try {
        
        KeyFactory kf = KeyFactory.getInstance("RSA"); 
        
        
        
        //Public Key  byte array to String
        String publicKeyString = rsaEncryption.convertPublicKeyToString(publicKey);
        
        System.out.println("Public Key String: "+publicKeyString);
        
        //Public Key String to Public Key
        publicKey = rsaEncryption.convertStringToPublicKey(publicKeyString);
        
        //Private Key  byte array to String
        String privateKeyString = rsaEncryption.convertPrivateKeyToString(privateKey);
        
        System.out.println("Private Key String: "+privateKeyString);
        
        //Private Key String array to private Key
        privateKey = rsaEncryption.convertStringToPrivateKey(privateKeyString);
        
        
        // encrypt the message
        byte[] encrypted = null;
        
		encrypted = rsaEncryption.sign(privateKey, "This is a secret message");
		   
        
        
        // decrypt the message
			boolean isVerify = false;
		
			isVerify = rsaEncryption.verify(publicKey, encrypted, "This is a secret message");
			
			System.out.println("Is Verified: "+isVerify);     // This is a secret message
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}                                 
        
		

        
	}

}
