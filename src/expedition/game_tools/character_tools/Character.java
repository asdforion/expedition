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
import expedition.game_tools.items.Item.ItemType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Scanner;

public class Character {

    public enum Stat {
        INT, AGI, STR, SPI, SPD, None;
        //Intelligence, Agility, Strength, Spirit, Magic
    }

    public static final String FALLEN = "Fallen";
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

    private ArrayList<StatusEffect> status = new ArrayList<StatusEffect>();

    private Ability[] abilities = new Ability[4];
    Scanner scan;

    private int level;
    private int experienceMax;
    private int experienceCurr;

    private int intelligence;
    private int agility;
    private int strength;
    private int spirit;

    private int speed;

    private Item equippedWeapon;
    private Item equippedArmor;
    private Item equippedTrinket;
    private Item equippedConsumable;

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
        spirit = rand.nextInt(4) + 3;
        speed = rand.nextInt(20) + 5;
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

    public Ability[] getAbilities() {
        return abilities;
    }

    public Item getEquippedConsumable() {
        return equippedConsumable;
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

    public int takeDamage(int damage, Type damageType, boolean hidden) {
        return takeDamage(damage, damageType, false, hidden);
    }

    //Deals damage to the current character. Returns the amount of damage done.
    //Damage is affected by any ARMOR status effects.
    public int takeDamage(int damage, Type damageType, boolean ignoreArmor, boolean hidden) {
        double calc = damage;
        int healthOg = healthCurr;

        if (this.getType().isWeakTo(damageType)) {
            damage *= VERYEFFECTIVE;
        } else if (this.getType().isStrongTo(damageType)) {
            damage *= NOTEFFECTIVE;
        }

        if (ignoreArmor == false) { //IGNORES DAMAGEBLOCKED
            for (StatusEffect i : status) {
                if (i.getEffect() == Effect.DAMAGEBLOCKEDPERCENT) {
                    calc = calc * i.getIntensity();
                }
            }
            for (StatusEffect i : status) {
                if (i.getEffect() == Effect.DAMAGEBLOCKEDFLAT) {
                    calc = calc + i.getIntensity();
                }
            }
        }
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

        healthCurr = healthCurr - ((int) Math.round(calc));
        tprintln(getName() + " has taken " + (healthOg - healthCurr) + " damage.", hidden);

        //death
        if (healthCurr <= 0) {
            healthCurr = 0;
            addStatus(new StatusEffect(FALLEN, "Reached 0 health, and is incapacitated until healed.", Effect.FALLEN, -1, 1.0, null, false, false));
        }
        return ((int) Math.round(calc));
    }

    public boolean spendEnergy(int energy) {
        return spendEnergy(energy, true);
    }

    public boolean spendEnergy(int energy, boolean hidden) {
        double calc = energy;
        int energyOg = energyCurr;

        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.ENERGYSPENTPERCENT) {
                calc = calc * i.getIntensity();
            }
        }
        for (StatusEffect i : status) {
            if (i.getEffect() == Effect.ENERGYSPENTFLAT) {
                calc = calc + i.getIntensity();
            }
        }

