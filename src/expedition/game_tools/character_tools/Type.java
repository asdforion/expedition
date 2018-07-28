/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expedition.game_tools.character_tools;

import expedition.Other;
import static expedition.Other.contains;
import static expedition.Other.pickRandom;
import java.util.HashMap;

/**
 *
 * @author J76785
 */
public enum Type {
    Normal("Normal"),
    Archer("Archer"),
    Ranger("Ranger"),
    Priest("Priest"),
    Monk("Monk"),
    Wizard("Wizard"),
    Warlock("Warlock"),
    Fighter("Fighter"),
    Rogue("Rogue"),
    Beast("Beast"),
    Demon("Demon"),
    Ghoul("Ghoul");

    private String name;

    Type(String name) {
        this.name = name;
    }

    public Type[] weaknesses() {
        Type[] out = new Type[3];
        switch (this) {
            case Archer:
                out[0] = Ranger;
                out[1] = Demon;
                out[2] = Ghoul;
            case Ranger:
                out[0] = Wizard;
                out[1] = Rogue;
                out[2] = Demon;
            case Priest:
                out[0] = Archer;
                out[1] = Warlock;
                out[2] = Beast;
            case Monk:
                out[0] = Fighter;
                out[1] = Rogue;
                out[2] = Beast;
            case Wizard:
                out[0] = Monk;
                out[1] = Beast;
                out[2] = Demon;
            case Warlock:
                out[0] = Priest;
                out[1] = Monk;
                out[2] = Wizard;
            case Fighter:
                out[0] = Archer;
                out[1] = Warlock;
                out[2] = Ghoul;
            case Rogue:
                out[0] = Fighter;
                out[1] = Rogue;
                out[2] = Ghoul;
            case Beast:
                out[0] = Archer;
                out[1] = Ranger;
                out[2] = Fighter;
            case Demon:
                out[0] = Priest;
                out[1] = Monk;
                out[2] = Warlock;
            case Ghoul:
                out[0] = Ranger;
                out[1] = Priest;
                out[2] = Wizard;
        }
        return out;
    }

    public Type[] advantages() {
        Type[] out = new Type[3];
        switch (this) {
            case Archer:
                out[0] = Priest;
                out[1] = Fighter;
                out[2] = Beast;
            case Ranger:
                out[0] = Archer;
                out[1] = Beast;
                out[2] = Ghoul;
            case Priest:
                out[0] = Warlock;
                out[1] = Demon;
                out[2] = Ghoul;
            case Monk:
                out[0] = Wizard;
                out[1] = Warlock;
                out[2] = Demon;
            case Wizard:
                out[0] = Ranger;
                out[1] = Warlock;
                out[2] = Ghoul;
            case Warlock:
                out[0] = Priest;
                out[1] = Fighter;
                out[2] = Demon;
            case Fighter:
                out[0] = Monk;
                out[1] = Rogue;
                out[2] = Beast;
            case Rogue:
                out[0] = Ranger;
                out[1] = Monk;
                out[2] = Rogue;
            case Beast:
                out[0] = Priest;
                out[1] = Monk;
                out[2] = Wizard;
            case Demon:
                out[0] = Archer;
                out[1] = Ranger;
                out[2] = Wizard;
            case Ghoul:
                out[0] = Archer;
                out[1] = Fighter;
                out[2] = Rogue;
        }
        return out;
    }

    public boolean isWeakTo(Type check) {
        return contains(this.weaknesses(), check);
    }

    public boolean isStrongTo(Type check) {
        return contains(this.advantages(), check);
    }

    public static Type getRandomPlayableType() {
        HashMap<Type, Integer> picker = new HashMap<>();
        picker.put(Archer, 1);
        picker.put(Fighter, 1);
        picker.put(Monk, 1);
        picker.put(Priest, 1);
        picker.put(Ranger, 1);
        picker.put(Rogue, 1);
        picker.put(Warlock, 1);
        picker.put(Wizard, 1);
        return pickRandom(picker);
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return name;
    }

    public Ability getAbilityForLevel(int level) {
        switch (this) {
            case Archer:
                return archerLevel(level);
            case Ranger:
                return rangerLevel(level);
            case Priest:
                return priestLevel(level);
            case Monk:
                return monkLevel(level);
            case Wizard:
                return wizardLevel(level);
            case Warlock:
                return warlockLevel(level);
            case Fighter:
                return fighterLevel(level);
            case Rogue:
                return rogueLevel(level);
            default:
                return null;
        }
    }

    public Ability archerLevel(int level) {
        switch (level) {
            case 3:
                return Ability.Quickshot;
            default:
                return null;
        }
    }

    public Ability rangerLevel(int level) {
        switch (level) {
            case 3:
                return Ability.BarbedNet;
            default:
                return null;
        }
    }

    public Ability priestLevel(int level) {
        switch (level) {
            case 3:
                return Ability.Prayer;
            default:
                return null;
        }
    }

    public Ability monkLevel(int level) {
        switch (level) {
            case 3:
                return Ability.ChakraBlock;
            default:
                return null;
        }
    }

    public Ability wizardLevel(int level) {
        switch (level) {
            case 3:
                return Ability.FireBlast;
            default:
                return null;
        }
    }

    public Ability warlockLevel(int level) {
        switch (level) {
            case 3:
                return Ability.FoolsCurse;
            default:
                return null;
        }
    }

    public Ability fighterLevel(int level) {
        switch (level) {
            case 3:
                return Ability.DecisiveSwing;
            default:
                return null;
        }
    }

    public Ability rogueLevel(int level) {
        switch (level) {
            case 3:
                return Ability.PreciseSlice;
            default:
                return null;
        }
    }
}
