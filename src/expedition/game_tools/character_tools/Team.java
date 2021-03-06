/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expedition.game_tools.character_tools;

import expedition.Expedition;
import static expedition.Other.*;
import expedition.game_tools.Date;
import expedition.game_tools.StatusEffect.Effect;
import expedition.game_tools.StatusEffect;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author J76785
 */
public class Team {

    private ArrayList<Character> team;
    Scanner scan;
    String name;

    public Team(Scanner scan, String name) {
        this.scan = scan;
        team = new ArrayList<>();
        this.name = name;
    }

    public void newPlayerTeam() {
        for (int i = 0; i < Expedition.numChars; i++) {
            team.add(new Character(scan));
        }
    }

    //no yee
    public int size() {
        return team.size();
    }

    public Character get(int which) {
        if (which > 0 && (which - 1) < (team.size())) {
            return team.get(which - 1);
        } else {
            return null;
        }
    }

    public void updateBirthday(Date date) {
        for (Character i : team) {
            if (i.getBirthday().equals(date)) {
                i.birthday();
            }
        }
    }

    public boolean contains(Character x) {
        if (x != null) {
            return team.contains(x);
        } else {
            return false;
        }
    }

    public boolean removeTraveler(Character x) {
        if (x != null) {
            boolean b = team.remove(x);
            
            if (b) {
                return b;
            } else {
                error("Error removing character " + x.getName() + " from team " + this.getName());
                return b;
            }
        } else {
            error("Error removing character \\ doesn't exist \\ from team " + this.getName());
            return false;
        }
    }

    //Prints ALL current travelers.
    public void printTravelers(Date date) {
        print("\n");
        tprintln(name + "'s Members: ");

        for (Character x : team) {
            printTravelerShort(x, date);
        }
    }

    public void printTravelerShort(Character x, Date date) {
        int untilBirthday;
        Race race;
        String name;
        Gender sex;
        int age;
        Date birthday;
        Type type;
        printTab();
        name = x.getName();
        race = x.getRace();
        birthday = x.getBirthday();
        sex = x.getSex();
        age = x.getAge();
        type = x.getType();
        untilBirthday = date.daysUntil(x.getBirthday());
        println("Traveler #" + (team.indexOf(x) + 1) + getRemainderSpaces(Integer.toString(team.indexOf(x)), 2) + ": "
                + x.getName() + "," + getRemainderSpaces(name, longestTravelerName()) + " a "
                + age + getRemainderSpaces(Integer.toString(age), 2) + " year-old, level " + x.getLevel() + grs(Integer.toString(x.getLevel()), 2) + " "
                + race.getName() + getRemainderSpaces(race.getName(), longestTravelerRace()) + " "
                + type.toString() + "."
                + getRemainderSpaces(type.toString(), longestTravelerType()));
    }

    //Returns the length of the longest traveler name.
    public int longestTravelerName() {
        int longest = 1;
        for (Character x : team) {
            if (x.getName().length() > longest) {
                longest = x.getName().length();
            }
        }
        return longest;
    }

    //Returns the length of the longest traveler name.
    public int longestTravelerRace() {
        int longest = 1;
        for (Character x : team) {
            if (x.getRace().getName().length() > longest) {
                longest = x.getRace().getName().length();
            }
        }
        return longest;
    }

    public int longestTravelerType() {
        int longest = 1;
        for (Character x : team) {
            if (x.getType().toString().length() > longest) {
                longest = x.getType().toString().length();
            }
        }
        return longest;
    }

    //Prints all birthdays.
    public void printBirthdays(Date date) {
        if (isABirthday(date)) {
            print("\n");
            printTab();
            println("The following travelers had a birthday today: ");
            for (Character y : whoseBirthdays(date)) {
                printTab();
                println(" > " + y.getName());
            }
            printTab();
            println("Happy Birthday!");
        }
    }

    //Returns true if someone's birthday is today.
    public boolean isABirthday(Date date) {
        for (Character x : team) {
            if (x.getBirthday().compare(date) == 0) {
                return true;
            }
        }
        return false;
    }

    //Returns an arraylist of everyone who's birthday is today.
    public ArrayList<Character> whoseBirthdays(Date date) {
        ArrayList<Character> birthdays = new ArrayList<>();
        for (Character x : team) {
            if (x.getBirthday().compare(date) == 0) {
                birthdays.add(x);
            }
        }
        return birthdays;
    }

    public boolean hasConsciousMembers() {
        for (Character c : team) {
            if (!c.hasEffect(Effect.FALLEN)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Character> list() {
        return team;
    }

    public String getName() {
        return name;
    }

}
