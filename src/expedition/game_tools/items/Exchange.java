/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expedition.game_tools.items;

import static expedition.Expedition.input;
import static expedition.Expedition.inputInt;
import static expedition.Other.*;
import expedition.game_tools.items.Item.ItemType;
import java.util.Scanner;

/**
 *
 * @author J76785
 */
public class Exchange {

    private final Inventory left;
    private final Inventory right;
    private final Boolean store;
    private final Scanner scan;
    private final int padding = 16;

    public Exchange(Inventory left, Inventory right, Boolean store, Scanner scan) throws InterruptedException {
        this.left = left;
        this.right = right;
        this.store = store;
        this.scan = scan;
        exchangeMenu();
    }

    public void give(String stringin, int quantity) {
        Item parse = Item.parseName(stringin);
        if (parse == null) {
            error("[" + stringin + "] isn't an item.");
        } else {
            give(parse.getID(), quantity);
        }
    }

    public void take(String stringin, int quantity) {
        Item parse = Item.parseName(stringin);
        if (parse == null) {
            error("[" + stringin + "] isn't an item.");
        } else {
            take(parse.getID(), quantity);
        }
    }

    public void give(int itemID, int quantity) {
        right.addItem(itemID, left.removeItem(itemID, quantity));
    }

    public void take(int itemID, int quantity) {
        left.addItem(itemID, right.removeItem(itemID, quantity));
    }

    public void exchangeMenu() throws InterruptedException {

        String in;
        int intIn;
        Boolean menu = true;
        while (menu) {
            printExchange();
            print("\n---\ninvex-");
            in = input(scan).toLowerCase();
            if (in.matches("^\\?|help$")) {
                printHelp();
            } else if (in.matches("^q|quit|exit|escape|leave$")) {
                menu = false;
                error("Exiting inventory exchange...");
            } else if (in.matches("^(give(\\s+)(\\d+)(\\s+)(\\d+))$")) { //give 3 4
                give(Integer.parseInt(in.split("\\s+")[1]), Integer.parseInt(in.split("\\s+")[2]));
            } else if (in.matches("^(take(\\s+)(\\d+)(\\s+)(\\d+))$")) { //take 3 4
                take(Integer.parseInt(in.split("\\s+")[1]), Integer.parseInt(in.split("\\s+")[2]));
            } else if (in.matches("^(give(\\s+)(.+)(\\s+)(\\d+))$")) { //give wax 4
                give(in.split("\\s+")[1].replace("_", " "), Integer.parseInt(in.split("\\s+")[2]));
            } else if (in.matches("^(take(\\s+)(.+)(\\s+)(\\d+))$")) { //take wax 4
                take(in.split("\\s+")[1].replace("_", " "), Integer.parseInt(in.split("\\s+")[2]));

            } else if (in.matches("^give(\\s+)(\\w+)$")) { //give wax
                give(in.split("\\s+")[1].replace("_", " "), 1);
            } else if (in.matches("^take(\\s+)(\\w+)$")) { //take wax
                take(in.split("\\s+")[1].replace("_", " "), 1);

            } else if (in.matches("^(ai(\\s+)(\\d+))|(additem(\\s+)(\\d+))$")) { //additem 3
                left.addItem(Integer.parseInt(in.split("\\s+")[1]));
            } else if (in.matches("^(ai(\\s+)(\\d+)(\\s+)(\\d+))|(additem(\\s+)(\\d+)(\\s+)(\\d+))$")) { //additem 3 4
                left.addItem(Integer.parseInt(in.split("\\s+")[1]), Integer.parseInt(in.split("\\s+")[2]));
            } else if (in.matches("^removeitem|ri$")) { //additem
                print("\n");
                printTab();
                println("Item ID:\n");
                left.removeItem(inputInt(scan));
            } else if (in.matches("^(ri(\\s+)(\\d+))|(removeitem(\\s+)(\\d+))$")) { //additem 3
                left.removeItem(Integer.parseInt(in.split("\\s+")[1]));
            } else if (in.matches("^(ri(\\s+)(\\d+)(\\s+)(\\d+))|(removeitem(\\s+)(\\d+)(\\s+)(\\d+))$")) { //additem 3 4
                left.removeItem(Integer.parseInt(in.split("\\s+")[1]), Integer.parseInt(in.split("\\s+")[2]));

            } else {
                error("Your input was misunderstood. Type >? or >help to view the manual.");
            }
        }
    }

