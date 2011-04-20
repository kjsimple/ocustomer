package org.opencustomer.framework.util.password;

import java.util.ArrayList;
import java.util.List;

public final class Profile
{
    public final static Profile DEFAULT_PROFILE;
    
    public final static Profile FIX_LENGTH8_PROFILE;

    static {
        DEFAULT_PROFILE = new Profile(8, 16);
        DEFAULT_PROFILE.addGroup(Group.GROUP_LOWER_CHAR, 1, 5);
        DEFAULT_PROFILE.addGroup(Group.GROUP_UPPER_CHAR, 1, 5);
        DEFAULT_PROFILE.addGroup(Group.GROUP_DIGIT, 1, 3);
        DEFAULT_PROFILE.addGroup(Group.GROUP_SIGN, 1, 3);

        FIX_LENGTH8_PROFILE = new Profile(8, 8);
        FIX_LENGTH8_PROFILE.addGroup(Group.GROUP_LOWER_CHAR, 1, 3);
        FIX_LENGTH8_PROFILE.addGroup(Group.GROUP_UPPER_CHAR, 1, 3);
        FIX_LENGTH8_PROFILE.addGroup(Group.GROUP_DIGIT, 1, 2);
        FIX_LENGTH8_PROFILE.addGroup(Group.GROUP_SIGN, 1, 2);
    }
    
    private int passwordMinLength;

    private int passwordMaxLength;

    private List<Bean> beans = new ArrayList<Bean>();
    
    public Profile(int passwordMinLength, int passwordMaxLength) 
        throws IllegalArgumentException {
        
        if(passwordMinLength < 1)
            throw new IllegalArgumentException("the minimum length have to be greater then 0");
        if(passwordMinLength > passwordMaxLength)
            throw new IllegalArgumentException("the minimum length may not be greater than maximum length");
        
        this.passwordMinLength = passwordMinLength;
        this.passwordMaxLength = passwordMaxLength;
    }
    
    public int getPasswordMinLength() {
        return passwordMinLength;
    }

    public int getPasswordMaxLength() {
        return passwordMaxLength;
    }
    public void addGroup(Group group, int min, int max) 
        throws IllegalArgumentException {
        
        if(min < 0)
            throw new IllegalArgumentException("minimum may not be lower than 0");
        if(max < 1)
            throw new IllegalArgumentException("maximum may not be lower than 1");
        if(min > max)
            throw new IllegalArgumentException("minimum not be greater than maximum");

        
        beans.add(new Bean(group, min, max));
    }
    
    public int getGroupSize() {
        return beans.size();
    }
    
    public Group getGroup(int index) {
        return beans.get(index).getGroup();
    }
    
    public int getGroupMin(int index) {
        return beans.get(index).getMin();
    }  
    
    public int getGroupMax(int index) {
        return beans.get(index).getMax();
    }  
    
    public boolean isPasswordValid(String password) {
        int invalidGroups = beans.size();
        
        char[] passwordChars = password.toCharArray();

        if(password != null) {
            for(Bean bean: beans) {
                char[] groupChars = bean.getGroup().getCharacters();
                
                int count = count(passwordChars, groupChars);
                if(count >= bean.getMin() && count <=bean.getMax()) {
                    invalidGroups--;
                }
            }
            
        }
        
        return (invalidGroups == 0);
    }
    
    private int count(char[] passwordChars, char[] groupChars) {
        int count = 0;
        
        for(char pwdChar : passwordChars) {
            for(char groupChar : groupChars) {
                if(pwdChar == groupChar) {
                    count++;
                    break;
                }
            }
        }
        
        return count;    
    }
    
    private final class Bean {
        private Group group;
        
        private int min;
        
        private int max;
        
        public Bean(Group group, int min, int max) {
            this.group = group;
            this.min   = min;
            this.max   = max;
        }

        public final Group getGroup() {
            return group;
        }
        
        public final void setGroup(Group group) {
            this.group = group;
        }
        
        public final int getMax() {
            return max;
        }
        
        public final void setMax(int max) {
            this.max = max;
        }
        
        public final int getMin() {
            return min;
        }

        public final void setMin(int min) {
            this.min = min;
        }
    }
}
