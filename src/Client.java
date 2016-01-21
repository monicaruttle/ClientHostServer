import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Client {
	
	private DatagramSocket ds;
	private DatagramPacket dpSend, dpReceive;
	private static int READ = 0x01;
	private static int WRITE = 0x02;
	
	public static void main(String args[]){
		//create the client and begin the program
		Client c = new Client();
		c.initializeSockets();
		c.loop();
	}
	
	public void initializeSockets(){
		try {
			//initialize the socket to communicate with the host
			ds = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void loop(){
		//loop 11 times. If the n-th loop is even, it is a read request. If it's odd, it's a write request. If it's the 11th loop, it's an invalid request.
		for (int i = 0; i < 11; i++){
			System.out.println("Request " + (i+1));
			if (i == 10)
				sendInvalid();
			else if ((i % 2) == 0){
				sendRead();
				receivePacket();
			} else {
				sendWrite();
				receivePacket();
			}
			System.out.println();
		}
		ds.close();
	}
	
	public void sendRead(){
		//generate the read message
		byte[] message = Converter.convertToBytes(READ);
		
		System.out.println("Byte representation of read at client to send to host: " + Arrays.toString(message));
		System.out.println("String representation of read at client to send to host: " + Converter.convertToString(message));
		
		//send a datagram packet to the host on port 68 with the generated read message
		try {
			dpSend = new DatagramPacket(message, message.length, InetAddress.getLocalHost(), 68);
			ds.send(dpSend);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendWrite(){
		//generate the write request
		byte[] message = Converter.convertToBytes(WRITE);
		
		System.out.println("Byte representation of write at client to send to host: " + Arrays.toString(message));
		System.out.println("String representation of write at client to send to host: " + Converter.convertToString(message));
		
		//send a datagram packet to the host on port 68 with the generate write message
		try {
			dpSend = new DatagramPacket(message, message.length, InetAddress.getLocalHost(), 68);
			ds.send(dpSend);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receivePacket(){
		//receive a datagram packet from the host
		byte data[] = new byte[100];
	    dpReceive = new DatagramPacket(data, data.length);

	     try {
	    	 ds.receive(dpReceive);
	     } catch(IOException e) {
	    	 e.printStackTrace();
	    	 System.exit(1);
	     }
	     
	     System.out.println();
	     System.out.println("Message from received packet at client from host: " + Arrays.toString(data));
	}
	
	public void sendInvalid(){
		byte[] message = {0, 0, 0, 0};
		
		System.out.println("Sending the invalid message: " + Arrays.toString(message));
		
		//send a datagram packet to the host on port 68 with the generate write message
		try {
			dpSend = new DatagramPacket(message, message.length, InetAddress.getLocalHost(), 68);
			ds.send(dpSend);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
