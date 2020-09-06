package com.gmail.hilgardvr.swingy.model.artifacts;

import lombok.Getter;
import javax.validation.constraints.Min;

@Getter
public class Weapon {
    
    @Min(0)
    private int damage;
    private WeaponType weaponType;

    public Weapon() {}

    public void setWeaponType(WeaponType type) {
        this.weaponType = type;
        switch (type) {
            case DAGGER:
                this.damage = 5;
                break;
            case SHORTBOW:
                this.damage = 8;
                break;
            case MACHINEGUN:
                this.damage = 15;
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return (weaponType.toString() + "\n with damage: " + damage);
    }
}