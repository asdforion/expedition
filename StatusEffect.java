package expedition.game_tools;

import expedition.game_tools.character_tools.Type;

public class StatusEffect {

    public enum Effect {
        //general non-stat status
        //HUNGER,
        //THIRST,
        FALLEN,
        NOACTION,
        //stat changes
        INTPERCENT,
        INTFLAT,
        AGIPERCENT,
        AGIFLAT,
        STRPERCENT,
        STRFLAT,
        SPIFLAT,
        SPIPERCENT,
        MAGFLAT,
        MAGPERCENT,
        SPDFLAT,
        SPDPERCENT,
        //amount of damage/healing/energy received.
        DAMAGETAKENFLAT,
        DAMAGETAKENPERCENT,
        HEALTAKENFLAT,
        HEALTAKENPERCENT,
        ENERGYRESTOREDFLAT,
        ENERGYRESTOREDPERCENT,
        //stat regen per turn
        ENERGYTICK,
        HEALTICK,
        DAMAGETICK;
    }

    private String name;
    private String description;
    private final Effect effect;
    private int timer;
    private double intensity; // 1 is default.
    private Type damageType;
    private boolean fightTimed;
    private boolean hidden;

    public StatusEffect(String name, String description, Effect effect, int timer, double intensity, Type damageType, boolean fightTimed, boolean hidden) {
        this.name = name;
        this.description = description;
        this.effect = effect;
        this.timer = timer;
        this.intensity = intensity; 
        this.damageType = damageType;
        this.fightTimed = fightTimed;
        this.hidden = hidden;
    }

    public Effect getEffect() {
        return effect;
    }

    public int getTimer() {
        return timer;
    }

    public double getIntensity() {
        return intensity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFightTimed() {
        return fightTimed;
    }
    
    public void tickTimer(){
        timer--;
    }
    
    public void addToTimer(){
        timer++;
    }

    public Type getDamageType() {
        return damageType;
    }

    public boolean isHidden() {
        return hidden;
    }
    
    
    
    

}
