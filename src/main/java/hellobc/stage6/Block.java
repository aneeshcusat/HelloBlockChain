package hellobc.stage6;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {

	private  String NUM_OF_ZEROES = "0000";
	
	private Transaction transaction;
	
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
	
	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Block (Block prevBlock, Transaction transaction) throws NoSuchAlgorithmException {
		
		if (prevBlock != null ) { 
			this.prevBlockHash = prevBlock.getOwnHash();
			this.prevBlock = prevBlock;
			}
		this.transaction = transaction;
		mineBlock();
		
	}
	
	/**
	 * Genesis block constructor
	 * @param publicKey
	 * @param privateKey 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public  Block(String publicKey, String privateKey) throws NoSuchAlgorithmException {
		Transaction genesisTransaction = new Transaction(null, publicKey, 1000, privateKey);
		this.prevBlock = null;
		this.transaction = genesisTransaction;
	}
	
	public void mineBlock() throws NoSuchAlgorithmException {
		this.nonce = calcNonce();
		this.ownHash = HashAlgorithm.hash(fullBlock(this.nonce));
	}
	
	
    public String calcNonce() throws NoSuchAlgorithmException  {
		
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
    
    
    public  boolean isValidNonce(String nonce) throws NoSuchAlgorithmException  {
		
		return HashAlgorithm.hash(fullBlock(nonce)).startsWith(NUM_OF_ZEROES);
	}
    
    public String fullBlock(String nonce) {
    	return this.transaction.toString()+this.prevBlockHash+nonce;
    }
    
	
	public boolean isValid() throws NoSuchAlgorithmException {
		return isValidNonce(this.nonce);
	}
	
	public String toString() {
		return String.join("\n", "---------------------------------------------------------------------------------------------------------------------------",
				"Previous hash: "+this.prevBlockHash,
				"Message: "+this.transaction.toString(),
				"Nonce: "+this.nonce,
				"Own Hash: "+this.ownHash,
				"---------------------------------------------------------------------------------------------------------------------------",
				"|",
				"|",
				"-");
	}
	
}
