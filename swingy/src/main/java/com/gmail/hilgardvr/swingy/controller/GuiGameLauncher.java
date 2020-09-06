/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.hilgardvr.swingy.controller;

import com.gmail.hilgardvr.swingy.logger.Logger;
import com.gmail.hilgardvr.swingy.model.artifacts.*;
import com.gmail.hilgardvr.swingy.model.characters.*;
import com.gmail.hilgardvr.swingy.view.*;
import java.util.ArrayList;
import java.awt.event.*;

/**
 *
 * @author hilgard
 */

public class GuiGameLauncher {
    private Game game;
    private LaunchGui lgui;
    private GetNewHero gnh;
    private ArrayList<Hero> heroList;
    private Hero userHero;
    private LoadHero lh;
    private GameGui gameGui;

    public GuiGameLauncher(LaunchGui lgui, ArrayList<Hero> heroes) {
        this.lgui = lgui;
        heroList = heroes;
        lgui.addNewHeroListener(new CreateNewHeroListener());
        lgui.addLoadHeroListener(new LoadHeroListener());
    }
    
    class LoadHeroListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            lgui.dispose();
            lh = new LoadHero(heroList);
            lh.addSubmitListener(new SelectHeroListener());
            lh.setVisible(true);
        }
    }
    
    class SelectHeroListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            userHero = lh.getBoxHero();
            lh.dispose();
            startGame();
        }
    }
    
    class CreateNewHeroListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (lgui != null)
                lgui.dispose();
            gnh = new GetNewHero();
            gnh.setVisible(true);
            gnh.addNewHeroListener(new NewHeroDataListener());
            return;
        }
    }
    
    class NewHeroDataListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = gnh.getName();
            CharacterType t = gnh.getCharType();
            WeaponType w;
            if (t == CharacterType.KNIGHT) {
                w = WeaponType.DAGGER;
            } else {
                w = WeaponType.SHORTBOW;
            }
            HeroBuilder heroBuild = new HeroBuilder(name, t, w, 
            ArmorType.CHAINMAIL, HelmType.BICYCLEHELMET, 1, 0);
            Hero hero = heroBuild.getHero();
            if (!heroBuild.validate(hero)) {
                gnh.dispose();
                gnh = new GetNewHero();
                gnh.setVisible(true);
                gnh.setHeadLabelText("Error: Hero name must be between 2-31 characters");
                gnh.addNewHeroListener(new NewHeroDataListener());
                return;
            }
            userHero = hero;
            heroList.add(hero);
            gnh.dispose();
            startGame();
        }
    }
    
    private void startGame() {
        gameGui = new GameGui(userHero);
        game = new Game(userHero, gameGui, heroList);
    }
}
