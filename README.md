# COMP212-Assignment-2
Assignment 2 regarding Sockets and Leader Election
Created by Chang-Woo Ham for the University of Liverpool's COMP 212 Distributed Systems class.

IDE Used: Visual Studio CODE

#Description:
For this assignment, the user is able to emulate a socket connection by creating a Server and Client. Extending off of assignment 1, the user requests the results of the LCR and HS algorithms through the Client, and the server returns the results back to the client. The server also takes in multiple client connections not simulataneously.

#Packages needed:
java.io.*,  java.util.*, java.net.*

How to Run the Programs after downloading the files:
1) Go in to the folder where the java files are located
2) Open up two terminals (by pressing "Ctrl + `") and on either one of the terminals enter "javac *.java" to compile them. 
3) Then on one of the terminals enter "java Main.java" (server), then one the other terminal enter "java NetworkClient" (client). (IMPORTANT NOTE: the server must be set up first before the client can connect)
4) Then, the client terminal will prompt the user to enter in the starting number of processes, number to step by, and the Maximum number of Processes allowed.
5) After this, the user will see all the results of HS and LCR algorithms for a Clockwise increasing, CounterClockwise increasing, and randomized Network of processes. (Returned from the server to the client)
6) When this process is finished, the user will see the total number of simulations. 
7) The client will then close but the server will still be running for clients to connect again, and repeat the process as many times as one likes.
8) To close server terminal, enter "Ctrl + C".

# Other Notes
1) In each iteration, the program writes only 1 data value for the "best.dat" and "worst.dat", however for random it shuffles the Network 5 times, so 4 data values go in for one iteration.
2) For the sake of time, I recommend these input values: Starting number: 5 (should be at least 3), Step Count: 5, Max Amount: 1000 (Can go up to 2000 if you like).
