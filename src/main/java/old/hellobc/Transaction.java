package old.hellobc;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Autowired;

public class Transaction {
	
	private RSAEncryption rsaEncryption;
	
	
	
	public RSAEncryption getRsaEncryption() {
		return rsaEncryption;
	}


	@Autowired
	public void setRsaEncryption(RSAEncryption rsaEncryption) {
		this.rsaEncryption = rsaEncryption;
	}
	
	private String from;
	
	private String to;
	
	private String amount;
	
	private String signature;
	
	public Transaction(String from, String to, String amount, String privateKey) {
		
		this.from = from;
		this.to = to;
		this.amount = amount;
		this.signature = getRsaEncryption().sign(privateKey, message());
		
	}
	
	public String message() {
		
		return hash(from + to + amount);
		
	}
	
	public boolean isGenesisTrans() {
		return this.from == null ? true : false;
	}
	
	public boolean isValidSignature() {
		return isGenesisTrans() || getRsaEncryption().verify(this.from, this.signature, message());
	}
	
public  String hash(String message)  {
    	
    	MessageDigest digest  = null;
    	try {
		digest = MessageDigest.getInstance("SHA-256");
    	} catch (Exception e) {
    		System.out.println("Error happened during algorithm retrieval");	
    	}
		byte[] encodedhash = digest.digest(
				message.getBytes(StandardCharsets.UTF_8));
		
		return bytesToHex(encodedhash);
	    
	    
	}
	
	private  String bytesToHex(byte[] hash) {
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < hash.length; i++) {
	    String hex = Integer.toHexString(0xff & hash[i]);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}

}
