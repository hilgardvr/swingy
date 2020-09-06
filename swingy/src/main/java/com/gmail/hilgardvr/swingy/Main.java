package com.gmail.hilgardvr.swingy;

import com.gmail.hilgardvr.swingy.controller.*;
import com.gmail.hilgardvr.swingy.view.LaunchGui;

public class Main
{
    public static void main( String[] args )
    {
        GameLauncher gameLauncher;
        LaunchGui lgui;
        if (args.length != 1) {
            System.out.println("Invalid usage: java -jar swingy.jar (option: console/gui)");
            System.exit(0);
        }
        
        if (args[0].equals("console")) {
            gameLauncher = new GameLauncher();
        } else if (args[0].equals("gui")) {
            lgui = new LaunchGui(); 
            gameLauncher = new GameLauncher(lgui);
        } else {
            System.out.println("Invalid option: " + args[0] + " option: console/gui - exiting");
            return;
        }
    }
}