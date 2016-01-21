import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Server {
	
	private DatagramSocket dsReceive, dsSend;
	private DatagramPacket dpReceive, dpSend;
	private byte response[];

	public static void main(String[] args) {
		//create the server and begin the program
		Server s = new Server();
		s.initializeSockets();
		s.loop();
	}
	
	public void initializeSockets(){
		//initialize the socket used to communicate with the host
		try {
			dsReceive = new DatagramSocket(69);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loop(){
		//the program will receive from the host and send back to the host
		int i = 1;
		while (true){
			System.out.println("Request " + i);
			System.out.println();
			receiveFromHost();
			sendToHost();
			i++;
		}
	}
	
	public void receiveFromHost(){
		//receive a datagram packet from the host
		byte data[] = new byte[100];
		dpReceive = new DatagramPacket(data, data.length);
		
		try {
			dsReceive.receive(dpReceive);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//the data without the following 0's
		byte[] messageArray = Arrays.copyOfRange(dpReceive.getData(), 0, dpReceive.getLength());
		
		//a string representation of the received message
		String message = Arrays.toString(dpReceive.getData());
		
		//check if the message was a read. If so, print what was received and create a message [0, 3, 0, 1].
		if (checkIfRead(messageArray)){
			
			//read request
			
			message = Converter.convertToString(dpReceive.getData());
			
			System.out.println("Byte representation of received packet from host at server: " + Arrays.toString(dpReceive.getData()));
			System.out.println("String representation of received packet from host at server: " + message);

			System.out.println();
			
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			
			output.write(0x00);
			output.write(0x03);
			output.write(0x00);
			output.write(0x01);
			
			response = output.toByteArray();
		
		//check if the message was a write. If so, print was what received and create a message [0, 4, 0, 0].
		} else if (checkIfWrite(messageArray)){
			//write request
			
			message = Converter.convertToString(dpReceive.getData());
			
			System.out.println("Byte representation of received packet from host at server: " + Arrays.toString(dpReceive.getData()));
			System.out.println("String representation of received packet from host at server: " + message);

			System.out.println();
			
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			
			output.write(0x00);
			output.write(0x04);
			output.write(0x00);
			output.write(0x00);
			
			response = output.toByteArray();
		} else {
			throw new Error("Invalid Message");
		}
	}
	
	public void sendToHost(){
		//create the datagram packet with the newly created message and send to the host.
		try {
			dsSend = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Response packet from server to host:" + Arrays.toString(response));
		System.out.println();
		System.out.println();
		
		try {
			dpSend = new DatagramPacket(response, response.length, InetAddress.getLocalHost(), dpReceive.getPort());
			
			dsSend.send(dpSend);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dsSend.close();
	}
	
	//varify if the message is a valid read
	public boolean checkIfRead(byte[] message){
		if (message[0] == 0 && message[1] == 1){
			int i = 2;
			while(message[i] != 0){
				i++;
			}
			i++;
			while(message[i] != 0){
				i++;
			}
			if (i == message.length-1)
				return true;
			else return false;
		} else return false;
	}
	
	//varify if the message is a valid write
	public boolean checkIfWrite(byte[] message){
		if (message[0] == 0 && message[1] == 2){
			int i = 2;
			while(message[i] != 0){
				i++;
			}
			i++;
			while(message[i] != 0){
				i++;
			}
			if (i == message.length-1)
				return true;
			else return false;
		} else return false;
	}

}
