package assignment;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import javax.json.Json;
import java.io.StringWriter;
import java.net.Socket;

public class Main {
	
	public static void main(String args[]) throws Exception {
		
		char input;
		
		System.out.println("Enter the operation to perform :\n"+ " j - JOIN \n l - LEAVE\n");
		Scanner sc = new Scanner(System.in);
		input = sc.next().charAt(0);//Takes the user input to run the following chunk
		
		if(input == 'j'){ //when the user wishes to join the chat 
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Node_Info ni = new Node_Info();
			System.out.println( "Please Enter Your Name:");
			ni.name = br.readLine();
			
			System.out.println("Enter The Port Number That You Want To Connect To:");//we are running LocalHost as Hostname and a random port.no
			ni.portno = Integer.parseInt(br.readLine());
			
			ActiveNode an = new ActiveNode(ni.portno);//Here a socket is created for port number entered
			an.start();
			
			ni.hasPredecessor = 0;
			ni.hasSucessor = 0;
			new Main().updateListentoMains(br,ni.name,an,ni);
		}
		else if(input =='l') {//when user wishes to exit
			System.out.println("exiting the application");
			System.exit(0);
		}
		else {
			System.err.println("Invalid input please give the correct input");//if user enters invalid inputs
			Recursion r=new Recursion();//we exit the program if invalid i/p is given more than once
			r.rec();
		}
		sc.close();
}
	public void updateListentoMains(BufferedReader buf_reader,String username,ActiveNode active_Node,Node_Info node) throws  Exception{
		System.out.println("Enter the hostname and portno you wanna chat with:");//Here the user specifies with whom they are ready to communicate
		System.out.println("Press 's' to Skip Messaging");//If user wishes to skip messaging
		
		String inputs = buf_reader.readLine();
		String[] inputvalues = inputs.split(" ");
		
		if(!inputs.equals("s")) 
			for(int i = 0 ; i < inputvalues.length; i++) {
				String[] address = inputvalues[i].split(":");
				System.out.println("address" + address[1]);
		        Socket Predecessor_node = null;
		        Socket Sucessor_node = null;
		        try {
		        	if(node.hasPredecessor == 0) {//This has been intialised in Node_Info file and we are calling over here
		        	Node_Info Predecessor = new Node_Info();
		        	Predecessor.portno = Integer.valueOf(address[1]);
		        	Predecessor.name = address[0];
		        	Predecessor_node = new Socket(address[0],Integer.valueOf(address[1]));
		        		new ChatNode(Predecessor_node).start();
		        	}
		        	else if(node.hasSucessor == 0)  {//This has been intialised in Node_Info file and we are calling over here
		        		Node_Info Sucessor = new Node_Info();
		        		Sucessor.portno = Integer.valueOf(address[1]);
		        		Sucessor.name = address[0];
		        		Sucessor_node = new Socket(address[0],Integer.valueOf(address[1]));
			        		new ChatNode(Sucessor_node).start();
		        	}
		        	else {
		        		System.out.print("Node has both Successor and Predecessor can't add more node!!!!");
		        	}
		        }
		        catch(Exception e) {//If the try block fails the compiler moves to catch block where the below chunk is run
		        	if(Predecessor_node != null) Predecessor_node.close();
		        	else System.out.println("Invalid input skip to next line");
		        	if(Sucessor_node != null) Sucessor_node.close();
		        	else System.out.println("Invalid input skip to next line");
		        	System.exit(0);	
		        }
			}
			Communicate(buf_reader,username,active_Node,node);
	}
	public void Communicate(BufferedReader buf_readere,String username,ActiveNode active_Node,Node_Info node) throws Exception{
		
		try {
			System.out.println("->Start Chatting else press[e exit, c change]");//If the user wishes to change with whom they want to communicate or leave chat
			boolean flag = true;
			while(flag) {
				String message = buf_readere.readLine();
				if(message.equals("e")) {
					flag = false;
					System.err.println("This User has left the Chat");
					break;
				}
				else if(message.equals("c")) {
					updateListentoMains(buf_readere,username,active_Node,node);
					}
				else {
					StringWriter stringwriter = new StringWriter();
					Json.createWriter(stringwriter).writeObject(Json.createObjectBuilder()
							.add("username",username)
							.add("message", message)
							.build());
					active_Node.sendMessage(stringwriter.toString());
			}
			}
			System.exit(0);	
			}
		catch(Exception e) {
			System.err.println("Invalid input");//If inputs other than change or exit are given we terminate the session
			System.out.println("Exiting application please run again");
			System.exit(0);		
		}
	}
}