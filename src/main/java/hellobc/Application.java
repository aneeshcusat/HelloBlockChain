package hellobc;

import java.util.Arrays;
import java.util.HashMap;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application  { 
	
	
    public static void main(String[] args) {
       ApplicationContext ctx = SpringApplication.run(Application.class, args);
        
    	
        System.out.println("Let's inspect the beans provided by Spring Boot:");
        
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
    
  
}

// private String port;


/*HashMap<String, Object> props = new HashMap<>();
props.put("server.port", 9999);

ApplicationContext ctx = new SpringApplicationBuilder()
.sources(Application.class)                
.properties(props)
.run(args);*/

/* @Override
public void customize(ConfigurableEmbeddedServletContainer container) {

	System.out.println("Setting server port");
    container.setPort(new Integer(port));

}*/

/*	@Override
public void run(ApplicationArguments args) throws Exception {
	System.out.println("Command-line arguments: {}"+ Arrays.toString(args.getSourceArgs()));
	
	
	System.out.println("Option Names: {}");
	
	System.out.println("Non Option Args: {}");
    for (String name : args.getNonOptionArgs()){
    	System.out.println("arg-" + name + "=" + args.getOptionValues(name));
    }
	
    for (String name : args.getOptionNames()){
    	System.out.println("arg-" + name + "=" + args.getOptionValues(name));
    }
	
    if(args.getOptionValues("port") != null)
	port = args.getOptionValues("port").get(0);
	
	
	
	System.out.println("Port is :"+port);
	
	System.out.println("peerPort is :"+peerPort);
	
	
	
	String randomMovie = mbList.get(rand.nextInt(250));
	System.out.println("Random Movie is: "+ randomMovie);
	MovieBean favMovieBean = new MovieBean();
	favMovieBean.setMovieName(randomMovie);
	favMovieBean.setVersionNum(version);
	
	
	if(port != null)
	updateState(port,favMovieBean);
	
	if(peerPort != null) {
		MovieBean peerDummyBean = new MovieBean();	
		peerDummyBean.setMovieName("dummy");
		peerDummyBean.setVersionNum(0);
	updateState(peerPort,peerDummyBean);
	}
	  
}
*/
