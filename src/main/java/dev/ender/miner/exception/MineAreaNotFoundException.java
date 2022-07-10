package dev.ender.miner.exception;

public class MineAreaNotFoundException extends Exception{
     public MineAreaNotFoundException(String areaName) {
         super("Area " + areaName + "not found in database.");
     }
}
