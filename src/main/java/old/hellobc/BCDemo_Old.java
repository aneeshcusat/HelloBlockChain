package old.hellobc;

public class BCDemo_Old {
	
public static void main(String [] args) throws Exception {
		
		BlockChain_Old bc = new BlockChain_Old("GenesisBlock");
		bc.addToChain("Cindrella");
		bc.addToChain("The 3 Stooges");
		bc.addToChain("Snow White");
		System.out.println("BC is: "+bc);
		System.out.println("Is BC Valid: "+bc.isBlockChainValid());
	}

}
