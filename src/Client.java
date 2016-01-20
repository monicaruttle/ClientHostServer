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
	static int READ = 0x01;
	static int WRITE = 0x02;
	
	public static void main(String args[]){
		Client c = new Client();
		c.initializeSockets();
		c.loop();
	}
	
	public void initializeSockets(){
		try {
			ds = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void loop(){
		for (int i = 0; i < 11; i++){
			System.out.println("Request " + i+1);
			if (i == 10)
				createInvalid();
			else if ((i % 2) == 0){
				sendRead();
				receivePacket();
			} else {
				sendWrite();
				receivePacket();
			}
			System.out.println();
		}
	}
	
	public void sendRead(){
		byte[] message = Converter.convertToBytes(READ);
		
		System.out.println("Byte representation of read at client to send to host: " + Arrays.toString(message));
		System.out.println("String representation of read at client to send to host: " + Converter.convertToString(message));
		
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
		byte[] message = Converter.convertToBytes(WRITE);
		
		System.out.println("Byte representation of write at client to send to host: " + Arrays.toString(message));
		System.out.println("String representation of write at client to send to host: " + Converter.convertToString(message));
		
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
		byte data[] = new byte[100];
	    dpReceive = new DatagramPacket(data, data.length);

	     try {
	    	 ds.receive(dpReceive);
	     } catch(IOException e) {
	    	 e.printStackTrace();
	    	 System.exit(1);
	     }
	     
	     System.out.println("Message from received packet at client from host: " + Arrays.toString(data));
	}
	
	public void createInvalid(){
		
	}
	
}
