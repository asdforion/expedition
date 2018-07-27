/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expedition.game_tools.character_tools;

import static expedition.Expedition.inputInt;
import static expedition.Other.*;
import expedition.game_tools.*;
import expedition.game_tools.StatusEffect.*;
import expedition.game_tools.items.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Character {

    public enum Stat {
        INT, AGI, STR, SPI, MAG, SPD, None;
        //Intelligence, Agility, Strength, Spirit, Magic
    }

    public static final double VERYEFFECTIVE = 1.5;
    public static final double NOTEFFECTIVE = 0.5;

    private String name;
    private int age;

    private final Race race;
    private final Gender sex;
    private final Date birthday;

    private int healthMax;
    private int energyMax;
    private int healthCurr;
    private int energyCurr;

    private ArrayList<StatusEffect> status;

    private Ability[] abilities = new Ability[4];
    Scanner scan;

    private int level;
    private int experienceMax;
    private int experienceCurr;

    private int intelligence;
    private int agility;
    private int strength;
    private int spirit;
    private int magic;

    private int speed;

    private Item equippedWeapon;
    private Item equippedArmor;
    private Item equippedTrinket;

    private Type type;

    public Character(Scanner scan) {
        Random rand = new Random();
        Names nam = new Names();
        sex = Gender.randomGender();
        race = Race.randomPlayableRace();
        name = nam.getRandomName(race, sex);
        type = Type.getRandomPlayableType();

        this.scan = scan;

        //picking an age
        int ageDeviation = race.getMaxLifespan() / 4; //maximum deviation of a new playable character is a quarter of max lifespan.
        int averageAge = race.getMaxLifespan() / 3; //assuming average lifespan of all races is max lifespan / 3.
        age = averageAge + rand.nextInt(ageDeviation * 2) - ageDeviation; //age is random, anywhere from average age - age deviation to age + age deviation.
        if (age < 16) {
            age = age + 4;
        }

        //picking stats
        int ageProficiencyBonus = this.age - 20 / 10;
        intelligence = race.getAverageInt() + rand.nextInt(3) - 1;
        agility = race.getAverageAgi() + rand.nextInt(3) - 1;
        strength = race.getAverageStr() + rand.nextInt(3) - 1;
        agility = race.getAverageAgi() + rand.nextInt(3) - 1;
        strength = race.getAverageStr() + rand.nextInt(3) - 1;
        level = 1;

        birthday = new Date();

        experienceCurr = 0;
        healthCurr = energyCurr = healthMax = energyMax = experienceMax = 100;

        this.addAbility(Ability.Punch);
    }

    //Basic Getters
    // <editor-fold defaultstate="collapsed" desc="getters">
    public String getName() {
        return name;
    }

    public Race getRace() {
        return race;
    }

    public int getAge() {
        return age;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getAgility() {
        return agility;
    }

    public int getStrength() {
        return strength;
    }

    public int getSpirit() {
        return spirit;
    }

    public int getMagic() {
        return magic;
    }

    public int getSpeed() {
        return speed;
    }

    public Gender getSex() {
        return sex;
    }

    public String getGenderString() {
        if (age >= 18) {
            return sex.getAdultName();
        } else {
            return sex.getYouthName();
        }
    }

    public Date getBirthday() {
        return birthday;
    }

    public int getHealthMax() {
        return healthMax;
    }

    public int getEnergyMax() {
        return energyMax;
    }

    public int getHealthCurr() {
        return healthCurr;
    }

    public int getEnergyCurr() {
        return energyCurr;
    }

    public int getLevel() {
        return level;
    }

    public int getExperienceMax() {
        return experienceMax;
    }

    public int getExperienceCurr() {
        return experienceCurr;
    }

    public Item getEquippedWeapon() {
        return equippedWeapon;
    }

    public Item getEquippedArmor() {
        return equippedArmor;
    }

    public Item getEquippedTrinket() {
        return equippedTrinket;
    }

    public Type getType() {
        return type;
    }

    public ArrayList<StatusEffect> getStatus() {
        return status;
    }

    // </editor-fold>
    //Status Calculation Getters
    // <editor-fold defaultstate="collapsed" desc="statcalcgetters">
    public int getStatIntelligence() {
        double calc = (double) intelligence;

        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.INTPERCENT) {
                calc = calc * i.getIntensity();
            }
        }
        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.INTFLAT) {
                calc = calc + i.getIntensity();
            }
        }
        return (int) Math.round(calc);
    }

    public int getStatAgility() {
        double calc = (double) agility;

        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.AGIPERCENT) {
                calc = calc * i.getIntensity();
            }
        }
        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.AGIFLAT) {
                calc = calc + i.getIntensity();
            }
        }
        return (int) Math.round(calc);
    }

    public int getStatStrength() {
        double calc = (double) strength;

        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.STRPERCENT) {
                calc = calc * i.getIntensity();
            }
        }
        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.STRFLAT) {
                calc = calc + i.getIntensity();
            }
        }
        return (int) Math.round(calc);
    }

    public int getStatSpirit() {
        double calc = (double) spirit;

        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.SPIPERCENT) {
                calc = calc * i.getIntensity();
            }
        }
        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.SPIFLAT) {
                calc = calc + i.getIntensity();
            }
        }
        return (int) Math.round(calc);
    }

    public int getStatMagic() {
        double calc = (double) magic;

        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.MAGPERCENT) {
                calc = calc * i.getIntensity();
            }
        }
        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.MAGFLAT) {
                calc = calc + i.getIntensity();
            }
        }
        return (int) Math.round(calc);
    }

    public int getStatSpeed() {
        double calc = (double) speed;

        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.SPDPERCENT) {
                calc = calc * i.getIntensity();
            }
        }
        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.SPDFLAT) {
                calc = calc + i.getIntensity();
            }
        }
        return (int) Math.round(calc);
    }

    // </editor-fold>
    //Other Methods
    public int takeDamage(int damage, Type damageType) {
        return takeDamage(damage, damageType, false, true);
    }

    //Deals damage to the current character. Returns the amount of damage done.
    //Damage is affected by any ARMOR status effects.
    public int takeDamage(int damage, Type damageType, boolean ignoreDamage, boolean hidden) {
        double calc = damage;

        if (this.getType().isWeakTo(damageType)) {
            damage *= VERYEFFECTIVE;
        } else if (this.getType().isStrongTo(damageType)) {
            damage *= NOTEFFECTIVE;
        }

        if (ignoreDamage == false) {
            for (StatusEffect i : status) {
                if (i.getEffect() == Effect.DAMAGETAKENPERCENT) {
                    calc = calc * i.getIntensity();
                }
            }
            for (StatusEffect i : status) {
                if (i.getEffect() == Effect.DAMAGETAKENFLAT) {
                    calc = calc + i.getIntensity();
                }
            }
        }

        healthCurr = healthCurr - ((int) Math.round(calc));

        //death
        if (healthCurr <= 0) {
            healthCurr = 0;
            addStatus(new StatusEffect("Knocked out.", "Reached 0 health, and is incapacitated until healed.", Effect.FALLEN, -1, 1.0, null, false, false));
        }
        return ((int) Math.round(calc));
    }

    public int healDamage(int heal) {
        double calc = heal;

        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.HEALTAKENPERCENT) {
                calc = calc * i.getIntensity();
            }
        }
        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.HEALTAKENFLAT) {
                calc = calc + i.getIntensity();
            }
        }
        healthCurr = healthCurr + ((int) Math.round(calc));

        //max
        if (healthCurr > healthMax) {
            healthCurr = healthMax;
        }
        return ((int) Math.round(calc));
    }

    public int restoreEnergy(int energy) {
        double calc = energy;

        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.ENERGYRESTOREDPERCENT) {
                calc = calc * i.getIntensity();
            }
        }
        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.ENERGYRESTOREDFLAT) {
                calc = calc + i.getIntensity();
            }
        }
        energyCurr = energyCurr + ((int) Math.round(calc));

        //max
        if (energyCurr > energyMax) {
            energyCurr = energyMax;
        }
        return ((int) Math.round(calc));
    }

    public void birthday() {
        age = age + 1;
    }

    public void addExperience(int experience) {
        experienceCurr += experience;
        if (experienceCurr >= experienceMax) {
            experienceCurr -= experienceMax;
            levelup();

            if (experienceCurr >= experienceMax) {
                int exp = experienceCurr;
                experienceCurr = 0;
                addExperience(exp);
            }
        }
    }

    public void levelup() {
        //increment the level.
        level++;
        error(this.getName() + " has leveled up! They are now level " + this.getLevel());
        Ability newAbility = this.getType().getAbilityForLevel(level);
        if (newAbility != null) {
            this.addAbility(newAbility, false);
        }
    }

    public void addStatus(StatusEffect in) {
        status.add(in);
    }

    //removes ALL status effects that relate to individual fight situations.
    public void clearCombatStatus() {
        for (StatusEffect i : status) {
            if (i.isFightTimed()) {
                status.remove(i);
            }
        }
    }

    //tick down the timer of every status effect whose timer still has time left
    // (> 0) and IS a fight-centric status. If it is ticked down to 0, remove the status.
    //Any /tick/ status effects have their effects processed.
    public void statusTickCombat() {
        for (StatusEffect i : status) {
            if (i.isFightTimed()) {

                if (i.getTimer() > 0) {
                    i.tickTimer();
                }

                if (i.getEffect() != null) //individual stat effects
                {
                    switch (i.getEffect()) {
                        case HEALTICK:
                            tprintln(this.getName() + " healed " + healDamage((int) i.getIntensity()) + " damage from " + i.getName());
                            break;
                        case DAMAGETICK:
                            tprintln(this.getName() + " took " + takeDamage((int) i.getIntensity(), i.getDamageType()) + " " + i.getDamageType() + " damage from " + i.getName());
                            break;
                        case ENERGYTICK:
                            tprintln(this.getName() + " restored " + restoreEnergy((int) i.getIntensity()) + " energy from " + i.getName());
                            break;
                        default:
                            break;
                    }
                }
                if (i.getTimer() == 0) {
                    status.remove(i);
                }

            }
        }
    }

    //tick down the timer of every status effect whose timer still has time left
    // (> 0) and IS NOT a fight-centric status. If it is ticked down to 0, remove the status.
    public void statusTickNonCombat() {
        for (StatusEffect i : status) {
            if (!i.isFightTimed()) {
                if (i.getTimer() > 0) {
                    i.tickTimer();
                }

                if (i.getEffect() != null) //individual stat effects
                {
                    switch (i.getEffect()) {
                        case HEALTICK:
                            tprintln(this.getName() + " healed " + healDamage((int) i.getIntensity()) + " damage from " + i.getName());
                            break;
                        case DAMAGETICK:
                            tprintln(this.getName() + " took " + takeDamage((int) i.getIntensity(), i.getDamageType()) + " " + i.getDamageType() + " damage from " + i.getName());
                            break;
                        case ENERGYTICK:
                            tprintln(this.getName() + " restored " + restoreEnergy((int) i.getIntensity()) + " energy from " + i.getName());
                            break;
                        default:
                            break;
                    }
                }

                if (i.getTimer() == 0) {
                    status.remove(i);
                }

            }
        }
    }

    public void addAbility(Ability newAbility) {
        addAbility(newAbility, true);
    }

    public void addAbility(Ability newAbility, boolean hidden) {
        if (newAbility == null) {
            error("No such ability.");
            return;
        }
        boolean placed = false;
        for (int i = 0; i < 4; i++) {
            if (abilities[i] == null) {
                abilities[i] = newAbility;
                if (!hidden) {
                    error(this.getName() + " has learned " + newAbility + " in ability slot " + (i + 1) + "!");
                }
                placed = true;
                break;
            }
        }
        if (!placed) {
            replaceAbility(newAbility);
        }
    }

    public void replaceAbility(Ability newAbility) {
        if (newAbility == null) {
            error("No such ability.");
            return;
        }
        boolean placed = false;
        error(this.getName() + " is trying to learn " + newAbility + ". Which ability do you want to forget?");
        int attempt;
        while (!placed) {
            printAbilities();
            tprintln("#5 ?? " + newAbility.getName() + grs(newAbility.getName(), getLongestAbilityName()) + " ??? " + newAbility.getDescription() + " << New Ability");
            attempt = inputInt(scan);
            if (attempt < 1 || attempt > 5) {
                error(attempt + " is not valid. Try again.");
            } else if (attempt == 5) {
                error(newAbility + " has been forgotten!");
                placed = true;
            } else {
                error(abilities[attempt - 1] + " has been forgotten, and " + newAbility + " has taken its place!");
                abilities[attempt - 1] = newAbility;
                placed = true;
            }
        }
    }

    public void printAbilities() {
        error(this.getName() + "'s abilities -------->");
        int longest = getLongestAbilityName();
        for (int i = 0; i < 4; i++) {
            if (abilities[i] != null) {
                tprintln("#" + (i + 1) + " >> " + abilities[i].getName() + grs(abilities[i].getName(), longest) + " :-: " + abilities[i].getDescription());
            } else {
                tprintln("#" + (i + 1) + " >> ");
            }
        }
    }

    public int getLongestAbilityName() {
        int longest = 1;
        for (Ability i : abilities) {
            if (i != null) {
                if (i.getName().length() > longest) {
                    longest = i.getName().length();
                }
            }
        }
        return longest;
    }

    public boolean hasEffect(Effect check) {
        for(StatusEffect s : status){
            if(s.getEffect() == check){
                return true;
            }
        }
        return false;
    }

}
