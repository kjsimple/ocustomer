package org.opencustomer.framework.db.vo;


public interface EntityAccess
{
    public static enum Access {
        NONE,
        READ,
        WRITE,
        WRITE_SYSTEM;
    }

    public void setOwnerUser(Integer ownerUser);

    public Integer getOwnerUser();

    public void setOwnerGroup(Integer ownerGroup);

    public Integer getOwnerGroup();

    public void setAccessUser(Access accessUser);

    public Access getAccessUser();

    public void setAccessGroup(Access accessGroup);

    public Access getAccessGroup();

    public void setAccessGlobal(Access accessGlobal);

    public Access getAccessGlobal();
}
