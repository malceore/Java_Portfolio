package io;
//Output stream made by brandon T. Wood, 2015
import java.io.*;
import java.net.*;
import java.util.*;
public class outputStream extends OutputStream{

        protected DatagramSocket socket;
        protected byte[] buffer = new byte[32];
	protected int pos = 0;
	protected int port;
	protected InetAddress address;
        //Constructor, takes a DAtagram object a address and port.
        public outputStream(DatagramSocket socket, InetAddress address, int port){

                this.socket = socket;
		this.address = address;
		this.port = port;
        }

 	public void write(int input){
		System.out.println("Why do I even need this!?!");
	}

	public void write(byte[] input){

		int i;
		for(i = 0; i < input.length-1; i++){
			if((1 + pos) > 32){

				flush();
				pos = 0;
			}//else{
			buffer[pos] = input[i];
			//}
			pos++;
		}
		//pos++;
		//}
		//pos = pos + i;
	}


	public void flush(){

		//System.out.println("  Flushing...");
		try {

			//System.out.println("  Flushing 1");
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
			//System.out.println("  Flushing 2");
	                socket.send(packet);
			buffer = new byte[32];
                } catch(IOException e) {

                                //listening = false;
                        System.err.println("Caught IOException: " +  e.getMessage());
                                //throw new IOException();
                }

	}
}
