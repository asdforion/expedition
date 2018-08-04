package expedition;

import expedition.game_tools.items.Inventory;
import expedition.game_tools.character_tools.Character;
import static expedition.Expedition.*;
import static expedition.Other.*;
import expedition.game_tools.*;
import expedition.game_tools.Environment.*;
import expedition.game_tools.character_tools.*;
import expedition.game_tools.items.Exchange;
import expedition.game_tools.items.Item;
import java.util.Scanner;

public class Frame {

    public final static String tab = "     ";

    Scanner scan;
    private boolean playing;

    String expeditionName;

    private int daysSinceEmbark;
    private Date date;
    private Environment env;
    boolean willEmbark;

    Inventory inventory;
    Team team;
    Team enemies;

    public Frame(Scanner scan) {
        this.scan = scan;

        expeditionName = "Testerly";

        team = new Team(scan, expeditionName);
        team.newPlayerTeam();

        enemies = new Team(scan, "Enemytesterly");
        enemies.newPlayerTeam();
        initShop();
        inventory = new Inventory(expeditionName + "'s inventory");

        this.date = new Date(Month.Abb, 1);
        this.env = new Environment(date);

        willEmbark = true;
        this.daysSinceEmbark = 0;
    }

    void begin() throws InterruptedException {
        playing = true;
        while (playing) {
            if (willEmbark) {
                travel();
            } else {
                print("\n");
                printTab();
                println("Your traveling company of " + team.size() + " adventurers wakes up after a brief night of rest.");
                Thread.sleep((textSpeed * 6) * 100);
            }
            printDayIntro();
            trigger_NewDay();

            menu();

            env.increment(date);
            daysSinceEmbark++;
        } // end of while(playing)
        scan.close();
    }

    //Day Intro
    private void printDayIntro() throws InterruptedException {
        printDate();
        Thread.sleep((textSpeed * 6) * 100);
        printEnvironment(env);
        Thread.sleep((textSpeed * 6) * 100);
        team.printBirthdays(date);
    }

    private void trigger_NewDay() {
        team.updateBirthday(date);
        for (Character i : team.list()) {
            i.statusTickNonCombat();
        }
    }