        if (energyCurr > ((int) Math.round(calc))) { //enough energy to spend!
            energyCurr = energyCurr - ((int) Math.round(calc));
            tprintln(getName() + " has used " + (energyOg - healthCurr) + " energy.", hidden);
            return true;
        } else {
            tprintln(getName() + " does not have enough energy!", hidden);
            return false;
        }

    }

    public void printTravelerVerbose(Date date) {

        int untilBirthday = date.daysUntil(getBirthday());
        error(getName() + " >> \n");

        tprintln("is a " + getAge() + " year old " + getRace() + " " + getGenderString() + ".");
        tprintln(((getSex() == Gender.FEMALE) ? "Her" : "His") + " birthday is on "
                + getBirthday() + ". That's " + ((untilBirthday == 0) ? ("today! Happy Birthday!") : ("in " + untilBirthday + " days.")) + "\n");

        tprintln("Level " + getLevel() + " " + getType() + " (" + getExperienceCurr() + "/" + getExperienceMax() + ")");
        tprintln("Health: " + getHealthCurr() + "/" + getHealthMax() + " | Energy: " + getEnergyCurr() + "/" + getEnergyMax() + "\n");

        if (status.size() > 0) {
            this.printStatus();
            println("");
        }

        tprintln("Weapon: ["
                + (equippedWeapon == null ? "none" : equippedWeapon.getName())
                + "]");

        tprintln("Armor: [" + (equippedArmor == null ? "none" : equippedArmor.getName()) + "]");
        tprintln("Trinket: [" + (equippedTrinket == null ? "none" : equippedTrinket.getName()) + "]");
        tprintln("Consumable: [" + (equippedConsumable == null ? "none" : equippedConsumable.getName()) + "]");
        println("");

        tprintln(getIntelligence() + statCalcDiff("INT") + " Intelligence" + "; " + getAgility() + statCalcDiff("AGI") + " Agility" + "; " + getStrength() + statCalcDiff("STR") + " Strength" + ".");
        tprintln(getSpirit() + statCalcDiff("SPI") + " Spirit" + "; " + getSpeed() + statCalcDiff("SPD") + " Speed" + ".");
        this.printAbilities();

    }

    public String statCalcDiff(String stat) {
        int diff = 0;
        switch (stat) {
            case "INT":
                diff = this.getStatIntelligence() - this.getIntelligence();
                break;
            case "STR":
                diff = this.getStatStrength() - this.getStrength();
                break;
            case "SPI":
                diff = this.getStatSpirit() - this.getSpirit();
                break;
            case "AGI":
                diff = this.getStatAgility() - this.getAgility();
                break;
            case "SPD":
                diff = this.getStatSpeed() - this.getSpeed();
                break;
        }
        String ret = "";
        if (diff == 0) {
            return ret;
        } else {
            return (" (" + (diff > 0 ? "+" : "") + diff + ")");
        }
    }

    public void printStatus() {
        for (StatusEffect e : status) {
            tprintln(e.getName() + " | " + e.getDescription());
        }
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
        this.removeStatus(FALLEN);

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
        for (StatusEffect i : status) {

            if (i.getName().equals(in.getName())) {
                removeStatus(i.getName());
                break;
            }
        }
        status.add(in);
    }

    public void addStatus(Collection<StatusEffect> in) {
        for (StatusEffect i : in) {
            addStatus(i);
        }
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

    public boolean isFallen() {
        return hasEffect(Effect.FALLEN);
    }

    public boolean hasEffect(Effect check) {
        if (status.size() > 0) {
            for (StatusEffect s : status) {
                if (s != null && s.getEffect() == check) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeStatus(String str) {
        ArrayList<StatusEffect> toRemove = new ArrayList<>();
        for (StatusEffect i : status) {
            if (i.getName().equals(str)) {
                toRemove.add(i);
            }
        }
        for (StatusEffect i : toRemove) {
            status.remove(i);
        }
    }

    public void removeItemStatus(Item ite) {
        ArrayList<StatusEffect> toRemove = new ArrayList<>();
        for (StatusEffect i : status) {
            if (i.getName().equals("[" + ite.getName() + "]")) {
                toRemove.add(i);
            }
        }
        for (StatusEffect i : toRemove) {
            status.remove(i);
        }
    }

    public Item unequipItem(Item ite) {
        return unequipItem(ite, true);
    }

    //unequips an item.
    public Item unequipItem(Item ite, boolean hidden) {
        Item out;
        if (ite != null && null != ite.getType()) {
            switch (ite.getType()) {
                case Armor:
                    if (equippedArmor == null) {
                        break;
                    } else if (equippedArmor == ite) {
                        removeItemStatus(ite);
                        out = this.equippedArmor;
                        equippedArmor = null;
                        tprintln(getName() + " has unequipped [" + out.getName() + "].", hidden);
                        return out;
                    } else {
                        tprintln(getName() + " doesn't have [" + ite.getName() + "].", hidden);
                        return null;
                    }
                case Trinket:
                    if (equippedTrinket == null) {
                        break;
                    } else if (equippedTrinket == ite) {
                        removeItemStatus(ite);
                        out = this.equippedTrinket;
                        equippedTrinket = null;
                        tprintln(getName() + " has unequipped [" + out.getName() + "].", hidden);
                        return out;
                    } else {
                        tprintln(getName() + " doesn't have [" + ite.getName() + "].", hidden);
                        return null;
                    }
                case Weapon:
                    if (this.equippedWeapon == null) {
                        break;
                    } else if (equippedWeapon == ite) {
                        removeItemStatus(ite);
                        out = equippedWeapon;
                        equippedWeapon = null;
                        tprintln(getName() + " has unequipped [" + out.getName() + "].", hidden);
                        return out;
                    } else {
                        tprintln(getName() + " doesn't have [" + ite.getName() + "].", hidden);
                        return null;
                    }
                case Consumable:
                    if (this.equippedConsumable == null) {
                        break;
                    } else if (equippedConsumable == ite) {
                        out = equippedConsumable;
                        equippedConsumable = null;
                        tprintln(getName() + " has unequipped [" + out.getName() + "].", hidden);
                        return out;
                    } else {
                        tprintln(getName() + " doesn't have [" + ite.getName() + "].", hidden);
                        return null;
                    }
            }
        }
        return null;
    }

    public Item unequipItem(String ite) {
        ArrayList<Item> ites = new ArrayList<>();
        ites.add(this.equippedArmor);
        ites.add(this.equippedConsumable);
        ites.add(this.equippedTrinket);
        ites.add(this.equippedWeapon);

        return unequipItem(Item.parseName(ite), false);
    }

    public Item unequipItem(ItemType ite) {
        return unequipItem(ite, true);
    }

    //unequips an item in ItemType slot.
    public Item unequipItem(ItemType ite, boolean hidden) {
        Item out;
        switch (ite) {
            case Weapon:
                if (equippedWeapon == null) {
                    return null;
                } else {
                    removeItemStatus(equippedWeapon);
                    out = equippedWeapon;
                    equippedWeapon = null;
                    tprintln(getName() + " has unequipped [" + out.getName() + "].", hidden);
                    return out;
                }
            case Armor:
                if (equippedArmor == null) {
                    return null;
                } else {
                    removeItemStatus(equippedArmor);
                    out = equippedArmor;
                    equippedArmor = null;
                    tprintln(getName() + " has unequipped [" + out.getName() + "].", hidden);
                    return out;
                }
            case Trinket:
                if (equippedTrinket == null) {
                    return null;
                } else {
                    removeItemStatus(equippedTrinket);
                    out = equippedTrinket;
                    equippedTrinket = null;
                    tprintln(getName() + " has unequipped [" + out.getName() + "].", hidden);
                    return out;
                }
            case Consumable:
                if (equippedConsumable == null) {
                    return null;
                } else {
                    removeItemStatus(equippedConsumable);
                    out = equippedConsumable;
                    equippedConsumable = null;
                    tprintln(getName() + " has unequipped [" + out.getName() + "].", hidden);
                    return out;
                }
        }
        return null;
    }

    public void equipItem(Item ite, Inventory inv) {
        equipItem(ite, inv, true);
    }

    public void equipItem(Item ite, Inventory inv, boolean hidden) {
        Item out;

        if (ite != null && null != ite.getType()) {
            switch (ite.getType()) {
                case Armor:
                    if (equippedArmor != null) {
                        out = unequipItem(ite.getType());
                        inv.addItem(out);
                        tprintln(getName() + " has unequipped [" + out.getName() + "], and has equipped [" + ite.getName() + "] in its place.", hidden);
                    } else {
                        tprintln(getName() + " has equipped [" + ite.getName() + "].", hidden);
                    }
                    equippedArmor = ite;
                    addStatus(ite.getEffects());
                    break;
                case Trinket:
                    if (equippedTrinket != null) {
                        out = unequipItem(ite.getType());
                        inv.addItem(out);
                        tprintln(getName() + " has unequipped [" + out.getName() + "], and has equipped [" + ite.getName() + "] in its place.", hidden);
                    } else {
                        tprintln(getName() + " has equipped [" + ite.getName() + "].", hidden);
                    }
                    this.equippedTrinket = ite;
                    addStatus(ite.getEffects());
                    break;
                case Weapon:
                    if (equippedWeapon != null) {
                        out = unequipItem(ite.getType());
                        inv.addItem(out);
                        tprintln(getName() + " has unequipped [" + out.getName() + "], and has equipped [" + ite.getName() + "] in its place.", hidden);
                    } else {
                        tprintln(getName() + " has equipped [" + ite.getName() + "].", hidden);
                    }
                    equippedWeapon = ite;
                    addStatus(ite.getEffects());
                    break;
                case Consumable:
                    if (equippedConsumable != null) {
                        out = unequipItem(ite.getType());
                        inv.addItem(out);
                        tprintln(getName() + " has unequipped [" + out.getName() + "], and has equipped [" + ite.getName() + "] in its place.", hidden);
                    } else {
                        tprintln(getName() + " has equipped [" + ite.getName() + "].", hidden);
                    }
                    equippedConsumable = ite;
                    break;
                default:
                    tprintln("[" + ite.getName() + "] is not equippable.", hidden);
                    inv.addItem(ite);
                    break;
            }
        } else {
            tprintln("No such item exists!", hidden);
        }
    }

    public void equipItem(String ite, Inventory inv) {
        equipItem(Item.parseName(ite), inv, false);
    }

    public void consumeItem(String ite, Inventory inv) {

        Item item = null;
        item = Item.parseName(ite);
        if (item == null) {
            ArrayList<Item> candidates = new ArrayList<>();
            for (Item i : inv.list().keySet()) {
                if (i.getName().contains(ite)) {
                    candidates.add(i);
                }
            }
            if (candidates.size() == 1) {
                item = candidates.get(0);
                consumeItem(item, false);
            } else {
                tprintln("[" + ite + "] doesn't exist!");
            }

        } else {
            consumeItem(item, false);
        }

    }

    public void consumeItem(Item ite) {
        consumeItem(ite, false);
    }

    public void consumeItem(Item ite, boolean hidden) {
        if (!isFallen()) {
            int ogHealth = this.getHealthCurr();
            int healthRestored;
            int ogEnergy = this.getEnergyCurr();
            int energyRestored;
            String restored = "";
            if (ite == null) {
                return;
            } else if (ite.getType() == ItemType.Consumable) {
                this.restoreEnergy(ite.getEnergyRestored());
                this.healDamage(ite.getHealthRestored());
                healthRestored = this.getHealthCurr() - ogHealth;
                energyRestored = this.getEnergyCurr() - ogEnergy;

                if (healthRestored > 0 && energyRestored < 1) {
                    restored = " It restored " + healthRestored + " health.";
                } else if (healthRestored > 0 && energyRestored > 0) {
                    restored = " It restored " + healthRestored + " health and " + energyRestored + " energy.";
                } else if (healthRestored < 1 && energyRestored > 0) {
                    restored = " It restored " + energyRestored + " energy.";
                }

                this.addStatus(ite.getEffects());
                tprintln(getName() + " has consumed the [" + ite.getName() + "]." + restored, hidden);

            } else {
                tprintln("[" + ite.getName() + "] is not consumable.", hidden);
            }
        } else {
            tprintln(getName() + " can't consume that; they're knocked out.", hidden);
        }
    }

    public boolean hasUsableItem() {
        return (equippedConsumable == null);
    }

    public boolean useEquippedItem() {
        if (equippedConsumable != null) {
            this.consumeItem(equippedConsumable, false);
            this.equippedConsumable = null;
            return true;
        } else {
            return false;
        }
    }

}
