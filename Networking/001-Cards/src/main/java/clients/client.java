/*
Brandon T. Wood
Card Server 1.0
NETWORKING
*/

package clients;
import Cards.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/*@Override
public enum Suit{
    Spade, Heart, Club, Diamond;
}
*/

public class client{

	/** Default port number; used if none is provided. */
        public static int DEFAULT_PORT = 6700 + 22;

        /** Default seed number; ised if none proveded. */
        public static String DEFAULT_NAME = "localhost";

        /** Default machine name is the local machine; used if none provided.$
        public final static String DEFAULT_MACHINE_NAME = "localhost";

        /** Command-line switches */
        public final static String ARG_PORT = "--port";
        public final static String ARG_MACHINE = "--machine";
        //public final static String ARG_MACHINE = "--machine";

        /** Message op-codes */
        public final static String MSG_HELLO = "Hello";
        public final static String MSG_GOODBYE = "Goodbye";

	/** Protocals **/
	public final static String DEAL_CARD = "DealCard";
	public final static String DISCARD = "Toss";
	public final static String PROMPT = "Here is your hand, enter the number of the card you wish to discard or 0 to quit.";
	public final static String COMMAND_BYE = "0";

        /** Port number of distant machine */
        //private int portNumber;

	public static void main(String [] args){
		//First handle input to change defaults if given input.
                for(int i = 0; i < args.length; i++) {

                        if(args[i].equals(ARG_PORT)){

                                DEFAULT_PORT = Integer.parseInt(args[i+1]);
                        }else if(args[i].equals(ARG_MACHINE)){

				DEFAULT_NAME = args[i+1];
                                //DEFAULT_SEED = Integer.parseInt(args[i+1]);
                        }
                }
		System.out.println("Card distributer client mark 1!");
		request();
	}


	public static void request(){

		int portNumber = DEFAULT_PORT;
		String machineName = DEFAULT_NAME;
		try{

      			Socket socket = new Socket(machineName, portNumber);
      			PrintStream sout = new PrintStream(socket.getOutputStream());
      			Scanner sin = new Scanner(socket.getInputStream());
      			System.out.format("Sending \"%s\"", MSG_HELLO);
      			sout.println(MSG_HELLO);

			//Initalize hand object.
			Hand hand = new Hand();
			String passer = sin.nextLine();;
			String passer2 = "";
			String[] Hand = {
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
			};
			try{

				Socket newSocket = new Socket(machineName, Integer.parseInt(passer));
                        	PrintStream zout = new PrintStream(newSocket.getOutputStream());
                        	Scanner zin = new Scanner(newSocket.getInputStream());
                        	System.out.format("Sending \"%s\"", MSG_HELLO);
                        	sout.println(MSG_HELLO);
				for(int i = 0; i <= 5; i++){

                                         //Send for a card five times
                                        zout.println(MSG_HELLO + "\n" + DEAL_CARD);
					Hand[i] = zin.nextLine();
					System.out.println(" Place:" + i + Hand[i]);
				}
				Scanner keyboard = new Scanner(System.in);
				System.out.println(PROMPT);
				while (keyboard.hasNextLine()) {

				        String msg = keyboard.nextLine();
				        if (msg.equals(COMMAND_BYE)){
          					break;
					}else if(msg.contains("1") || msg.contains("2") || msg.contains("3") || msg.contains("4") || msg.contains("5") || msg.contains("6") || msg.contains("7") || msg.contains("8")){
						if(!Hand[Integer.parseInt(msg)].equals("")){

							//System.out.println("time to replace with: " + msg);
							zout.println(MSG_HELLO + "\n" + DEAL_CARD);
							//passer = zin.nextLine();
                                        		Hand[Integer.parseInt(msg)] = zin.nextLine();
							//Hand[Integer.parseInt(msg)] = "";
						}
					}else{

        					zout.println(MSG_HELLO + "\n" + msg);
				        	passer = zin.nextLine();
				        	//System.out.format("Client saw \"%s\"\n", serverResponse)
					}
					for(int i = 0; i < Hand.length; i++){

						if(!Hand[i].equals("") && !Hand[i].contains("Server")){

							System.out.println(" Place:" + i + Hand[i]);
						}
					}
				        System.out.println(PROMPT);
			        }
				zout.println(MSG_GOODBYE);
			}catch(UnknownHostException uhe){

	                        // the host name provided could not be resolved
        	                uhe.printStackTrace();
                	        System.exit(1);
                	}catch(IOException ioe){

                	        // there was a standard input/output error (lower-level)
                	        ioe.printStackTrace();
                	        System.exit(1);
               	 	}

			//Here we do interaction between the server and client.
			//for(int i = 1; i < 5; i++){
			//	sout.println(DEAL_CARD);
				//Take in resulting infor about card
				//System.out.println(sin.nextLine());
			//	passer = sin.nextLine();
			//	System.out.println(passer);
				//Make a card object and add it to hand.
			//}

      			/*System.out.println("Receiving from server");
      			String serverResponse = sin.nextLine();
      			System.out.format("Client saw \"%s\"\n", serverResponse);
      			System.out.format("Sending \"%s\"", MSG_GOODBYE);
      			sout.println(MSG_GOODBYE);
			*/
      			sout.close();
      			sin.close();
    		}catch(UnknownHostException uhe){

	      		// the host name provided could not be resolved
      			uhe.printStackTrace();
      			System.exit(1);
    		}catch(IOException ioe){

      			// there was a standard input/output error (lower-level)
      			ioe.printStackTrace();
      			System.exit(1);
    		}
	}
}
