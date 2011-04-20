package org.opencustomer.framework.webapp.util.html.renderer;

import org.opencustomer.framework.db.util.engine.Row;
import org.opencustomer.framework.webapp.util.html.Column;
import org.opencustomer.framework.webapp.util.html.Table;

public class TableRenderer {

    private final static String BASE = "TableRenderer."; 
    
    public final static String TABLE_CLASS              = BASE+"table.class"; 
    public final static String ROW_CLASS                = BASE+"row.class"; 
    public final static String ROW_ACTIVE_CLASS         = BASE+"row.active.class"; 
    public final static String ROW_NOENTRIES_CLASS      = BASE+"row.noEntries.class"; 
    public final static String ROW_NOENTRIES_MESSAGEKEY = BASE+"row.noEntries.messageKey"; 
    
    private OrderRenderer sortRenderer = new OrderRenderer();

    public final String createHtml(RendererContext context, String style, Table table, String onclick) {
        StringBuilder builder = new StringBuilder();
        
        writeTableStart(context, builder, style);
        writeColGroups(context, builder, table.getColumns());
        writeTableHeadStart(context, builder);
        writeHeadRowStart(context, builder, table.getColumns());
        for(Column column : table.getColumns()) {
            writeHeadCellStart(context, builder, column);
            if(column.getMessageKey() != null) {
                writeHeadCellContent(context, builder, table.getColumns(), column);
            }
            writeHeadCellEnd(context, builder, column);
        }
        writeHeadRowEnd(context, builder, table.getColumns());
        writeTableHeadEnd(context, builder);
        writeTableBodyStart(context, builder);
        for(Row row : table.getRows()) {
            writeRowStart(context, builder, row);
            for(int i=0; i<row.getColumns().length; i++) {
                Object cell = row.getColumns()[i];
                writeCellStart(context, builder, row, table.getColumns()[i], cell, onclick);
                writeCellContent(context, builder, row, table.getColumns()[i], cell);
                writeCellEnd(context, builder, row, table.getColumns()[i], cell);
            }
            writeRowEnd(context, builder, row);
        }
        if(table.getRows().isEmpty()) {
            writeEmptyRow(context, builder, table.getColumns());
        }
        writeTableBodyEnd(context, builder);
        writeTableEnd(context, builder);
        
        return builder.toString();
    }

    protected void writeColGroups(RendererContext context, StringBuilder builder, Column[] columns) {
        builder.append("<colgroup>");
        for(Column column : columns) {
            builder.append("<col width=\"1*\"/>");
        }
        builder.append("</colgroup>\n");
    }
    
    protected void writeTableStart(RendererContext context, StringBuilder builder, String style) {
        String styleClass = context.getSettings().getProperty(TABLE_CLASS);

        builder.append("\n<table");
        if(styleClass != null) {
            builder.append(" class=\"").append(styleClass).append("\"");
        }
        if(style != null) {
            builder.append(" style=\"").append(style).append("\"");
        }
        builder.append(">\n");
    }

    protected void writeTableEnd(RendererContext context, StringBuilder builder) {
        builder.append("</table>\n");
    }

    protected void writeTableHeadStart(RendererContext context, StringBuilder builder) {
        builder.append("<thead>\n");
    }

    protected void writeTableHeadEnd(RendererContext context, StringBuilder builder) {
        builder.append("</thead>\n");
    }

    protected void writeTableBodyStart(RendererContext context, StringBuilder builder) {
        builder.append("<tbody>\n");
    }

    protected void writeTableBodyEnd(RendererContext context, StringBuilder builder) {
        builder.append("</tbody>\n");
    }

    protected void writeRowStart(RendererContext context, StringBuilder builder, Row row) {
        String styleClass       = context.getSettings().getProperty(ROW_CLASS);
        String styleClassActive = context.getSettings().getProperty(ROW_ACTIVE_CLASS);

        builder.append("<tr");
        if(styleClass != null) {
            builder.append(" class=\"").append(styleClass).append("\"");
        }
        if(styleClassActive != null) {
            builder.append(" onmouseover=\"this.className='").append(styleClassActive).append("';\"");
            if(styleClass != null) {
                builder.append(" onmouseout=\"this.className='").append(styleClass).append("';\"");
            } else {
                builder.append(" onmouseout=\"this.className=null;\"");
            }
        }
        builder.append(">");
    }
    
    protected void writeRowEnd(RendererContext context, StringBuilder builder, Row row) {
        builder.append("</tr>\n");
    }
    
    protected void writeCellStart(RendererContext context, StringBuilder builder, Row row, Column column, Object value, String onclick) {
        builder.append("<td");
        if(onclick != null) {
            builder.append(" onclick=\"").append(onclick.replaceAll("\\{id\\}", Integer.toString(row.getId()))).append("\"");
        }
        builder.append(">");
    }
    
    protected void writeCellContent(RendererContext context, StringBuilder builder, Row row, Column column, Object value) {
        builder.append(column.getFormatter().format(context.getResources(), context.getLocale(), value));
    }

    protected void writeCellEnd(RendererContext context, StringBuilder builder, Row row, Column column, Object value) {
        builder.append("</td>");
    }
    
    protected void writeHeadCellStart(RendererContext context, StringBuilder builder, Column column) {
        builder.append("<th>");
    }
    
    protected void writeHeadCellContent(RendererContext context, StringBuilder builder, Column[] columns, Column column) {
        if(column.isSortable()) {
            builder.append(sortRenderer.createHtml(context, columns, column));
        } else {
            builder.append(context.getResources().getMessage(context.getLocale(), column.getMessageKey()));
        }
    }

    protected void writeHeadCellEnd(RendererContext context, StringBuilder builder, Column column) {
        builder.append("</th>");
    }
    
    protected void writeHeadRowStart(RendererContext context, StringBuilder builder, Column[] columns) {
        builder.append("<tr>");
    }
    
    protected void writeHeadRowEnd(RendererContext context, StringBuilder builder, Column[] columns) {
        builder.append("</tr>\n");
    }
    
    protected void writeEmptyRow(RendererContext context, StringBuilder builder, Column[] columns) {
        String styleClass = context.getSettings().getProperty(ROW_NOENTRIES_CLASS);
        String message    = context.getSettings().getProperty(ROW_NOENTRIES_MESSAGEKEY);
        
        builder.append("<tr");
        if(styleClass != null) {
            builder.append(" class=\"").append(styleClass).append("\"");
        }
        builder.append(">");
        builder.append("<td");
        builder.append(" colspan=\"").append(columns.length).append("\"");
        builder.append(">");
        builder.append(context.getResources().getMessage(context.getLocale(), message));
        builder.append("</td>");
        builder.append("</tr>\n");
    }
}
