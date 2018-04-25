package old.hellobc;
/*package hellobc.old;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimplCoin implements ApplicationRunner {
	
	
	private RSAEncryption rsaEncryption;
	
	
	
	public RSAEncryption getRsaEncryption() {
		return rsaEncryption;
	}


	@Autowired
	public void setRsaEncryption(RSAEncryption rsaEncryption) {
		this.rsaEncryption = rsaEncryption;
	}

	private BlockChain_Old blockChain = null;
	
	

	ConcurrentHashMap<String,String> concurrentMap = new ConcurrentHashMap<String,String>();
	Set<String> concurrentPeerSet = concurrentMap.newKeySet();
	
	KeyPair keyPair = rsaEncryption.buildKeyPair();
    PublicKey publicKey = keyPair.getPublic();
    PrivateKey privateKey = keyPair.getPrivate();


	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		if(args.getOptionValues("port") != null)
			concurrentPeerSet.add(args.getOptionValues("port").get(0));
		//port = args.getOptionValues("port").get(0);
		
		if(args.getOptionValues("peerport") == null) {
			// This node is the progenitor
			blockChain = new BlockChain_Old(msg);
		}
		peerPort = args.getOptionValues("peerport").get(0);
		
		System.out.println("Port is :"+port);
		
		System.out.println("peerPort is :"+peerPort);
		
		
		
		String randomMovie = mbList.get(rand.nextInt(250));
		//System.out.println("Random Movie is: "+ randomMovie);
		MovieBean favMovieBean = new MovieBean();
		favMovieBean.setMovieName(randomMovie);
		favMovieBean.setVersionNum(version);
		
		
		updateState(port,favMovieBean);
		
		System.out.println("Favourite movie forever is" + favMovieBean.getMovieName());
		
		MovieBean peerDummyBean = new MovieBean();	
			peerDummyBean.setMovieName("dummy");
			peerDummyBean.setVersionNum(0);
		updateState(peerPort,peerDummyBean);
		
		  
	}
}
*/