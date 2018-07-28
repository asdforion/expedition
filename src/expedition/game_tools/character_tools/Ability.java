package expedition.game_tools.character_tools;

import expedition.game_tools.StatusEffect;
import expedition.game_tools.StatusEffect.Effect;
import expedition.game_tools.character_tools.Character.Stat;

public enum Ability {

    // <editor-fold defaultstate="collapsed" desc="all abilities">
    // GENERAL ABILITIES                                                                    //String description, Effect effect, int timer, double intensity, boolean fightTimed, boolean hidden
    //ABILITY  /Ability Name   /Dscription  
    //      /Type     /Energy /Target /Damage /Stat Ratio     /Effects to Target                                    /Effects to User
    Punch("Punch", "Strike the target with your fists, dealing 10 + (1.0 * Strength) Normal Damage.",
            Type.Normal, 5, true, 10, Stat.STR, 1.0, false,
            null,
            null),
    ChakraBlock("Chakra Block", "Strike the target's soul, dealing 15 + (0.6 * Spirit) Monk Damage and blocking their chakras for 4 turns. "
            + "The chakra block reduces their MAGIC stat by 25%.",
            Type.Monk, 15, true, 15, Stat.SPI, 0.6, false,
            new StatusEffect[]{new StatusEffect("Blocked Chakras", "Soul is blocked by a precise strike. Magic is reduced by 20%", Effect.MAGPERCENT, 4, 0.8, null, true, false)},
            null),
    Prayer("Prayer", "Pray for the target, healing them for 15 + (2.0 * Spirit).",
            Type.Priest, 15, true, -15, Stat.SPI, -2.0, false,
            null,
            null),
    DecisiveSwing("DecisiveSwing", "Swing your weapon with a great amount of strength, dealing 20 + (1.5 * STR) Fighter Damage.",
            Type.Fighter, 30, true, 20, Stat.STR, 1.5, false,
            null,
            null),
    BarbedNet("Barbed Net", "Hurl a barbed net at your target, dealing 5 + (0.8 * Strength) Ranger Damage. "
            + "The net lasts for 2 turns, slowing their speed by 25% and dealing 6 Normal damage per turn.",
            Type.Ranger, 15, true, 5, Stat.STR, 0.8, false,
            new StatusEffect[]{new StatusEffect("Barbed Net - Trapped", "Trapped! Your speed is reduced by 25%.", Effect.SPDPERCENT, 2, 0.75, null, true, false),
                new StatusEffect("Barbed Net - Barbed", "Trapped! Take 6 Normal damage per turn.", Effect.DAMAGETICK, 2, 6.0, Type.Normal, true, false)},
            null),
    PreciseSlice("Precise Slice", "Stab your target inbetween their armor, dealing 15 + (0.5 * Agility) Rogue Damage, ignoring their armor.",
            Type.Rogue, 10, true, 15, Stat.AGI, 0.5, true,
            null,
            null),
    Quickshot("Quickshot", "Fire an arrow at the target, dealing 15 + (0.6 * Agility) Archer Damage. Increase your speed by 15% for 3 turns.",
            Type.Archer, 10, true, 15, Stat.AGI, 0.6, false,
            null,
            new StatusEffect[]{new StatusEffect("Quickshot", "In the zone! Your speed is increased by 15%.", Effect.SPDPERCENT, 3, 1.15, null, true, false)}),
    FireBlast("Fireblast", "Summon a ball of fire to fly at your target, dealing 20 + (0.5 * Magic) Wizard Damage and burning them for 9 Wizard damage over three turns.",
            Type.Wizard, 20, true, 20, Stat.MAG, 0.5, false,
            new StatusEffect[]{new StatusEffect("Burning", "ON FIRE! Taking 3 Wizard damage per turn.", Effect.DAMAGETICK, 3, 3.0, Type.Wizard, true, false)},
            null),
    FoolsCurse("Fool's Curse", "Curse the enemy opponent with foolishness, dealing 8 + (0.3 * Magic) Warlock damage. "
            + "The curse lasts for 3 turns, dealing 6 damage per turn and reducing the target's intelligence by 20% for the duration.",
            Type.Warlock, 15, true, 8, Stat.MAG, 0.3, false,
            new StatusEffect[]{new StatusEffect("Fool's Curse - Addled Brain", "Take 6 Warlock damage every turn.", Effect.DAMAGETICK, 3, 6.0, Type.Warlock, true, false),
                new StatusEffect("Fool's Curse - Moronification", "Intelligence is reduced by 20%.", Effect.INTPERCENT, 3, 0.8, null, true, false)},
            null);
    // </editor-fold>

    private String name;
    private String description;
    private Type damageType;
    private int energyCost;
    private boolean targeted;
    private int flatDamage;
    private Stat statRatio;
    private double statRatioVal;
    private boolean ignoreArmor;
    private StatusEffect[] effectsToTarget;
    private StatusEffect[] effectsToUser;

    Ability(String name, String description, Type damageType, int energyCost, boolean targeted, int flatDamage, Stat statRatio, double statRatioVal, boolean ignoreArmor, StatusEffect[] effectsToTarget, StatusEffect[] effectsToUser) {
        this.name = name;
        this.description = description;
        this.energyCost = energyCost;
        this.damageType = damageType;
        this.targeted = targeted;
        this.flatDamage = flatDamage;
        this.statRatio = statRatio;
        this.statRatioVal = statRatioVal;
        this.ignoreArmor = ignoreArmor;
        this.effectsToTarget = effectsToTarget;
        this.effectsToUser = effectsToUser;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Type getDamageType() {
        return damageType;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public boolean isTargeted() {
        return targeted;
    }

    public int getFlatDamage() {
        return flatDamage;
    }

    public Stat getStatRatio() {
        return statRatio;
    }

    public double getStatRatioVal() {
        return statRatioVal;
    }

    public boolean isIgnoreArmor() {
        return ignoreArmor;
    }

    public StatusEffect[] getEffectsToTarget() {
        return effectsToTarget;
    }

    public StatusEffect[] getEffectsToUser() {
        return effectsToUser;
    }
    
    public String toString(){
        return this.getName();
    }

    
    
}
