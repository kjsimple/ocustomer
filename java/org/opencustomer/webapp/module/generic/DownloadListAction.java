package org.opencustomer.webapp.module.generic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.opencustomer.db.vo.system.ListConfigurationVO;
import org.opencustomer.framework.db.util.engine.Row;
import org.opencustomer.framework.util.CSVFormat;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.panel.PanelStack;
import org.opencustomer.framework.webapp.struts.ActionForm;
import org.opencustomer.framework.webapp.struts.DownloadAction;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.framework.webapp.util.html.Column;
import org.opencustomer.framework.webapp.util.html.Table;

public class DownloadListAction extends DownloadAction {
    
    private final static Logger log = Logger.getLogger(DownloadListAction.class);
    
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmm");
    
    @Override
    protected StreamInfo getStreamInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PanelStack stack = Panel.getPanelStack(request);
        
        CvsStreamInfo streamInfo = new CvsStreamInfo();
        
        if(stack.isEmpty() || stack.peek().getAttribute(DynamicListAction.TABLE_KEY) == null) {
            throw new IllegalArgumentException("no panel or table found");
        } else {
            Table table = (Table)stack.peek().getAttribute(DynamicListAction.TABLE_KEY);
            table.query(true);
            
            ArrayList<String> header = new ArrayList<String>();
            for(Column column : table.getColumns()) {
                header.add(removeHTML(MessageUtil.message(request, column.getMessageKey())));
            }
            streamInfo.addLine(header);
       
            for(Row row : table.getRows()) {
                ArrayList<String> values = new ArrayList<String>();
                for(int i=0; i<row.getColumns().length; i++) {
                    Object cell = row.getColumns()[i];
                    if(cell == null) {
                        values.add("");
                    } else { 
                        String value = table.getColumns()[i].getFormatter().format(
                                (MessageResources)request.getSession().getServletContext().getAttribute(Globals.MESSAGES_KEY), 
                                (Locale)request.getSession().getAttribute(Globals.LOCALE_KEY), 
                                cell);
                        values.add(removeHTML(value));
                    }
                }
                
                streamInfo.addLine(values);
            }
            
            String fileName = sdf.format(new Date())+" ";
            if(ListConfigurationVO.Type.PERSON.getName().equals(table.getName())) {
                fileName += MessageUtil.message(request, "entity.system.listConfiguration.type.persons");
            } else if(ListConfigurationVO.Type.COMPANY.getName().equals(table.getName())) {
                fileName += MessageUtil.message(request, "entity.system.listConfiguration.type.companies");
            } else {
                fileName += "download";
            }
            fileName += ".csv";
            
            response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
        }
        
        return streamInfo;
    }
    
    private static String removeHTML(String value) {
        value = value.replaceAll("&nbsp;", " ");
        value = value.replaceAll("&#160;", " ");
        value = value.replaceAll("&amp;", "&");
        
        return value;
    }
    
    public static class CvsStreamInfo implements StreamInfo {
        private CSVFormat format = new CSVFormat();
        
        private StringBuilder data = new StringBuilder();
        
        public String getContentType() {
            return format.getMimeType();
        }
        
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data.toString().getBytes());
        }
        
        public void addLine(List<String> values) {
            data.append(format.format(values)).append(format.getLineSeperator());
        }
    }
}