package dev.ender.miner.exception;

public class UnexpectedConfigFileException extends Exception{
    public UnexpectedConfigFileException(String materialName) {
        super(String.format("Material '%s' not found in config", materialName));
    }
}
