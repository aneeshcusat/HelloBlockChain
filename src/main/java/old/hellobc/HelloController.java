package old.hellobc;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HelloController {
	
	HashMap<String,Integer> balances = new HashMap<String,Integer>();
	{
	balances.put("ANG",100000);
	}
	
	public void printState() {
		System.out.println("Balance State");
		balances.forEach((X,Y)->{System.out.println("Key: "+X + "Value: "+Y);});
	}
	
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    
    @RequestMapping(value="/balance",method=RequestMethod.GET)
    public void getBalances(@RequestParam(value="user") String name) {
    	System.out.println("Balance of" + name + "is: "+ balances.get(name));
    	printState();
		
    }
    
    @RequestMapping(value="/users",method=RequestMethod.POST)
    public void addUsers(@RequestParam(value="user") String name) {
    	balances.put(name, 0);
    	System.out.println("User: "+name+" Added");
    	printState();
		
    }
    
    @RequestMapping(value="/transfers",method=RequestMethod.POST)
    public void doTransfers(@RequestParam(value="from") String from,
    		@RequestParam(value="to") String to,
    		@RequestParam(value="amount") String amount) {
    	int amountInInt = new Integer(amount);
    	if(balances.get(from)>=amountInInt) {
    		balances.put(from,balances.get(from)-amountInInt);
    		balances.put(to,balances.get(to)+amountInInt);
    		printState();
    	}
    	else {
    		throw new Error("Insuffcient Balance");
    	}
    	
		
    }
    
}
