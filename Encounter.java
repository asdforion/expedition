/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expedition.game_tools;

import expedition.game_tools.character_tools.Team;

/**
 *
 * @author J76785
 */
public class Encounter {

    private final Team player;
    private final Team other;
    private final int padding = 20;

    public Encounter(Team player, Team other) {
        this.player = player;
        this.other = other;
    }

    public void fight() {
        boolean fight = true;
        while (playable()) {
            if (!fight) {
                break;
            }
            
            
        }
    }

    public boolean playable() {
        return player.hasConsciousMembers() && other.hasConsciousMembers();
    }

}
