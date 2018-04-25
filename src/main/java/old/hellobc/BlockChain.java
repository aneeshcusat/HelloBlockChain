package old.hellobc;

import java.util.LinkedList;

public class BlockChain {
	
	LinkedList<Block> blockChain;
	
	public BlockChain(String msg) {
		
		this.blockChain = new LinkedList<Block>();
		this.blockChain.add(new Block(null,msg));
		
	}
	
	public void addToChain(String msg) {
		this.blockChain.add(new Block(this.blockChain.getLast(),msg));
		System.out.println(this.blockChain.getLast());
		
	}
	
	public boolean isBlockChainValid() {
		return 
				this.blockChain.stream().allMatch(block -> block instanceof Block) &&
				this.blockChain.stream().allMatch(block -> block.isValid()) &&
				this.blockChain.stream().allMatch(block -> (block.getPrevBlockHash() == null) || (block.getPrevBlockHash() == block.getPrevBlock().getOwnHash())) ;	
	}
	
	public String toString() {
		return this.blockChain.toString();
	}
	

}
