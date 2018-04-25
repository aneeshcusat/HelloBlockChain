package old.hellobc;

import java.util.LinkedList;

public class BlockChain_Old {
	
	LinkedList<Block_Old> blockChain;
	
	public BlockChain_Old(String msg) {
		
		this.blockChain = new LinkedList<Block_Old>();
		this.blockChain.add(new Block_Old(null,msg));
		
	}
	
	public void addToChain(String msg) {
		this.blockChain.add(new Block_Old(this.blockChain.getLast(),msg));
		System.out.println(this.blockChain.getLast());
		
	}
	
	public boolean isBlockChainValid() {
		return 
				this.blockChain.stream().allMatch(block -> block instanceof Block_Old) &&
				this.blockChain.stream().allMatch(block -> block.isValid()) &&
				this.blockChain.stream().allMatch(block -> (block.getPrevBlockHash() == null) || (block.getPrevBlockHash() == block.getPrevBlock().getOwnHash())) ;	
	}
	
	public String toString() {
		return this.blockChain.toString();
	}
	

}
