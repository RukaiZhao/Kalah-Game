package mancala;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;




public class MancalaClient {
private static int PORT = 8901;
private Socket socket;
private BufferedReader in;
private PrintWriter out;
MancalaAI Client_AI;
house[] mancala_client;//data
int mode;//two mode play as AI or play as GUI
long time;
boolean turn;/// true player 1 , false player 2
//mode one play as AI
//mode two play as GUI
ArrayList<Integer> AI_move;//MOVE AI
ArrayList<Integer> GUI_move;//MOVE GUI

public MancalaClient(String serverAddress,int mod) throws Exception{
    socket = new Socket(serverAddress, PORT);
	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);
    mode = mod;
 }

public void play() {
	String process;
	try {
		process= in.readLine();
		//first recieves Welcome and INFO initialize everything (setting up)
		if(process.startsWith("WELCOME")) {
		while(true){
			process=in.readLine();
			if(process == "INFO")
				break;
		}
		}
		if(process.startsWith("INFO")) {//if the class recieves INFO
		String[] parts = process.split(" ");
		int numofholes = Integer.parseInt(parts[1]);//num of hole
		int board1 = numofholes;
		int board2 = numofholes*2+1;
		int numofseeds = Integer.parseInt(parts[2]);
		time = Integer.parseInt(parts[3]);
		mancala_client = new house[numofholes*2+2];//initialize mancalaboard
		//part[4] is the turn
		if(parts[4]=="F")//initialize turn
			turn = true;
		else turn =false;
		if(parts[5]=="R") {
			for(int i=0;i<numofholes;i++) {
				mancala_client[i].set_seeds(Integer.parseInt(parts[6+i]));
			}
			for(int i=board1+1;i<board2;i++) {
				mancala_client[i].set_seeds(mancala_client[i-numofholes-1].get_seeds());
			}
			mancala_client[board1].set_seeds(0);
			mancala_client[board2].set_seeds(0);
		}
		else {
			for(int i=0;i<numofholes;i++) {
				mancala_client[i].set_seeds(numofseeds);
			}
			for(int i=board1+1;i<board2;i++) {
				mancala_client[i].set_seeds(numofseeds);
			}
			mancala_client[board1].set_seeds(0);
			mancala_client[board2].set_seeds(0);
		}
		if(mode == 1)//initialize AI
			Client_AI= new MancalaAI(6,mancala_client,turn);	
		else {
			//GUI input
		}
		//return ready if recieved
		out.println("READY");
		//depending on the turn do move
		if(turn) {//1
			if(mode == 1) {//doing move
				Client_AI.update_AI();
				AI_move = new ArrayList<Integer> (Client_AI.bestmove());
				String notify_move = new String();
				for(int i=0;i<AI_move.size();i++) {
					int location = AI_move.get(i) + 1;
					notify_move += Integer.toString(location)+" ";
				}
				out.println(notify_move);
				AI_move.clear();
			}
			else {//gui move
				
			}
		}
		}
		while(true) {
			process = in.readLine();
		if(process.startsWith("OK")) {
			continue;
		}
		else if(process.startsWith("TIME")) {
			continue;
		}
		else if(process.startsWith("ILLEGAL")) {
			continue;
		}
		else if(process.startsWith("WIN")) {
			//GUI OUTPUT "YOU WIN"
			break;
		}
		else if(process.startsWith("LOSER")) {
			//GUI OUTPUT "YOU LOSE"
			break;
		}
		else if(process.startsWith("TIE")) {
			//GUI OUTPUT "TIE"
			break;
		}
		else {//if recieve numbers
			String[] parts = process.split(" ");//update copy
			for(int i =0;i<parts.length;i++) {
				int location =Integer.parseInt(parts[i]);
				if(!turn) {//opponent is player 1
				location = location -1;
				MancalaBoard.update_house(mancala_client,location,!turn);
				}
				else {
				int sizeofpits = mancala_client.length;
		        int board1=(sizeofpits-2)/2;//the board for the first player side
				location = location+board1;
				MancalaBoard.update_house(mancala_client,location,!turn);
				}
			}
			out.println("OK");
			
			//need to add pie rule
			if(mode == 1) {//doing move
				Client_AI.update_AI();
				AI_move = new ArrayList<Integer> (Client_AI.bestmove());
				String notify_move = new String();
				for(int i=0;i<AI_move.size();i++) {
				if(turn) {//player 1 
					int location = AI_move.get(i) + 1;
					notify_move += Integer.toString(location)+" ";
					}
				else {//player 2
					if(Client_AI.is_pie()) {//there is a pie move
						notify_move = "P";
					}
					else{
					int location = AI_move.get(i);
					int size = mancala_client.length;
					int board1 = (size-2)/2;
					location = location -board1;
					notify_move += Integer.toString(location)+" ";}
				}
				}
				out.println(notify_move);
				AI_move.clear();
			}
			else {//gui move
				
			}
		
		}
		
		
	} 
		}
	catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	
	
	
	
}

public static void main(String[] args) throws Exception  {
    while (true) {

    	    int mode =1;
        String serverAddress = (args.length == 0) ? "localhost" : args[1];
        MancalaClient client = new MancalaClient(serverAddress,mode);
        client.play();
    }
}
}
