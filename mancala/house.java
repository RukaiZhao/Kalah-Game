package mancala;

//class for holding the seeds
public class house {
boolean sides;//determine which side the house belongs to
int seeds; //holds the number of seeds currently in the house

public house() {
	sides = true;
	seeds = 0;
}

public void add_seeds() {// the adding function that can add seeds
	seeds++;
}

public void dec_seeds() {//decreasing function that we can decrease seeds
	seeds--;
}

public void set_sides(boolean flag) {//setting the side of the house
	sides = flag;
}

public void set_seeds(int see) {//add seeds 
	seeds=see;
}

public void empty_seeds() {//empty seeds
	seeds=0;
}

public int get_seeds() {//get the value of the seeds in the house
	return seeds;
}

public boolean get_sides() {//get the side of the house
	return sides;
}

}