    //Daily Menu
    private void menu() throws InterruptedException {

        String in;
        int intIn;
        boolean menu = true;
        Character temp = null;
        Item ite = null;
        while (menu) { // t | p | s | q | d | i
            print("\n---\n");
            in = input(scan).toLowerCase();
            if (in.matches("^(\\?|help)(\\s*)$")) {
                printHelp();
            } else if (in.matches("^(q|quit|exit|escape|leave)(\\s*)$")) {
                menu = false;
                playing = false;
            } else if (in.matches("^(travelers|t)(\\s*)$")) {
                team.printTravelers(date);
            } else if (in.matches("^(pfe|embark|p)(\\s*)$")) {
                willEmbark = !willEmbark;
                if (willEmbark) {
                    print("\n");
                    printTab();
                    println("Your travelers plan to pack up their things tonight in preparation for an early embark tomorrow morning.");
                } else {
                    print("\n");
                    printTab();
                    println("Your travelers stop packing their things, and are no longer preparing to travel tomorrow.");
                }
            } else if (in.matches("^(sleep|s)(\\s*)$")) { //if(in.matches("^(sleep)(\\s)*(\\d)+$"))
                menu = false;
                printSleep();
            } else if (in.matches("^((sleep(\\s+)(-s))|((s)(\\s)*(-s)))(\\s*)$")) { //sleep -s || s -s
                menu = false;
            } else if (in.matches("^(date|d)(\\s*)$")) {
                printDate();
            } else if (in.matches("^(weather|w)(\\s*)$")) {
                printEnvironment(env);
            } else if (in.matches("^(ai|additem)(\\s*)$")) { //additem
                print("\n");
                printTab();
                println("Item ID:\n");
                inventory.addItem(inputInt(scan));

            } else if (in.matches("^((ai(\\s+)(\\d+))|(additem(\\s+)(\\d+)))(\\s*)$")) { //additem 3
                inventory.addItem(Integer.parseInt(in.split("\\s+")[1]));

            } else if (in.matches("^((ai(\\s+)(\\d+)(\\s+)(\\d+))|(additem(\\s+)(\\d+)(\\s+)(\\d+)))(\\s*)$")) { //additem 3 4

                inventory.addItem(Integer.parseInt(in.split("\\s+")[1]), Integer.parseInt(in.split("\\s+")[2]));
            } else if (in.matches("^(removeitem|ri)(\\s*)$")) { //removeitem
                print("\n");
                printTab();
                println("Item ID:\n");
                inventory.removeItem(inputInt(scan));
            } else if (in.matches("^((ri(\\s+)(\\d+))|(removeitem(\\s+)(\\d+)))(\\s*)$")) { //removeitem 3
                inventory.removeItem(Integer.parseInt(in.split("\\s+")[1]));
            } else if (in.matches("^((ri(\\s+)(\\d+)(\\s+)(\\d+))|(removeitem(\\s+)(\\d+)(\\s+)(\\d+)))(\\s*)$")) { //removeitem 3 4
                inventory.removeItem(Integer.parseInt(in.split("\\s+")[1]), Integer.parseInt(in.split("\\s+")[2]));
            } else if (in.matches("^((ai(\\s+)(\\S+))|(additem(\\s+)(\\S+)))(\\s*)$")) { //additem itemname
                inventory.addItem(in.split("\\s+")[1].replace("_", " "));
            } else if (in.matches("^((ri(\\s+)(\\S+))|(removeitem(\\s+)(\\S+)))(\\s*)$")) { //remove itemname
                ite = inventory.removeItem(in.split("\\s+")[1].replace("_", " "));
                if (ite == null) {
                    tprintln("Not a valid item.");
                }
            } else if (in.matches("^(inventory|i|items)(\\s*)$")) {
                inventory.printInventory();
            } else if (in.matches("^(test)(\\s*)$")) {
                tester();
            } else if (in.matches("^(test2)(\\s*)$")) {
                tester2();
            } else if (in.matches("^trav(\\s+)(\\d+)(\\s*)$")) {
                temp = team.get(Integer.parseInt(in.split("\\s+")[1]));
                if (temp != null) {
                    temp.printTravelerVerbose(date);
                } else {
                    error("Not a valid traveler.");
                }
            } else if (in.matches("^exp(\\s+)(\\d+)(\\s+)(\\d+)(\\s*)$")) {
                addExperience(Integer.parseInt(in.split("\\s+")[1]), Integer.parseInt(in.split("\\s+")[2]));

            } else if (in.matches("^(equip(\\s+)(.+)(\\s+)(\\d+))(\\s*)$")) { //equip item_name trav# 
                tprintln("");
                ite = inventory.removeItem((in.split("\\s+")[1].replace("_", " ")));
                if (ite != null) {
                    temp = team.get(Integer.parseInt(in.split("\\s+")[2]));
                    if (temp != null) {
                        temp.equipItem(ite, inventory);
                    } else {
                        tprintln("Not a valid traveler.");
                    }
                }
            } else if (in.matches("^(unequip(\\s+)(.+)(\\s+)(\\d+)(\\s*))$")) { //unequip item_name trav#
                tprintln("");
                temp = team.get(Integer.parseInt(in.split("\\s+")[2]));
                if (temp != null) {
                    ite = temp.unequipItem((in.split("\\s+")[1].replace("_", " ")));
                    if (ite != null) {
                        inventory.addItem(ite);
                    } else {
                        tprintln("Not a valid item.");
                    }
                } else {
                    tprintln("Not a valid traveler.");
                }
            } else if (in.matches("^(consume(\\s+)(.+)(\\s+)(\\d+)(\\s*))$")) { //consume item_name trav#
                tprintln("");
                temp = team.get(Integer.parseInt(in.split("\\s+")[2]));
                if (temp != null) {
                    ite = inventory.removeItem(in.split("\\s+")[1].replace("_", " "));
                    if (ite != null) {
                        temp.consumeItem(ite, false);
                    }
                } else {
                    tprintln("Not a valid traveler.");
                }
            } else if (in.matches("^(damage(\\s+)(\\d+)(\\s+)(\\d+)(\\s*))$")) { //damage damage# trav#
                tprintln("");
                temp = team.get(Integer.parseInt(in.split("\\s+")[2]));
                if (temp != null) {
                    temp.takeDamage(Integer.parseInt(in.split("\\s+")[1].replace("_", " ")), Type.Normal, false);
                } else {
                    tprintln("Not a valid traveler.");
                }
            } else if (in.matches("^(spend(\\s+)(\\d+)(\\s+)(\\d+)(\\s*))$")) { //spend spend# trav#
                tprintln("");
                temp = team.get(Integer.parseInt(in.split("\\s+")[2]));
                if (temp != null) {
                    temp.spendEnergy(Integer.parseInt(in.split("\\s+")[1].replace("_", " ")), false);
                } else {
                    tprintln("Not a valid traveler.");
                }
            } else if (in.matches("^(listitems|li)(\\s*)$")) {
                Item.printItems();
            } else {
                print("\n");
                printTab();
                println("Your input was misunderstood. Type >? or >help to view the manual.");
            }

            //end of while(menu)
        } // end of menu()
    }
    //Prints help menu.

