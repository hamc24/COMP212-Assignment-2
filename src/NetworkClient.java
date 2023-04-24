import java.net.*;
import java.io.*;

public class NetworkClient {

	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) throws IOException {

		if (args.length != 2) {
			System.err.println(
					"Usage: java HelloClient <host name> <port number>");
			System.exit(1);
		}

		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);

		try (
				Socket myClientSocket = new Socket(hostName, portNumber);
				PrintWriter output = new PrintWriter(myClientSocket.getOutputStream(), true);
				BufferedReader input = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));) {
			String userInput;
			String regInput;
			System.out.println(input.readLine()); // reads the first message from the server and prints it

			// Client sending Starting number to Server
			System.out.println("Enter Starting amount of Processes (at least 4 preferred and then press enter): ");
			userInput = stdIn.readLine(); // reads user's input
			output.println(userInput); // user's input transmitted to server

			// Client sending sterrping number to server
			System.out.println("Enter number to step by: ");
			userInput = stdIn.readLine();
			output.println(userInput);

			// Client sending Max number to Server
			System.out.println("Enter max number of processes allowed: ");
			userInput = stdIn.readLine();
			output.println(userInput);

			// Printing all the ouput data recieved from server
			boolean temp = true;
			while (temp) {
				regInput = input.readLine();
				if (isInteger(regInput)) {
					temp = false;
				} else {
					System.out.println(regInput);
				}
			}

			// Printing the final pnumber of simulations on Client side
			System.out.println(input.readLine());

			System.out.println(input.readLine()); // reads server's ack and prints it
			System.out.println("-----------End of communication-----------");
			System.out.println("\nCommunication with server " + hostName + " was successful! Now closing...");

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " +
					hostName);
			e.printStackTrace();
			System.exit(1);
		}

	}
}