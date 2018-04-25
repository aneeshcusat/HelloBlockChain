package hellobc.stage1;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller is used to receive requests that does basic account operations
 * @author ageorg3
 *
 */
@RestController
public class HBCController {
	

	HashMap<String,Integer> balances = new HashMap<String,Integer>();
	{
	balances.put("ANG",100000);
	}
	
	public void printState() {
		System.out.println("************Balance Details*************");
		balances.forEach((X,Y)->{System.out.println("User: "+X + " Balance: "+Y);});
	}
	
    @RequestMapping(value="/balance",method=RequestMethod.GET)
    public void getBalances(@RequestParam(value="user") String name) {
    	System.out.println("Balance of" + name + "is: "+ balances.get(name));
    	printState();
		
    }
    
    @RequestMapping(value="/users",method=RequestMethod.POST)
    public void addUsers(@RequestBody UserBean user) {
    	balances.put(user.getName(), 0);
    	System.out.println("User: "+user.getName()+" Added");
    	printState();
		
    }
    
    @RequestMapping(value="/transfers",method=RequestMethod.POST)
    public void doTransfers(@RequestBody TransactionBean transaction) {
    	int amountInInt = new Integer(transaction.getAmount());
    	String from = transaction.getFrom();
    	String to = transaction.getTo();
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
