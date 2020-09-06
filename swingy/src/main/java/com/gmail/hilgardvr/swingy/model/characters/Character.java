package com.gmail.hilgardvr.swingy.model.characters;
import lombok.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
abstract class Character {
    
    @NotNull
    @Size(min = 2, max = 31)
    private String name;
    private int level;
    private int experience;
    private int attack;
    private int defence;
    private int hitPoints;
    private int xLoc;
    private int yLoc;
}