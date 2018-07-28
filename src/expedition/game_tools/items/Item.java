package expedition.game_tools.items;

public enum Item {

    // <editor-fold defaultstate="collapsed" desc="all items">
    //currency
    Ducat("ducat", 300, ItemType.Currency, 0, 0), //gold piece \doo-cat\
    Silleon("silleon", 20, ItemType.Currency, 0, 1), //silver piece \sill-ay-en\
    Peant("peant", 1, ItemType.Currency, 0, 2), //nickel piece \pay-ant\
    //goods
    Wax("wax", 10, ItemType.Goods, 8, 3),
    Wood("wood", 10, ItemType.Goods, 10, 4),
    //consumables
    Milk("gallon of milk", 8, ItemType.Consumable, 10, 5),
    Wine_Peach("bottle of peach wine", 25, ItemType.Consumable, 10, 6),
    Bread("loaf of bread", 5, ItemType.Consumable, 3, 7),
    //trinkets
    Necklace_Ivory("ivory necklace", 50, ItemType.Trinket, 0, 8),
    //armors
    Cuirass_Bronze("bronze cuirass", 150, ItemType.Armor, 60, 9),
    //weapons
    Sword_Bronze("bronze shortsword", 90, ItemType.Weapon, 19, 10),
    Sword_Steel("steel shortsword", 90, ItemType.Weapon, 15, 11);
    // </editor-fold>

    private final String name;
    private final int value;
    private final ItemType type;
    private final int weight;
    private final int id;

    public static final int lastID = Item.values().length - 1;

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
        this.value = value;
        this.type = type;
        this.weight = weight;
        this.id = id;
    }

    public String toString() {
        return getName();
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
}
