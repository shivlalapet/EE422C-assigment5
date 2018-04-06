package assignment5;

import javafx.scene.paint.Color;

public class HamzaCritter1 extends Critter {
    private int direction;

    public HamzaCritter1(){
        direction = Critter.getRandomInt(7);
    }

    public String toString(){
        return "#";
    }

    @Override
    public boolean fight(String oponent) {
        return Critter.getRandomInt(2) > 0;
    }

    /**
     * Critter executes its turn
     */
    public void doTimeStep(){
        run(direction);
        if(getEnergy() > 100){
            HamzaCritter1 offspring = new HamzaCritter1();
            reproduce(offspring, Critter.getRandomInt(5));
        }
        else if(getEnergy() < 75){
            walk(direction);
        }
        direction = Critter.getRandomInt(7);
    }

    /**
     * View the shape of the critter
     * @return critter shape
     */
    @Override
    public CritterShape viewShape() {
        return CritterShape.STAR;
    }

    /**
     * View the color of the critter
     * @return critter color
     */
    @Override
    public Color viewColor() {
        return Color.YELLOW;
    }
}
