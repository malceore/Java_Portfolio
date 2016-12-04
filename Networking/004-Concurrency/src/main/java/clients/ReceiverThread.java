package clients;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ReceiverThread extends Thread {
  final Socket socket;
  Scanner scanner;

  public ReceiverThread(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try {
      scanner = new Scanner(socket.getInputStream());
      String line;
      while ((line = scanner.nextLine()) != null) {
        System.out.println(line);
      }
    } catch (SocketException e) {
      // output thread closed the socket
    } catch (IOException e) {
      System.err.println(e);
    }
  }

}
