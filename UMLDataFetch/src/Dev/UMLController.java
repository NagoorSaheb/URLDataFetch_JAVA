/*
Author: Nagoor Saheb Shaik
Description: 
This is controller class to implement XML Handler. This class calls the online XML page and extracts data.
checks periodically until the desired data element reaches expected value.
Notifies the user once teh value is reached.

*
*
*/



package Dev;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class UMLController {

	public static String currencyExchange;
	public static float targetRate;
	public static boolean verified = false;
	public static boolean targetReached = false;
	private static boolean runNew = true;
	
	public static void main(String[] args) {
   
		beginSearch();	
	}
		
  private static void beginSearch() {
		
	  XMLHandler xmlhandler = new XMLHandler();
	if(runNew)	{
	   do{
	       readInputs();
		   // verify for valid currency exchange rate ;  targetRate is not required as parameter, in case any new parameter need ot be checked, we can replace it.
		   verified = xmlhandler.verifyInputs(currencyExchange,targetRate);
		   if(!verified){
		       System.out.println("Please check the Currency exchange rate and target value provided");
			 }
		  }while(!verified);
       }
		// once the inputs are verified, run timer to check for notifications
		
      try {
			
        	Timer timer = new Timer();  // to check the values of xml every 5 secs
        	TimerTask myTask = new TimerTask() {
        	@Override
			public void run() {
				targetReached=  xmlhandler.targetCheck(currencyExchange,targetRate);
				if(targetReached){
					String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
					System.out.println("The "+currencyExchange +" Reached the expected Target Value "+targetRate + " at "+timeStamp);
					targetReached = false; // value reset;
					// timer will be stopped once the value is reached. to make new search or search again functionality available
					timer.cancel();
					timer.purge();
					
					//this is used to pause the timer
					/*try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					
					@SuppressWarnings("resource")
					Scanner sr = new Scanner(System.in);	
		    		System.out.print("Enter Y to run the search again and N to start new search or any other key to exit the application ");
		    		try {
		    			String response = sr.nextLine();
		    			
		    			if(response.equalsIgnoreCase("Y")){
		    				runNew = false;
		    				beginSearch();
		    			}
		    			else if (response.equalsIgnoreCase("N")){
		    				runNew = true;
		    				beginSearch();
		    			}
		    			else {
		    				System.exit(0);
		    			}
		    			
		    		}catch(Exception e){
		    			System.out.println("Please enter valid response");
		    		}
		      	}
			   }
        	};
        	timer.schedule(myTask,0, 30000);   // timer scheduled to check for target rate every 30 seconds
		} catch (Exception e) {
			System.out.println("Timer is encountered with a problem!");
			e.printStackTrace();
		}
	}

	public static void readInputs() {
		@SuppressWarnings("resource")
		Scanner sr = new Scanner(System.in);	
		System.out.print("Enter currency Exchange rate : ");
		try {
			currencyExchange = sr.nextLine();
		}catch(Exception e){
			System.out.println("Please enter valid currency Exchange");
		}
		System.out.print("Enter Target rate : ");
		try{
		    targetRate =  sr.nextFloat(); 
		}
		catch(InputMismatchException e)
		{
			System.out.println("Please enter valid Target Rate");
		}	
	}
}
