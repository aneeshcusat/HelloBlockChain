package hellobc.stage6;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Transaction {
	
	private String from;
	
	private String to;
	
	private Double amount = 0.0d;
	
	private String signature;
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Transaction(String from, String to, double amount, String privateKey) {
		
		this.from = from;
		this.to= to;
		this.amount = amount;
		try {
			this.signature = RSAEncryption.sign(privateKey,message());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public String message() throws NoSuchAlgorithmException{
		
		return HashAlgorithm.hash(from+to+amount.toString());
		
	}
	
	public boolean is_valid_signature() {
		if(genesis_txn()) return true;
		try {
			return RSAEncryption.verify(this.from, this.signature, message());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean genesis_txn(){
		if(this.from == null) return true;
		else return false;
	}
	
	public String toString(){
		try {
			return message();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	

}
