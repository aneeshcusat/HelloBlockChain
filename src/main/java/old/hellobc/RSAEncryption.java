package old.hellobc;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.stereotype.Component;

@Component
public class RSAEncryption {
    
    /*public static void main(String [] args) throws Exception {
        // generate public and private keys
        KeyPair keyPair = buildKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        System.out.println("Private Key: "+privateKey.toString());
        
        System.out.println("Public Key: "+publicKey.toString());
        
        // encrypt the message
        byte [] encrypted = sign(privateKey, "This is a secret message");     
        System.out.println("Encrypted text: "+new String(encrypted));  // <<encrypted message>>
        
        // decrypt the message
        boolean isVerify = verify(publicKey, encrypted, "This is a secret message");                                 
        System.out.println("Is Verified: "+isVerify);     // This is a secret message
    }*/

    public  KeyPair buildKeyPair() {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = null;
        try { 
         keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        }
        catch (Exception e ) {
        	 System.out.println("Error while building keypair: "+e);	
        }
        keyPairGenerator.initialize(keySize);      
        return keyPairGenerator.genKeyPair();
    }
    
    public String sign(String privateKey, String message) {
    	String encrypted = null;
    	try {
    		KeyFactory kf = KeyFactory.getInstance("RSA");

            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            PrivateKey privKey = kf.generatePrivate(keySpecPKCS8); 
            sign(privKey,message);
    	} catch(Exception e) {
    		System.out.println("Error while signing: "+e);
    	}

        return encrypted;
    }

    public  byte[] sign(PrivateKey privateKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  

        return cipher.doFinal(message.getBytes());  
    }
    
    public  boolean verify(String pPublicKey, String encrypted, String plainTextToVerify)  {
        boolean verify = false;
        try {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(pPublicKey));
        PublicKey pubKey =  kf.generatePublic(keySpecX509);
        verify(pubKey,encrypted.getBytes(),plainTextToVerify);
        } catch(Exception e) {
        	System.out.println("Error while verifying signature");
        }
        return verify;
        
    }
    
    public  boolean verify(PublicKey publicKey, byte [] encrypted, String plainTextToVerify) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedMessage = cipher.doFinal(encrypted);
        System.out.println("Decrypted text: "+new String(decryptedMessage));
        return plainTextToVerify.equalsIgnoreCase(new String(decryptedMessage));
    }
}