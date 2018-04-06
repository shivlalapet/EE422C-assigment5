package assignment5;

import javafx.scene.paint.Color;

public class HamzaCritter extends Critter {
    private int direction;

    public HamzaCritter(){
        direction = Critter.getRandomInt(7);
    }

    public String toString(){
        return "H";
    }

    @Override
    public boolean fight(String oponent) {
        return Critter.getRandomInt(2) > 0;
    }

    /**
     * Critter executes its turn
     */
    public void doTimeStep(){
        if(getEnergy() > 100){
            run(direction);
        }
        else
            walk(direction);
        if(getEnergy() > 80){
            HamzaCritter offspring = new HamzaCritter();
            reproduce(offspring, Critter.getRandomInt(5));
        }
        direction = Critter.getRandomInt(5);
    }

    /**
     * View the shape of the critter
     * @return critter shape
     */
    @Override
    public CritterShape viewShape() {
        return CritterShape.DIAMOND;
    }

    /**
     * View the color of the critter
     * @return critter color
     */
    @Override
    public Color viewColor() {
        return Color.RED;
    }
}
