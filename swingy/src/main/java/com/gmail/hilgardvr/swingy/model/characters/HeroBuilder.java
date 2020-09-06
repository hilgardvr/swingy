package com.gmail.hilgardvr.swingy.model.characters;

import java.util.Set;

//import com.gmail.hilgardvr.swingy.*;
import com.gmail.hilgardvr.swingy.model.artifacts.*;

import lombok.Getter;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Getter
public class HeroBuilder {
    private Hero hero;

    public HeroBuilder(String name, CharacterType characterClass, WeaponType weaponType, ArmorType armorType, HelmType helmType, int level, int exp) {
        this.hero = new Hero();
        this.hero.setName(name);
        this.hero.setCharacterClass(characterClass);
        this.hero.setLevel(level);
        this.hero.setExperience(exp);
        this.hero.setXLoc(0);
        this.hero.setYLoc(0);
        initStats();
        this.buildWeapon(weaponType);
        this.buildArmor(armorType);
        this.buildHelm(helmType);
    }
    
    public boolean validate(Hero hero) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Hero>> constraintViolations = validator.validate(hero);  
        if (constraintViolations.size() > 0 ) {
            for (ConstraintViolation<Hero> constraints : constraintViolations) {
                System.out.println(constraints.getRootBeanClass().getSimpleName()+
                        "." + constraints.getPropertyPath() + " " + constraints.getMessage());
            }
            return false;
        }
        return true;
    }
    
    private void initStats() {
        switch (this.hero.getCharacterClass()) {
            case KNIGHT:
                this.hero.setAttack((int)(28 * Math.pow(1.1, this.hero.getLevel())));
                this.hero.setDefence((int)(10 * Math.pow(1.1, this.hero.getLevel())));
                this.hero.setHitPoints((int)(120 * Math.pow(1.1, this.hero.getLevel())));
                break;
            case ELF:
                this.hero.setAttack((int)(38 * Math.pow(1.1, this.hero.getLevel())));
                this.hero.setDefence((int)(5 * Math.pow(1.1, this.hero.getLevel())));
                this.hero.setHitPoints((int)(100 * Math.pow(1.1, this.hero.getLevel())));
                break;
        }
    }

    private void buildWeapon(WeaponType weaponType) {
        if (hero.getWeapon() != null)
            hero.setAttack(hero.getAttack() - hero.getWeapon().getDamage());
        Weapon weapon = new Weapon();
        weapon.setWeaponType(weaponType);
        this.hero.setWeapon(weapon);
        int currentAttack = this.hero.getAttack();
        this.hero.setAttack(currentAttack + weapon.getDamage());
    }

    private void buildArmor(ArmorType armorType) {
        if (hero.getArmor() != null)
            hero.setDefence(hero.getDefence() - hero.getArmor().getDefence());
        Armor armor = new Armor();
        armor.setArmorType(armorType);
        this.hero.setArmor(armor);
        int currentArmor = this.hero.getDefence();
        this.hero.setDefence(currentArmor + armor.getDefence());
    }

    private void buildHelm(HelmType helmType) {
        if (hero.getHelm() != null)
            hero.setHitPoints(hero.getHitPoints() - hero.getHelm().getHitPoints());
        Helm helm = new Helm();
        helm.setHelmType(helmType);
        this.hero.setHelm(helm);
        int currentHit = this.hero.getHitPoints();
        this.hero.setHitPoints(currentHit + helm.getHitPoints());
    }
}