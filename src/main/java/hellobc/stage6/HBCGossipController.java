package hellobc.stage6;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class HBCGossipController implements ApplicationRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(HBCGossipController.class);

	private RestClient restClient;

	public RestClient getRestClient() {
		return restClient;
	}

	@Autowired
	public void setRestClient(RestClient restClient) {
		this.restClient = restClient;
	}

	@Autowired
	Environment environment;

	// HashMap to save the state of different nodes
	ConcurrentHashMap<String, MovieBean> state = new ConcurrentHashMap<String, MovieBean>();

	// The port in which server is started.
	String port = null;

	// Port of the first node to connect too
	String peerPort = null;

	// Holds the version number to keep track if which data is most uptodate
	int version = 0;

	// MovieBean favMovieBean = new MovieBean();
	// List to hold movieNames
	ArrayList<String> mbList = new ArrayList<String>();

	Random rand = new Random();

	/**
	 * This method will run when application starts up
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		Scanner sc;

		/**
		 * Load the movies into a map for processing
		 */
		String currentLine = null;
		try {
			sc = new Scanner(new File("resources/movies.txt")).useDelimiter("\n");

			while (sc.hasNext()) {
				currentLine = sc.next();
				mbList.add(currentLine);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LOGGER.debug("Size of movieList is: " + mbList.size());

		// Putting port and peer port to a map with a random movie, so that the
		// ports will be available for gossip protocol communication
		port = environment.getProperty("local.server.port");

		LOGGER.info("Port is :" + port);

		if (args.getOptionValues("peerport") != null)
			peerPort = args.getOptionValues("peerport").get(0);

		LOGGER.info("peerPort is :" + peerPort);

		String randomMovie = mbList.get(rand.nextInt(250));
		LOGGER.debug("Random Movie is: " + randomMovie);
		MovieBean favMovieBean = new MovieBean();
		favMovieBean.setMovieName(randomMovie);
		favMovieBean.setVersionNum(version);

		state.put(port, favMovieBean);

		if (peerPort != null)
		state.put(peerPort, favMovieBean);

	}

	/**
	 * Method update state map with the updated movie for each port from
	 * broadcasts, if the movie is not the latest one, determined based on
	 * version, then the map is not updated
	 * 
	 * @param port
	 * @param movieBean
	 */
	public void updateState(String port, MovieBean movieBean) {

		LOGGER.debug("Updating state");

		if (port == null)
			return;
		if(port != this.port) {
		try {
		restClient.pingNode(port);
		}
		catch (Exception e) {
			LOGGER.debug("Error while pinging to peer port :" + port + " .Removing port" + " from map");
			state.remove(port);
		
		}
		}
		
		MovieBean tempMvBean = state.get(port);

		if (tempMvBean == null || tempMvBean.getMovieName() == null) {

			LOGGER.debug("Adding new entry Favourite movie of " + port + " is: " + movieBean.getMovieName()
					+ " Version is: " + movieBean.getVersionNum());

			state.put(port, movieBean);
		} else if (movieBean.getVersionNum() > tempMvBean.getVersionNum()) {
			LOGGER.debug("Updating entry");
			LOGGER.debug("Updating Favourite movie of " + port + " is: " + movieBean.getMovieName() + " Version is: "
					+ movieBean.getVersionNum());
			state.put(port, movieBean);

		}
		else if(port == this.port) {
			this.version = tempMvBean.getVersionNum();
		}
	}

	/**
	 * Scheduler to change the movie choice of a node at intervals
	 */
	@Scheduled(fixedRate = 8000)
	public void changeFavouriteMovie() {
		LOGGER.debug("Invoking Scheduler");
		if (mbList.isEmpty())
			return;
		String randomMovie = mbList.get(rand.nextInt(250));
		LOGGER.debug("Random Movie is: " + randomMovie);
		MovieBean favMovieBean = new MovieBean();
		favMovieBean.setMovieName(randomMovie);
		version = version + 1;
		favMovieBean.setVersionNum(version);
		LOGGER.info("Favourite movie changes to: " + favMovieBean.getMovieName() +" & Version is: "+favMovieBean.getVersionNum());

		state.put(port, favMovieBean);
	}

	/**
	 * Scheduler that runs at fixed interval that communicates to every node in
	 * the state map , gets the gossip response from each node and also passes
	 * its response map to other nodes
	 */
	@Scheduled(fixedRate = 3000)
	public void callPeer() {
		state.forEach((port, movieBean) -> {

			if (port == this.port)
				return;

			String requestJson = "";
			String peerResponseJson = "";
			ObjectMapper mapper = new ObjectMapper();
			Map<String, MovieBean> peerResponseMap = new HashMap<String, MovieBean>();
			try {
				requestJson = mapper.writeValueAsString(state);
				LOGGER.debug("Map JSON for port: "+port+" is: " + requestJson);

				peerResponseJson = restClient.call(port, requestJson);

				if (peerResponseJson == "")
					return;
				peerResponseMap = mapper.readValue(peerResponseJson, new TypeReference<Map<String, MovieBean>>() {
				});

				peerResponseMap.forEach((nodePort, nodeMovieBean) -> updateState(nodePort, nodeMovieBean));

			} catch (Exception e) {
				LOGGER.error("Error while connecting to peer port :" + port + " .Removing port" + " from map");
				state.remove(port);
			
			}

		});

		LOGGER.debug("Printing state");
		LOGGER.info("*************************************************");
		state.forEach((port, movieBean) -> LOGGER.info(port + " likes " + movieBean.getMovieName()));
		LOGGER.info("*************************************************");
	}

	@RequestMapping(value = "/gossip", method = RequestMethod.POST)
	public ResponseEntity<?> gossip(@RequestBody String inputJson) {
		LOGGER.debug("Input JSON: " + inputJson);

		Map<String, MovieBean> map = new HashMap<String, MovieBean>();
		ObjectMapper mapper = new ObjectMapper();
		String responseJson = "";
		// convert JSON string to Map
		try {
			map = mapper.readValue(inputJson, new TypeReference<Map<String, MovieBean>>() {
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.forEach((port, movieBean) -> updateState(port, movieBean));
		try {
			responseJson = mapper.writeValueAsString(state);

			LOGGER.debug("Map JSON: " + responseJson);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return new ResponseEntity<String>(responseJson, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public ResponseEntity<?> ping() {
		LOGGER.debug("Ping Success");
		return new ResponseEntity<String>("Success", HttpStatus.OK);
		
	}

}
