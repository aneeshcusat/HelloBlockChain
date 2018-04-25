package hellobc.stage4;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProofOfWork {
	
	private static String NUM_OF_ZEROES = "0000";
	
	public static String hash(String message) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedhash = digest.digest(
				message.getBytes(StandardCharsets.UTF_8));
		
		return bytesToHex(encodedhash);
	    
	    
	}
	
	private static String bytesToHex(byte[] hash) {
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < hash.length; i++) {
	    String hex = Integer.toHexString(0xff & hash[i]);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
	
	private static String findNonce(String message) throws NoSuchAlgorithmException {
		
		String nonce = "THIS SENTENCE WILL GIVE NONCE";
		
		int count = 0;
		
		while(!is_valid_nonce(nonce,message)) {
			
			nonce = StringUtils.next(nonce);
			
			count = count +1;
		}
		
		System.out.println("Nonce :"+nonce);
		
		System.out.println("Count :"+count);
		
		return nonce;
		
	}
	
	private static boolean is_valid_nonce(String nonce, String message) throws NoSuchAlgorithmException {
		
		return hash(message+nonce).startsWith(NUM_OF_ZEROES);
	}
	
	public static void main(String [] args) throws Exception {
		
		String message = "Hello World";
		
		String nonce = findNonce(message);
		
		System.out.println("Hash is: "+ hash(message+nonce));
		
	}

}
