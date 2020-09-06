package com.gmail.hilgardvr.swingy.view;

import com.gmail.hilgardvr.swingy.model.characters.*;
import com.gmail.hilgardvr.swingy.model.artifacts.*;
import com.gmail.hilgardvr.swingy.model.Directions;

import java.util.*;

public class LaunchConsoleView {
    Scanner sc;

    public LaunchConsoleView() {
        this.sc = new Scanner(System.in);
    }

    public int loadOrCreate() {
        int loadOrSave = 0;

        System.out.println("1. Create new hero");
        System.out.println("2. Load saved hero");
        System.out.print("Please enter choice: ");
        
        String temp = this.sc.nextLine();
        try {
            loadOrSave = Integer.parseInt(temp);
            if (loadOrSave < 1 || loadOrSave > 2) {
                System.out.println("Invalid choice - please try again");
                return loadOrCreate();
            }
            else {
                return loadOrSave;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice - please try again");
            return loadOrCreate();
        }
    }

    public int getHero(ArrayList<Hero> heroList) {
        int ctr = 0;
        for (Hero hero : heroList) {
            System.out.print(1 + ctr + ". ");
            System.out.println(hero);
            ctr++;
        }
        System.out.println("Please choose hero: ");
        try {
            int heroChoice = Integer.parseInt(sc.nextLine()) - 1;
            if (heroChoice < 0 || heroChoice >= heroList.size()) {
                System.out.println("Invalid choice - try again");
                return getHero(heroList);
            } else {
                return heroChoice;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice - try again");
            return getHero(heroList);
        }
    }

    public Hero createNew() {
        String name = null;
        int ctr = 0;
        int charType = -1;
        System.out.println("Please enter the name of the hero: ");
        name = this.sc.nextLine();
        for (CharacterType t : Arrays.asList(CharacterType.values())) {
            ctr++;
            System.out.println(ctr + " " + t);
        }
        System.out.println("Please choose a character type: ");
        try {
            charType = Integer.parseInt(this.sc.nextLine()) - 1;
            if (charType < 0 || charType > 1) {
                System.out.println("Invalid choice - please try again");
                return createNew();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice - please try again");
            return createNew();
        }
        
        HeroBuilder heroBuild = new HeroBuilder(name, CharacterType.values()[charType], WeaponType.values()[charType], 
            ArmorType.CHAINMAIL, HelmType.BICYCLEHELMET, 1, 0);
        if (!heroBuild.validate(heroBuild.getHero())) {
            System.out.println("Please try again");
            heroBuild = null;
            return createNew();
        }
        return heroBuild.getHero(); 
    }

    public void printMap(int mapSize) {
        int coord = mapSize / 2 ;
        System.out.println("Map: " + coord + "°N " + coord + "°W to " + coord + "°S " + coord + "°E");
    }

    public void printHeroLoc(Hero hero, int mapSize) {
        int eastWest = hero.getXLoc() - mapSize / 2;
        int northSouth = hero.getYLoc() - mapSize / 2;
        System.out.print("Your location is: ");
        if (northSouth >= 0) {
            System.out.print(northSouth + "°N ");
        } else {
            System.out.print(Math.abs(northSouth) + "°S ");
        }
        if (eastWest >= 0) {
            System.out.print(eastWest + "°E ");
        } else {
            System.out.print(Math.abs(eastWest) + "°W ");
        }
        System.out.println();
    }

    public int getDirectionChoice() {
        int ctr = 0;
        int choice = 0;
        for (Directions d : Arrays.asList(Directions.values())) {
            ctr++;
            System.out.println(ctr + " " + d);
        }
        System.out.println("Please enter choice: ");
        try {
            choice = Integer.parseInt(this.sc.nextLine());
        } catch (Exception ignException){}
        return (choice);
    }

    public void printEndGame() {
        System.out.println("Game over");
    }

    public boolean fight(Hero hero, Villian villian) {
        String fight = null;
        System.out.println("Your stats: ");
        System.out.println(hero);
        System.out.println("You have encountered: ");
        System.out.println(villian);
        System.out.print("(F)ight or (R)un: ");
        while (fight == null || (!fight.toUpperCase().equals("F") && !fight.toUpperCase().equals("R"))) {
            System.out.println("F or R?");
            fight = this.sc.nextLine();
        }
        if (fight.toUpperCase().equals("F")) {
            System.out.println("Fighting");
            return true;
        }
        return false;
    }

    public void toOutput(String s) {
        System.out.println(s);
    }

    public void toOutput() {
        System.out.println();
    }

    public void finalize() {
        this.sc.close();
    }
}