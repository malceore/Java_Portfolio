package peer;

import common.UDPBase;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

				
import io.*;
				

/**
 * UDPPeer - A peer-to-peer, UDP (or datagram) based alternating chat program. The UDP protocol is a message-oriented,
 * best-effort-delivery protocol. The UDPPeer class's main() method begins by checking the command-line for optional
 * command-line parameters:
 * <p>
 * UDPPeer [--localPort <lp>] [--machine <name>] [--port <port>] [--listen]
 * <p>
 * where <lp> is the UDP port this instance of the program is to use (default value provided in UDPBase); <name> is the
 * name of the machine to which this UDPPeer should connect; <port> is the UDP port number to which this instance of the
 * program should address messages; and --listen means this program begins execution waiting for the other end to send a
 * message.
 * <p>
 */

public class UDPPeer extends UDPBase {

  protected DatagramSocket socket;

  /**
   * distant machine name as a human-readable string
   */
  protected String distantMachineName;
  protected int distantPortNumber;

  /**
   * local machine Internet address
   */
  private InetAddress localMachineAddress;
  private InetAddress distantMachineAddress;

  /**
   * Construct a brand new UDPPeer object. Need to know the local port number and the remote machine and port. Note that
   * the next step is to add multiple machine/port pairs
   *
   * @param localPortNumber    port number bing used by this peer
   * @param distantMachineName Default machine name for sending
   * @param distantPortNumber  Default machine port for sending
   * @param verbose            level of logging to use
   * @param listening          is this peer listening
   */
  public UDPPeer(int localPortNumber, String distantMachineName, int distantPortNumber,
                 int verbose, boolean listening) {
    super(localPortNumber, verbose);
    this.distantMachineName = distantMachineName;
    this.distantPortNumber = distantPortNumber;
    this.listening = listening;
  }

  /**
   * The main program. Processed command-line arguments, creates a UDPPeer object and calls run().
   *
   * @param args provided command-line arguments
   */
  public static void main(String[] args) {
    int localPort = 0;
    int port = DEFAULT_DISTANT_PORT_NUMBER;
    String machine = DEFAULT_DISTANT_MACHINE_NAME;
    int verbosity = DEFAULT_VERBOSE;
    boolean listen = DEFAULT_LISTEN_FIRST;

    /*
     * Parsing parameters. argNdx will move forward across the indices;
     * remember for arguments that have their own parameters, you must
     * advance past the value for the argument too.
     */
    int argNdx = 0;

    while (argNdx < args.length) {
      String curr = args[argNdx];

      if (curr.equals(ARG_DISTANT_PORT)) {
        ++argNdx;

        String numberStr = args[argNdx];
        port = Integer.parseInt(numberStr);
      } else if (curr.equals(ARG_LOCAL_PORT)) {
        ++argNdx;

        String numberStr = args[argNdx];
        localPort = Integer.parseInt(numberStr);
      } else if (curr.equals(ARG_DISTANT_MACHINE)) {
        ++argNdx;
        machine = args[argNdx];
      } else if (curr.equals(ARG_VERBOSE)) {
        ++argNdx;

        String numberStr = args[argNdx];
        verbosity = Integer.parseInt(numberStr);
      } else if (curr.equals(ARG_LISTEN_FIRST)) {
        listen = !listen;
      } else {

        // if there is an unknown parameter, give usage and quit
        System.err.println("Unknown parameter \"" + curr + "\"");
        System.exit(1);
      }

      ++argNdx;
    }

    new UDPPeer(localPort, machine, port, verbosity, listen).run();
  } // main


  /**
   * Extract the data from a datagram packet into a string.
   *
   * @param packet packet to extract
   * @return string representation of the data
   */
  private String stringFromDatagramPacket(DatagramPacket packet) {
    byte[] packetData = packet.getData();
    return new String(packetData, 0, packetData.length);
  }

