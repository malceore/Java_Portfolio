
import java.util.*;
import java.net.*;
import java.io.*;
import io.inputStream;
public class main{

	public static void main(String [] args){

		try{
			DatagramSocket socket = new DatagramSocket(4445);
			Scanner scan = new Scanner(new inputStream(socket));
			System.out.println(scan.nextLine());
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}
}
