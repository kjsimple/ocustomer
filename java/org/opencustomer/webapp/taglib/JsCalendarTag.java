package org.opencustomer.webapp.taglib;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.util.configuration.UserConfiguration;
import org.opencustomer.webapp.Globals;

public final class JsCalendarTag extends TagSupport
{
    private static final long serialVersionUID = 428305407335964656L;
    
    private String input;
    
    private String button;
    
    private String formatKey;
    
    private boolean disabled;
    
    @Override
    public int doStartTag() throws JspException
    {
        UserConfiguration conf = (UserConfiguration)pageContext.getSession().getAttribute(Globals.CONFIGURATION_KEY);

        boolean isDisabled = disabled;
        if(pageContext.getAttribute("i_panel_editable") != null) {
            if(pageContext.getAttribute("i_panel_editable").equals(Boolean.FALSE))
                isDisabled = true;
        }
        
        if(!isDisabled) {
            StringBuilder builder = new StringBuilder();
            
            builder.append("<script type=\"text/javascript\">");
            
            if(pageContext.getAttribute("jsCalendar_isLocaleInitialized") == null) {
                initLocale(builder);
                
                pageContext.setAttribute("jsCalendar_isLocaleInitialized", Boolean.TRUE);
            }
            
            builder.append("Calendar.setup(");
            builder.append("{");
            
            builder.append("inputField : \""+input+"\",");
            builder.append("button : \""+button+"\",");
            if(formatKey != null) {
                String jsFormat = getJsFormat(formatKey);
                builder.append("ifFormat : \""+jsFormat+"\",");
    
                if(jsFormat.contains("%M") 
                        ||jsFormat.contains("%H")
                        || jsFormat.contains("%k")
                        || jsFormat.contains("%I")
                        || jsFormat.contains("%l")) {
                    builder.append("showsTime : true,");
                    builder.append("singleClick : false,");
                }
    
                if(jsFormat.contains("%I")
                        || jsFormat.contains("%l")) {
                    builder.append("timeFormat : \"12\",");
                } else {
                    builder.append("timeFormat : \"24\",");
                }
            }
            builder.append("step : 5,");
            builder.append("firstDay : "+getJsFirstDayOfWeek(conf.getIntValue(UserConfiguration.Key.CALENDAR_FIRST_DAY_OF_WEEK)));
            builder.append("}");
            builder.append(");");
            builder.append("</script>");
            
            TagUtils.getInstance().write(pageContext, builder.toString());
        }
        
        return super.doStartTag();
    }
    
    private void initLocale(StringBuilder builder) {
        Locale locale = (Locale)pageContext.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY);
        
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();        
        //MessageUtil.message(request, formatKey);
        
        Calendar cal = GregorianCalendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", locale);
        
        builder.append("\nCalendar._DN = new Array");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        builder.append("(\""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\");\n");

        sdf = new SimpleDateFormat("EE", locale);
        
        builder.append("Calendar._SDN = new Array");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        builder.append("(\""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\");\n");

        // set to first of month
        cal.set(Calendar.DAY_OF_MONTH, 1);

        sdf = new SimpleDateFormat("MMMM", locale);
        
