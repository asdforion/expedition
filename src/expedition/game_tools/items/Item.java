package expedition.game_tools.items;

import expedition.game_tools.StatusEffect;
import expedition.game_tools.StatusEffect.Effect;
import java.util.ArrayList;

public enum Item {

    // <editor-fold defaultstate="collapsed" desc="all items">
    //currency
    Ducat("ducat", "A gold coin.", 300, ItemType.Currency, 0, 0), //gold piece \doo-cat\
    Silleon("silleon", "A silver coin.", 20, ItemType.Currency, 0, 1), //silver piece \sill-ay-en\
    Peant("peant", "A nickel coin.", 1, ItemType.Currency, 0, 2), //nickel piece \pay-ant\
    //goods
    Wax("wax", 10, ItemType.Goods, 8, 3),
    Wood("wood", 10, ItemType.Goods, 10, 4),
    //consumables
    Milk("gallon of milk", "A glass bottle of milk. This drink restores 15 energy.", 8, ItemType.Consumable, 10, 5),
    Wine_Peach("bottle of peach wine", "A tall bottle of peach wine. This drink restores 15 energy and increases intelligence by 1 for the day.", 25, ItemType.Consumable, 10, 6),
    Bread("loaf of bread", "An unassuming loaf of bread. This food restores 15 energy.", 5, ItemType.Consumable, 3, 7),
    Potion_MinorHealth("minor health potion", "A small vial of crimson liquid. This potion restores 25 health.", 5, ItemType.Consumable, 3, 13),
    //trinkets
    Necklace_Ivory("ivory necklace", 30, ItemType.Trinket, 0, 8),
    Necklace_Sapphire("sapphire necklace", 75, ItemType.Trinket, 0, 12),
    //armors
    Cuirass_Bronze("bronze cuirass", 150, ItemType.Armor, 60, 9),
    //weapons
    Sword_Bronze("bronze shortsword", 90, ItemType.Weapon, 19, 10),
    Sword_Steel("steel shortsword", 90, ItemType.Weapon, 15, 11);
    // </editor-fold>

    private final String name;
    private final String description;
    private final int value;
    private final ItemType type;
    private final int weight;
    private final int id;

    public static final int lastID = Item.values().length - 1; //13

    public enum ItemType {
        Currency,
        Weapon,
        Armor,
        Trinket,
        Consumable,
        Goods;

    }

    Item(String name, int value, ItemType type, int weight, int id) {
        this.name = name;
        this.description = "This item has no description.";
        this.value = value;
        this.type = type;
        this.weight = weight;
        this.id = id;
    }

    Item(String name, String description, int value, ItemType type, int weight, int id) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.type = type;
        this.weight = weight;
        this.id = id;
    }

    public String toString() {
        return getName();
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public ItemType getType() {
        return type;
    }

    public int getWeight() {
        return weight;
    }

    public int getID() {
        return id;
    }
    
    public static void printItems(){
        for(Item i : Item.values()){
            System.out.println(i.getID() + " - " + i.getName() + " : " + i.getDescription());
        }
    }

    public static Item parseID(int in) {
        for (Item i : Item.values()) {
            if (i.getID() == in) {
                return i;
            }
        }
        return null;
    }

    public static Item parseName(String in) {
        in = in.toLowerCase();
        for (Item i : Item.values()) {
            if (i.getName().equals(in)) {
                return i;
            }
        }
        return null;
    }

    public ArrayList<StatusEffect> getEffects() { //Consumable effects are applied when consumed; other effects are applied when equipped.
        ArrayList<StatusEffect> effs = new ArrayList<>();

        switch (this) {
            case Necklace_Sapphire:
                effs.add(new StatusEffect("[sapphire necklace]", "Aura of Intelligence : +2 Intelligence.", Effect.INTFLAT, -1, 2, null, false, false));
                break;
            case Cuirass_Bronze:
                effs.add(new StatusEffect("[bronze cuirass]", "Armored : -1 Damage from every attack.", Effect.DAMAGEBLOCKEDFLAT, -1, -1, null, false, false));
                effs.add(new StatusEffect("[bronze cuirass]", "Shock Absorption : -10% Damage from every attack.", Effect.DAMAGEBLOCKEDPERCENT, -1, 0.9, null, false, false));
                break;
            case Sword_Bronze:
                effs.add(new StatusEffect("[bronze shortsword]", "Heavy Sword : +3 Damage to every physical attack made.", Effect.PHYSDAMAGEDONEFLAT, -1, 3, null, false, false));
                break;
            case Sword_Steel:
                effs.add(new StatusEffect("[steel shortsword]", "Sharp Sword : +20% Damage to every physical attack made.", Effect.PHYSDAMAGEDONEPERCENT, -1, 1.2, null, false, false));
                break;
            case Wine_Peach:
                effs.add(new StatusEffect("[peach wine]", "Intelligent Buzz : +1 Intelligence.", Effect.INTFLAT, 1, 1.0, null, false, false));
                break;
        }

        return effs;
    }

    public int getHealthRestored() {
        int ret = 0;
        switch (this) {
            case Potion_MinorHealth:
                ret = 15;
        }
        return ret;
    }

    public int getEnergyRestored() {
        int ret = 0;
        switch (this) {
            case Milk:
                ret = 15;
            case Wine_Peach:
                ret = 15;
            case Bread:
                ret = 15;
        }
        return ret;
    }

}
