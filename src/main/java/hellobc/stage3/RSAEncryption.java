package hellobc.stage3;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.stereotype.Component;

@Component
public class RSAEncryption {
   
	
	
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
    
   
    

    public  byte[] sign(PrivateKey privateKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  

        return cipher.doFinal(message.getBytes());  
    }
    
    
    public  boolean verify(PublicKey publicKey, byte [] encrypted, String plainTextToVerify) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedMessage = cipher.doFinal(encrypted);
        System.out.println("Decrypted text: "+new String(decryptedMessage));
        return plainTextToVerify.equalsIgnoreCase(new String(decryptedMessage));
    }
    
    public String convertPublicKeyToString(PublicKey publicKey) {
    	return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    	
    }
    
    public PublicKey convertStringToPublicKey(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
    	byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
    	KeyFactory kf = KeyFactory.getInstance("RSA"); 
    	return kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    	
    }
    
    public  String convertPrivateKeyToString(PrivateKey privateKey) {
    	return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }
    
    public PrivateKey convertStringToPrivateKey(String privateKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
    	byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
    	KeyFactory kf = KeyFactory.getInstance("RSA"); 
    	return kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
    	
    }
    
    
}