package com.gmail.hilgardvr.swingy.controller;

import java.util.*;
import java.awt.event.*;

import com.gmail.hilgardvr.swingy.logger.*;
import com.gmail.hilgardvr.swingy.model.characters.*;
import com.gmail.hilgardvr.swingy.view.*;
import com.gmail.hilgardvr.swingy.model.artifacts.*;
import com.gmail.hilgardvr.swingy.model.Directions;

class Game {
    private ArrayList<Villian> gameChars;
    private ArrayList<Hero> heroList;
    private Hero hero;
    private int mapSize;
    private LaunchConsoleView cv;
    private GameGui gameGui;
    Villian vToRemove;
    private int prevX;
    private int prevY;

    //console constructor
    Game(Hero hero, ArrayList<Hero> heroList) {
        this.heroList = heroList;
        int lvl = hero.getLevel();
        mapSize = (lvl - 1) * 5 + 10 - (lvl % 2);
        int coord = mapSize / 2;
        cv = new LaunchConsoleView();
        hero.setXLoc(coord);
        hero.setYLoc(coord);
        this.hero = hero;
        cv.printMap(mapSize);
        initMap(lvl);
        cv.printHeroLoc(hero, mapSize);
    }

    //gui constructor
    Game(Hero hero, GameGui gameGui, ArrayList<Hero> heroList) {
        int lvl = hero.getLevel();
        mapSize = (lvl - 1) * 5 + 10 - (lvl % 2);
        int coord = mapSize / 2;
        hero.setXLoc(coord);
        hero.setYLoc(coord);
        this.hero = hero;
        this.gameGui = gameGui;
        initMap(lvl);
        this.heroList = heroList;
        gameGui.addMoveListener(new MoveListener());
        gameGui.addFightListener(new FightListener());
        gameGui.addRunListener(new RunListener());
        gameGui.setVisible(true);
        gameGui.setLocationField(guiHeroLoc(hero, mapSize));
        gameGui.setHeroStatsField(hero.toString());
        gameGui.disableButtons();
        gameGui.enableBox();
    }

    class MoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            gameGui.setFightResultsFields("");
            prevX = hero.getXLoc();
            prevY = hero.getYLoc();
            Directions d = Directions.valueOf(gameGui.getDirectionBox());
            switch (d) {
                case NORTH:
                    hero.setYLoc(hero.getYLoc() + 1);
                    break;
                case SOUTH:
                    hero.setYLoc(hero.getYLoc() - 1);
                    break;
                case EAST:
                    hero.setXLoc(hero.getXLoc() + 1);
                    break;
                case WEST:
                    hero.setXLoc(hero.getXLoc() - 1);
                    break;
                default:
                    break;
            }

            //check for end of game
            int mapToArr = mapSize / 2;
            int x = Math.abs(hero.getXLoc() - mapToArr);
            int y = Math.abs(hero.getYLoc() - mapToArr);
            if (x > mapToArr || y > mapToArr) {
                gameGui.setHeroStatsField("Your Reached The Border Of The Map\n\n\tGame Over");
                gameGui.disableButtons();
                gameGui.enableBox();
                return;
            }

            //update hero location in gui
            gameGui.setEnemyStatsField("");
            gameGui.setLocationField(guiHeroLoc(hero, mapSize));
            
