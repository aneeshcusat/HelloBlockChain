package hellobc;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloBCController {

    @RequestMapping("/")
    public String index() {
    	System.out.println("Test");
        return "Greetings from HBC Spring Boot!";
    }

}