package mancala;
import java.util.Scanner;
import java.util.Random;
public class MancalaBoard {
//function for updating the houses
public static void print_board(house[] h) {
	System.out.print("  ");
	int size = h.length;
	int board1 = (size-2)/2;
	int board2 = size-1;
	for(int i=board2-1;i>board1;i--) {
		System.out.print(h[i].get_seeds()+" ");
	}
	System.out.print("\n"+h[board2].get_seeds()+"             "+h[board1].get_seeds()+"\n");
	System.out.print("  ");
	for(int i=0;i<board1;i++) {
		System.out.print(h[i].get_seeds()+" ");
	}
	System.out.println();
	System.out.println();
}

public static int update_house(house[] h,int choice,boolean sides) {//reference false for the AI,automatically change house
	int numofpits=h.length;
	int board1 = (numofpits-2)/2;
	int board2 = numofpits -1;
	int num = h[choice].get_seeds();//get the seeds in the choice
	h[choice].empty_seeds();//empty the seeds in the house
	int score_board;
	int turn;
	if(sides) {
		score_board = board1;
	}
	else score_board = board2;
	
	if(sides) {//user side
	while(num > 1) {//adding seeds to the following choice
		choice = (choice + 1) % numofpits;
		if(choice != board2)
		{
		h[choice].add_seeds();//increase by 1
		num --;
		}
	}
	}
	else {// another user or AI side
		while(num > 1) {//adding seeds to the following choice
			choice = (choice + 1) % numofpits;
			if(choice != board1)
			{
			h[choice].add_seeds();//increase by 1
			num --;
			}
		}	
	}
	//determine whether the last one is the scoreboard
	if(sides) {
		if((choice+1)%numofpits==board1)
			turn = 1;
		else turn = 2;
	}
	else {
		if((choice+1)%numofpits==board2)
			turn = 1;
		else turn = 2;
	}
	//determine if the last one is empty
	if(num!=0) {//shouldn't remove from 0 seeds
	if(sides) {//user side
	if((choice+1)%numofpits>=0 && (choice+1)%numofpits<=(board1-1)) {
		choice=(choice+1)%numofpits;
		if(h[numofpits-2-choice].get_seeds()!=0 && h[choice].get_seeds()==0) {
			h[choice].add_seeds();
			h[score_board].set_seeds(h[numofpits-2-choice].get_seeds() + h[choice].get_seeds()+h[score_board].get_seeds());
			h[numofpits-2-choice].empty_seeds();
			h[choice].dec_seeds();
		}
		else {
			h[choice].add_seeds();
		}
	}
	else if((choice+1)%numofpits==board2){
		choice=(choice+2)%numofpits;
		if(h[numofpits-2-choice].get_seeds()!=0 && h[choice].get_seeds()==0) {
			h[choice].add_seeds();
			h[score_board].set_seeds(h[numofpits-2-choice].get_seeds() + h[choice].get_seeds()+h[score_board].get_seeds());
			h[numofpits-2-choice].empty_seeds();
			h[choice].dec_seeds();
		}
		else {
			h[choice].add_seeds();
		}
	}
	else {
		h[(choice+1)%numofpits].add_seeds();
	}
	}
	else {//AI side
		if((choice+1)%numofpits>=(board1+1) && (choice+1)%numofpits<=(board2-1) ) {
			choice=(choice+1)%numofpits;
			if(h[numofpits-2-choice].get_seeds()!=0 && h[choice].get_seeds()==0) {
				h[choice].add_seeds();
				h[score_board].set_seeds(h[numofpits-2-choice].get_seeds() + h[choice].get_seeds()+h[score_board].get_seeds());
				h[numofpits-2-choice].empty_seeds();
				h[choice].dec_seeds();
			}
			else {
				h[choice].add_seeds();
			}
		}
		else if((choice+1)%numofpits==board1) {
			choice=(choice+2)%numofpits;
			if(h[numofpits-2-choice].get_seeds()!=0 && h[choice].get_seeds()==0) {
				h[choice].add_seeds();
				h[score_board].set_seeds(h[numofpits-2-choice].get_seeds() + h[choice].get_seeds()+h[score_board].get_seeds());
				h[numofpits-2-choice].empty_seeds();
				h[choice].dec_seeds();
			}
			else {
				h[choice].add_seeds();
			}
			
		}
		else {
			h[(choice+1)%numofpits].add_seeds();
		}	
	}
	}
	return turn;
}
//checking the state of the board, if all of the 12 houses are empty
//then compare the number of each of the score board
public static boolean check_state(house[] h) {//true means continue, false means end of game
	boolean flag1 = false;
	boolean flag2 = false;
	int numofpits = h.length;
	int board1 = (numofpits-2)/2;
	int board2 = numofpits-1;
	for(int i =0;i<board1;i++) {//flag1 is player 1 side
		if(h[i].get_seeds()!=0) {
			flag1 = false;
			break;
		}
		else 
			flag1 = true;
	}
	
	for(int i = (board1+1);i<board2;i++) {//flag2 is player 2 side
		if(h[i].get_seeds()!=0) {
			flag2 = false;
			break;
		}
		else 
			flag2 = true;
	}
	
	if(flag2||flag1)//if one of it is true
	{
		/*if(flag2) {//player2 all zero
			int sum=h[board1].get_seeds();
			for(int i =0;i<board1;i++) {
				sum+=h[i].get_seeds();
			}
			if(h[board1].get_seeds() >h[board2].get_seeds())
				System.out.println("You Win!!!!!\n");
			else if(h[board1].get_seeds()==h[board2].get_seeds())
				System.out.println("Game Over!!!!!\n");
			else
				System.out.println("You lost!!!\n");
			
			return false;
			
		}
		else if(flag1) {
			int sum=h[board2].get_seeds();
			for(int i = (board1+1);i<board2;i++) {
				sum+=h[i].get_seeds();
			}
			if(h[board1].get_seeds() >h[board2].get_seeds())
				System.out.println("You Win!!!!!\n");
			else if(h[board1].get_seeds()==h[board2].get_seeds())
				System.out.println("Game Over!!!!!\n");
			else
				System.out.println("You lost!!!\n");
			
			return false;
		}*/
	   return false;		
	}
	else 
		return true;
		
}
public static String decide_winner(house[] h,boolean side) {
	int sizeofpits = h.length;
    int board1=(sizeofpits-2)/2;//the board for the first player side
    int player1_score=0;
    int board2=sizeofpits-1;//the board for the second player side
    int player2_score=0;
    for(int i=0;i<=board1;i++) {//player 1
    	  player1_score+=h[i].get_seeds();
    }
    for(int i=board1+1;i<=board2;i++) {//player 2
    	player2_score+=h[i].get_seeds();
    }
    if(side) {//player 1 side
    	if(player1_score>player2_score)
    		return "WINNER";//player 1 win
    	else if(player1_score==player2_score)
    		return "TIE";
    	else if(player1_score<player2_score)
    		return "LOSER";
    }
    else {//player 2 side
     	if(player1_score<player2_score)
    		return "WINNER";//player 1 win
      	else if(player1_score==player2_score)
    		return "TIE";
      	else if(player1_score>player2_score)
    		return "LOSER";
    }
    return "SOMETHING";//never need to execute this line

}
public static house[] pie_rule(house[] state) {
	house[] update = new house[state.length];
	int sizeofpits =state.length;
	int half = sizeofpits/2;
	int board1=(state.length-2)/2;//the board for the first player side
	int board2=sizeofpits-1;//the board for the second player side
	for(int i =0;i<board1+1;i++) {
		update[i]= new house();
		update[i].set_sides(true);//setting the user side to be true
		if(i==board1) {
			update[i].set_seeds(state[board2].get_seeds());
		}
		else {
			update[i].set_seeds(state[half+i].get_seeds());
		}
		}

		//setting for the AI side(opponent)
		for(int i =board1+1;i<sizeofpits;i++) {
			update[i]=new house();
			update[i].set_sides(false);//setting the AI side to be false
		if(i==board2) {
			update[i].set_seeds(state[board1].get_seeds());
		}
		else {
			update[i].set_seeds(state[i-half].get_seeds());
		}
		}
		return update;
}
	

}