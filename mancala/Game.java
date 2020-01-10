package mancala;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;


class Game{
	house[] my_house;//house for the game
	int mode;
	String INFO;
	String Holeconfig;
	Player currentPlayer;
	boolean nowPlayer;
	long time;
	
	public Game(int pickedNumber, int numofseeds,long t) {//picked number of pits, 
		time = t;
		INFO = new String();
		INFO = "INFO"+" "+Integer.toString(pickedNumber)+" "+Integer.toString(numofseeds)+" "+Long.toString(t)+" ";
		Holeconfig = new String();
		Holeconfig = "S"+" ";
		int sizeofpits = pickedNumber*2+2;
		int board1=pickedNumber;//the board for the first player side
		int board2=sizeofpits-1;//the board for the second player side
		my_house=new house[sizeofpits];//there are 14 houses(2 of which are score boards)
		for(int i =0;i<board1+1;i++) {
			my_house[i]= new house();
			my_house[i].set_sides(true);//setting the user side to be true
			if(i==board1) {
				my_house[i].set_seeds(0);
			}
			else {
				my_house[i].set_seeds(numofseeds);
			}
			}

			//setting for the AI side(opponent)
			for(int i =board1+1;i<sizeofpits;i++) {
			my_house[i]=new house();
			my_house[i].set_sides(false);//setting the AI side to be false
			if(i==board2) {
				my_house[i].set_seeds(0);
			}
			else {
				my_house[i].set_seeds(numofseeds);
			}
			}
	}
	public Game(int pickedNumber, int numofseeds,int ran,long t) {//random distribution
		time = t;
		INFO = new String();
		INFO = "INFO"+" "+Integer.toString(pickedNumber)+" "+Integer.toString(numofseeds)+" "+Long.toString(t)+" ";
		Holeconfig = new String();
		Holeconfig = "R"+" ";
		int sizeofpits = pickedNumber*2+2;
		int board1=pickedNumber;//the board for the first player side
		int board2=sizeofpits-1;//the board for the second player side
		my_house=new house[sizeofpits];//there are 14 houses(2 of which are score boards)
		
		Random rand1 = new Random();  
		for(int i =0;i<board1+1;i++) {
			int pickedseeds = rand1.nextInt(10) + 1; 
			Holeconfig+=(Integer.toString(pickedseeds)+" ");
			my_house[i]= new house();
			my_house[i].set_sides(true);//setting the user side to be true
			if(i==board1) {
				my_house[i].set_seeds(0);
			}
			else {
				my_house[i].set_seeds(pickedseeds);
			}
			}

			//setting for the AI side
			for(int i =board1+1;i<sizeofpits;i++) {
			my_house[i]=new house();
			my_house[i].set_sides(false);//setting the AI side to be false
			if(i==board2) {
				my_house[i].set_seeds(0);
			}
			else {
				my_house[i].set_seeds(my_house[i-board1-1].get_seeds());
			}
			}	
	}
	public void switch_player() {
		currentPlayer = currentPlayer.opponent;
	}
	
    public synchronized boolean legalMove(int location, Player player) {//check valid move
        
            boolean sid = player.sides;
            int sizeofpits = my_house.length;
            int board1=(sizeofpits-2)/2;//the board for the first player side
            int board2=sizeofpits-1;//the board for the second player side
            if(sid) {//true sides
            	location = location -1;//modify to fit
            	 if(location>=0 && location<board1 && my_house[location].get_seeds()!=0) {
            		 MancalaBoard.update_house(my_house, location, sid);//update local copy
            		 //currentPlayer = currentPlayer.opponent;
            		// currentPlayer.otherPlayerMoved(location);
                  return true;
            	 }else return false;
            }
            else {
            	location = location +board1;//modify to fit
            	if(location>board1 && location<board2 && my_house[location].get_seeds()!=0) {
            		MancalaBoard.update_house(my_house, location, sid);//update local copy
            		//currentPlayer = currentPlayer.opponent;
            		//currentPlayer.otherPlayerMoved(location);
                return true;
           	 }else return false;
            }
       
        //return false;
    }
	 /**
     * The class for the helper threads in this multi threaded server
     * application.  A Player is identified by a boolean side
     * which is either false or true.  For communication with the
     * client the player has a socket with its input and output
     * streams.  Since only text is being communicated we use a
     * reader and a writer.//try GUI later
     */
	class Player extends Thread{//player thread for
		boolean sides;//turn
		Player opponent;
		Socket socket;
		BufferedReader input;//get the message from the client
		PrintWriter output;//to write message to the client
		/**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields, displays the first two
         * welcoming messages.
         */
        public Player(Socket sock, boolean sid) {
            socket = sock;
            sides = sid;
            try {
                input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                
                output.println("WELCOME");
               
                
                System.out.println("MESSAGE Waiting for opponent to connect");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }
        /**
         * Accepts notification of who the opponent is.
         */
        public void setOpponent(Player opponent) {
            this.opponent = opponent;
        }
        /**
         * Handles the otherPlayerMoved message.
         */
        
        public void otherPlayerMoved(int location) {
         int sizeofpits = my_house.length;
         int board1=(sizeofpits-2)/2;//the board for the first player side
         int board2=sizeofpits-1;//the board for the second player side
        //	output.println("OPPONENT_MOVED " + location);
        	if(!MancalaBoard.check_state(my_house)) {
        	if(sides) {//score board is board1 PLAYER1
        		if(my_house[board1].get_seeds() >my_house[board2].get_seeds())
        			output.println("WIN");
        		else if(my_house[board1].get_seeds()==my_house[board2].get_seeds())
        			output.println("TIE");
        		else
        			output.println("LOSER");
        		}
        	else {//PLAYER 2
        		if(my_house[board1].get_seeds() >my_house[board2].get_seeds())
        			output.println("LOSER");
        		else if(my_house[board1].get_seeds()==my_house[board2].get_seeds())
        			output.println("TIE");
        		else
        			output.println("WIN");
        		}
        	}
        	}
        
        /**
         * The run method of this thread.
         */
        public void run() {
        	try {
        		 // The thread is only started after everyone connects.
        		//INFO
        		if(sides) {
        			INFO+=("F"+" ");
        		}
        		else {
        			INFO+=("S"+" ");
        		}
             output.println(INFO+Holeconfig);   

                // Tell the first player that it is her turn.
               /* if (sides) {
                    output.println("MESSAGE Your move");
                }*/
        		
             // Repeatedly get commands from the client and process them.
              outerloop:{  while (true) {
                    String command = input.readLine();
                    if (command.startsWith("OK")) {
                        continue;
                    } 
                    else if (command.startsWith("READY")) {
                        continue;
                    }
                    else {//recieve opponent move
                    	String[] parts = command.split(" ");//update copy
            			for(int i =0;i<parts.length;i++) {
            			    int location = Integer.parseInt(parts[i]);
                        if (legalMove(location, this.opponent)) {//means the move is legal
                           //check winner
                           	if(!MancalaBoard.check_state(my_house)) {
                           	String response = MancalaBoard.decide_winner(my_house, sides);
                           	output.println(response);
                           	break;
                           	}
                           	
                        } else {
                            output.println("ILLEGAL");
                            output.println("LOSER");
                            break outerloop;
                        }
                        }
            			output.println("OK");
            			//sending move (GUI or AI)
            			
                    
                    
                    }
                }
             }
        		
        	}catch (IOException e) {
                System.out.println("Player died: " + e);
        } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }
        
	}
	 
}
