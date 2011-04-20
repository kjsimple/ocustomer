package org.opencustomer.webapp.module.system.sessions;

public class SessionStatisticBean
{

    private int underMinute1;

    private int underMinute5;

    private int underMinute15;

    private int underMinute30;

    private int underMinute60;

    private int overMinute60;

    public void add(long seconds)
    {
        if (seconds < 60)
            underMinute1++;
        else if (seconds < 300)
            underMinute5++;
        else if (seconds < 900)
            underMinute15++;
        else if (seconds < 1800)
            underMinute30++;
        else if (seconds < 3600)
            underMinute60++;
        else
            overMinute60++;
    }

    public int getOverMinute60()
    {
        return overMinute60;
    }

    public int getUnderMinute1()
    {
        return underMinute1;
    }

    public int getUnderMinute15()
    {
        return underMinute15;
    }

    public int getUnderMinute30()
    {
        return underMinute30;
    }

    public int getUnderMinute5()
    {
        return underMinute5;
    }

    public int getUnderMinute60()
    {
        return underMinute60;
    }

}
