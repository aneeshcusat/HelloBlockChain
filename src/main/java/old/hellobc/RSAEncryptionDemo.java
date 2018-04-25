package old.hellobc;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAEncryptionDemo {

	public static void main(String[] args) {
		RSAEncryption rsaEncryption = new RSAEncryption();
		// generate public and private keys
        KeyPair keyPair = rsaEncryption.buildKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        System.out.println("Private Key: "+privateKey.toString());
        
        System.out.println("Public Key: "+publicKey.toString());
        
        // encrypt the message
        byte[] encrypted = null;
        String encryptedString = null;
		try {
			//encrypted = rsaEncryption.sign(privateKey, "This is a secret message");
			encryptedString = rsaEncryption.sign(privateKey.toString(), "This is a secret message");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}     
        System.out.println("Encrypted text: "+new String(encryptedString));  // <<encrypted message>>
        
        // decrypt the message
        boolean isVerify = false;
		try {
			isVerify = rsaEncryption.verify(publicKey.toString(), encryptedString, "This is a secret message");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}                                 
        System.out.println("Is Verified: "+isVerify);     // This is a secret message
		

	}

}
