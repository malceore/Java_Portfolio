/*
Brandon T. Wood
Card Server 1.0
NETWORKING AT SUNY POTSDAM
*/

package servers;
import Cards.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class server{

  	/** Default port number; used if none is provided. */
  	public static int DEFAULT_PORT = 6700 + 22;

	/** Default seed number; ised if none proveded. */
	public static int DEFAULT_SEED = 424242;

  	/** Default machine name is the local machine; used if none provided. */
  	public final static String DEFAULT_MACHINE_NAME = "localhost";

  	/** Command-line switches */
  	public final static String ARG_PORT = "--port";
	public final static String ARG_SEED = "--seed";
  	//public final static String ARG_MACHINE = "--machine";

  	/** Message op-codes */
  	public final static String MSG_HELLO = "Hello";
  	public final static String MSG_GOODBYE = "Goodbye";

        /** Protocals **/
	//DEAL_CARD equates to GIVE_CARD but better.
        public final static String DEAL_CARD = "DealCard";
        public final static String DISCARD = "Toss";

  	/** Port number of distant machine */
  	private int portNumber;

	//This is main, where input is first taken and disected
	public static void main(String [] args){

		//First handle input to change defaults if given input.
		for(int i = 0; i < args.length; i++) {

			if(args[i].equals(ARG_PORT)){

				DEFAULT_PORT = Integer.parseInt(args[i+1]);
			}else if(args[i].equals(ARG_SEED)){

				DEFAULT_SEED = Integer.parseInt(args[i+1]);
			}
        	}
		listen();
		System.out.println("Server closing..");
	}

	public static Deck deck;
	//Moved to deck class, made methodes that synchronized.
	public static int deckCounter = 0;
	public static int portnumber = DEFAULT_PORT;
	public static int threadCount = 1;
	public static void listen(){

		//int portNumber = DEFAULT_PORT;
		try{

			//Setting up deck using seed.
			Random rand = new Random(DEFAULT_SEED);
			deck = new Deck(rand);
			deckCounter++;
			deck.populate();
 			ServerSocket server = new ServerSocket(portnumber);
      			System.out.format("Server now accepting connections on port %d\n", portnumber);
      			Socket client;
		      	while ((client = server.accept()) != null) {

        			System.out.format("Connection from %s\n", client);
        			Scanner cin = new Scanner(client.getInputStream());
        			PrintStream cout = new PrintStream(client.getOutputStream());
        			String clientMessage = "";
        			clientMessage = cin.nextLine();
		        	if(clientMessage.equals(MSG_HELLO)){

						//Output the new port inwhich to contact the thread.
						cout.println(portnumber + threadCount);
						System.out.println("Someone is connecting!! 0 _ 0  Better direct them to a new thread");
						Thread thread = new Thread(){
    						public void run(){
							try{

								//Setup new port and connect with the client through it!
                        					ServerSocket server = new ServerSocket(portnumber + threadCount);
			        	                	Socket newClient;
								while((newClient = server.accept()) != null){

									System.out.println(" Creating new thread and connecting to client on port, " + (portnumber + threadCount));
									threadCount++;

									//Setting back up input and output.
					                                //System.out.format("Connection from %s\n", newClient);
                                					Scanner zin = new Scanner(newClient.getInputStream());
				        	                        PrintStream zout = new PrintStream(newClient.getOutputStream());
                        					        String newClientMessage = "";
                                					newClientMessage = zin.nextLine();
									//Loop waiting to either close or deal a new card.
									//while((newClient = server.accept()) != null){
									//Lets do some work! But we need a loop.
									//System.out.println("  We have connection sir.");
								      	//String clientMessage = "";
								        //clientMessage = cin.nextLine();

								        if (newClientMessage.equals(MSG_HELLO)) {
          									zout.format("Server saw \"%s\"\n", newClientMessage);
          									System.out.format("Server saw \"%s\"\n", newClientMessage);

          									while (zin.hasNextLine() && (!(newClientMessage = zin.nextLine()).equals(MSG_GOODBYE))) {
            										//zout.format("Server saw \"%s\"\n", newClientMessage);
            										//System.out.format("Server saw \"%s\"\n", newClientMessage);
											if(newClientMessage.equals(DEAL_CARD)){
												Card card = deck.deal();
												//numDealt++;
												//deck.increase();
												zout.println(" Serial:" + deckCounter+""+deck.numDealt + " Value:" + card.value + " Suit:" + card.suit);
											}
          									}
          									if (!newClientMessage.isEmpty()) {
            										System.out.format("Server saw \"%s\" and is exiting.\n", newClientMessage);
          									}
        								} else if(newClientMessage.equals(DEAL_CARD)){
                                                                                        Card card = deck.deal();
                                                                                        zout.println(card.value + ":" + card.suit);

									}else{
          									System.err.format("Server saw \"%s\"; unknown message.\n", newClientMessage);
          									System.exit(1);
        								}
        								zout.close();
        								zin.close();
      								//}
								}
							//Start listening and then send them cards!
     							//System.out.println("Thread Number " + temp + " running..");
							}catch (IOException ioe) {

                        					// there was a standard input/output error (lower-level from uhe)
                        					ioe.printStackTrace();
                        					System.exit(1);
			                		}
						}
  					};
  					thread.start();
				}
				/*

          				cout.format("Server saw \"%s\"\n", clientMessage);
          				System.out.format("Server saw \"%s\"\n", clientMessage);
          				while (cin.hasNextLine() && (!(clientMessage = cin.nextLine()).equals(MSG_GOODBYE))) {

            					cout.format("Server saw \"%s\"\n", clientMessage);
            					System.out.format("Server saw \"%s\"\n", clientMessage);
          				}
			          	if(!clientMessage.isEmpty()){

            					System.out.format("Server saw \"%s\" and is exiting.\n", clientMessage);
          				}
				}else{

			        	System.err.format("Server saw \"%s\"; unknown message.\n", clientMessage);
          				System.exit(1);
        			}
				*/
        			cout.close();
		        	cin.close();
      			}
    		} catch (IOException ioe) {

      			// there was a standard input/output error (lower-level from uhe)
      			ioe.printStackTrace();
      			System.exit(1);
    		}
	}
}
