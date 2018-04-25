package hellobc.stage2;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RestClient {
	
 
	 
	 private ClientHttpRequestFactory getClientHttpRequestFactory() {
	     int timeout = 5000;
	     HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
	       = new HttpComponentsClientHttpRequestFactory();
	     clientHttpRequestFactory.setConnectTimeout(timeout);
	     return clientHttpRequestFactory;
	 }
	 
	 RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
	 
	 
	 public String call(String port, String requestBody) throws Exception {
		 
		 	ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory();
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			HttpEntity<String> request = new HttpEntity<>(requestBody);
			
			return restTemplate.postForObject("http://localhost:"+port+"/gossip", request, String.class);
			
	 }
	 
	 public String pingNode(String port) throws Exception {
		 
		 ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory();
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			return restTemplate.getForObject("http://localhost:"+port+"/ping", String.class);
	 }

}
