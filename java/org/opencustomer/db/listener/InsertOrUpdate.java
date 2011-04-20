package org.opencustomer.db.listener;

import org.opencustomer.db.vo.system.UserVO;

public interface InsertOrUpdate
{
    public void onInsertOrUpdate(UserVO user);
}
