package expedition.game_tools.character_tools;

import java.util.Random;

public enum Gender {
    MALE("Male", "Man", "Boy"),
    FEMALE("Female", "Woman", "Girl");

    private final String scientificName;
    private final String adultName;
    private final String youthName;

    Gender(String scientificName, String adultName, String youthName) {
        this.scientificName = scientificName;
        this.adultName = adultName;
        this.youthName = youthName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getAdultName() {
        return adultName;
    }

    public String getYouthName() {
        return youthName;
    }

    public static Gender randomGender() {
        Random rand = new Random();
        switch (rand.nextInt(2)) {
            case 0: return Gender.MALE;
            case 1: return Gender.FEMALE;
            default: return Gender.MALE;
        }
    }

}
