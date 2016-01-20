import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public abstract class Converter {
	
	static String DATA = "test.txt";
	static String MODE = "octet";

	public static byte[] convertToBytes(int readOrWrite){
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		byte[] dataArray = DATA.getBytes();
		byte[] modeArray = MODE.getBytes();
		
		try {
			//write initial 01 or 02
			output.write(0x00);
			output.write(readOrWrite);
			
			//write the text file
			output.write(dataArray);
			
			//write a 0
			output.write(0x00);
			
			//write the octet mode
			output.write(modeArray);
			
			//write the last 0
			output.write(0x00);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output.toByteArray();
	}
	
	public static String convertToString(byte[] message){
		
		byte[] data;
		byte[] mode;
		int data_start;
		int mode_start;
		
		//check if the message starts with 01 or 02
		if (!(message[0] == 0x00 && (message[1] == 0x01) || (message[1] == 0x02))){
			throw new Error("Invalid Message");
		} else {
			//the data starts at index 2 and goes until the message index. Copy that into the data array.
			data_start = 2;
			
			int messageIndex = data_start;
			
			while (message[messageIndex] != 0){
				messageIndex++;
			}
			data = Arrays.copyOfRange(message, data_start, messageIndex);
			messageIndex++;
			
			//the mode start at index mode_start and goes until the message index. Copy that into the mode array.
			mode_start = messageIndex;
			
			while(message[messageIndex] != 0){
				messageIndex++;
			}
			
			mode = Arrays.copyOfRange(message, mode_start, messageIndex);
		}
		
		//return the String representation
		return "Data: " + new String(data, StandardCharsets.UTF_8) + " Mode: " + new String(mode, StandardCharsets.UTF_8);
	}
}