    public void printExchange() {
        int leftHeight = left.numUniqueItems();
        int rightHeight = right.numUniqueItems();
        Item[] leftKeys = new Item[leftHeight];
        Item[] rightKeys = new Item[rightHeight];

        Inventory longest = ((left.numUniqueItems() >= right.numUniqueItems()) ? left : right);
        Inventory shortest = ((left.numUniqueItems() >= right.numUniqueItems()) ? right : left);

        int longHeight = longest.numUniqueItems();

        int arrayIndex = 0;
        for (ItemType t : ItemType.values()) {
            for (Item i : left.getInventory().keySet()) {
                if (i.getType() != ItemType.Currency && i.getType() == t) {
                    leftKeys[arrayIndex] = i;
                    arrayIndex++;
                }
            }
        }
        arrayIndex = 0;
        for (ItemType t : ItemType.values()) {
            for (Item i : right.getInventory().keySet()) {
                if (i.getType() != ItemType.Currency && i.getType() == t) {
                    rightKeys[arrayIndex] = i;
                    arrayIndex++;
                }
            }
        }

        int totalWidth = left.width() + right.width() + padding;

        //print seperator bar
        br();
        tprintln(repeatStr("-", totalWidth));
        //print names
        tprintln(left.getName() + " contains:" + grs((left.getName() + " contains:"), left.width())
                + repeatStr(" ", padding)
                + grs(right.getName() + " contains:", right.width()) + right.getName() + " contains:\n");
        //print money
        tprintln(left.getMoneyStr() + grs((left.getMoneyStr()), left.width())
                + repeatStr(" ", padding)
                + grs(right.getMoneyStr(), right.width()) + right.getMoneyStr());

        //print top of two inventories
        tprintln(repeatStr("-", left.width()) + repeatStr(" ", padding) + repeatStr("-", right.width()));

        int itemIndex = 0;
        String build = "";
        while (longHeight > 0) {
            build = repeatStr(" ", left.getLongestItemName()) + " |  "
                    + repeatStr(" ", left.getLongestItemQuantity()) + " |     "
                    + repeatStr(" ", left.getLongestItemWeight());
            if (leftHeight > 0) {
                tprint(leftKeys[itemIndex] + grs(leftKeys[itemIndex].toString(), left.getLongestItemName()));
                print(" | x");
                print(left.get(leftKeys[itemIndex]) + grs(((Integer) left.get(leftKeys[itemIndex])).toString(), left.getLongestItemQuantity()));
                print(" | (");
                print(leftKeys[itemIndex].getWeight() * left.get(leftKeys[itemIndex]) + "su)"
                        + grs(((Integer) (leftKeys[itemIndex].getWeight() * left.get(leftKeys[itemIndex]))).toString(), left.getLongestItemWeight()) + grs(build, left.width()));
            } else {
                tprint(build + grs(build, left.width()));
            }
            build = "";
            print(repeatStr(" ", padding));
            if (rightHeight > 0) {
                print(rightKeys[itemIndex] + grs(rightKeys[itemIndex].toString(), right.getLongestItemName()));
                print(" | x");
                print(right.get(rightKeys[itemIndex]) + grs(((Integer) right.get(rightKeys[itemIndex])).toString(), right.getLongestItemQuantity()));
                print(" | (");
                print(rightKeys[itemIndex].getWeight() * right.get(rightKeys[itemIndex]) + "su)"
                        + grs(((Integer) (rightKeys[itemIndex].getWeight() * right.get(rightKeys[itemIndex]))).toString(), right.getLongestItemWeight()));
            } else {
                build = repeatStr(" ", right.getLongestItemName()) + " |  "
                        + repeatStr(" ", right.getLongestItemQuantity()) + " |     "
                        + repeatStr(" ", right.getLongestItemWeight());
                print(build + grs(build, right.width()));
                build = "";
            }
            br();
            longHeight--;
            leftHeight--;
            rightHeight--;
            itemIndex++;
        }

        //print bottom of two inventories
        tprintln(repeatStr("-", left.width()) + repeatStr(" ", padding) + repeatStr("-", right.width()));

        //print totals
        tprint(getRemainderSpaces("total:", left.getLongestItemName()) + "total:" + repeatStr(" ", 7 + left.getLongestItemQuantity()) + "(" + left.getTotalWeight() + "su)");
        print(repeatStr(" ", padding));
        print(getRemainderSpaces("total:", right.getLongestItemName()) + "total:" + repeatStr(" ", 7 + right.getLongestItemQuantity()) + "(" + right.getTotalWeight() + "su)");
        print("\n\n");

        //print frame end
        tprintln(repeatStr("-", totalWidth));

    }

    public void printHelp() {

    }

}
