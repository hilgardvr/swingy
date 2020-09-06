package com.gmail.hilgardvr.swingy.controller;

import com.gmail.hilgardvr.swingy.model.characters.*;
import com.gmail.hilgardvr.swingy.logger.*;
import com.gmail.hilgardvr.swingy.model.artifacts.*;
import com.gmail.hilgardvr.swingy.view.*;

import lombok.*;

import java.io.*;
import java.util.*;

@Getter
@Setter
public class GameLauncher {
    private ArrayList<Hero> heroList = new ArrayList<Hero>();
    Hero userHero;
    LaunchConsoleView lcv;
    GuiGameLauncher ggl;

    public GameLauncher() {
        lcv = new LaunchConsoleView();
        loadGameData();
        loadOrCreate();
        startGame();
        //saveData();
    }
    
    public GameLauncher(LaunchGui lgui) {
        lgui.setVisible(true);
        loadGameData();
        ggl = new GuiGameLauncher(lgui, heroList);
        //saveData();
    }

    private void createNew () {
        this.userHero = this.lcv.createNew();
        this.heroList.add(userHero);
    }

    public void loadGameData() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("gameData.txt"));
            String line;
            while ( (line = reader.readLine()) != null) {
                String split[] = line.split(",");
                HeroBuilder heroBuild = new HeroBuilder(split[0], CharacterType.valueOf(split[1]), WeaponType.valueOf(split[2]),
                    ArmorType.valueOf(split[3]), HelmType.valueOf(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6]));
                Hero hero = heroBuild.getHero();
                this.heroList.add(hero);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occured reading the file:");
            e.printStackTrace();
        }
    }

    public void loadOrCreate() {
        int userChoice = this.lcv.loadOrCreate();

        if (userChoice == 1) {
            this.createNew();
        } else { // if (userChoice == 2)
            userChoice = this.lcv.getHero(this.heroList);
            this.userHero = this.heroList.get(userChoice);
        }
    }

    public void startGame() {
        Game game = new Game(this.userHero, heroList);
        game.playGame();
    }

    public void saveData() {
        Logger.createFile();
        for (Hero hero : heroList) {
            Logger.writeToFile(hero.getName() + "," + hero.getCharacterClass() + "," + hero.getWeapon().getWeaponType() + "," + hero.getArmor().getArmorType()
                + "," + hero.getHelm().getHelmType() + "," + hero.getLevel() + "," + hero.getExperience() + "\n");
        }
    }
}