/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is the OpenCustomer CRM.
 * 
 * The Initial Developer of the Original Code is Thomas Bader (Bader & Jene
 * Software-Ingenieurbüro). Portions created by the Initial Developer are
 * Copyright (C) 2005 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.framework.webapp.taglib.common;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;
import org.opencustomer.framework.db.util.engine.configuration.DateSearch;
import org.opencustomer.framework.db.util.engine.configuration.EnumSearch;
import org.opencustomer.framework.db.util.engine.configuration.ListSelectSearch;
import org.opencustomer.framework.db.util.engine.configuration.TextSearch;
import org.opencustomer.framework.db.util.engine.configuration.TextSelectSearch;
import org.opencustomer.framework.webapp.util.html.Column;
import org.opencustomer.framework.webapp.util.html.Table;
import org.opencustomer.framework.webapp.util.html.renderer.TableRenderer;

public final class SearchTag extends TagSupport {
    private static final long serialVersionUID = 3544387011285628472L;

    private final static String BASE = "SearchTag."; 

    public final static String TABLE_CLASS       = BASE+"table.class"; 
    public final static String INNER_TABLE_CLASS = BASE+"table.inner.class"; 
    public final static String HALF_INPUT_CLASS = BASE+"input.half.class"; 

    private TableRenderer renderer = new TableRenderer();
    
    private String name;

    private String property;

    private String scope;

    private String style;
    
