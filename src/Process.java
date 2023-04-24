public class Process {
	/* Parameters */
	//For clockwise msg sending
	int sendID;
	int inID;
	int sendDir;// 0 will represent out, 1 will represent in.
	int inDir; 
	int sendHop;
	int inHop;
	//For Counter-Clockwise msg sending
	int sendCCID;
	int inCCID;
	int sendCCDir; // 0 will represent out, 1 will represent in.
	int inCCDir;
	int sendCCHop;
	int inCCHop;
	//Every thing below will be used for both HS and LCR algorithms
	int myID;
	int round;
	int phase;
	int sentCount;
	int leader;
	boolean send;
	boolean sendCC;
	String status;
	Process next;
	Process prev;
	
	/* Constructor */
	public Process(int n) { //n will be the id number
		this.myID = n;
		this.sendID = myID;
		this.inCCID = 0;
		this.round = 1;
		this.phase = 0;
		this.sentCount = 0;
		this.send = true;
		this.status = "unknown";
	}
	
	/* Methods */
	//Method for LCR message generation
	public void LCRgen() {
		if(round == 1) {
			send = true;
		} else {
			if(inCCID > myID) {
				sendID = inCCID;
				inCCID = 0;
				send = true;
			} else if (inCCID == myID) {
				status = "leader";
			} else {
				send = false;
			}
		}
	}
	
	//Method for LCR message sending
	public void LCRSend() {
		if(send) {
			this.next.inCCID = sendID;
			sentCount++;
		}
		round++;
	}
	
	//Method for LCR message generation
	public void HSgen() {
		if(round == 1) {
			sendID = myID;
			sendCCID = myID;
			sendDir = 0;
			sendCCDir = 0;
			sendHop = 1;
			sendCCHop = 1;
			send = true;
			sendCC = true;
		} else {
			//If outgoing message coming from the counterclockwise
			if(inCCDir == 0) {
				if(inCCID > myID && inCCHop > 1) {
					sendID = inCCID;
					sendDir = 0;
					sendHop = inCCHop - 1;
					inCCID = -1;
					send = true;
				} else if(inCCID > myID && inCCHop == 1){
					sendCCID = inCCID;
					sendCCDir = 1;
					sendCCHop = 1;
					inCCID = -1;
					sendCC = true;
				} else if(inCCID == myID) {
					status = "leader";
				}
			}
			
			//If outgoing message coming from the clockwise
			if(inDir == 0) {
				if(inID > myID && inHop > 1) {
					sendCCID = inID;
					sendCCDir = 0;
					sendCCHop = inHop - 1;
					inID = -1;
					sendCC = true;
				} else if(inID > myID && inHop == 1){
					sendID = inID;
					sendDir = 1;
					sendHop = 1;
					inID = -1;
					send = true;
				} else if(inID == myID) {
					status = "leader";
				}
			}
			
			if(inCCDir == 1) {
				if(inCCID != myID && inCCID != -1) {
					sendID = inCCID;
					sendDir = 1;
					sendHop = 1;
					inCCID = -1;
					send = true;
				}
			}
			
			if(inDir == 1) {
				if(inID != myID && inID != -1) {
					sendCCID = inID;
					sendCCDir = 1;
					sendCCHop = 1;
					inID = -1;
					sendCC = true;
				}
			}
			
			if(inDir == 1 && inCCDir == 1 && inID == myID && inCCID == myID) {
				phase++;
				sendID = myID;
				sendDir = 0;
				sendHop = (int) Math.pow(2, phase);
				sendCCID = myID;
				sendCCDir = 0;
				sendCCHop = (int) Math.pow(2, phase);
				inID = -1;
				inCCID = -1;
				send = true;
				sendCC = true;
			}
			
		}
	}
	
	//Method for single process execution of the HS algorithm
	public void HSSend() {
		if(send && sendCC) {
			next.inCCID = sendID;
			prev.inID = sendCCID;
			next.inCCDir = sendDir;
			prev.inDir = sendCCDir;
			next.inCCHop = sendHop;
			prev.inHop = sendCCHop; 
			sentCount += 2;
		} else if(send && !sendCC) {
			next.inCCID = sendID;
			next.inCCDir = sendDir;
			next.inCCHop = sendHop;
			sentCount++;
		} else if(!send && sendCC) {
			prev.inID = sendCCID;
			prev.inDir = sendCCDir;
			prev.inHop = sendCCHop;
			sentCount++;
		}
		send = false;
		sendCC = false;
		round++;
	}
}
