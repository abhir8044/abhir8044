import java.util.Properties;
import util.PropertyHandler;

public class TransactionalClient extends Thread{
	   
	    private Properties properties;
	    private String host = "127.0.0.1";
	    private int portNum = 5001;
	    private int totalAccounts = 10;
            public TransactionalClient() {
            System.out.println(" Host: " + host);
            System.out.println(" Port: " + portNum);
            }
	   
	    public TransactionalClient(String serverPropertiesFile) {
              
	        try {
                    
	           /* properties = new PropertyHandler(serverPropertiesFile);
	            host = properties.getProperty("HOST");
	            System.out.println(" Host: " + host);
	            port = Integer.parseInt(properties.getProperty("PORT"));
	            System.out.println("Port: " + port);
	            numberOfAccounts = Integer.parseInt(properties.getProperty("NUMBER_OF_ACCOUNT"));
*/
	        } catch (Exception ex) {
	            System.err.println("Error: " + ex);
	            ex.printStackTrace();
	        }
	    }

	   
	    @Override
	    public void run() {
	        try {
	            
	            TransactionalServerProxy transactionalServerProxy = new TransactionalServerProxy(host,portNum);

	            int transID = transactionalServerProxy.openTrans();

	            
	            transactionalServerProxy.getAllAcountTotal();

	            System.out.println(" Trans #" + transID + " opened in client.");
	            int withdrawnAcc = (int) Math.floor( Math.random() * totalAccounts);
	            int depositedAcc = (int) Math.floor( Math.random() * totalAccounts);

	            int transferAmount = (int) Math.ceil( Math.random() );
	          
	            int amountFrom = transactionalServerProxy.read(withdrawnAcc);
	       
	            int amountFromRemain = transactionalServerProxy.write(withdrawnAcc, amountFrom - transferAmount);
	            
	            int accountTo = transactionalServerProxy.read(depositedAcc);
	            
	            int amountToRemain = transactionalServerProxy.write(depositedAcc, accountTo + transferAmount );
	            
	            System.out.println(" Account " + withdrawnAcc + " deposited $" + transferAmount + " to account " + depositedAcc );
	            System.out.println("Withdrawn Account " + withdrawnAcc + " = $" + amountFromRemain + "," + depositedAcc + " =$" + amountToRemain);
	           
	            transactionalServerProxy.getAllAcountTotal();
	            
	            transactionalServerProxy.closeTrans();

	        }catch (Exception e) {
	            System.err.println("Error: " + e);
	            e.printStackTrace();
	        }
	    }
	    
	    public static void main(String[] args) {
	        
	        for (int i=0;i<=10;i++){
	            TransactionalClient c = new TransactionalClient("../../../config/Server.properties");
	            c.start();
	        }
	        
	    }  
	}