    private void printHelp() {
        print("\n");
        printTab();
        println("HELP: >quit to exit the game.");
        printTab();
        tprintln(" >travelers (or t) to view the travelers in your expedition.");
        printTab();
        tprintln(" >trav travelernumber | view all information about one traveler in your expedition.");
        printTab();
        tprintln(" >sleep (or s) to rest your travelers for the night (-s to skip animation)");
        printTab();
        tprintln(" >date (or d) to view what day it is.");
        printTab();
        tprintln(" >weather to view what the environment outside is like.");
        printTab();
        tprintln(" >pfe (or p or embark) to toggle preparing tonight for morning embark.");
        printTab();
        tprintln(" >inventory (or i or items) to take inventory.");
        tprintln("TEST: >additem (or ai) itemcode quantity | add items to your inventory. (quantity is optional)");
        printTab();
        tprintln(" >removeitem (or ri) itemcode quantity | remove items from your inventory. (quantity is optional)");
        printTab();
        tprintln(" >test | run the test method");
        printTab();
        tprintln(" >exp travnumber num | add num exp to traveler travnumber.");
    }

    //Triggers a travel.
    private void travel() throws InterruptedException {
        print("\n");
        printTab();
        print("Your travelers wake up early before dawn and hitch up their wagon for travel");
        ellipsis(3);
        Thread.sleep((textSpeed * 6) * 100);
        print("\n");
        switch (d2(3)) {
            case 1: //stay in same biome
                printTab();
                println("They travel all morning until they grow tired and find a good place to stop.");
                break;
            case 2: //new biome
                Biome prevBiome = env.getBiome();
                env.switchBiome();
                printTab();
                print("After a long morning's worth of travel, ");
                switch (prevBiome) {
                    case Grasslands:
                        print("they find the edge of the grasslands and ");
                        break;
                    case Woodlands:
                        print("they notice the trees of the woodlands growing further and further apart, and ");
                        break;
                    case Tundra:
                        print("they notice the icy permafrost appears to thaw and ");
                        break;
                    case Desert:
                        print("they notice the rolling dunes getting shorter and shorter and ");
                        break;
                    case Mountains:
                        print("they reach the base of the last rocky hill and ");
                        break;
                }
                switch (env.getBiome()) {
                    case Grasslands:
                        println("reach a wide expanse of tall grass devoid of any big rocks or trees.");
                        break;
                    case Woodlands:
                        println("reach the outer edge of a massive forest.");
                        break;
                    case Tundra:
                        println("feel an arctic chill as they reach a flat tundra covered in permafrost.");
                        break;
                    case Desert:
                        println("reach the outer edge of an expanse of sandy desert.");
                        break;
                    case Mountains:
                        println("reach the base of a massive mountain range.");
                        break;
                }

        }
        Thread.sleep((textSpeed * 6) * 100);
        printTab();
        print("They come to rest their caravan ");
        switch (env.getBiome()) {
            case Grasslands:
                println("on a nice flat area of shorter grass.");
                break;
            case Woodlands:
                println("in a flat clearing they found in the woods.");
                break;
            case Tundra:
                println("on an outcrop of granite protruding from the icy tundra.");
                break;
            case Desert:
                println("at the base of a particularly tall sand dune, providing them with ample shade.");
                break;
            case Mountains:
                println("on the flattest outcrop of stone they could find on the rocky mountain path.");
                break;

        }
        Thread.sleep((textSpeed * 6) * 100);

    }

