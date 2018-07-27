/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expedition.game_tools.items;

import static expedition.Other.*;
import expedition.game_tools.items.Item;
import expedition.game_tools.items.Item.ItemType;
import java.util.HashMap;

/**
 *
 * An INVENTORY is a dataset representing a collection of items. The INVENTORY
 * class implements a number of methods used to update, maintain, and compare
 * collections of items.
 */
public class Inventory {

    private HashMap<Item, Integer> inventory;
    private int totalWeight;
    private String name;

    public Inventory(String name) {
        inventory = new HashMap<>();
        this.name = name;
        totalWeight = 0;
    }

    public String getName() {
        return name;
    }

    public HashMap<Item, Integer> getInventory() {
        return inventory;
    }

    //public get alias
    public int get(Item key) {
        return inventory.get(key);
    }

    //additem alias for item object input.
    public int addItem(Item in) {
        return addItem(in.getID());
    }

    //additem alias for single object.
    public int addItem(int in) {
        return addItem(in, 1);
    }

    //additem alias for item string input.
    public int addItem(String in) {
        Item item = Item.parseName(in);
        if (item != null) {
            return addItem(item.getID());
        } else {
            printTab();
            println("[" + in.toLowerCase() + "] is not an item.");
            return 0;
        }
    }

    //general case for additem based on ID
    public int addItem(int in, int quant) {
        int numAdded = 0;
        if (in > Item.lastID || in < 0) { // check if item exists
            println("ERROR - No Such Item");
        } else {
            Item item = Item.parseID(in);
            for (int i = 0; i < quant; i++) {
                if (inventory.containsKey(item)) {
                    inventory.put(item, inventory.get(item) + 1);
                } else {
                    inventory.put(item, 1);
                }
                totalWeight += item.getWeight();
                numAdded++;
            }
            print("\n");
            printTab();
            if (quant <= 1) {
                //println("[" + item.toString() + "] has been added to your inventory. You now have " + inventory.get(item) + " of them.");
            } else {
               // println("[" + item.toString() + "] has been added to your inventory " + numAdded + " times. You now have " + inventory.get(item) + " of them.");
            }

        }
        return numAdded;
    }

    //removeitem alias for single object.
    public int removeItem(int in) {
        return removeItem(in, 1);
    }

    //additem alias for item object input.
    public int removeItem(Item in) {
        return removeItem(in.getID());
    }

    //additem alias for item string input.
    public int removeItem(String in) {
        Item item = Item.parseName(in);
        if (item != null) {
            return removeItem(item.getID());
        } else {
            printTab();
            println("[" + in.toLowerCase() + "] is not an item.");
            return 0;
        }
    }

    public int removeItem(int in, int quant) {
        int numRemoved = 0;
        if (in > Item.lastID || in < 0) { // check if item exists
            println("ERROR - No Such Item");
        } else {
            Item item = Item.parseID(in);
            print("\n");
            for (int i = 0; i < quant; i++) {
                if (inventory.containsKey(item)) {
                    if (inventory.get(item) <= 1) {
                        inventory.remove(item); // remove from inventory
                    } else {
                        inventory.put(item, (inventory.get(item) - 1)); // remove one
                    }
                    totalWeight -= item.getWeight();
                    numRemoved++;
                } else {
                    printTab();
                    //println("You don't have enough [" + item + "] to remove " + quant + " of them from your inventory.");
                    break;
                }
            }

            printTab();
            if (quant <= 1) {
                //println("[" + item + "] has been removed from your inventory. You now have " + ((inventory.containsKey(item)) ? inventory.get(item) : "none") + " of them.");
            } else {
                //println("[" + item + "] has been removed from your inventory " + numRemoved + " times. You now have " + ((inventory.containsKey(item)) ? inventory.get(item) : "none") + " of them.");
            }
        }
        return numRemoved;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    //returns the longest possible table width.
    public int width() {
        int width = (this.name + " contains:").length();
        int tablewidth = getLongestItemName() + getLongestItemQuantity() + getLongestItemWeight() + 11;
        return (width > tablewidth) ? width : tablewidth;
    }

    public void printInventory() {
        int itemNameWidth = getLongestItemName();
        int itemQuantWidth = getLongestItemQuantity();
        int itemWeightWidth = getLongestItemWeight();

        print("\n");
        printTab();
        println("Your inventory contains:\n");
        printMoney();
        printTab();
        println(repeatStr("-", width()));
        //sorted by item type
        for (ItemType t : Item.ItemType.values()) {
            if (t.equals(ItemType.Currency)) {
                continue;
            }
            for (Item i : inventory.keySet()) {
                if (i.getType() == t) {
                    printTab();
                    println(i.toString() + getRemainderSpaces(i.toString(), itemNameWidth)
                            + " | x" + inventory.get(i) + getRemainderSpaces(inventory.get(i).toString(), itemQuantWidth)
                            + " | (" + i.getWeight() * inventory.get(i) + "su)");
                }
            }
        }
        printTab();
        println(repeatStr("-", width()));
        printTab();
        println(getRemainderSpaces("total:", itemNameWidth) + "total:" + repeatStr(" ", 7 + itemQuantWidth) + "(" + totalWeight + "su)");
    }

    public void printMoney() {
        printTab();
        String money = "" + (inventory.containsKey(Item.Ducat) ? inventory.get(Item.Ducat) : "0") + "d, "
                + (inventory.containsKey(Item.Silleon) ? inventory.get(Item.Silleon) : "0") + "s, and "
                + (inventory.containsKey(Item.Peant) ? inventory.get(Item.Peant) : "0") + "p.";
        println(money);
    }

    public String getMoneyStr() {
        return "" + (inventory.containsKey(Item.Ducat) ? inventory.get(Item.Ducat) : "0") + "d, "
                + (inventory.containsKey(Item.Silleon) ? inventory.get(Item.Silleon) : "0") + "s, and "
                + (inventory.containsKey(Item.Peant) ? inventory.get(Item.Peant) : "0") + "p.";
    }

    public int getLongestItemName() {
        int longest = 6;
        int current;
        for (Item i : inventory.keySet()) {
            if (i.getType() != ItemType.Currency) {
                current = i.getName().length();
                if (current > longest) {
                    longest = current;
                }
            }
        }
        return longest;
    }

    public int getLongestItemWeight() {
        int longest = 1;
        int current;
        for (Item i : inventory.keySet()) {
            if (i.getType() != ItemType.Currency) {
                current = Integer.toString(inventory.get(i) * i.getWeight()).length();
                if (current > longest) {
                    longest = current;
                }
            }
        }
        return longest;
    }

    public int getLongestItemQuantity() {
        int longest = 1;
        int current;
        for (Item i : inventory.keySet()) {
            if (i.getType() != ItemType.Currency) {
                current = Integer.toString(inventory.get(i)).length();
                if (current > longest) {
                    longest = current;
                }
            }
        }
        return longest;
    }

    public int numUniqueItems() { //DOES NOT INCLUDE CURRENCY
        int num = 0;
        for (Item i : inventory.keySet()) {
            if (i.getType() != ItemType.Currency) {
                num++;
            }
        }
        return num;
    }

}
