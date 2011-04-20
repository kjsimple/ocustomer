package org.opencustomer.framework.webapp.util.html.renderer;

import org.opencustomer.framework.webapp.util.html.Column;

public class OrderRenderer {

    private final static String BASE = "SortRenderer."; 
    
    public final static String TABLE_CLASS          = BASE+"table.class"; 
    public final static String BUTTON_ASC_CLASS     = BASE+"button.asc.class"; 
    public final static String BUTTON_ASC_TITLEKEY  = BASE+"button.asc.titleKey"; 
    public final static String BUTTON_DESC_CLASS    = BASE+"button.desc.class"; 
    public final static String BUTTON_DESC_TITLEKEY = BASE+"button.desc.titleKey"; 
    
    public final String createHtml(RendererContext context, Column[] columns, Column column) {
        int position = 0;
        for(int i=0; i<columns.length; i++) {
            if(columns[i].equals(column)) {
                position = i;
                break;
            }
        }
        
        StringBuilder builder = new StringBuilder();
        
        if(column.isSortable()) {
            String styleClass = context.getSettings().getProperty(TABLE_CLASS);

            builder.append("<table");
            if(styleClass != null) {
                builder.append(" class=\"").append(styleClass).append("\"");
            }
            builder.append("><tr><td>");
        }
        builder.append(context.getResources().getMessage(context.getLocale(), column.getMessageKey()));
        if(column.isSortable()) {
            builder.append("</td><td>");
            
            builder.append("<button type=\"submit\"");
            builder.append(" name=\"order\"");
            builder.append(" value=\"asc_").append(position).append("\"");
            builder.append(" class=\"").append(context.getSettings().getProperty(BUTTON_ASC_CLASS)).append("\"");
            if(context.getSettings().getProperty(BUTTON_ASC_TITLEKEY) != null) {
                builder.append(" title=\"").append(context.getResources().getMessage(context.getLocale(), context.getSettings().getProperty(BUTTON_ASC_TITLEKEY))).append("\"");
            }
            builder.append("><span>&gt;</span></button>");

            builder.append("<button type=\"submit\"");
            builder.append(" name=\"order\"");
            builder.append(" value=\"desc_").append(position).append("\"");
            builder.append(" class=\"").append(context.getSettings().getProperty(BUTTON_DESC_CLASS)).append("\"");
            if(context.getSettings().getProperty(BUTTON_DESC_TITLEKEY) != null) {
                builder.append(" title=\"").append(context.getResources().getMessage(context.getLocale(), context.getSettings().getProperty(BUTTON_DESC_TITLEKEY))).append("\"");
            }
            builder.append("><span>&lt;</span></button>");
            
            builder.append("</td></tr></table>");
        }
        
        return builder.toString();
    }    
}
