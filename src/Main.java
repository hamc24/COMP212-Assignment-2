import java.net.*;
import java.io.*; // Import the File class
import java.io.FileWriter;
import java.io.IOException; // Import the IOException class to handle errors

public class Main {

	// Putting some repeating code here so that the main function
	// Isn't too crowded
	public static void createBestF() {
		try {
			File myObj = new File("best.dat");
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("'best.dat' already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static void createWorstF() {
		try {
			File myObj = new File("worst.dat");
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("'worst.dat' already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static void createTestF() {
		try {
			File myObj = new File("test.dat");
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("'test.dat' already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static void initializeFiles(int LCRmsg, int HSmsg) {
		// Writing initial values on the files
		try {
			FileWriter myWriter = new FileWriter("best.dat");
			myWriter.write("0 " + LCRmsg + " 0 " + HSmsg);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		try {
			FileWriter myWriter = new FileWriter("worst.dat");
			myWriter.write("0 " + LCRmsg + " 0 " + HSmsg);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		try {
			FileWriter myWriter = new FileWriter("test.dat");
			myWriter.write("0 " + LCRmsg + " 0 " + HSmsg);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static void writeBestF(int LCRRound, int LCRmsg, int HSRound, int HSmsg) {
		try {
			FileWriter myWriter = new FileWriter("best.dat", true);
			myWriter.write("\n" + LCRRound + " " + LCRmsg + " " + HSRound + " " + HSmsg);
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static void writeWorstF(int LCRRound, int LCRmsg, int HSRound, int HSmsg) {
		try {
			FileWriter myWriter = new FileWriter("worst.dat", true);
			myWriter.write("\n" + LCRRound + " " + LCRmsg + " " + HSRound + " " + HSmsg);
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static void writeTestF(int LCRRound, int LCRmsg, int HSRound, int HSmsg) {
		try {
			FileWriter myWriter = new FileWriter("test.dat", true);
			myWriter.write("\n" + LCRRound + " " + LCRmsg + " " + HSRound + " " + HSmsg);
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	// Start of main
	public static void main(String[] args) throws IOException {

		// For Socket
		int portNumber;

		// Error Checking ETC
		if (args.length < 1) {
			System.out
					.println("Warning: You have provided no arguments\nTrying to connect to the default port 8000...");
			portNumber = 8000;
		} else if (args.length == 1) {
			portNumber = Integer.parseInt(args[0]);
		} else {
			System.out.println(
					"Warning: You have provided > 1 arguments\nTrying with the first argument to connect to a port...");
			portNumber = Integer.parseInt(args[0]);
		}

		while (true) { // in order to serve multiple clients but sequentially, one after the other
			try (
					ServerSocket myServerSocket = new ServerSocket(portNumber);
					Socket aClientSocket = myServerSocket.accept();
					PrintWriter output = new PrintWriter(aClientSocket.getOutputStream(), true);
					BufferedReader input = new BufferedReader(new InputStreamReader(aClientSocket.getInputStream()));) {
				// For Leader Election algorithms
				int LCRRound = 0;
				int HSRound = 0;
				responseObj LCRmsg = new responseObj();
				responseObj HSmsg = new responseObj();
				int totSimulation = 0;

				// Initial creation of the data files
				// All data files hold 4 values in each line structured as follows:
				// (Rounds of LCR, Messages sent in LCR, Rounds of HS, Messages sent in HS)
				createBestF();
				createWorstF();
				createTestF();
				System.out.println("Connection established with a new client with IP address: "
						+ aClientSocket.getInetAddress() + "\n");
				output.println("Server says: Hello Client " + aClientSocket.getInetAddress() + ". This is server "
						+ myServerSocket.getInetAddress() +
						" speaking. Our connection has been successfully established!");

				// Get starting number of processes from client
				String inputLine = input.readLine();
				System.out.println("Client says: " + inputLine + " for starting number of processes.");
				int numP = Integer.parseInt(inputLine); // User-inputed values for starting # of processes

				// Get stepping amount from client
				inputLine = input.readLine();
				System.out.println("Client says: " + inputLine + " for number to step by.");
				int numStep = Integer.parseInt(inputLine);

				// Get max amount of processes from client
				inputLine = input.readLine();
				System.out.println("Client says: " + inputLine + " for max number of Processes allowed.");
				int maxP = Integer.parseInt(inputLine);

				Network test = new Network(numP);

				initializeFiles(LCRmsg.msgTot, HSmsg.msgTot);

				// Running both algorithms and getting the Best, Worst and Random samples.
				while (test.size() <= maxP) {
					// Testing for sorted network
					test.sortNetwork();
					LCRmsg = test.LCRElectLeader();
					LCRRound = test.round;
					test.resetNetwork();
					HSmsg = test.HSElectLeader();
					HSRound = test.round;
					totSimulation += 2;
					output.println(LCRmsg.msgString); // Send the message to input
					output.println(HSmsg.msgString);

					writeBestF(LCRRound, LCRmsg.msgTot, HSRound, HSmsg.msgTot);

					// Testing for reversed sorted network
					test.reverse();
					LCRmsg = test.LCRElectLeader();
					LCRRound = test.round;
					test.resetNetwork();
					HSmsg = test.HSElectLeader();
					HSRound = test.round;
					totSimulation += 2;
					output.println(LCRmsg.msgString);
					output.println(HSmsg.msgString);

					writeWorstF(LCRRound, LCRmsg.msgTot, HSRound, HSmsg.msgTot);

					for (int i = 0; i < 5; i++) {
						// Testing for randomized network
						test.shuffleNetwork();
						LCRmsg = test.LCRElectLeader();
						LCRRound = test.round;
						test.resetNetwork();
						HSmsg = test.HSElectLeader();
						HSRound = test.round;
						totSimulation += 2;
						output.println(LCRmsg.msgString);
						output.println(HSmsg.msgString);

						writeTestF(LCRRound, LCRmsg.msgTot, HSRound, HSmsg.msgTot);
					}

					for (int i = 1; i < numStep + 1; i++) {
						test.add(new Process(test.size() + 1));
					}
				}

				output.println("1");

				output.println("\nTotal number of Simulations: " + totSimulation);
				test.resetNetwork();

				// For the very end
				System.out.println("Connection with client " + aClientSocket.getInetAddress() + " is now closing...\n");
			} catch (IOException e) {
				System.out.println("Exception caught when trying to listen on port "
						+ portNumber + " or listening for a connection");
				System.out.println(e.getMessage());
			}
		}

	}

}
