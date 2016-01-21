import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Host {
	
	DatagramSocket dsReceive, dsSendAndReceive;
	DatagramPacket dpReceiveFromClient, dpSendToServer, dpReceiveFromServer, dpSendToClient;
	
	public static void main(String args[]){
		//create the host and being the program
		Host h = new Host();
		h.initializeSockets();
		h.loop();
	}
	
	public void initializeSockets(){
		//initialize the sockets to communicate with the client and the server
		try {
			dsReceive = new DatagramSocket(68);
			dsSendAndReceive = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loop(){
		int i = 1;
		while(true){
			//the algorithm receives from client, send to server, receives from server and send to client
			System.out.println("Request " + i);
			System.out.println();
			receiveFromClient();
			sendToServer();
			waitForServer();
			sendToClient();
			i++;
		}
	}
	
	public void receiveFromClient(){
		//receive a datagram packet from the client and print the data contained in the packet
		byte data[] = new byte[100];
	    dpReceiveFromClient = new DatagramPacket(data, data.length);
	    
	     try {
	    	 dsReceive.receive(dpReceiveFromClient);
	     } catch(IOException e) {
	    	 e.printStackTrace();
	    	 System.exit(1);
	     }

		 System.out.println("Byte representation of received packet from client at host: " + Arrays.toString(data));
		 System.out.println();
	}
	
	public void sendToServer(){
		//send what was received from the client to the server (only the necessary bytes in the byte array)
		try {
			dpSendToServer = new DatagramPacket(Arrays.copyOfRange(dpReceiveFromClient.getData(), 0, dpReceiveFromClient.getLength()), dpReceiveFromClient.getLength(), InetAddress.getLocalHost(), 69);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			dsSendAndReceive.send(dpSendToServer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Byte representation of packet to send to server at host: " + Arrays.toString(dpSendToServer.getData()));
		
		System.out.println();
	}
	
	public void waitForServer(){
		//wait to receive a datagram packet from the server and print out the received information
		byte data[] = new byte[100];
	    dpReceiveFromServer = new DatagramPacket(data, data.length);
	    
	    try {
	    	dsSendAndReceive.receive(dpReceiveFromServer);
	    } catch(IOException e) {
	    	e.printStackTrace();
	    	System.exit(1);
	    }

		System.out.println("Message from received packet from server at host: " + Arrays.toString(data));

		System.out.println();
	}
	
	public void sendToClient(){
		//send the message received from the server to the client.
		byte[] responseArray = Arrays.copyOfRange(dpReceiveFromServer.getData(), 0, dpReceiveFromServer.getLength());
		
		try {
			DatagramSocket dsSend = new DatagramSocket();
			dpSendToClient = new DatagramPacket(responseArray, dpReceiveFromServer.getLength(), InetAddress.getLocalHost(), dpReceiveFromClient.getPort());
			
			System.out.println("Response to send to client at host: " + Arrays.toString(responseArray));

			System.out.println();
			System.out.println();
			
			dsSend.send(dpSendToClient);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
