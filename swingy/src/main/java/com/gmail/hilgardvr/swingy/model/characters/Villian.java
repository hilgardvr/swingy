package com.gmail.hilgardvr.swingy.model.characters;

import java.util.Random;

public class Villian extends Character {
    String names[];
    Random rand;
    boolean dropArtifact;
    int attackArr[];
    int defenceArr[];
    int hitArr[];

    public Villian(Hero hero) {
        names = new String[] { "Troll", "Monster", "Arsenal Supporter" };
        this.attackArr = new int[] { 30, 25, 35 };
        this.defenceArr = new int[] { 10, 12, 8 };
        this.hitArr = new int[] { 100, 120, 80 };
    }

    @Override
    public String toString() {
        return (this.getName() + "\n\tlevel: " + this.getLevel() + "\n\tattack: " + this.getAttack() + "\n\tdefence: "
                + this.getDefence() + "\n\thit points: " + this.getHitPoints());
    }

    public void setStats(int level) {
        rand = new Random();
        int n = rand.nextInt(3);
        int temp = rand.nextInt(3);
        if (n == 2) {
            dropArtifact = true;
        }
        n = rand.nextInt(3);
        this.setName(names[n]);
        this.setLevel(level + temp);
        this.setAttack((int)(attackArr[n] * Math.pow(1.09, this.getLevel())));
        this.setDefence((int)(defenceArr[n] * Math.pow(1.09, this.getLevel())));
        this.setHitPoints((int)(hitArr[n] * Math.pow(1.09, this.getLevel())));
    }

    public boolean getDropArtifact() {
        return this.dropArtifact;
    }
}