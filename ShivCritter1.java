package assignment5;

import javafx.scene.paint.Color;

public class ShivCritter1 extends Critter {
    private int direction;

    public ShivCritter1(){
        direction = Critter.getRandomInt(7);
    }

    public String toString(){
        return "S";
    }

    @Override
    public boolean fight(String oponent) {
        return Critter.getRandomInt(2) > 0;
    }

    /**
     * Critter executes its turn
     */
    public void doTimeStep(){
        if(getEnergy() > 200){
            ShivCritter1 offspring = new ShivCritter1();
            reproduce(offspring, Critter.getRandomInt(5));
        }
        else if(getEnergy() < 150){
            run(direction);
        }
        else
            walk(direction);
        direction = Critter.getRandomInt(6);
    }

    /**
     * View the shape of the critter
     * @return critter shape
     */
    @Override
    public CritterShape viewShape() {
        return CritterShape.SQUARE;
    }

    /**
     * View the color of the critter
     * @return critter color
     */
    @Override
    public Color viewColor() {
        return Color.MAGENTA;
    }
}
