package hellobc.stage6;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;

public class BlockChain {
	
	LinkedList<Block> blockChain;
	
	public BlockChain(String creatorPublicKey, String creatorPrivatekey) throws NoSuchAlgorithmException {
		
		this.blockChain = new LinkedList<Block>();
		this.blockChain.add(new Block(creatorPublicKey, creatorPrivatekey));
		
	}
	
	public int length() {
		return blockChain.size();
	}
	
	public void addToChain(Transaction tx) throws NoSuchAlgorithmException {
		this.blockChain.add(new Block(this.blockChain.getLast(),tx));
		
	}
	
	public boolean isBlockChainValid() throws NoSuchAlgorithmException {
		return 
				this.blockChain.stream().allMatch(block -> block instanceof Block) 
				&& checkAllBlocksValid() 
				&& this.blockChain.stream().allMatch(block -> 
				   (block.getPrevBlockHash() == null) || (block.getPrevBlockHash() == block.getPrevBlock().getOwnHash())) 
				&& all_spends_valid();	
	}
	
	public String toString() {
		return this.blockChain.toString();
		
	}
	
	public boolean checkAllBlocksValid() throws NoSuchAlgorithmException {
		for(Block block : this.blockChain) {
			if(!block.isValid()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean all_spends_valid() {
		
		for(String key : compute_balances().keySet()) {
			if(compute_balances().get(key) < 0.0d) return false; 
		}
		
		return true;
		
	}

	public HashMap<String,Double> compute_balances() {
		HashMap<String,Double> balanceMap = new HashMap<String,Double>();
		for(Block block : this.blockChain) {
			if(block.getTransaction().getFrom()!= null) {
				Double amount = balanceMap.get(block.getTransaction().getFrom());
				if(amount != null) amount = amount - block.getTransaction().getAmount();
				else amount = 0.0d;
				balanceMap.put(block.getTransaction().getFrom(), amount);
			}
			Double amount =  balanceMap.get(block.getTransaction().getTo());
			if(amount != null) amount = amount + block.getTransaction().getAmount();
			else amount = 0.0d;
			balanceMap.put(block.getTransaction().getTo(), amount);
		}
		return balanceMap;
	}
}
