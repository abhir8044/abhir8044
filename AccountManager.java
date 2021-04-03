import java.util.ArrayList;

public class AccountManager implements FlagValues {

    static private ArrayList<Account> accs;

    public AccountManager() {
        accs = new ArrayList<>();

       
        for (int i = 0; i < 10; i++) {
            Account tempAcc = new Account();
            tempAcc.setAmount(10);
            tempAcc.setId(i);
            accs.add(tempAcc);
        }
        System.out.println("accounts initialized with $10.");

    }

   
    public int read(int accID,int transID){
        TransactionalServer.optMngr.enterWorkPhase(accs.get(accID),transID,READ_CC);
        Account acc = accs.get(accID);
        int bal = acc.getAmount();
        System.out.println("transID " + transID + " read account " + acc.getId());
        return bal;
    }

   
    public int write(int accID,int transID, int amt){
        TransactionalServer.optMngr.enterWorkPhase(accs.get(accID),transID,WRITE_CC);
        Account acc = accs.get(accID);
        acc.setAmount(amt);
        System.out.println("transID " + transID + " wrote $" + amt + " to " + acc.getId());
        return acc.getAmount();
    }

    
    public int getAllAccountTotal(){
        int tot = 0;
        for (int i=0; i<accs.size(); i++){
            tot += accs.get(i).getAmount();
        }
        return tot;
    }

}