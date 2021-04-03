import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class TransactionalManager {

    private static int transactional_Count;
    private static ArrayList<Trans> transactions;

    TransactionalManager(){
        transactional_Count = 0;
        transactions = new ArrayList();
    }

    public void runTrans(Socket client){
        System.out.println("running Transaction");
        int transID = transactional_Count;
        transactional_Count++;
        Trans trans = new Trans(client, transID);
        transactions.add(trans);
        trans.start();
    }

   
    private class Trans extends Thread implements MessageTypes{

        private int trans_ID;
        private Socket client;
        private ObjectInputStream read;
        private ObjectOutputStream write;
        private Message message;
        public int trans_cnt = 20;

       Trans(Socket client, int TID){
            System.out.println("constructor() called in TransManager");
            this.trans_ID = TID;
            this.client = client;
        }

        @Override
        public void run() {
            System.out.println("run() called TransManager.");

            for(int i = 0 ; i < trans_cnt ; i++){
                try {
                    write = new ObjectOutputStream(client.getOutputStream());
                    read = new ObjectInputStream(client.getInputStream());

                    message = (Message) read.readObject();

                    Serializable_objects s_obj = (Serializable_objects) message.getContent();
                    int fromAccountID;
                    int toAccountID;
                    int amount;
                    int balance;

                    switch (message.getType()) {
                        
                        case OPEN_TRANSACTION:
                            write.writeObject(trans_ID);
                            System.out.println(" OPEN_TRANSACTION "+ trans_ID +".");
                            break;

                        case CLOSE_TRANSACTION:
                            transactions.remove(this);
                            transactional_Count--;
                            TransactionalServer.optMngr.Change_Commit(trans_ID);
                            System.out.println(" CLOSE_TRANSACTION "+ trans_ID +".");
                            return;
                        case READ_REQ:
                            fromAccountID = (int) s_obj.num;
                            balance = TransactionalServer.AccMngr.read(fromAccountID,trans_ID);
                            write.writeObject(balance);
                            System.out.println(" READ_REQUEST -> account " + fromAccountID + ": $" + balance + ".");
                            break;

                        case WRITE_REQ:
                            toAccountID = (int) s_obj.num;
                            amount = (int) s_obj.amounts;
                            balance = TransactionalServer.AccMngr.write(toAccountID,trans_ID,amount);
                            write.writeObject(balance);
                            System.out.println("WRITE_REQUEST to account " + toAccountID + ": $" + amount + ".");
                            break;
                        case ACC_REQ:
                            balance = TransactionalServer.AccMngr.getAllAccountTotal();
                            write.writeObject(balance);
                            System.out.println(" ACCOUNT_TOTAL_REQUEST " + balance);
                            break;
                        default:
                            System.err.println("Invalid Option");
                    }
                }
                catch (Exception ex) {
                    System.err.println(" Message could not be read from object stream.");
                    System.exit(1);
                }
            }
        }
    }


}