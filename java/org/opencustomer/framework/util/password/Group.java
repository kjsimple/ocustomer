package org.opencustomer.framework.util.password;

import java.util.Random;

public final class Group
{    
    public static final Group GROUP_LOWER_CHAR = new Group("lower character", "abcdefghijklmnopqrstuvwxyz".toCharArray());

    public static final Group GROUP_UPPER_CHAR = new Group("upper character", "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray());

    public static final Group GROUP_DIGIT = new Group("digit", "0123456789".toCharArray());

    public static final Group GROUP_SIGN = new Group("sign", "+-.:_!$&%§#*".toCharArray());

    private String name;
    
    private char[] characters;
    
    public Group(String name, char[] characters) {
        this.name       = name;
        this.characters = characters;
    }

    public final char[] getCharacters()
    {
        return characters;
    }
    

    public final void setCharacters(char[] characters)
    {
        this.characters = characters;
    }
    

    public final String getName()
    {
        return name;
    }
    

    public final void setName(String name)
    {
        this.name = name;
    }
    
     char getCharacterByRandom(Random random) {
        int index = random.nextInt(characters.length);
        return characters[index];
    }
}
