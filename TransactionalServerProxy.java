	import java.io.*;
	import java.net.*;

	
	public class TransactionalServerProxy implements MessageTypes{

	    private String hostNum;
	    private int portNum;
	    private Socket sckt;

	    public TransactionalServerProxy(String host, int port) throws IOException{
	        this.hostNum = host;
	        this.portNum = port;
	        this.sckt = new Socket(this.hostNum,this.portNum);
	    }

	    public int openTrans() throws IOException,ClassNotFoundException{
	        int num;

	        
	        ObjectOutputStream write = new ObjectOutputStream(this.sckt.getOutputStream());
	        ObjectInputStream read = new ObjectInputStream(this.sckt.getInputStream());

	        Message mes = new Message();
	        mes.setType(OPEN_TRANSACTION);
                
	        write.writeObject(mes);
	        num = (int) read.readObject();

	        System.out.println(" trans # " + num + " is open.");

	        return num;
	    }

	    public void closeTrans() throws IOException, ClassNotFoundException{

	        ObjectOutputStream write = new ObjectOutputStream(this.sckt.getOutputStream());

	        Message message = new Message();
	        message.setType(CLOSE_TRANSACTION);

	        write.writeObject(message);

	    }

	    public int read(int accountID) throws IOException, ClassNotFoundException{

	        int bal = 0;

	        ObjectInputStream read = new ObjectInputStream(this.sckt.getInputStream());
	        ObjectOutputStream write = new ObjectOutputStream(this.sckt.getOutputStream());

	        Message message = new Message();
	        message.setType(READ_REQ);
	        Serializable_objects sObject = new Serializable_objects();
	        sObject.num = accountID;
	        message.setContent(sObject);

	        write.writeObject(message);
	        bal = (int) read.readObject();

	        System.out.println("Account" + accountID + ": $" + bal + ".");

	        return bal;

	    }
 public int write (int accID, int amt) throws IOException, ClassNotFoundException{

	        int bal;

	        ObjectInputStream read = new ObjectInputStream(this.sckt.getInputStream());
	        ObjectOutputStream write = new ObjectOutputStream(this.sckt.getOutputStream());

	        Serializable_objects sObjs = new Serializable_objects();
	        sObjs.num = accID;
	        sObjs.amounts = amt;

	        Message mes = new Message();
	        mes.setType(WRITE_REQ);
	        mes.setContent(sObjs);

	        write.writeObject(mes);
	        bal = (int) read.readObject();

	        System.out.println("$" + amt + " written to account " + accID);

	        return bal;

	    }
	    public int getAllAcountTotal() throws IOException, ClassNotFoundException{

	        int bal;

	        ObjectInputStream read = new ObjectInputStream(this.sckt.getInputStream());
	        ObjectOutputStream write = new ObjectOutputStream(this.sckt.getOutputStream());

	        Message mes = new Message();
	        mes.setType(ACC_REQ);

	        write.writeObject(mes);
	        bal = (int) read.readObject();

	        System.out.println("$" + bal);

	        return bal;

	    }

	}

