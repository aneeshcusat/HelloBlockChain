package old.hellobc;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Block {

	private  String NUM_OF_ZEROES = "0000";
	
	private String message;
	
	private String prevBlockHash;
	
	private String ownHash;
	
	private String nonce;
	
	private Block prevBlock;
	
	public Block getPrevBlock() {
		return prevBlock;
	}

	public String getPrevBlockHash() {
		return prevBlockHash;
	}

	public String getOwnHash() {
		return ownHash;
	}

	public Block (Block prevBlock, String msg) {
		
		if (prevBlock != null ) { 
			this.prevBlockHash = prevBlock.getOwnHash();
			this.prevBlock = prevBlock;
			}
		this.message = msg;
		
		mineBlock();
		
	}
	
	/*public Block createGenesisBlock() {
		
		
	}*/
	
	public void mineBlock() {
		this.nonce = calcNonce();
		this.ownHash = hash(fullBlock(this.nonce));
	}
	
	
    public String calcNonce()  {
		
    	String nonce = "HELP I'M TRAPPED IN A NONCE FACTORY";
		
		int count = 0;
		
		while(!isValidNonce(nonce)) {
			
			nonce = StringUtils.next(nonce);
			
			count = count +1;
		}
		
		System.out.println("Nonce :"+nonce);
		
		System.out.println("Count :"+count);
		
		return nonce;
		
	}
    
    
    public  boolean isValidNonce(String nonce)  {
		
		return hash(fullBlock(nonce)).startsWith(NUM_OF_ZEROES);
	}
    
    public String fullBlock(String nonce) {
    	return this.message+this.prevBlockHash+nonce;
    }
    
    
    
	
	public boolean isValid() {
		return isValidNonce(this.nonce);
	}
	
	public String toString() {
		return String.join("\n", "---------------------------------------------------------------------------------------------------------------------------",
				"Previous hash: "+this.prevBlockHash,
				"Message: "+this.message,
				"Nonce: "+this.nonce,
				"Own Hash: "+this.ownHash,
				"---------------------------------------------------------------------------------------------------------------------------",
				"|",
				"|",
				"↓");
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