        builder.append("Calendar._MN = new Array");
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        builder.append("(\""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.MARCH);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.APRIL);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.MAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.JUNE);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.JULY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.AUGUST);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.NOVEMBER);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        builder.append(" \""+sdf.format(cal.getTime())+"\");\n");

        sdf = new SimpleDateFormat("MMM", locale);
        
        builder.append("Calendar._SMN = new Array");
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        builder.append("(\""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.MARCH);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.APRIL);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.MAY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.JUNE);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.JULY);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.AUGUST);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.NOVEMBER);
        builder.append(" \""+sdf.format(cal.getTime())+"\",");
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        builder.append(" \""+sdf.format(cal.getTime())+"\");\n");

        builder.append("Calendar._TT = {};\n");
        builder.append("Calendar._TT[\"INFO\"] = \""+MessageUtil.message(request, "default.jscalendar.INFO")+"\";\n");

        builder.append("Calendar._TT[\"ABOUT\"] =");
        builder.append("\"DHTML Date/Time Selector\\n");
        builder.append("(c) dynarch.com 2002-2005 / Author: Mihai Bazon\\n");
        builder.append("For latest version visit: http://www.dynarch.com/projects/calendar/\\n");
        builder.append("Distributed under GNU LGPL.  See http://gnu.org/licenses/lgpl.html for details.");
        builder.append("\\n\\n");
        builder.append(MessageUtil.message(request, "default.jscalendar.ABOUT"));
        builder.append("\";");        
        builder.append("\nCalendar._TT[\"ABOUT_TIME\"] = \""+MessageUtil.message(request, "default.jscalendar.ABOUT_TIME")+"\";");

        builder.append("\nCalendar._TT[\"TOGGLE\"] = \""+MessageUtil.message(request, "default.jscalendar.TOGGLE")+"\";\n");
        builder.append("Calendar._TT[\"PREV_YEAR\"] = \""+MessageUtil.message(request, "default.jscalendar.PREV_YEAR")+"\";\n");
        builder.append("Calendar._TT[\"PREV_MONTH\"] = \""+MessageUtil.message(request, "default.jscalendar.PREV_MONTH")+"\";\n");
        builder.append("Calendar._TT[\"GO_TODAY\"] = \""+MessageUtil.message(request, "default.jscalendar.GO_TODAY")+"\";\n");
        builder.append("Calendar._TT[\"NEXT_MONTH\"] = \""+MessageUtil.message(request, "default.jscalendar.NEXT_MONTH")+"\";\n");
        builder.append("Calendar._TT[\"NEXT_YEAR\"] = \""+MessageUtil.message(request, "default.jscalendar.NEXT_YEAR")+"\";\n");
        builder.append("Calendar._TT[\"SEL_DATE\"] = \""+MessageUtil.message(request, "default.jscalendar.SEL_DATE")+"\";\n");
        builder.append("Calendar._TT[\"DRAG_TO_MOVE\"] = \""+MessageUtil.message(request, "default.jscalendar.DRAG_TO_MOVE")+"\";\n");
        builder.append("Calendar._TT[\"PART_TODAY\"] = \""+MessageUtil.message(request, "default.jscalendar.PART_TODAY")+"\";\n");

        builder.append("Calendar._TT[\"DAY_FIRST\"] = \""+MessageUtil.message(request, "default.jscalendar.DAY_FIRST")+"\";\n");

        builder.append("Calendar._TT[\"WEEKEND\"] = \"0,6\";\n");

        builder.append("Calendar._TT[\"CLOSE\"] = \""+MessageUtil.message(request, "default.jscalendar.CLOSE")+"\";\n");
        builder.append("Calendar._TT[\"TODAY\"] = \""+MessageUtil.message(request, "default.jscalendar.TODAY")+"\";\n");
        builder.append("Calendar._TT[\"TIME_PART\"] = \""+MessageUtil.message(request, "default.jscalendar.TIME_PART")+"\";\n");

        builder.append("Calendar._TT[\"DEF_DATE_FORMAT\"] = \"%d.%m.%Y\";\n");
        builder.append("Calendar._TT[\"TT_DATE_FORMAT\"] = \"%A (%d.%m.%Y)\";\n");

        builder.append("Calendar._TT[\"WK\"] = \""+MessageUtil.message(request, "default.jscalendar.WK")+"\";\n");
        builder.append("Calendar._TT[\"TIME\"] = \""+MessageUtil.message(request, "default.jscalendar.TIME")+"\";\n");
    }
    
    private int getJsFirstDayOfWeek(final int firstDayOfWeek) {
        int jsFirstDayOfWeek = 1;
        
        switch(firstDayOfWeek) {
            case Calendar.MONDAY:
                jsFirstDayOfWeek = 1;
                break;
            case Calendar.TUESDAY:
                jsFirstDayOfWeek = 2;
                break;
            case Calendar.WEDNESDAY:
                jsFirstDayOfWeek = 3;
                break;
            case Calendar.THURSDAY:
                jsFirstDayOfWeek = 4;
                break;
            case Calendar.FRIDAY:
                jsFirstDayOfWeek = 5;
                break;
            case Calendar.SATURDAY:
                jsFirstDayOfWeek = 6;
                break;
            case Calendar.SUNDAY:
                jsFirstDayOfWeek = 0;
                break;
        }
        
        return jsFirstDayOfWeek;
    }

    private String getJsFormat(String format) {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();        
        String jsFormat = MessageUtil.message(request, formatKey);

        if(jsFormat.contains("HH"))
            jsFormat = jsFormat.replaceAll("HH", "%H");
        else if(jsFormat.contains("H"))
            jsFormat = jsFormat.replaceAll("H", "%k");
        else if(jsFormat.contains("hh"))
            jsFormat = jsFormat.replaceAll("hh", "%I");
        else if(jsFormat.contains("h"))
            jsFormat = jsFormat.replaceAll("h", "%l");
        
        if(jsFormat.contains("mm"))
            jsFormat = jsFormat.replaceAll("mm", "%M");
        else if(jsFormat.contains("m"))
            jsFormat = jsFormat.replaceAll("m", "%M");
        
        if(jsFormat.contains("yyyy"))
            jsFormat = jsFormat.replaceAll("yyyy", "%Y");
        else if(jsFormat.contains("yyy"))
            jsFormat = jsFormat.replaceAll("yyy", "%y");
        else if(jsFormat.contains("yy"))
            jsFormat = jsFormat.replaceAll("yy", "%Y");
        else if(jsFormat.contains("y"))
            jsFormat = jsFormat.replaceAll("y", "%Y");

        if(jsFormat.contains("MMMM"))
            jsFormat = jsFormat.replaceAll("MMMM", "%B");
        else if(jsFormat.contains("MMM"))
            jsFormat = jsFormat.replaceAll("MMM", "%b");
        else if(jsFormat.contains("MM"))
            jsFormat = jsFormat.replaceAll("MM", "%m");

        if(jsFormat.contains("dd"))
            jsFormat = jsFormat.replaceAll("dd", "%d");
        else if(jsFormat.contains("d"))
            jsFormat = jsFormat.replaceAll("d", "%e");
        else if(jsFormat.contains("d"))
            jsFormat = jsFormat.replaceAll("d", "%e");
        
        if(jsFormat.contains("DDD"))
            jsFormat = jsFormat.replaceAll("DDD", "%j");
        else if(jsFormat.contains("DD"))
            jsFormat = jsFormat.replaceAll("DD", "%j");
        else if(jsFormat.contains("D"))
            jsFormat = jsFormat.replaceAll("D", "%j");
        
        if(jsFormat.contains("'"))
            jsFormat = jsFormat.replaceAll("'", "");
        
        return jsFormat;
    }

    public final String getButton()
    {
        return button;
    }
    

    public final void setButton(String button)
    {
        this.button = button;
    }
    

    public final String getFormatKey()
    {
        return formatKey;
    }
    

    public final void setFormatKey(String formatKey)
    {
        this.formatKey = formatKey;
    }
    

    public final String getInput()
    {
        return input;
    }
    

    public final void setInput(String input)
    {
        this.input = input;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }   
    
}
