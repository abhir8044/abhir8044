import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class OptimisticControlManager implements FlagValues {

    private HashMap<Account, OptimisticController> conts;

   
    public OptimisticControlManager(){
        conts = new HashMap();
    }

    public void enterWorkPhase(Account acc, int transID, int op){

        OptimisticController controller;

        synchronized(this){
            controller = conts.get(acc);

            if (controller == null){
                controller = new OptimisticController (acc);
                conts.put(acc,controller);
                System.out.println("Enter Transaction to Working phase " + acc.getId());
            }

            controller.Working_phase(transID,op);
            System.out.println("Enter Work Phase " + op + " set on TID " + transID + ".");
        }
    }


    public synchronized void Change_Commit(int transID) {

        OptimisticController tempCont;
        ArrayList<Integer> transIDs;

        Iterator itr = conts.entrySet().iterator();
        while (itr.hasNext()){;
            tempCont = (OptimisticController) ((HashMap.Entry) itr.next()).getValue();
            transIDs = tempCont.getCurrentList();
            if ( transIDs.contains(transID) ){
                tempCont.Update_Phase(transID);
            }
            itr.remove();
        }
        System.out.println("Transaction is in validationPhase - Updated");
    }

    public HashMap<Account, OptimisticController> getTransInPhase(){
        return this.conts;
    }

}