  /**
   * This is the "client" end of the echo conversation. Get a line from the user, pack it up. and send it to the
   * distant end. Then wait for a response and flash it on the screen. Do that forever.
   *
   * Note: Blocking calls on UDP (without timeouts or other threads than can save them) are asking for trouble.
   * Here, in a sample program, it is not that big of a deal.
   */
																			
  private void handleTalking() {
    Scanner keyboard = new Scanner(System.in);
    outputStream  os = new outputStream(socket, distantMachineAddress, distantPortNumber);
    while (true) {
      System.out.print("> ");
      String line = keyboard.nextLine().trim();
      //if (line.isEmpty()) continue;
      line = line + '\n';
      byte[] lineBytes = line.getBytes();
      os.write(lineBytes);
      os.flush();
	//if(os.hasNextLine()){
	//	System.out.println("");
	//}
	
      /*
      DatagramPacket sendPacket = new DatagramPacket(lineBytes, lineBytes.length, distantMachineAddress, distantPortNumber);

      byte[] buffer = new byte[bufferSize];
      DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

      try {
        socket.send(sendPacket);

        socket.receive(receivePacket);
        System.out.println(String.format("Distant End Says \"%s\"", stringFromDatagramPacket(receivePacket)));
      } catch (IOException e) {
        e.printStackTrace();
      }
	*/
		
    }
    //os.close();
  }

  /**
   * This is the "server" end of the echo conversation. Listen, get a message, turn it around and send it back,
   * and so on.
   *
   * Note: Blocking calls on UDP (without timeouts or other threads than can save them) are asking for trouble.
   * Here, in a sample program, it is not that big of a deal.
   */

																		

  private void handleListening() {

    System.out.println(String.format("Listening @ %s:%d", distantMachineAddress, distantPortNumber));
    try{
    	DatagramSocket socket2 = new DatagramSocket(distantPortNumber);
    	inputStream is = new inputStream(socket2);
    	//Scanner scan = new Scanner(new inputStream(socket));
    	while (true) {

		//is.fill();
		int jeckle = is.read();
		//System.out.println("  Data read: " + is.read());
                //try{
                        //Scanner scan = new Scanner(new inputStream(socket));
                        //System.out.println(scan.nextLine());
		//if(scan.hasNextLine()){
		//	System.out.println("  Recieved" + scan.nextLine());
		//}
                //} catch (IOException e) {
                //      System.err.println("Caught IOException: " + e.getMessage());
                //}
			

	/*
      byte[] buffer = new byte[bufferSize];
      DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

      try {
        socket.receive(receivePacket);

        InetAddress fromMachineAddress = receivePacket.getAddress();
        int fromPortNumber = receivePacket.getPort();
        String line = stringFromDatagramPacket(receivePacket);
        byte[] lineBytes = line.getBytes();

        println(1, String.format("%s:%d \"%s\"", fromMachineAddress, fromPortNumber, line));

        DatagramPacket sendPacket = new DatagramPacket(lineBytes, lineBytes.length, fromMachineAddress, fromPortNumber);
        socket.send(sendPacket);

      } catch (IOException e) {
        e.printStackTrace();
      }*/

    	}
     }catch(IOException e) {
              //listening = false;
              System.err.println("Caught IOException: " +  e.getMessage());
	      //throw new IOException();
     }

  }

  /**
   * The run method; overrides the version defined in the abstract base class
   */
  @Override
  public void run() {
    try {
      this.localMachineAddress = InetAddress.getByName("localhost");
      this.distantMachineAddress = InetAddress.getByName(distantMachineName);
      this.socket = new DatagramSocket(localPortNumber);

      // Based on listening, be a client or a server.
      if (listening)
        handleListening();
      else
        handleTalking();

    } catch (SocketException e) {
      System.err.println(
          "Error in establishing local or distant connection.");
      e.printStackTrace();
      System.exit(2);
    } catch (UnknownHostException e) {
      System.err.println("Unknown host " + distantMachineName);
      e.printStackTrace();
    }
  }
}
