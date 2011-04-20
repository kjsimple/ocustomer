package org.opencustomer.db;

import org.opencustomer.db.vo.system.UserVO;

public interface UserAssigned
{
    public void setAssignedUser(UserVO user);
    
    public UserVO getAssignedUser();
}
