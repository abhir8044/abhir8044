import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import util.PropertyHandler;

public class TransactionalServer extends Thread{

  
    public static TransactionalManager transManager;
    public static AccountManager AccMngr;
    public static OptimisticControlManager optMngr;

   
    static ServerSocket serverSocket;
    
  
    private String hostNum="127.0.0.1";
    private int portNum = 5001;
    private Properties properties;
    public TransactionalServer(String serverPropertiesFile) {
        
        
        try {       
            serverSocket = new ServerSocket(portNum);
        
        } catch (Exception ex) {
            System.err.println(" Error: " + ex);
            ex.printStackTrace();
        }
        transManager = new TransactionalManager();

        AccMngr = new AccountManager();

        optMngr = new OptimisticControlManager();

    }

    public void run() {

        while (true) {
            System.out.println("Server waiting to accept a client on port " + portNum + "... ");
            try{
                
                transManager.runTrans(serverSocket.accept());
                System.out.println(" Socket accepted.");

            }catch (IOException e) {
                System.err.println(" Error: " + e);
                e.printStackTrace();
            }
        }
    }


  
    public static void main(String[] args) {
        
        TransactionalServer transServer;
        if(args.length == 1) {
            transServer = new TransactionalServer(args[0]);
        } else {
            transServer = new TransactionalServer("../../../config/Server.properties");
        }
        transServer.start();

        new Thread() {
            public void run(){
                try{
                    Thread.sleep(1000);
                }  catch (InterruptedException e){
                    System.out.println(" Error : " + e);
                }
                OptimisticController tempcont;
                ArrayList<Integer> tentativeTrans;
                HashMap<Account, OptimisticController> occ_controller  = optMngr.getTransInPhase();
               
                Iterator itr = occ_controller.entrySet().iterator();
                
                while (itr.hasNext()){
                    tempcont = (OptimisticController) ((HashMap.Entry) itr.next()).getValue(); // get locks from hash map
                    tentativeTrans = tempcont.getListWaiting();
                    for (int i=0; i< tentativeTrans.size(); i++){
                        System.out.print("TID: " + tentativeTrans.get(i));
                    }
                    System.out.print("\n");
                    itr.remove();
                }
            }


        }.start();
    }
}