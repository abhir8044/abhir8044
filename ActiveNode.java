package assignment;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ActiveNode extends Thread {
    Node_Info n_inf = new Node_Info();
    private ServerSocket serversocket;
    private Set<ActiveNodeWorker> serverThreadThreads = new HashSet<ActiveNodeWorker>();
    public ActiveNode() {}
    public ActiveNode(int portno) throws IOException{
    	serversocket = new ServerSocket(portno);//Used for creating a socket and passing port number
    }
    public void run() {
    	try {
    		while(true) {
    			ActiveNodeWorker act_nodeworker = new ActiveNodeWorker(serversocket.accept(),this);
    			serverThreadThreads.add(act_nodeworker);
    			act_nodeworker.start();	
    		}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
  }
    public void sendMessage(String message) {
       try {
    	   serverThreadThreads.forEach(t->t.getPrintWriter().println(message)); 	   
       }
       catch(Exception e) {
    	   e.printStackTrace();
       } 	
    }
    public Set<ActiveNodeWorker> getServerThreadThreads(){
    	 return serverThreadThreads;  	
    }
	boolean Join(String LogicalName,int portno) {		
		return true;
	}
}