import java.util.ArrayList;

public class OptimisticController implements FlagValues {

    private Object object; // the object being protected by the lock// the transIDs of current holders
    private int curFlagVal; // the current type
    private ArrayList<Integer> curPos;
    private ArrayList<Integer> afterPos;

    
    public OptimisticController(Object object){
        this.object = object;
        this.curPos = new ArrayList<Integer>();
        this.afterPos = new ArrayList<Integer>();
    }

    
    public synchronized void Working_phase(int tranID, int posType){

        while(isConflict(tranID,posType)){
            try {
                afterPos.add(tranID);
                wait();
                afterPos.remove(afterPos.indexOf(tranID));
            }
            catch ( InterruptedException e){e.printStackTrace();}
        }

        if (curPos.isEmpty()) {
            curPos.add(tranID);
            curFlagVal = posType;
            System.out.println("Current transaction list is Empty");

        } else if (!curPos.contains(tranID)) {
           
            curPos.add(tranID);
            curFlagVal = posType;
            System.out.println("Current transaction list is not empty so adding to group");
         //validation if a particular transactions is having write in it before read committing it
        } else if ( !curPos.contains(tranID) && curFlagVal == WRITE_CC && posType == READ_CC ){
            
            curFlagVal = posType;
            System.out.println("Validating the transaction");
        }

        System.out.println("transID:" + tranID + "Validated the transaction" + posType + ".");

    }

    public synchronized void Update_Phase(int transID) {

        curPos.remove(curPos.indexOf(transID));

        if (curPos.isEmpty()){
            curFlagVal = EMPTY_CC;
        }
        notifyAll();
        System.out.println("Committed the transaction transID " + transID + ".");
    }

   
    public boolean isConflict(int transactionID, int operation){

       
        if (curPos.isEmpty()){
           
            System.out.println("No conflict yet.. Everything works good");
            return false;
        }
        else if (curPos.size() == 1 && curPos.contains(transactionID)){
            System.out.println("One Transaction : Conflict in transaction.");
            return false;
        }
        else if (curFlagVal == READ_CC && operation == READ_CC){
            System.out.println("No conflict occurs because it's READ-READ");
            return false;
        }
        else if (curFlagVal == WRITE_CC && operation == WRITE_CC){
            System.out.println("No conflict occurs because it's WRITE-WRITE");
            return false;
        }
        else if (curFlagVal == READ_CC && operation == WRITE_CC){
            System.out.println("No conflict occurs because it's READ-WRITE");
            return false;
        }
        else {
            System.out.println("Conflict OCcured for tentative transaction : WRITE:READ");
            return true;
        }
    }

    public ArrayList<Integer> getCurrentList(){
        return curPos;
    }
  
    public ArrayList<Integer> getListWaiting(){ return afterPos ;}
}