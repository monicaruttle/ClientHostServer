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
		Host h = new Host();
		h.initializeSockets();
		h.loop();
	}
	
	public void initializeSockets(){
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
		byte data[] = new byte[100];
	    dpReceiveFromClient = new DatagramPacket(data, data.length);
	    
	     try {
	    	 dsReceive.receive(dpReceiveFromClient);
	     } catch(IOException e) {
	    	 e.printStackTrace();
	    	 System.exit(1);
	     }

		 System.out.println("Byte representation of received packet from client at host: " + Arrays.toString(data));
		 System.out.println("String representation of received packet from client at host: " + Converter.convertToString(data));
		 System.out.println();
	}
	
	public void sendToServer(){
		try {
			dpSendToServer = new DatagramPacket(Arrays.copyOfRange(dpReceiveFromClient.getData(), 0, dpReceiveFromClient.getLength()), dpReceiveFromClient.getLength(), InetAddress.getLocalHost(), 69);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Byte representation of packet to send to server at host: " + Arrays.toString(dpSendToServer.getData()));
		System.out.println("String representation of packet to send to server at host: " + Converter.convertToString(dpSendToServer.getData()));

		System.out.println();
		
		try {
			dsSendAndReceive.send(dpSendToServer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void waitForServer(){
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
