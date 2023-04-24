import java.util.*;

public class Network {
	/* Parameters */
	int round;
	int leaderNode;
	String order;
	ArrayList<Process> net;

	/* Constructor */
	public Network(int n) { // n will be size of array
		round = 1;
		leaderNode = 0;
		net = new ArrayList<Process>();
		order = "IDs increasing Clockwise";

		if (n <= 0) {
			throw new ArithmeticException("Error: network has to have more than 0 processes");
		} else if (n == 1) {
			net.add(new Process(1));
			net.get(0).next = net.get(0);
			net.get(0).prev = net.get(0);
		} else {
			for (int i = 0; i < n; i++) {
				net.add(new Process(i + 1));
				net.get(i).next = null;
				net.get(i).prev = null;
			}

			for (int i = 0; i < n; i++) {
				if (i == 0) {
					net.get(i).next = net.get(i + 1);
					net.get(i).prev = net.get(n - 1);
				} else if (i == n - 1) {
					net.get(i).next = net.get(0);
					net.get(i).prev = net.get(i - 1);
				} else {
					net.get(i).next = net.get(i + 1);
					net.get(i).prev = net.get(i - 1);
				}
			}
		}
	}

	/* Methods */
	// Adds a Process to the network
	public void add(Process p) {
		net.add(p);
	}

	// Returns the size of network
	public int size() {
		return net.size();
	}

	// Printing Network mainly used for testing
	public void printNet() {
		System.out.println("Round " + round + ":");
		for (Process i : net) {
			System.out.println(i.sentCount + " InId: " + i.inID + ", Prev Id: " + i.prev.myID + ", My Id: "
					+ i.myID + ", Next Id: " + i.next.myID + ", sendID: " + i.sendID);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	// Also used for testing
	public void printNetHS() {
		System.out.println("Round " + (round - 1) + ":");
		for (Process i : net) {
			System.out.println(i.sentCount + " " + i.sendCC + " CCin: " + i.inCCID + " " + i.inCCDir + " " + i.inCCHop +
					", CCSend: " + i.sendCCID + i.sendCCDir + i.sendCCHop + ", My Id: " + i.myID + ", ClockSend: "
					+ i.sendID
					+ i.sendDir + i.sendHop + ", InID: " + i.inID + " " + i.inDir + " " + i.inHop + i.send);
		}
	}

	// Method for reseting the network to non-leader selected form
	public void resetNetwork() {
		for (Process i : net) {
			i.sendID = i.myID;
			i.sendCCID = i.myID;
			i.inID = 0;
			i.inCCID = 0;
			i.round = 1;
			i.sentCount = 0;
			i.send = true;
			i.sendCC = true;
			i.status = "unknown";
			i.phase = 0;
			i.leader = 0;
		}
		round = 0;
		leaderNode = 0;
		for (int i = 0; i < net.size(); i++) {
			if (i == 0) {
				net.get(i).next = net.get(i + 1);
				net.get(i).prev = net.get(net.size() - 1);
			} else if (i == net.size() - 1) {
				net.get(i).next = net.get(0);
				net.get(i).prev = net.get(i - 1);
			} else {
				net.get(i).next = net.get(i + 1);
				net.get(i).prev = net.get(i - 1);
			}
		}
	}

	// Method for shuffling the Network to give a random sequence of Processes
	public void shuffleNetwork() {
		Collections.shuffle(net);
		resetNetwork();
		order = "IDs shuffled";
	}

	// Comparator used in Sorting
	class SortByID implements Comparator<Process> {
		@Override
		public int compare(Process a, Process b) {
			return a.myID - b.myID;
		}
	}

	// Used to sort the network increasing clockwise
	public void sortNetwork() {
		Collections.sort(net, new SortByID());
		resetNetwork();
		order = "IDs increasing Clockwise";
	}

	// Used to sort the network increasing counter-clockwise
	public void reverse() {
		sortNetwork();
		Collections.reverse(net);
		resetNetwork();
		order = "IDs increasing Counter-clockwise";
	}

	// Method for choosing leader using LCR
	public responseObj LCRElectLeader() {
		while (leaderNode == 0) {
			for (Process i : net) {
				i.LCRgen();
				if (i.status == "leader") {
					leaderNode = i.myID;
				}
			}
			// printNet();

			for (Process i : net) {
				i.LCRSend();
			}
			round++;
		}

		for (Process i : net) {
			i.leader = leaderNode;
		}

		int msgTot = 0;
		for (Process i : net) {
			msgTot += i.sentCount;
		}

		responseObj msg = new responseObj();
		msg.msgString = "For " + order + " (LCR) with " + net.size() + " processes:\n" + "Leader: " + leaderNode
				+ ", Messages sent: " + msgTot + ", Rounds: " + round + "\n";
		msg.msgTot = msgTot;
		return msg;
	}

	// Method for choosing leader using HS
	public responseObj HSElectLeader() {
		while (leaderNode == 0) {
			// printNetHS();
			for (Process i : net) {
				i.HSgen();
				if (i.status == "leader") {
					leaderNode = i.myID;
				}
			}

			for (Process i : net) {
				i.HSSend();
			}
			round++;
		}

		for (Process i : net) {
			i.leader = leaderNode;
		}
		int msgTot = 0;
		for (Process i : net) {
			msgTot += i.sentCount;
		}

		responseObj msg = new responseObj();
		msg.msgString = "For " + order + " (HS) with " + net.size() + " processes:\n" + "Leader: " + leaderNode
				+ ", Messages sent: " + msgTot + ", Rounds: " + round + "\n";
		msg.msgTot = msgTot;
		return msg;
	}

}
