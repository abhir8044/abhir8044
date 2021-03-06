package assignment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;

public class ChatNode extends Thread {
	private BufferedReader buf_reader;
	public ChatNode(Socket socket) throws IOException{
		buf_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
	}
public void run() {
	 boolean flag = true;
	 while(flag) {
		 try {
		 JsonObject jsonobject = Json.createReader(buf_reader).readObject();
		 if(jsonobject.containsKey("username")) {
      		 System.out.println("[" + jsonobject.getString("username") + "]: "+ jsonobject.getString("message") );
		 }
		}
	 catch(Exception e) {
		 flag = false;
		 interrupt();
	 }
 }
}
}