    //Prints a sleep animation.
    private void printSleep() throws InterruptedException {
        print("\n");
        printTab();
        print("Your travelers turn in for the night as the sunset falls over the horizon");
        ellipsis(3);
        println("\n");
        printTab();
        slowPrintln("              .");
        slowPrintln("");
        printTab();
        slowPrintln("              |");
        printTab();
        slowPrintln("     .               /");
        printTab();
        slowPrintln("      \\       I");
        printTab();
        slowPrintln("                  /");
        printTab();
        slowPrintln("        \\  ,g88R_");
        printTab();
        slowPrintln("          d888(`  ).");
        printTab();
        slowPrintln(" -  --==  888(     ).=--           .+(`  )`.");
        printTab();
        slowPrintln(")         Y8P(       '`.          :(   .    )");
        printTab();
        slowPrintln("        .+(`(      .   )     .--  `.  (    ) )");
        printTab();
        slowPrintln("       ((    (..__.:'-'   .+(   )   ` _`  ) )");
        printTab();
        slowPrintln("`.     `(       ) )       (   .  )     (   )  ._ ");
        printTab();
        slowPrintln("  )      ` __.:'   )     (   (   ))     `-'.-(`  )");
        printTab();
        slowPrintln(")  )  ( )       --'       `- __.'         :(      ))");
        printTab();
        slowPrintln(".-'  (_.'          .')                    `(    )  ))");
        printTab();
        slowPrintln("                  (_  )                     ` __.:'");
        slowPrintln("");
        printTab();
        slowPrintln("--..,___.--,--'`,---..-.--+--.,,-,,..._.--..-._.-a:f--.");
        Thread.sleep((textSpeed + 6) * 100);

    }

    //Prints an environment.
    private void printEnvironment(Environment in) {
        print("\n");
        println(in.descriptionOfEnvironment());
    }

    //Prints the current date.
    private void printDate() {
        print("\n");
        printTab();
        println("It's " + date + ".");
    }

    private Inventory other = new Inventory("Their inventory");

    private void initShop() {
        other.addItem(0, 9);
        other.addItem(1, 2);
        other.addItem(2, 6);
        other.addItem(3, 4);
        other.addItem(4, 2);
        other.addItem(5, 9);
        other.addItem(6, 2);
        other.addItem(7, 6);
        other.addItem(8, 4);
        other.addItem(9, 2);
        other.addItem(10, 9);
    }

    private void tester() throws InterruptedException {
        Encounter en = new Encounter(team, enemies, date, scan);
    }

    private void tester2() throws InterruptedException {
        Exchange ex = new Exchange(inventory, other, false, scan);
    }

    private void addExperience(int which, int exp) {
        Character charac = team.get(which);
        charac.addExperience(exp);
    }

}
