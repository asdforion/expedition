package expedition.game_tools;

import static expedition.Other.*;
import expedition.game_tools.character_tools.Team;
import expedition.game_tools.character_tools.Character;
import java.util.ArrayList;

public class Encounter {

	private final Team player;
	private final Team other;
	private ArrayList<Character> temp;
	private ArrayList<Character> playOrder;
	private final int padding = 20;

	public Encounter(Team player, Team other) {
		this.player = player;
		this.other = other;
		if (other.size() > 0 && player.size() > 0) {
			playOrder = new ArrayList<Character>();
			temp = new ArrayList<Character>();
			for (Character x : player.list()) {
				playOrder.add(x);
			}
			for (Character x : other.list()) {
				playOrder.add(x);
			}
			fight();
		}
	}

	public void fight() {
		tprintln("Encounter: " + player.getName() + " v.s. " + other.getName() + ".");
		while (playable()) { // while both teams have surviving players.

			while (playOrder.size() > 0 && playable()) { // Action round.
				speedSort(playOrder); // Sort remaining players by speed.

				Character turn = playOrder.remove(0);
				if(turn.isFallen()) {
					continue;
				}
				// Pop the quickest character left in the round and let them act.
				act(turn, player.contains(turn));
				// After their action, push the character into temp.
				temp.add(turn);

			}
			System.out.println("Round finished! ");

		} // a team ran out of surviving players.
	}

	public void act(Character a, Boolean player) {
		if (player) {
			// player action
			tprintln("YOU: " + a.getName() + "'s turn! They have " + a.getSpeed() + " speed.");
		} else {
			// ai action
			tprintln("AI : " + a.getName() + "'s turn! They have " + a.getSpeed() + " speed.");
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
