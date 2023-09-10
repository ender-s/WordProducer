package enders.wordproducer.generator;

import java.util.List;

public abstract class Generator
{
    protected final List<String> generationPool;

    /**
     * Constructor of Generator class
     * @param generationPool a List of Strings that defines the symbols used for word generation.
     */
    public Generator(List<String> generationPool)
    {
        this.generationPool = generationPool;
    }
}
