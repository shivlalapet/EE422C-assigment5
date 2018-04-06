package assignment5;

import javafx.scene.paint.Color;

public class ShivCritter2 extends Critter {
    private int direction;

    public ShivCritter2(){
        direction = Critter.getRandomInt(7);
    }

    public String toString(){
        return "2";
    }

    @Override
    public boolean fight(String oponent) {
        return Critter.getRandomInt(2) > 0;
    }

    /**
     * Critter executes its turn
     */
    public void doTimeStep(){
        walk(direction);
        if(getEnergy() > 100){
            ShivCritter2 offspring = new ShivCritter2();
            reproduce(offspring, Critter.getRandomInt(5));
        }
        direction = Critter.getRandomInt(4);
    }

    /**
     * View the shape of the critter
     * @return critter shape
     */
    @Override
    public CritterShape viewShape() {
        return CritterShape.CIRCLE;
    }

    /**
     * View the color of the critter
     * @return critter color
     */
    @Override
    public Color viewColor() {
        return Color.ORANGE;
    }
}