            //get new hero location for checking against villians
            int heroX = hero.getXLoc();
            int heroY = hero.getYLoc();
            
            
            for (Villian v : gameChars) {
                if (v.getXLoc() == heroX && v.getYLoc() == heroY) {
                    v.setStats(hero.getLevel());
                    gameGui.setEnemyStatsField(v.toString());
                    vToRemove = v;
                    gameGui.enableButtons();
                    gameGui.disableBox();
                }
            }
        }
    }
    
    class FightListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //System.out.println("fight");
            if (guiFightSim()) {
                gameGui.setFightResultsFields(gameGui.getFightResultsField() + "\nYou Won The Fight\n");
            } else {
                gameGui.setFightResultsFields(gameGui.getFightResultsField() + "\nYou Lost The Fight\n\n*** Game Over ***");
                gameGui.disableBox();
                gameGui.disableButtons();
            }
            saveData();
        }
    }
    
    class RunListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Random random = new Random();
            int ff = random.nextInt(2);
            if (ff == 1) {
                gameGui.setFightResultsFields("Unlucky You Have To Fight\n");
                if (guiFightSim()) {
                    gameGui.setFightResultsFields(gameGui.getFightResultsField() + "\nYou Won The Fight\n");
                } else {
                    gameGui.setFightResultsFields(gameGui.getFightResultsField() + "\nYou Lost The Fight\n\n*** Game Over ***");
                    gameGui.disableBox();
                    gameGui.disableButtons();
                }
                saveData();
            } else {
                gameGui.setFightResultsFields("You have successfully run away\n");
                hero.setXLoc(prevX);
                hero.setYLoc(prevY);
                gameGui.setLocationField(guiHeroLoc(hero, mapSize));
                gameGui.enableBox();
                gameGui.disableButtons();
            }
        }
    }
    
    private void setMapSizeField(String str) {
        gameGui.setMapSizeField(str);
    }

    private void initMap(int lvl) {
        int mapBefore = mapSize;
        mapSize = (lvl - 1) * 5 + 10 - (lvl % 2);
        int x = hero.getXLoc() + (mapSize - mapBefore) / 2;
        int y = hero.getYLoc() + (mapSize - mapBefore) / 2;
        hero.setXLoc(x);
        hero.setYLoc(y);
        placeVillians();
        if (gameGui != null) {
            int coord = mapSize / 2 ;
            setMapSizeField(coord + "°N " + coord + "°W to " + coord + "°S " + coord + "°E");
        }
    }

    private boolean checkLocation(int x, int y) {
        for (Villian v : gameChars) {
            if (v.getXLoc() == x && v.getYLoc() == y) {
                return false;
            } else if (hero.getXLoc() == x && hero.getYLoc() == y) {
                return false;
            }
        }
        return true;
    }

    private void placeVillians() {
        gameChars = new ArrayList<Villian>();
        Random rand = new Random();
        int numVillians = mapSize * mapSize / 3;
        int ctr = 0;
        while (ctr < numVillians) {
            int x = rand.nextInt(mapSize);
            int y = rand.nextInt(mapSize);
            if (checkLocation(x, y)) {
                Villian newVil = new Villian(hero);
                newVil.setXLoc(x);
                newVil.setYLoc(y);
                gameChars.add(newVil);
                ctr++;
            }
        }
    }

    private boolean fightSim(Villian v) {
        if (v.getHitPoints() <= 0 ) {
            return true;
        }
        int heroHit = hero.getHitPoints();
        Random rand = new Random();
        while (heroHit > 0 && v.getHitPoints() > 0) {
            int heroAttack = hero.getAttack();
            int hRand = rand.nextInt(heroAttack) - (heroAttack / 3);
            int damage = hero.getAttack() + hRand - v.getDefence();
            v.setHitPoints(v.getHitPoints() - damage);
            cv.toOutput("Hero hits for: " + damage);
            if (v.getHitPoints() <= 0) {
                v.setHitPoints(0);
                cv.toOutput("Villian hit points: " + v.getHitPoints());
                return true;
            }
            cv.toOutput("Villian hit points: " + v.getHitPoints());
            int vAttack = v.getAttack();
            int vRand = rand.nextInt(vAttack) - (vAttack / 3);
            damage = v.getAttack() + vRand - hero.getDefence();
            heroHit -= damage;
            cv.toOutput("Villian hits for: " + damage);
            cv.toOutput("Hero hit points: " + heroHit);
        }
        return false;
    }

    private void checkForArtifact(Villian v) {
        if (v.getDropArtifact()) {
            Random rand = new Random();
            int arti = rand.nextInt(3);
            switch (arti) {
                case 0:
                    arti = rand.nextInt(3);
                    WeaponType w = WeaponType.values()[arti];
                    hero.buildWeapon(w);
                    if (gameGui != null) {
                        gameGui.setFightResultsFields(gameGui.getFightResultsField() + "Villian drops:\n  " + w + "\n");
                    } else {
                        cv.toOutput("You now have: " + w);
                        cv.toOutput("New attack: " + hero.getAttack());
                    }
                    break;
                case 1:
                    arti = rand.nextInt(3);
                    ArmorType a = ArmorType.values()[arti];
                    hero.buildArmor(a);
                    if (gameGui != null) {
                        gameGui.setFightResultsFields(gameGui.getFightResultsField() + "Villian drops:\n  " + a + "\n");
                    } else {
                        cv.toOutput("You now have: " + a);
                        cv.toOutput("New defence: " + hero.getDefence());
                    }
                    break;
                case 2:
                    arti = rand.nextInt(3);
                    HelmType h = HelmType.values()[arti];
                    hero.buildHelm(h);
                    if (gameGui != null) {
                        gameGui.setFightResultsFields(gameGui.getFightResultsField() + "Villian drops:\n  " + h  + "\n");
                    } else {
                        cv.toOutput("You now have: " + h);
                        cv.toOutput("New hitpoints: " + hero.getHitPoints());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void playGame() {
        while (true) {
            int mapToArr = mapSize / 2;
            int x = Math.abs(hero.getXLoc() - mapToArr);
            int y = Math.abs(hero.getYLoc() - mapToArr);
            if (x > mapToArr || y > mapToArr) {
                break;
            }
            int move = 0;
            while (move < 1 || move > 4) {
                move = cv.getDirectionChoice();
            }
            prevX = hero.getXLoc();
            prevY = hero.getYLoc();
            switch (move) {
                case 1:
                    hero.setYLoc(hero.getYLoc() + 1);
                    break;
                case 2:
                    hero.setYLoc(hero.getYLoc() - 1);
                    break;
                case 3:
                    hero.setXLoc(hero.getXLoc() + 1);
                    break;
                case 4:
                    hero.setXLoc(hero.getXLoc() - 1);
                    break;
                default:
                    break;
            }
            int heroX = hero.getXLoc();
            int heroY = hero.getYLoc();
            Villian toRemove = null;
            for (Villian v : gameChars) {
                if (v.getXLoc() == heroX && v.getYLoc() == heroY) {
                    v.setStats(hero.getLevel());
                    if (cv.fight(hero, v)) {
                        if (!fightSim(v)) {
                            cv.toOutput("**** You loose ****");
                            cv.toOutput();
                            cv.printEndGame();
                            return;
                        } else {
                            cv.toOutput("**** You won ****");
                            cv.toOutput();
                            hero.calcNewExperience(v);
                            initMap(hero.getLevel());
                            toRemove = v;
                            checkForArtifact(v);
                        }
                    } else {
                        Random rand = new Random();
                        if (rand.nextInt(2) == 1) {
                            cv.toOutput("Unlucky... you have to fight");
                            cv.toOutput();
                            if (!fightSim(v)) {
                                cv.toOutput("**** You loose ****");
                                cv.toOutput();
                                cv.printEndGame();
                                return;
                            } else {
                                cv.toOutput("**** You won ****");
                                cv.toOutput();
                                hero.calcNewExperience(v);
                                initMap(hero.getLevel());
                                toRemove = v;
                                checkForArtifact(v);
                            }
                        } else {
                            cv.toOutput("You have run away...");
                            hero.setXLoc(prevX);
                            hero.setYLoc(prevY);
                        }
                    }
                }
            }
            if (toRemove != null) {
                gameChars.remove(toRemove);
                toRemove = null;
            }
            cv.toOutput(hero.toString());
            cv.printHeroLoc(hero, mapSize);
            cv.printMap(mapSize);
            saveData();
        }
        cv.toOutput("You have reached the border of the map");
        cv.printEndGame();
    }

    public String guiHeroLoc(Hero hero, int mapSize) {
        int eastWest = hero.getXLoc() - mapSize / 2;
        int northSouth = hero.getYLoc() - mapSize / 2;
        String location = "";
        if (northSouth >= 0) {
            location += northSouth + "°N ";
        } else {
            location += Math.abs(northSouth) + "°S ";
        }
        if (eastWest >= 0) {
            location += eastWest + "°E ";
        } else {
            location += Math.abs(eastWest) + "°W ";
        }
        return location;
    }

    private boolean guiFightSim() {
        if (vToRemove.getHitPoints() <= 0 ) {
            return true;
        }
        int heroHit = hero.getHitPoints();
        Random rand = new Random();
        while (heroHit > 0 && vToRemove.getHitPoints() > 0) {
            int heroAttack = hero.getAttack();
            int hRand = rand.nextInt(heroAttack) - (heroAttack / 3);
            int damage = hero.getAttack() + hRand - vToRemove.getDefence();
            vToRemove.setHitPoints(vToRemove.getHitPoints() - damage);
            
            String str = gameGui.getFightResultsField();
            if (str == null) {
                str = "";
            }
            gameGui.setFightResultsFields(str + "Hero hits for: " + damage + "\n");
            if (vToRemove.getHitPoints() <= 0) {
                vToRemove.setHitPoints(0);
                str = gameGui.getFightResultsField();
                if (str == null) {
                    str = "";
                }
                gameGui.setFightResultsFields(str + "Villian hit points: " + vToRemove.getHitPoints() + "\n");
                hero.calcNewExperience(vToRemove);
                initMap(hero.getLevel());
                checkForArtifact(vToRemove);
                gameChars.remove(vToRemove);
                vToRemove = null;
                gameGui.disableButtons();
                gameGui.enableBox();
                gameGui.setHeroStatsField(hero.toString());
                return true;
            }
            gameGui.setFightResultsFields(gameGui.getFightResultsField() + "Villian hit points: " + vToRemove.getHitPoints() + "\n");
            int vAttack = vToRemove.getAttack();
            int vRand = rand.nextInt(vAttack) - (vAttack / 3);
            damage = vToRemove.getAttack() + vRand - hero.getDefence();
            heroHit -= damage;
            gameGui.setFightResultsFields(gameGui.getFightResultsField() + "Villian hits for: " + damage + "\n");
            gameGui.setFightResultsFields(gameGui.getFightResultsField() + "Hero hit points: " + heroHit + "\n");
        }
        return false;
    }

    public void saveData() {
        Logger.createFile();
        for (Hero hero : heroList) {
            Logger.writeToFile(hero.getName() + "," + hero.getCharacterClass() + "," + hero.getWeapon().getWeaponType() + "," + hero.getArmor().getArmorType()
                + "," + hero.getHelm().getHelmType() + "," + hero.getLevel() + "," + hero.getExperience() + "\n");
        }
    }
}