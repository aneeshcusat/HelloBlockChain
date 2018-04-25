package old.hellobc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class GossipController implements ApplicationRunner // ,EmbeddedServletContainerCustomizer 
{
	private RestClient restClient;
	
	public RestClient getRestClient() {
		return restClient;
	}

	@Autowired
	public void setRestClient(RestClient restClient) {
		this.restClient = restClient;
	}

	// HashMap to save the state of different nodes
	ConcurrentHashMap<String,MovieBean> state = new ConcurrentHashMap<String,MovieBean>();
	
	// The port to start server.
	String port = null;
	
	// Port of the first node to connect too
	String peerPort= null;
	
	// Holds the version number to keep track if which data is most uptodate
	int version = 0;
	
	//MovieBean favMovieBean = new MovieBean();
	// List to hold movieNames
	ArrayList<String> mbList = new ArrayList<String>();
	
	Random rand = new Random(); 
	 
	// Load movies from file to memory list
	 {
		 
		Scanner sc;
		
		String currentLine = null;
		try {
			sc = new Scanner(new File("movies.txt")).useDelimiter("\n");
		
        while (sc.hasNext())
        {
        	 currentLine = sc.next();
        	 mbList.add(currentLine);
        }
        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("Size of movieList is: "+mbList.size());
		
		
		 
	}
	
	 
	
	 
	 public void updateState(String port, MovieBean movieBean) {
		 
			//System.out.println("Updating state");
		 
		 if(port == null) return;
		 
		 
			
			MovieBean tempMvBean = state.get(port);
			
			
			
			if(tempMvBean == null || tempMvBean.getMovieName() == null ) {
				
				/*System.out.println("Adding new entry");
				System.out.println("Adding new entry Favourite movie of "+ port+ " is: "+movieBean.getMovieName() +
						" Version3 is: "+movieBean.getVersionNum());
				*/
				state.put(port, movieBean);
			} else if (movieBean.getVersionNum() > tempMvBean.getVersionNum()) {
				/*System.out.println("Updating entry");
			System.out.println("Updating Favourite movie of "+ port+ " is: "+movieBean.getMovieName() +
					" Version4 is: "+movieBean.getVersionNum());*/
			state.put(port, movieBean);
			
			
			}
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {
			//System.out.println("Command-line arguments: {}"+ Arrays.toString(args.getSourceArgs()));
			
			
			//System.out.println("Option Names: {}");
			
			//System.out.println("Non Option Args: {}");
	        /*for (String name : args.getNonOptionArgs()){
	        	System.out.println("arg-" + name + "=" + args.getOptionValues(name));
	        }
			
	        for (String name : args.getOptionNames()){
	        	System.out.println("arg-" + name + "=" + args.getOptionValues(name));
	        }*/
			
	        if(args.getOptionValues("port") != null)
			port = args.getOptionValues("port").get(0);
			
			if(args.getOptionValues("peerport") != null)
			peerPort = args.getOptionValues("peerport").get(0);
			
			/*System.out.println("Port is :"+port);
			
			System.out.println("peerPort is :"+peerPort);*/
			
			
			
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
		
	 

	 
	 
	@Scheduled(fixedRate = 8000)
    public void changeFavouriteMovie() {
       // System.out.println("Invoking Scheduler");
        String randomMovie = mbList.get(rand.nextInt(250));
		// System.out.println("Random Movie is: "+ randomMovie);
		MovieBean favMovieBean = new MovieBean();
		favMovieBean.setMovieName(randomMovie);
		version = version+1;
		favMovieBean.setVersionNum(version);
		System.out.println("Favourite movie changes to: "+favMovieBean.getMovieName());
		
		updateState(port,favMovieBean);
    }
	
	/*@Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
 
		System.out.println("Setting server port");
        container.setPort(new Integer(port));
 
    }*/
	
	
	
	
	@Scheduled(fixedRate = 3000)
	public void callPeer() {
		state.forEach((port, movieBean) -> {
			
			if(port == this.port) return;
				
		String requestJson = "";
		String peerResponseJson = "";
		ObjectMapper mapper = new ObjectMapper();
		Map<String, MovieBean> peerResponseMap = new HashMap<String, MovieBean>();
		try {
			requestJson = mapper.writeValueAsString(state);
            //System.out.println("Map JSON: "+requestJson);
			
			peerResponseJson = restClient.call(port, requestJson);
			
			if (peerResponseJson == "") return;
			peerResponseMap = mapper.readValue(peerResponseJson, new TypeReference<Map<String, MovieBean>>(){});
			
			peerResponseMap.forEach((port1,movieBean1)-> updateState(port1,movieBean1));
        
		}
		catch(Exception e) {
			System.out.println("Error while connecting to peer port :"+port+" .Removing port"
					+ " from list");
			state.remove(port);
			
			System.out.println("Printing state after removing port*************************************************");
			state.forEach((port1,movieBean1)->System.out.println(port1 + " likes " + movieBean1.getMovieName()));
			System.out.println("*************************************************");
			
		}
		
			
		});
		
		// System.out.println("Printing state");
		System.out.println("*************************************************");
		state.forEach((port,movieBean)->System.out.println(port + " likes " + movieBean.getMovieName()));
		System.out.println("*************************************************");
	}
	
    @RequestMapping(value="/gossip",method=RequestMethod.POST)
    public ResponseEntity<?> gossip(@RequestBody String inputJson) {
    	System.out.println("Input JSON: "+inputJson);
    	
    	Map<String, MovieBean> map = new HashMap<String, MovieBean>();
    	ObjectMapper mapper = new ObjectMapper();
    	String responseJson="";
		// convert JSON string to Map
		try {
			map = mapper.readValue(inputJson, new TypeReference<Map<String, MovieBean>>(){});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	map.forEach((port,movieBean)-> updateState(port,movieBean));
    	try {
    		responseJson = mapper.writeValueAsString(state); 
		
       // System.out.println("Map JSON: "+outputJson);
    	}
        catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
    	} 
    	
    	
    	return new ResponseEntity<String>(responseJson, HttpStatus.OK);
		
    }
	

}

