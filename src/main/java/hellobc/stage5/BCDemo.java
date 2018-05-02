package hellobc.stage5;

public class BCDemo {
	
public static void main(String [] args) throws Exception {
		
		BlockChain bc = new BlockChain("GenesisBlock");
		bc.addToChain("Cindrella");
		bc.addToChain("The 3 Stooges");
		bc.addToChain("Snow White");
		System.out.println("BC is: "+bc);
		System.out.println("Is BC Valid: "+bc.isBlockChainValid());
	}

}
