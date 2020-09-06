package com.gmail.hilgardvr.swingy.model.artifacts;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Min;

@Getter
@Setter
public class Helm {
    
    @Min(0)
    private int hitPoints;
    private HelmType helmType;

    public Helm() {}

    public void setHelmType(HelmType type) {
        this.helmType = type;
        switch (this.helmType) {
            case BICYCLEHELMET:
                this.hitPoints = 10;
                break;
            case IRONHELM:
                this.hitPoints = 20;
                break;
            case VANADIUMHELM:
                this.hitPoints = 30;
                break;
            default:
                this.hitPoints = 0;
                break;
        }
    }

    @Override
    public String toString() {
        return (helmType.toString() + "\n with hitpoints: " + this.hitPoints);
    }
}