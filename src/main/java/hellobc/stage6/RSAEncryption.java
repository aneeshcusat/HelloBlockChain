package hellobc.stage6;

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
   
	
	
    public static KeyPair buildKeyPair() {
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
    
   
    

    public static byte[] sign(PrivateKey privateKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  

        return cipher.doFinal(message.getBytes());  
    }
    
    
    public static boolean verify(PublicKey publicKey, byte [] encrypted, String plainTextToVerify) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedMessage = cipher.doFinal(encrypted);
        System.out.println("Decrypted text: "+new String(decryptedMessage));
        return plainTextToVerify.equalsIgnoreCase(new String(decryptedMessage));
    }
    
    /**
     * Method converts public key to byte array and then to String
     * @param publicKey
     * @return
     */
    public static String convertPublicKeyToString(PublicKey publicKey) {
    	return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    	
    }
    
    /**
     * Method converts String to byte array and then to Public key
     * @param publicKeyString
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey convertStringToPublicKey(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
    	byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
    	KeyFactory kf = KeyFactory.getInstance("RSA"); 
    	return kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    	
    }
    
    /**
     * Method converts private key to byte array and then to String
     * @param privateKey
     * @return
     */
    public static String convertPrivateKeyToString(PrivateKey privateKey) {
    	return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }
    
    /**
     * Method converts String to byte array and then to public key
     * @param privateKeyString
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey convertStringToPrivateKey(String privateKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
    	byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
    	KeyFactory kf = KeyFactory.getInstance("RSA"); 
    	return kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
    	
    }
    
    /**
     * Convert byte array to String
     * @param privateKey
     * @return
     */
    public static String convertByteArrayToString(byte[] byteArray) {
    	return Base64.getEncoder().encodeToString(byteArray);
    }
    
    
    public static byte[] convertStringToByteArray(String inputString) {
    	return Base64.getDecoder().decode(inputString);
    }
    
    
    public  static String sign(String privateKey, String message) throws Exception {
    	return convertByteArrayToString(sign(convertStringToPrivateKey(privateKey), message));
    }
    
    public static boolean verify(String publicKey, String encrypted, String plainTextToVerify) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, convertStringToPublicKey(publicKey));
        byte[] decryptedMessage = cipher.doFinal(convertStringToByteArray(encrypted));
        System.out.println("Decrypted text: "+new String(decryptedMessage));
        return plainTextToVerify.equalsIgnoreCase(new String(decryptedMessage));
    }
    
    
}