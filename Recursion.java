package assignment;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Recursion {
	private char input;
	public void rec() throws Exception {
	System.out.println("Enter the operation to perform :\n"+ " j - JOIN \n l - LEAVE\n");
	Scanner sc = new Scanner(System.in);
	input = sc.next().charAt(0);
	
	if(input == 'j'){ 
   BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
   Node_Info ni = new Node_Info();
   System.out.println( "Please Enter Your Name:");
     ni.name = br.readLine();
		System.out.println("Enter The Port Number That You Want To Connect To:");
	ni.portno = Integer.parseInt(br.readLine());
     ActiveNode an = new ActiveNode(ni.portno);
     an.start();
     ni.hasPredecessor = 0;
     ni.hasSucessor = 0;
     new Main().updateListentoMains(br,ni.name,an,ni);
	}
	else if(input =='l') {
		System.out.println("exiting the application");
		System.exit(0);
	}
	else {
		System.out.println("Invalid input please run the application again");
	}
	sc.close();
	}	
}



