package com.gmail.hilgardvr.swingy.model.artifacts;

import lombok.Getter;
import javax.validation.constraints.Min;

@Getter
public class Armor {
    
    @Min(0)
    private int defence;
    private ArmorType armorType;

    public Armor() {}

    public void setArmorType(ArmorType type) {
        this.armorType = type;
        switch (this.armorType) {
            case CHAINMAIL:
                this.defence = 2;
                break;
            case PLATEMAIL:
                this.defence = 5;
                break;
            case IRONMANSUIT:
                this.defence = 10;
                break;
            default:
                this.defence = 0;
                break;
        }
    }

    @Override
    public String toString() {
        return (armorType.toString() + "\n with defence: " + this.defence);
    }
}