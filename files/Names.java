package expedition.game_tools.character_tools;

import static expedition.Other.fixCase;
import expedition.game_tools.character_tools.Race;
import expedition.game_tools.character_tools.Gender;
import java.util.ArrayList;
import java.io.*;
import java.util.Random;

public class Names {

    private final String namesDirectory = "proj_names/";
    private final int maxLines = 700;
    Random rand;

    public Names() {
        rand = new Random();
    }

    public static String removeLeadingChars(String str, int length) {
        return str.substring(length);
    }

    public String getRandomName(Race race, Gender sex) {
        /*switch (race) {
            case HUMAN:*/
        return getHumanFirstname(sex) + getHumanSurname() + ((sex == Gender.MALE) ? getSuffix() : "");
        /*case DWARF:
                return getDwarfFirst(sex) + getDwarfSurname() + ((sex == Gender.MALE) ? getSuffix() : "");
            case ELF:
                return getElvenFirst(sex) + getElvenSurname();
            case HALFLING:
                return getHalflingFirst(sex) + getHalflingSurname();
            default:
                return "Er Rror !";
        }*/
    }

    public String getHumanFirstname(Gender sex) {
        ArrayList<String> names = new ArrayList<>();

        if (sex == Gender.MALE) {
            try {
                BufferedReader re = new BufferedReader(new FileReader(new File(namesDirectory + "dist.male.first")));
                //Scanner re = new Scanner(new URL("http://www2.census.gov/topics/genealogy/1990surnames/dist.male.first").openStream());
                String line;
                int count = 0;
                while ((line = re.readLine()) != null && count < maxLines) {
                    names.add(fixCase((line.split("\\s+")[0])));
                    count++;
                }
                re.close();
            } catch (FileNotFoundException ex) {
                return "!//human male name directory not found//!";
            } catch (IOException ex) {
                return "!//bad human male file read//!";
            }
        } else {
            try {
                BufferedReader re = new BufferedReader(new FileReader(new File(namesDirectory + "dist.female.first")));
                //Scanner re = new Scanner(new URL("http://www2.census.gov/topics/genealogy/1990surnames/dist.female.first").openStream());
                String line;
                int count = 0;
                while ((line = re.readLine()) != null && count < maxLines) {
                    names.add(fixCase((line.split("\\s+")[0])));
                    count++;
                }
                re.close();
            } catch (FileNotFoundException ex) {
                return "!//human female name directory not found//!";
            } catch (IOException ex) {
                return "!//bad human female file read//!";
            }
        }
        return names.get(rand.nextInt(names.size()));
    }

    public String getHumanSurname() {
        ArrayList<String> names = new ArrayList<>();
        try {
            BufferedReader re = new BufferedReader(new FileReader(new File(namesDirectory + "dist.all.last")));
            //Scanner re = new Scanner(new URL("http://www2.census.gov/topics/genealogy/1990surnames/dist.all.last").openStream());
            String line;
            int count = 0;
            while ((line = re.readLine()) != null && count < maxLines) {
                names.add(fixCase((line.split("\\s+")[0])));
                count++;
            }
            re.close();
        } catch (FileNotFoundException ex) {
            return "!//human surname directory not found//!";
        } catch (IOException ex) {
            return "!//bad human surname file read//!";
        }

        return " " + names.get(rand.nextInt(names.size()));
    }

    public boolean between(int num, int low, int high) {
        if (num >= low && num <= high) {
            return true;
        } else {
            return false;
        }
    }

    public String getSuffix() {
        int num = rand.nextInt(1000) + 1;

        if (between(num, 1, 950)) {
            return "";
        } else if (between(num, 951, 970)) {
            return " II";
        } else if (between(num, 971, 980)) {
            return " III";
        } else if (between(num, 981, 985)) {
            return " IV";
        } else if (between(num, 986, 990)) {
            return " V";
        } else if (between(num, 991, 995)) {
            return " VI";
        } else if (between(num, 996, 1000)) {
            return " VII";
        } else {
            return "";
        }
    }

    private String getDwarfFirst(Gender sex) {
        ArrayList<String> names = new ArrayList<>();
        try {
            BufferedReader re = new BufferedReader(new FileReader(new File(namesDirectory + "dwarf.male.first")));
            //Scanner re = new Scanner(new URL("http://www2.census.gov/topics/genealogy/1990surnames/dist.all.last").openStream());
            String line;
            int count = 0;
            while ((line = re.readLine()) != null && count < maxLines) {
                names.add(fixCase(line));
                count++;
            }
            re.close();
        } catch (FileNotFoundException ex) {
            return "!//dwarf first name directory not found//!";
        } catch (IOException ex) {
            return "!//bad dwarf first file read//!";
        }

        return " " + names.get(rand.nextInt(names.size()));
    }

    private String getDwarfSurname() {
        return "";
    }

    private String getElvenFirst(Gender sex) {
        return "";
    }

    private String getElvenSurname() {
        return "";
    }

    private String getHalflingSurname() {
        return "";
    }

    private String getHalflingFirst(Gender sex) {
        return "";
    }

}
