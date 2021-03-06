package assignment5;
/*
 * Critter2 - GUI
 * Shiv Lalapet
 * EID: sl39596
 * Hamza Sharif
 * EID: mhs2285
 * Unique: 15495
 * Spring 2018
 */

import java.util.List;

public abstract class Critter {

	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static int[] insight = new int[population.size()*2];

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	/**
	 *
	 * @param direction, which way Critter decides to look
	 * @param steps, how many spaces Critters looks to (1 or 2 spaces)
	 * @return String, name of Critter in sight or null
	 */
	protected final String look(int direction, boolean steps) {
		int x = this.x_coord;
		int y = this.y_coord;
		int spaces = 1;
		if(steps)
			spaces = 2;
		switch (direction){
			case 0:
				x += spaces; //east
				break;
			case 1:
				x += spaces;
				y -= spaces; //northeast
				break;
			case 2:
				y -= spaces; //north
				break;
			case 3:
				x -= spaces;
				y -= spaces; //northwest
				break;
			case 4:
				x -= spaces; //west
				break;
			case 5:
				x -= spaces;
				y += spaces; //southwest
				break;
			case 6:
				y += spaces; //south
				break;
			case 7:
				x += spaces;
				y += spaces; //southeast
				break;
			default:
				break;
		}
		this.energy -= Params.look_energy_cost;
		for(int i=0; i < insight.length; i++){
			if(insight[i] == x && insight[i+1] == y){
				if(this.isFighting && population.get(i/2).energy <= 0){
					continue;
				}
				return population.get(i/2).toString();
			}
		}
		return null;
	}
	
	/* rest is unchanged from Project 4 */
	
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	private boolean hasMoved;
	private boolean isFighting;

	/**
	 * helper function for walk and run
	 * @param steps number of spaces
	 * @return int, final position on the x-axis
	 */
	private final int moveX(int steps){
		if((x_coord + steps) < 0){
			return Params.world_width - steps;
		}
		else if(x_coord + steps > Params.world_width - 1){
			return steps - 1;
		}
		else
			return x_coord += steps;
	}

	/**
	 * helper function for walk and run
	 * @param steps number of spaces
	 * @return int, final position on the y-axis
	 */
	private final int moveY(int steps){
		if((y_coord + steps) < 0){
			return Params.world_height - steps;
		}
		else if(y_coord + steps > Params.world_height - 1){
			return steps - 1;
		}
		else
			return y_coord += steps;
	}

	/**
	 * Allows the critter to move one space in a specific direction
	 * @param direction
	 */
	protected final void walk(int direction) {
		if(this.hasMoved){return;}
		switch (direction){
			case 0:
				x_coord = moveX(1); //move east
			case 1:
				x_coord = moveX(1);
				y_coord = moveY(-1); //move northeast
			case 2:
				y_coord = moveY(-1); //move north
			case 3:
				x_coord = moveX(-1);
				y_coord = moveY(-1); //move northwest
			case 4:
				x_coord = moveX(-1); //move west
			case 5:
				x_coord = moveX(-1);
				y_coord = moveY(1); //move southwest
			case 6:
				y_coord = moveY(1); //move south
			case 7:
				x_coord = moveX(1);
				y_coord = moveY(1); //move southeast
		}
		energy -= Params.walk_energy_cost;
		this.hasMoved = true;
	}

	/**
	 * Allows the critter to move 2 spaces at a time
	 * @param direction
	 */
	protected final void run(int direction) {
		if(this.hasMoved){return;}
		switch (direction){
			case 0:
				x_coord = moveX(2); //move east
			case 1:
				x_coord = moveX(2);
				y_coord = moveY(-2); //move northeast
			case 2:
				y_coord = moveY(-2); //move north
			case 3:
				x_coord = moveX(-2);
				y_coord = moveY(-2); //move northwest
			case 4:
				x_coord = moveX(-2); //move west
			case 5:
				x_coord = moveX(-2);
				y_coord = moveY(2); //move southwest
			case 6:
				y_coord = moveY(2); //move south
			case 7:
				x_coord = moveX(2);
				y_coord = moveY(2); //move southeast
		}
		energy -= Params.walk_energy_cost;
		this.hasMoved = true;

	}

