package expedition.game_tools;

import static expedition.Expedition.*;
import expedition.Other;
import static expedition.Other.*;
import expedition.game_tools.character_tools.Ability;
import expedition.game_tools.character_tools.Team;
import expedition.game_tools.character_tools.Character;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Encounter {

	private final Team player;
	private final Team other;
	private final Date date;
	private ArrayList<Character> temp;
	private ArrayList<Character> playOrder;
	private Character currentChar;
	private final int padding = 20;
	private Scanner scan;

	private boolean exit;

	public Encounter(Team player, Team other, Date date, Scanner scan) throws InterruptedException {
		this.player = player;
		this.other = other;
		this.scan = scan;
		this.date = date;
		exit = false;
		if (other.size() > 0 && player.size() > 0) {
			playOrder = new ArrayList<>();
			temp = new ArrayList<>();
			for (Character x : player.list()) {
				playOrder.add(x);
			}
			for (Character x : other.list()) {
				playOrder.add(x);
			}
			fight();
		}
	}

	public void fight() throws InterruptedException {
		print("\n");
		tprintln("Encounter: " + player.getName() + " v.s. " + other.getName() + ".");
		while (playable()) { // while both teams have surviving players.

			while (playOrder.size() > 0 && playable()) { // Action round.
				speedSort(playOrder); // Sort remaining players by speed.

				// Pop the quickest player in the round and add them to the post-queue
				currentChar = playOrder.remove(0);

				currentChar.statusTickCombat();
				// If they're dead, skip the action.
				if (checkDeath(currentChar)) {
					continue;
				} else {

				}
				// Let that character act.
				act(currentChar, player.contains(currentChar));

				if (exit) {
					break;
				}

			}

			if (exit) {
				break;
			}

			System.out.println("Round finished! ");
			for (Character i : temp) {
				playOrder.add(i);
			}
			for (Character i : playOrder) {
				temp.remove(i);
			}

		} // a team ran out of surviving players.
		if (exit) {
			error("User has ended the fight, nobody wins!");
		} else {
			error((player.hasConsciousMembers() ? player.getName() : other.getName()) + " has won the battle!");
		}
	}

	public void act(Character c, Boolean player) throws InterruptedException {
		c.statusTickCombat();
		if (player) {
			// player action
			playerMenu();
			Thread.sleep(1000);
		} else {
			// ai action
			br();
			tprintln("AI : " + c.getName() + "'s turn! They have " + c.getSpeed() + " speed.");
			Thread.sleep(1000);
		}
	}

	public boolean checkDeath(Character c) {
		if (c.isFallen() && (player.contains(c) || other.contains(c))) {
			playOrder.remove(c);
			temp.remove(c);
			error(c.getName() + " has fallen unconscious!");
			return true;
		}
		return false;
	}

	public void playerMenu() throws InterruptedException {
		String in;
		int intIn;
		boolean menu = true;
		while (menu) { // t | p | s | q | d | i
			printFight(currentChar);
			print("\n---\n");
			print("#" + (player.list().indexOf(currentChar) + 1) + ": " + currentChar.getName() + " ");
			Character ch;
			in = input(scan).toLowerCase();
			if (in.matches("^\\?|help$")) {
				printHelp();
			} else if (in.matches("^(trav|t)(\\s+)(\\d+)$")) {
				ch = player.get(Integer.parseInt(in.split("\\s+")[1]));
				if (ch != null) {
					ch.printTravelerVerbose(date);
				} else {
					tprintln("No such traveler.");
				}
			} else if (in.matches("^^(view|v)(\\s*)$")) { // fight
				br();
				printFight(currentChar);
			} else if (in.matches("^^(item|i)(\\s*)$")) { // item
				if (item()) {
					menu = false;
					temp.add(currentChar); // turn end
				}
			} else if (in.matches("^(ability|a)(\\s*)$")) { // abilities
				// if (ability()) {
				// menu = false;
				// temp.add(currentChar); // turn end
				// }
			} else if (in.matches("^(run|r)(\\s*)$")) { // abilities
				br();
				if (run()) {
					menu = false;
					// temp.add(currentChar); this is called in unsuccessfulRun(), turn end
				}
			} else if (in.matches("^(q|quit|exit)(\\s*)$")) { // abilities
				menu = false;
				executiveExit();
			} else {
				br();
				printTab();
				println("Your input was misunderstood. Type >? or >help to view the manual.");
			}
		}
	}

	private void executiveExit() {
		exit = true;
	}

	private boolean ability() throws InterruptedException {
		br();
		String in;
		int input;
		boolean menu = true;
		currentChar.printAbilities();
		while (menu) {

			tprintln("Which ability would you like to use? (q/quit/exit to return)");
			print("#" + (player.list().indexOf(currentChar) + 1) + ": " + currentChar.getName() + " > Abilities >");
			in = input(scan);

			if (in.matches("^(q|quit|exit)(\\s*)$")) {
				return false;
			} else if (in.matches("^(\\d)(\\s*)$")) {

				input = Integer.parseInt(in.substring(0, 1));
				if ((input < 1) || (input > 4) || (currentChar.getAbilities()[input - 1] == null)) {
					tprintln("That isn't an ability... Try again!");

				} else {
					Ability abil = currentChar.getAbilities()[input - 1]; // ABILITY CHOSEN
					// pick target now
					boolean menu2 = true;

					while (menu2) {
						if (abil.isTargeted()) {

							tprintln("Who do you want to use " + abil.getName() +" on? (q/quit/exit to return)");
							print("#" + (player.list().indexOf(currentChar) + 1) + ": " + currentChar.getName()
									+ " > Abilities >");
							in = input(scan);
							if (in.matches("^(q|quit|exit)(\\s*)$")) {
								return false;
							} else if (in.matches("^(\\d)(\\s*)$")) {

								input = Integer.parseInt(in.substring(0, 1));
								if ((input < 1) || (input > 4) || (currentChar.getAbilities()[input - 1] == null)) {
									tprintln("That isn't an ability... Try again!");

								} else {

								}
								// do the thing
							} else {
								error("Your input was misunderstood. Type the number of a target, or 'quit' to back out.");
							}
						} else { //untargeted ability
							
						}
					}
				}

			} else {
				error("Your input was misunderstood. Type the number of an ability, or 'quit' to back out.");
				enter(scan);
			}
		}
		return false;

	}

	private boolean run() throws InterruptedException {
		int otherTeamSpeed = averageSpeedOfOpposingTeam();
		tprintln(currentChar.getName() + "'s speed is " + currentChar.getStatSpeed() + ", and the enemy's average is "
				+ otherTeamSpeed + ". Are you sure you want to try to run?");
		if (inputAffirm(scan)) { // yes run
			if (currentChar.getStatSpeed() > otherTeamSpeed) { // If current character's speed is higher than the
																// enemy's average, run away is successful.
				successfulRun();
			} else {
				HashMap<Boolean, Integer> a = new HashMap<>(); // If current character's speed is less, chance is lower
																// (odds are char's speed to enemy's average)
				a.put(true, (currentChar.getStatSpeed() * 2));
				a.put(false, otherTeamSpeed);
				if (Other.pickRandom(a)) {
					successfulRun();
				} else {
					unsuccessfulRun();
				}
			}
			return true;
		} else { // don't run
			return false;
		}
	}

	private void successfulRun() throws InterruptedException {
		br();
		tprintln(currentChar.getName() + " manages to run away from the fight!");
		enter(scan);
		Team t = (player.contains(currentChar) ? player : other);
		t.removeTraveler(currentChar);
	}

	private void unsuccessfulRun() throws InterruptedException {
		br();
		tprintln(currentChar.getName() + " fails to run away! They remain in the fight.");
		enter(scan);
		temp.add(currentChar);
	}

	private int averageSpeedOfOpposingTeam() {
		int total = 0;
		Team opposite = (player.contains(currentChar) ? other : player);
		for (Character i : opposite.list()) {
			total += i.getStatSpeed();
		}
		return total /= opposite.size();
	}

	private boolean item() throws InterruptedException {
		if (currentChar.hasUsableItem()) {
			tprintln(currentChar.getName() + " is holding " + "[" + currentChar.getEquippedConsumable() + "]" + " : "
					+ currentChar.getEquippedConsumable().getDescription());
			tprintln("Would you like to use it?");
			if (inputAffirm(scan)) {
				boolean ex = currentChar.useEquippedItem();
				enter(scan);
				return ex;
			}
		} else {
			tprintln(currentChar.getName() + " doesn't have a usable item.");
			enter(scan);
		}
		return false;
	}

	public void printHelp() {
		br();
		printTab();
		println("HELP: >view (or v) to view the members of the fight.");
		printTab();
		tprintln(" >trav traveler# | view all information about one of your travelers.");
		printTab();
		tprintln(" >ability (or a) | to choose an ability to use.");
		printTab();
		tprintln(" >item (or i) | to use an equipped item.");
		printTab();
		tprintln(" >run (or r) | to attempt to run.");
	}

	public void printFight(Character current) {
		br();
		int iter = 1;
		ArrayList<Character> comboOrder = new ArrayList<>();
		speedSort(playOrder);
		for (Character p : playOrder) {
			comboOrder.add(p);
		}
		for (Character p : temp) {
			comboOrder.add(p);
		}

		tprintln("- " + player.getName() + " ---- ");
		for (Character p : player.list()) {
			tprintln("#" + (player.list().indexOf(p) + 1) + ": " + p.getName() + " - Level " + p.getLevel() + " "
					+ p.getType() + "     Turns Until Play: (" + comboOrder.indexOf(p) + ")");
			tprintln("    Health: " + p.getHealthCurr() + "/" + p.getHealthMax() + " | Energy: " + p.getEnergyCurr()
					+ "/" + p.getEnergyMax() + "");
			tprintln("");
		}

		tprintln("- " + other.getName() + " ---- ");
		for (Character p : other.list()) {
			tprintln("#" + (other.list().indexOf(p) + 1 + player.list().size()) + ": " + p.getName() + " - Level "
					+ p.getLevel() + " " + p.getType() + "     Turns Until Play: (" + comboOrder.indexOf(p) + ")");
			tprintln("    Health: " + p.getHealthCurr() + "/" + p.getHealthMax() + " | Energy: " + p.getEnergyCurr()
					+ "/" + p.getEnergyMax() + "");
			tprintln("");
		}
	}

	public boolean playable() {
		return player.hasConsciousMembers() && other.hasConsciousMembers();
	}

	public static void speedSort(ArrayList<Character> arr) {
		int n = arr.size();
		int k;
		for (int m = n; m >= 0; m--) {
			for (int i = 0; i < n - 1; i++) {
				k = i + 1;
				if (arr.get(i).getSpeed() < arr.get(k).getSpeed()) {
					swapIndex(i, k, arr);
				}
			}
		}
	}

	public static void swapIndex(int i, int j, ArrayList<Character> arr) {
		Character c;
		c = arr.get(i);
		arr.set(i, arr.get(j));
		arr.set(j, c);
	}

}
