package expedition.game_tools;

import java.util.Random;

public enum Month {
    Abb("Abb", 1, 15), //Cool & Rainy season.
    Bea("Bea", 2, 15), //Hot season.
    Cee("Cee", 3, 15), //Warm and Dry season.
    Dei("Dei", 4, 15); //Cold season.

    private static final int numMonths = 4;
    private final String name;
    private final int numDays;
    private final int monthNum;

    Month(String name, int monthNum, int numDays) {
        this.name = name;
        this.monthNum = monthNum;
        this.numDays = numDays;
    }

    public static Month getRandomMonth() {
        Random rand = new Random();
        switch (rand.nextInt(4)) {
            case 0:
                return Month.Abb;
            case 1:
                return Month.Bea;
            case 2:
                return Month.Cee;
            case 3:
                return Month.Dei;
            default:
                return Month.Abb;
        }
    }

    public int getNumMonths() {
        return numMonths;
    }

    public String getName() {
        return name;
    }
    
    public String toString(){
        return name;
    }

    public int getNumDays() {
        return numDays;
    }

    public int getNum() {
        return monthNum;
    }
    
    public boolean equals (Month in){
        return in.getName() == this.getName();
    }
    

}