    public int doEndTag() throws JspException {
        Table table                = (Table)TagUtils.getInstance().lookup(pageContext, name, property, scope);

        Properties settings        = (Properties) pageContext.getSession().getServletContext().getAttribute(org.opencustomer.webapp.Globals.TAG_SETTINGS_KEY);
        MessageResources resources = (MessageResources) pageContext.getSession().getServletContext().getAttribute(Globals.MESSAGES_KEY);
        Locale locale              = (Locale) pageContext.getSession().getAttribute(Globals.LOCALE_KEY);

        StringBuilder builder = new StringBuilder();
        
        builder.append("<table");
        if(settings.getProperty(TABLE_CLASS) != null) {
            builder.append(" class=\"").append(settings.getProperty(TABLE_CLASS)).append("\"");
        }
        builder.append(">");
        builder.append("<colgroup>");
        for(Column column : table.getColumns()) {
            builder.append("<col width=\"1*\"/>");
        }
        builder.append("</colgroup>");
        builder.append("<tr>\n");
        for(Column column : table.getColumns()) {
            if(column.getSearch() != null) {
                builder.append("<td>");
                builder.append("<table");
                if(settings.getProperty(INNER_TABLE_CLASS) != null) {
                    builder.append(" class=\"").append(settings.getProperty(INNER_TABLE_CLASS)).append("\"");
                }
                builder.append(">");
                builder.append("<tr>");
                builder.append("<td>");
                if(column.getSearch() != null) {
                    if(column.getSearch() instanceof DateSearch) {
                        builder.append("<label for=\"search_").append(column.getPosition()).append("_start\">");
                        builder.append(resources.getMessage(locale, column.getMessageKey()));
                        builder.append("</label>");
                    } else {
                        builder.append("<label for=\"search_").append(column.getPosition()).append("\">");
                        builder.append(resources.getMessage(locale, column.getMessageKey()));
                        builder.append("</label>");
                    }
    
                    builder.append("</td>");
                    builder.append("</tr>");
                    builder.append("<tr>");
                    builder.append("<td>");
                    
                    if(column.getSearch() instanceof EnumSearch) {
                        EnumSearch search = (EnumSearch)column.getSearch();

                        builder.append("<select");
                        builder.append(" name=\"search_").append(column.getPosition()).append("\"");
                        builder.append(" id=\"search_").append(column.getPosition()).append("\"");
                        builder.append(">");
                        for(Enum e : search.getMessageKeys().keySet()) {
                            String messageKey = search.getMessageKeys().get(e);
                            
                            builder.append("<option value=\"").append(e == null ? "" : e.name()).append("\"");
                            if((e == null && search.getValue() == null) || (e != null && search.getValue() != null && e.equals(search.getValue()))) {
                                builder.append("selected=\"selected\"");
                            }
                            builder.append(">");
                            builder.append(resources.getMessage(locale, messageKey));
                            builder.append("</option>");
                        }
                        builder.append("</select>");
                    } else if(column.getSearch() instanceof ListSelectSearch) {
                        ListSelectSearch search = (ListSelectSearch)column.getSearch();
                        search.loadValues();
                        
                        builder.append("<select");
                        builder.append(" name=\"search_").append(column.getPosition()).append("\"");
                        builder.append(" id=\"search_").append(column.getPosition()).append("\"");
                        builder.append(">");
                        for(Integer id : search.getBeans().keySet()) {
                            ListSelectSearch.Bean bean = search.getBeans().get(id);
                            
                            builder.append("<option value=\"").append(id == null ? "" : id).append("\"");
                            if((id == null && search.getValue() == null) || (id != null && search.getValue() != null && id.equals(search.getValue()))) {
                                builder.append("selected=\"selected\"");
                            }
                            builder.append(">");
                            if(bean.getMessageKey() != null)
                                builder.append(resources.getMessage(locale, bean.getMessageKey()));
                            else
                                builder.append(bean.getMessage());
                            builder.append("</option>");
                        }
                        builder.append("</select>");
                    } else if(column.getSearch() instanceof TextSelectSearch) {
                        TextSelectSearch search = (TextSelectSearch)column.getSearch();

                        builder.append("<select");
                        builder.append(" name=\"search_").append(column.getPosition()).append("\"");
                        builder.append(" id=\"search_").append(column.getPosition()).append("\"");
                        builder.append(">");
                        for(String e : search.getBeans().keySet()) {
                            String messageKey = search.getBeans().get(e).getMessageKey();
                            
                            builder.append("<option value=\"").append(e == null ? "" : e).append("\"");
                            if((e == null && search.getValue() == null) || (e != null && search.getValue() != null && e.equals(search.getValue()))) {
                                builder.append("selected=\"selected\"");
                            }
                            builder.append(">");
                            builder.append(resources.getMessage(locale, messageKey));
                            builder.append("</option>");
                        }
                        builder.append("</select>");
                    } else if(column.getSearch() instanceof TextSearch) {
                        TextSearch search = (TextSearch)column.getSearch();
                        
                        builder.append("<input");
                        builder.append(" type=\"text\"");
                        builder.append(" name=\"search_").append(column.getPosition()).append("\"");
                        builder.append(" id=\"search_").append(column.getPosition()).append("\"");
                        builder.append(" value=\"").append(search.getValue() == null ? "" : search.getValue()).append("\"");
                        builder.append("/>");
                    } else if(column.getSearch() instanceof DateSearch) {
                        DateSearch search = (DateSearch)column.getSearch();
                        
                        SimpleDateFormat sdf = new SimpleDateFormat(resources.getMessage(locale, search.getFormatKey()));
                        
                        builder.append("<input");
                        builder.append(" type=\"text\"");
                        if(settings.getProperty(HALF_INPUT_CLASS) != null) {
                            builder.append(" class=\"").append(settings.getProperty(HALF_INPUT_CLASS)).append("\"");
                        }
                        builder.append(" name=\"search_").append(column.getPosition()).append("_start\"");
                        builder.append(" id=\"search_").append(column.getPosition()).append("_start\"");
                        builder.append(" value=\"").append(search.getValueStart() == null ? "" : sdf.format(search.getValueStart())).append("\"");
                        builder.append("/>");
                        
                        builder.append("<input");
                        builder.append(" type=\"text\"");
                        if(settings.getProperty(HALF_INPUT_CLASS) != null) {
                            builder.append(" class=\"").append(settings.getProperty(HALF_INPUT_CLASS)).append("\"");
                        }
                        builder.append(" name=\"search_").append(column.getPosition()).append("_end\"");
                        builder.append(" id=\"search_").append(column.getPosition()).append("_end\"");
                        builder.append(" value=\"").append(search.getValueEnd() == null ? "" : sdf.format(search.getValueEnd())).append("\"");
                        builder.append("/>");
                    }
                    builder.append("</td>");
                    builder.append("</tr>");
                    builder.append("</table>");
                } else {
                    builder.append("&nbsp;");
                }
                
                builder.append("</td>\n");
            } else {
                builder.append("<td>&nbsp;</td>\n");
            }
        }
        builder.append("</tr>");
        builder.append("</table>\n");
        
        TagUtils.getInstance().write(pageContext, builder.toString());
        
        release();

        return EVAL_PAGE;
    }

    @Override
    public void release() {
        super.release();
        
        this.name     = null;
        this.property = null;
        this.scope    = null;
        this.style    = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }    
}
