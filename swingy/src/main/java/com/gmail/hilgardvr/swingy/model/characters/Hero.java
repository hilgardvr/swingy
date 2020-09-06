package com.gmail.hilgardvr.swingy.model.characters;

import com.gmail.hilgardvr.swingy.model.artifacts.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hero extends Character {
    private CharacterType characterClass;
    private Weapon weapon;
    private Armor armor;
    private Helm helm;

    public Hero() {}
   
    public void calcNewExperience(Villian v) {
        int xpGain = v.getLevel() * 450;
        this.setExperience(this.getExperience() + xpGain);
        int lvl = 1;
        int toLvlUp = lvl * 1000 + (lvl - 1) * (lvl - 1) * 450;
        int xp = this.getExperience();
        while (xp >= toLvlUp) {
            lvl++;
            toLvlUp = lvl * 1000 + (lvl - 1) * (lvl - 1) * 450;
        }
        this.setLevel(lvl);
        switch (this.characterClass) {
            case KNIGHT:
                this.setAttack((int)(28 * Math.pow(1.1, this.getLevel())) + weapon.getDamage());
                this.setDefence((int)(10 * Math.pow(1.1, this.getLevel())) + armor.getDefence());
                this.setHitPoints((int)(120 * Math.pow(1.1, this.getLevel())) + helm.getHitPoints());
                break;
            case ELF:
                this.setAttack((int)(38 * Math.pow(1.1, this.getLevel())) + weapon.getDamage());
                this.setDefence((int)(5 * Math.pow(1.1, this.getLevel())) + armor.getDefence());
                this.setHitPoints((int)(100 * Math.pow(1.1, this.getLevel())) + helm.getHitPoints());
                break;
        }
    }

    public void buildWeapon(WeaponType weaponType) {
        //System.out.println("Old attack: " + this.getAttack());
        if (weapon != null)
            this.setAttack(this.getAttack() - weapon.getDamage());
        Weapon weapon = new Weapon();
        weapon.setWeaponType(weaponType);
        this.setWeapon(weapon);
        int currentAttack = this.getAttack();
        this.setAttack(currentAttack + weapon.getDamage());
        //System.out.println("You now have: " + weaponType);
        //System.out.println("New attack: " + this.getAttack());
    }

    public void buildArmor(ArmorType armorType) {
        //System.out.println("Old defence: " + this.getDefence());
        if (armor != null)
            this.setDefence(this.getDefence() - this.getArmor().getDefence());
        Armor armor = new Armor();
        armor.setArmorType(armorType);
        this.setArmor(armor);
        int currentArmor = this.getDefence();
        this.setDefence(currentArmor + armor.getDefence());
        //System.out.println("You now have: " + armorType);
        //System.out.println("New defence: " + this.getDefence());
    }

    public void buildHelm(HelmType helmType) {
        //System.out.println("Old hit points: " + this.getHitPoints());
        if (helm != null)
            this.setHitPoints(this.getHitPoints() - this.getHelm().getHitPoints());
        Helm helm = new Helm();
        helm.setHelmType(helmType);
        this.setHelm(helm);
        int currentHit = this.getHitPoints();
        this.setHitPoints(currentHit + helm.getHitPoints());
        
    }

    @Override
    public String toString() {
        return (this.getName() + "\n  level: " + this.getLevel() + "\n  attack: " + this.getAttack() 
        + "\n  defence: " + this.getDefence() + "\n  hit points: " + this.getHitPoints()
        + "\nWeapon: " + weapon + "\nArmor: " + armor + "\nHelm: " + helm);
    }
}