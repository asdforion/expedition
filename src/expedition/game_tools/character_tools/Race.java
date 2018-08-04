package expedition.game_tools.character_tools;

import expedition.game_tools.character_tools.Character.Stat;
import java.util.Random;

public enum Race {
    //name, int, agi, str, max age, strong stat, weak stat, spi
    HUMAN("Human", 7, 6, 5, 80, Stat.INT, Stat.None, 7),
    ELF("Elf", 5, 7, 6, 120, Stat.SPI, Stat.INT, 8),
    DWARF("Dwarf", 7, 5, 7, 150, Stat.STR, Stat.AGI, 7),
    OGRE("Ogre", 2, 5, 9, 90, Stat.STR, Stat.AGI, 6),
    ORC("Orc", 4, 6, 8, 70, Stat.AGI, Stat.INT, 6),
    HALFLING("Halfling", 7, 7, 4, 100, Stat.SPI, Stat.STR, 4);

    private static final int numPlayableRaces = 4;

    private final String name;
    private final int averageInt;
    private final int averageAgi;
    private final int averageStr;
    private final int maxLifespan;
    private final Stat strongStat;
    private final Stat weakStat;
    private final int averageSpi;

    Race(String name, int averageInt, int averageAgi, int averageStr, int maxLifespan, Stat strongStat, Stat weakStat, int averageSpi) {
        this.name = name;
        this.averageInt = averageInt;
        this.averageAgi = averageAgi;
        this.averageStr = averageStr;
        this.maxLifespan = maxLifespan;
        this.strongStat = strongStat;
        this.weakStat = weakStat;
        this.averageSpi = averageSpi;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }

    public int getAverageInt() {
        return averageInt;
    }

    public int getAverageAgi() {
        return averageAgi;
    }

    public int getAverageStr() {
        return averageStr;
    }

    public int getMaxLifespan() {
        return maxLifespan;
    }

    public static Race randomPlayableRace() {
        Random rand = new Random();
        switch (rand.nextInt(10)) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return Race.HUMAN;
            case 6:
                return Race.HALFLING;
            case 7:
            case 8:
                return Race.DWARF;
            case 9:
                return Race.ELF;
            default:
                return Race.HUMAN;
        }
    }

}
