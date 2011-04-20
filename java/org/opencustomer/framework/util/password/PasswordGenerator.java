package org.opencustomer.framework.util.password;

import java.util.ArrayList;
import java.util.Random;

public final class PasswordGenerator
{
    private Profile profile;
    
    private Random random;
    
    public PasswordGenerator() {
        this(Profile.DEFAULT_PROFILE, null);
    }

    public PasswordGenerator(String secret) {
        this(Profile.DEFAULT_PROFILE, secret);
    }
    
    public PasswordGenerator(Profile profile) {
        this(profile, null);
    }
    
    public PasswordGenerator(Profile profile, String secret)
        throws IllegalArgumentException {
        int size = profile.getGroupSize();
        
        // check number of profiles
        if(size == 0) {
            throw new IllegalArgumentException("a profile must have at least 1 group");
        }
        
        // check sum of minimum
        int min = 0;
        for(int i=0; i<size; i++) {
            min += profile.getGroupMin(i);
        }
        if(min > profile.getPasswordMinLength()) {
            throw new IllegalArgumentException("the sum of group minimums may not be greater than the password minium length (length="+profile.getPasswordMinLength()+")");
        }
        
        // check sum of maximum
        int max = 0;
        for(int i=0; i<size; i++) {
            max += profile.getGroupMax(i);
        }
        if(max < profile.getPasswordMaxLength()) {
            throw new IllegalArgumentException("the sum of group maximums may not be smaller than the password maximum length (length="+profile.getPasswordMaxLength()+")");
        }
        
        this.profile = profile;
        
        int secretHash = 0;
        if(secret != null) {
            secretHash = secret.hashCode();
        }
        this.random = new Random(System.currentTimeMillis()+secretHash);
    }
    
    public Profile getProfile() {
        return profile;
    }
    
    public String generate() {
        return generate(null);
    }
    
    public String generate(String base) {
        if(base != null) {
            random.setSeed(System.currentTimeMillis()+base.hashCode());
        }
        
        State state = new State(profile);
        int size = profile.getGroupSize();
        
        StringBuilder password = new StringBuilder();
        
        int passwordLengh = profile.getPasswordMinLength();
        if(profile.getPasswordMaxLength()-profile.getPasswordMinLength() > 0) {
            passwordLengh += random.nextInt(profile.getPasswordMaxLength()-profile.getPasswordMinLength()+1);
        }

        for(int i=0; i<passwordLengh; i++) {
            
            ArrayList<Group> usedGroups = new ArrayList<Group>();
            for(int j=0; j<size; j++) {
                if(state.getQuantity(j) < profile.getGroupMin(j))
                    usedGroups.add(profile.getGroup(j));
            }
            if(usedGroups.isEmpty()) {
                for(int j=0; j<size; j++) {
                    if(state.getQuantity(j) < profile.getGroupMax(j))
                        usedGroups.add(profile.getGroup(j));
                }
            }
            
            Group group = usedGroups.get(random.nextInt(usedGroups.size()));
            password.append(group.getCharacterByRandom(random));
            int index = getIndex(group);
            state.incrementQuantity(index);
        }
        
        return password.toString();
    }

    private int getIndex(Group group) {
        int size = profile.getGroupSize();
        for(int i=0; i<size; i++) {
            if(profile.getGroup(i).equals(group))
                return i;
        }
        
        return -1;
    }
    
    private final class State {
        
        private int[] quantities;
        
        public State(Profile profile) {
            quantities = new int[profile.getGroupSize()];
        }
        
        public int getQuantity(int index) {
            return quantities[index];
        }
        
        public void incrementQuantity(int index) {
            quantities[index]++;
        }
    }
    
}
