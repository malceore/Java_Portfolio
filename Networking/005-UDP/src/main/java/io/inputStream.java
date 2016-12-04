package io;
//Input class created by Brandon T. Wood 2015
import java.net.*;
import java.io.*;
public class inputStream extends InputStream{

	protected DatagramSocket socket;
	protected byte[] buffer = new byte[1024];
	protected int count = 0;
	protected int pos = 0;
	//Constructor, takes a DAtagram object.
	public inputStream(DatagramSocket socket){

		this.socket = socket;
  	}


	public int fill(){
		
		//System.out.println("  Fill 1");
		boolean listening = true;
		int j = 0; //TEMP
		while(listening){

			// Check if there is a packet in line, if there is then load it into the buffer.
			//if(socket.hasNextLine()){
			try {
				
				//System.out.println("  Fill 2");
				//Recieving data from datagram package.
				byte[] tempBuf = new byte[256];
	        	        DatagramPacket data = new DatagramPacket(tempBuf, tempBuf.length);
		                socket.receive(data);
				//System.out.println("  Fill 2.5");
				byte[]temp = data.getData();
				//String dString = "Hello";
				//byte[] temp = dString.getBytes();
				if(temp.length < 32 || j > 512){
					//This means we got a half empty packet and thus the tail end of the oacket.
					listening = false;
				}//else if(buffer.length < buffer.length + temp.length){

					//Incase the files being sent are too large for the buffer.
				//	 throw new OutOfMemoryError("Required buffer size too large");
				//}
				//System.out.println("  Fill 3");
				//Next iterate through the packet
				int i;
				for(i = 0; i < temp.length; i++){

					buffer[i+pos] = temp[i];
					j++;
				}
				pos = pos + i;
				//System.out.println("  Fill 3.5");
				listening = false;
			} catch(IOException e) {

        			listening = false;
				System.err.println("Caught IOException: " +  e.getMessage());
				//throw new IOException();
	       		}

		}
		//System.out.println("  Fill 4");
		//buffer = new byte[1024];
		//socket.close();
		return 1;
	}


  	public int read(){

		//System.out.println("  Read is running!");
	  	//if(buffer.length <= 0){
		//try {
			int temp = fill();
			//System.out.println("  Attempting to fill: " + fill());
                	//throw new IllegalArgumentException("Buffer size <= 0");
                //}
		//String outpaut = "";
		//Soemthing with buffer that makes a string.
			String output = new String(buffer);
			System.out.print("\n< " + output);
			this.buffer = new byte[1024];
			pos = 0;
		//} catch(IOException e) {

                  //      System.err.println("Caught IOException: " +  e.getMessage());
                        //throw new IOException();
                //}

		return 1;
  	}
}