	/**
	 * Allows critter to reproduce. Offspring gets half the energy.
	 * @param offspring the child that the critter reproduces
	 * @param direction which direction to insert the offspring in the world
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if(this.energy < Params.min_reproduce_energy){return;}
		offspring.energy = this.energy / 2;
		this.energy = (int)Math.ceil(this.energy / 2);
		switch (direction){
			case 0:
				offspring.x_coord = this.x_coord;
				offspring.y_coord = this.moveY(-1); //move north
			case 1:
				offspring.x_coord = this.moveX(1);
				offspring.y_coord = this.moveY(-1); //move northeast
			case 2:
				offspring.x_coord = this.moveX(1); //move east
				offspring.y_coord = this.y_coord;
			case 3:
				offspring.x_coord = this.moveX(1);
				offspring.y_coord = this.moveY(1); //move southeast
			case 4:
				offspring.x_coord = this.x_coord;
				offspring.y_coord = this.moveY(1); //move south
			case 5:
				offspring.x_coord = this.moveX(-1);
				offspring.y_coord = this.moveY(1); //move southwest
			case 6:
				offspring.x_coord = this.moveX(-1); //move west
				offspring.y_coord = this.y_coord;
			case 7:
				offspring.x_coord = this.moveX(-1);
				offspring.y_coord = this.moveY(-1); //move northwest
		}
		babies.add(offspring);

	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	public int getX() {
		return this.x_coord;
	}
	public int getY() {
		return this.y_coord;
	}


	/**
	 * Executes one world step (i.e. each Critter gets one turn)
	 */
	public static void worldTimeStep() {
		insight = new int[population.size()*2];
		int pos = 0;

		for(int x=0; x < population.size(); x++){
			insight[pos] = population.get(x).x_coord;
			insight[pos+1] = population.get(x).y_coord;
			pos += 2;
		}


		for (Critter c : population) {
			c.doTimeStep();
		}

		pos = 0;
		for(int x=0; x < population.size(); x++){
			insight[pos] = population.get(x).x_coord;
			insight[pos+1] = population.get(x).y_coord;
			pos += 2;
		}

		//check for critters in same spot
		for (int i=0; i < population.size(); i++) {
			Critter crit1 = population.get(i);
			if (crit1.getEnergy() <= 0) {
				continue;
			}
			for (int j=i+1; j < population.size(); j++) {
				if (crit1.energy <= 0) {
					break;
				}
				Critter crit2 = population.get(j);
				if (crit2.energy <= 0) {
					continue;
				}
				//if critters are in same spot, fight
				if (crit1.x_coord == crit2.x_coord && crit1.y_coord == crit2.y_coord) {
					crit1.isFighting = true;
					crit2.isFighting = true;

					//check if critters want to fight
					boolean c1 = crit1.fight(crit2.toString());
					boolean c2 = crit2.fight(crit1.toString());

					//critters finished fighting
					crit1.isFighting = false;
					crit2.isFighting = false;

					//check if the critters are still alive after fighting
					if (crit1.energy >= 0 && crit2.energy >= 0) {
						//if both are still alive and in same spot
						if (crit1.x_coord == crit2.x_coord && crit1.y_coord == crit2.y_coord) {
							//roll the dice
							int r1 = 0;
							int r2 = 0;
							if (c1)
								r1 = Critter.getRandomInt(crit1.energy + 1);
							if (c2)
								r2 = Critter.getRandomInt(crit2.energy + 1);

							//which critter won fight
							if (r1 > r2) {
								crit1.energy += crit2.energy / 2;
								crit2.energy = 0;
							} else {
								crit2.energy += crit1.energy / 2;
								crit1.energy = 0;
							}
						}
					}
				}
			}
		}
		//resupply algae and add babies to population
		for (int j = 0; j < Params.refresh_algae_count; j++) {
			try {
				makeCritter("Algae");
			} catch (InvalidCritterException e){}
		}
		for (Critter c : babies){
			population.add(c);
		}
		babies.clear();

		//scan for dead critters
		int popSize = population.size();
		int index = 0;
		for(int k=0; k < popSize; k++){
			population.get(index).energy -= Params.rest_energy_cost;
			if(population.get(index).energy <= 0)
				population.remove(population.get(index));
			else
				index++;
		}

		//critter finished moving
		for(Critter c : population){
			c.hasMoved = false;
		}
	}


	//public static void displayWorld(Object pane) {}
	/* Alternate displayWorld, where you use Main.<pane> to reach into your
	   display component.
	*/
	public static void displayWorld() {
		Main.refreshWindow();
	}


	/* create and initialize a Critter subclass
	 * critter_class_name must be the name of a concrete subclass of Critter, if not
	 * an InvalidCritterException must be thrown
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try{
			Critter c = (Critter)Class.forName(myPackage + "." + critter_class_name).newInstance();
			Critter.population.add(c);
			c.energy = Params.start_energy;
			c.x_coord = Critter.getRandomInt(Params.world_width);
			c.y_coord = Critter.getRandomInt(Params.world_height);
			c.hasMoved = false;
			c.isFighting = false;
		}
		catch (InstantiationException e){
			throw new InvalidCritterException(critter_class_name);
		}
		catch (IllegalAccessException e){
			throw new InvalidCritterException(critter_class_name);
		}
		catch (ClassNotFoundException e){
			throw new InvalidCritterException(critter_class_name);
		}
	}

	/**
	 * gets the next critter in arraylist of critters
	 * @param critter_class_name the name of the arraylist
	 * @return the list of critters
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		Class<?> crit;
		if(critter_class_name.equals("Crittersgetter")){
			return population;
		}
		try{
			crit = Class.forName(myPackage + "." + critter_class_name);
		}
		catch (ClassNotFoundException e){throw new InvalidCritterException(critter_class_name); }

		List<Critter> result = new java.util.ArrayList<>();
		for(Critter c: population){
			if(crit.isInstance(c))
				result.add(c);
		}
		return result;
	}

	/**
	 * Displays how many of a specified critters are in the world
	 * @param critters the list of critters
	 * @return how many of critter x is in the world
	 */
	public static String runStats(List<Critter> critters) {
		String output = "";
		output += "" + critters.size() + " critters as follows -- ";
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string, 1);
			}
			else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			output += prefix + s + ":" + critter_count.get(s);
			prefix = ", ";
		}
		return output;
	}
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure thath the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctup update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}
	
	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		for(Critter c : population){
			population.remove(c);
		}
		for(Critter c : babies){
			babies.remove(c);
		}
	}
	
	
}
