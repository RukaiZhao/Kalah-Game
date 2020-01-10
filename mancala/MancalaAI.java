package mancala;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
public class MancalaAI {
private int Max_depth;
private house[] state;
private house[] update_state;
private house[] original;
ArrayList<Integer> best_moves;
boolean sides;//which side you are playing true is the first player false is the second player
boolean opponent_sides;
int numofpie;//how many times of pie rule, since the pie rule only run once
int bestm_index;
boolean pie_move;
MancalaAI(int depth, house[] h,boolean sid){
	Max_depth = depth;
	state = h.clone();
	sides =sid;
	update_state = h.clone();
	opponent_sides = !sides;
	numofpie = 0;
	 original = new house[h.length];
	 best_moves = new ArrayList<Integer>();
	 bestm_index=0;
	 pie_move=false;
	//System.arraycopy(h, 0, temp, 0, h.length);
	for(int j=0;j<h.length;j++) {
		original[j] = new house();
		original[j].set_sides(h[j].get_sides());
		original[j].set_seeds(h[j].get_seeds());
	}
}
public int utility(house[] h) {//utility function
	int size = h.length;//size of the house[] h
	int board1 = (size-2)/2;
	int board2 = size-1;
	
	return h[board2].get_seeds()-h[board1].get_seeds();
}

public boolean Terminal_test(house[] h,int depth,boolean flag) {//terminal test function
   if (is_leaf(h,flag)||depth>=Max_depth)//true than return true
	   return true;
   else return false;
	   
}
public boolean is_leaf(house[] h,boolean flag) {
    ArrayList<house[]> successors_house = Successors(h,flag);
    if(successors_house.size() == 0)
    	return true;
    else 
    	return false;
}
public ArrayList<house[]> Successors(house[] h,boolean flag) {//house[] h suppose to be the current house[]
	ArrayList<house[]> successors_house = new ArrayList<house[]>();
	int size = h.length;//size of the house[] h
	int board1 = size/2;
	int board2 = size-1;
	if(flag == false) {
	for(int i = board1; i<board2;i++) {//traverse the element to generate different type of results
		
		house[] temp = new house[h.length];
		//System.arraycopy(h, 0, temp, 0, h.length);
		for(int j=0;j<temp.length;j++) {
			temp[j] = new house();
			temp[j].set_sides(h[j].get_sides());
			temp[j].set_seeds(h[j].get_seeds());
		}
		
		MancalaBoard.update_house(temp,i,false);
		//MancalaBoard.print_board(temp);
		//MancalaBoard.print_board(h);
		successors_house.add(temp);
		
	}
	}
	else {
		for(int i =0; i<board1-1;i++) {//traverse the element to generate different type of results
			if(h[i].get_seeds()!= 0) 
			{
			house[] temp = new house[h.length];
			//System.arraycopy(h, 0, temp, 0, h.length);
			for(int j=0;j<temp.length;j++) {
				temp[j] = new house();
				temp[j].set_sides(h[j].get_sides());
				temp[j].set_seeds(h[j].get_seeds());
			}
			
			MancalaBoard.update_house(temp,i,true);
			//MancalaBoard.print_board(temp);
			//MancalaBoard.print_board(h);
			successors_house.add(temp);
			}
		}
	}
	return successors_house;
}
public int MAX(int a, int b) {
	if(a>=b)
		return a;
	else
		return b;
}
public int MIN(int a, int b) {
	if(a<=b)
		return a;
	else
		return b;
}
//MINMAX TREE
public int Max_Value(house[] h, int depth, int alpha, int beta) {
	if(Terminal_test(h,depth,sides))
		return utility(h);
	int v = Integer.MIN_VALUE;
	int index =0;
	ArrayList<house[]> successors_house = Successors(h,sides);
	for(int i = 0;i<successors_house.size();i++) {
		int m = v;
		v = MAX(v,Min_Value(successors_house.get(i),depth+1,alpha,beta));
		if(v>m)
		index = i;
		
		alpha = MAX(v,alpha);
		if(v>beta) {
		    update_state = successors_house.get(i);
			return v;
		 }
	}
	update_state=successors_house.get(index);
	bestm_index=index;
	return v;
	
}
public int Min_Value(house[]h,int depth, int alpha, int beta) {
	if(Terminal_test(h,depth,opponent_sides))
		return utility(h);
	int v = Integer.MAX_VALUE;
	int index =0;//index for changing state
	ArrayList<house[]> successors_house = Successors(h,opponent_sides);
	for(int i = 0;i<successors_house.size();i++) {
		int m = v;
		v = MIN(v,Max_Value(successors_house.get(i),depth+1,alpha,beta));
		if(v<m)
		index = i;//change it to this state
		
		beta = MIN(v,beta);
		if(v<alpha)
		 return v;
		
	}
	update_state=successors_house.get(index);
	bestm_index=index;
	return v;
}
public ArrayList<Integer> bestmove() {
	ArrayList<Integer> temp = new ArrayList<Integer>(best_moves);
	best_moves.clear();
	return temp;
}
public int move() {
	/*MancalaBoard.print_board(original);
	int size = original.length;//size of the house[] h
	int board1 = size/2;
	int board2 = size-1;
	int index = bestm_index+1;
	if(sides) {
		for(int i=0;i<board1-1;i++) {
			if(original[i].get_seeds()!=0) {
				index--;
				if(index == 0)
					return i;
			}
				
		}
	}
	else {
		for(int i=board1;i<board2;i++) {
			if(original[i].get_seeds()!=0) {
				index--;
				if(index == 0)
					return i;
			}
		}
	}*/
	int size = original.length;//size of the house[] h
	int board1 = size/2;
	int board2 = size-1;
	if(sides) 
	return bestm_index;
	else
		return board1+bestm_index;
}
public boolean is_pie() {
	return pie_move;//true than is pie, false than is not pie
}
public house[] update_AI() {
	int alpha = Integer.MIN_VALUE;
	int beta = Integer.MAX_VALUE;
	int size =state.length;//size of the house[] h
	//check if there is a free turn
	if(numofpie == 0) {
		numofpie++;//only play pie rule once
	if(sides == false) {//if is the second player
		//System.out.println("Pie Rule? 1 for Yes, 2 for No");
		//Scanner scan = new Scanner(System.in);
		Random rand = new Random(); 
		int choice = rand.nextInt(2) + 1;//read the choice
		
		if(choice == 1) {
			state = MancalaBoard.pie_rule(state);
			pie_move = true;
			update_state = state;
			return update_state;
			
		}
	}
	}
	
	
	
	int board_1 = size/2;
	int board_2 = size-1;
	int turn1=1;
	int index=0;
    while(turn1 == 1 && MancalaBoard.check_state(state)) {
    	   if(!sides) {
		for(int i = board_1; i<board_2;i++) {//traverse the element to generate different type of results
			if(state[i].get_seeds()!= 0) 
			{
			house[] temp = new house[state.length];
			int turn = 0;
			//System.arraycopy(h, 0, temp, 0, h.length);
			for(int j=0;j<temp.length;j++) {
				temp[j] = new house();
				temp[j].set_sides(state[j].get_sides());
				temp[j].set_seeds(state[j].get_seeds());
			}
			
			turn = MancalaBoard.update_house(temp,i,sides);
			if(turn == 1)
			{
				for(int j=0;j<temp.length;j++) {//copy temp to house
					state[j].set_sides(temp[j].get_sides());
					state[j].set_seeds(temp[j].get_seeds());
				}
				turn1=1;
				index =i;
				break;
			}
			else turn1=0;	
			//MancalaBoard.print_board(temp);
			//MancalaBoard.print_board(h);
			}
		}
    	   }
    	   else {
    			for(int i = 0; i<board_1-1;i++) {//traverse the element to generate different type of results
    				if(state[i].get_seeds()!= 0) 
    				{
    				house[] temp = new house[state.length];
    				int turn = 0;
    				//System.arraycopy(h, 0, temp, 0, h.length);
    				for(int j=0;j<temp.length;j++) {
    					temp[j] = new house();
    					temp[j].set_sides(state[j].get_sides());
    					temp[j].set_seeds(state[j].get_seeds());
    				}
    				
    				turn = MancalaBoard.update_house(temp,i,sides);
    				if(turn == 1)
    				{
    					for(int j=0;j<temp.length;j++) {//copy temp to house
    						state[j].set_sides(temp[j].get_sides());
    						state[j].set_seeds(temp[j].get_seeds());
    					}
    					turn1=1;
    					index=i;
    					break;
    				}
    				else turn1=0;	
    				//MancalaBoard.print_board(temp);
    				//MancalaBoard.print_board(h);
    				}
    			}
    	   }
	if(turn1==1) {
    best_moves.add(index);
	MancalaBoard.print_board(state);
	update_state=state;
	}
    }
	int max = Max_Value(state,0,alpha, beta );
	
	state= update_state;
    best_moves.add(move());
	return update_state;
}

}
