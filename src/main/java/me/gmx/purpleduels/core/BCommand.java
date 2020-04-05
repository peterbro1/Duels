package me.gmx.purpleduels.core;


import me.gmx.purpleduels.PurpleDuels;

import java.util.ArrayList;

public class BCommand {

    public PurpleDuels main;
    public ArrayList<BSubCommand> subcommands;

    public BCommand(PurpleDuels ins) {
        this.main = ins;
        subcommands = new ArrayList<BSubCommand>();
    }

}
