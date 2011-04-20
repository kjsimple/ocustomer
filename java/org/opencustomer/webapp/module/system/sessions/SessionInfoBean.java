package org.opencustomer.webapp.module.system.sessions;

import java.util.Date;

public final class SessionInfoBean
{

    private String username;

    private Date loginTime;

    private Date lastAccessTime;

    private long inactiveTime;

    public long getInactiveTime()
    {
        return inactiveTime;
    }

    public void setInactiveTime(long inactiveTime)
    {
        this.inactiveTime = inactiveTime;
    }

    public Date getLastAccessTime()
    {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime)
    {
        this.lastAccessTime = lastAccessTime;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getInactiveString()
    {
        return calculate(inactiveTime);
    }

    public Date getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Date loginTime)
    {
        this.loginTime = loginTime;
    }

    private String calculate(long time)
    {
        StringBuilder builder = new StringBuilder();

        long secTime = time / 1000;

        long hour = secTime / 3600;
        long minute = (secTime % 3600) / 60;
        long second = secTime % 60;

        if (hour < 10)
            builder.append("0");
        builder.append(hour);
        builder.append(":");
        if (minute < 10)
            builder.append("0");
        builder.append(minute);
        builder.append(":");
        if (second < 10)
            builder.append("0");
        builder.append(second);

        return builder.toString();
    }
}
