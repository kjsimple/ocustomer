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

package org.opencustomer.webapp.module.generic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.ImageButtonBean;
import org.opencustomer.db.dao.system.ListConfigurationDAO;
import org.opencustomer.db.vo.system.ListConfigurationVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.util.html.Table;
import org.opencustomer.util.configuration.UserConfiguration;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.util.table.TableFactory;

public abstract class DynamicListAction<E extends DynamicListForm> extends org.opencustomer.framework.webapp.action.ListAction<E> {
    private static Logger log = Logger.getLogger(DynamicListAction.class);

    protected abstract ListConfigurationVO.Type getType();
    
    @Override
    protected final boolean isAdditionalButtonUsed(E form) {
        return form.getDoChooseList() != null || form.getDoEditList().isSelected() || form.getDoAddList().isSelected();
    }
    
    @Override
    protected final void search(Panel panel, E form, boolean formCached, ActionMessages errors, HttpServletRequest request, HttpServletResponse response) {
        final ListConfigurationVO.Type type = getType();
        
        UserVO user            = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
        UserConfiguration conf = (UserConfiguration)request.getSession().getAttribute(Globals.CONFIGURATION_KEY);
        
        ListConfigurationVO choosenList = (ListConfigurationVO)panel.getAttribute("choosenList");
        
        if(panel.getAttribute("listConfigurations") == null) {
            List<ListConfigurationVO> listConfigurations = new ListConfigurationDAO().getByTypeAndUser(type, user);
            if(!listConfigurations.isEmpty()) {
                ListBean bean = new ListBean();
                for(ListConfigurationVO vo : listConfigurations) {
                    List<ListConfigurationVO> group = bean.getGroups().get(vo.getUser() == null);
                    group.add(vo);
                }
                
                if(!listConfigurations.contains(choosenList)) {
                    if(log.isDebugEnabled())
                        log.debug("list is not yet available: "+choosenList);
                    panel.removeAttribute("choosenList");
                }
                
                panel.setAttribute("listConfigurations", bean);
            }
        }
        
        if(form.getDoChooseList() != null) {
            if(log.isDebugEnabled())
                log.debug("load new list configuration: "+form.getListId());
            
            panel.removeAttribute(TABLE_KEY);
            
            choosenList = new ListConfigurationDAO().getById(form.getListId());

            form.setDoChooseList(null);
        } else if(panel.getAttribute(TABLE_KEY) == null) {
            if(log.isDebugEnabled())
                log.debug("load default configuration: ");
            
            if(choosenList == null)
                choosenList = new ListConfigurationDAO().getDefaultListConfiguration(type, user);
            else
                choosenList = new ListConfigurationDAO().getById(choosenList.getId());
            
            if(choosenList != null)
                form.setListId(choosenList.getId());
        }
        
        if(choosenList != null)
            panel.setAttribute("choosenList", choosenList);
        
        // query database
        Table table = (Table)panel.getAttribute(TABLE_KEY);
        if(table == null) {
            Integer[] columns = null;
            if(choosenList != null)
                columns = choosenList.getColumns();
            table = TableFactory.getInstance().createTable(type.getName(), user, columns);
            table.getPage().setStep(conf.getIntValue(UserConfiguration.Key.LIST_NUMBER_ROWS));
            panel.setAttribute(TABLE_KEY, table);
        }
        
        if(!formCached && !form.getDoResetSearch().isSelected()) {
            table.loadSearch(errors, request);
        }
        table.getPage().setPage(form.getPage());
        table.getOrder().add(form.getOrder());

        table.query();   
    }

    @Override
    protected final ActionForward findForward(ActionMapping mapping, E form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(form.getDoAddList().isSelected()) {
            form.setDoAddList(new ImageButtonBean());
            
            return mapping.findForward("listConfiguration");
        } else if(form.getDoEditList().isSelected()) {
            form.setDoEditList(new ImageButtonBean());
            request.setAttribute("external_listConfiguration_id", form.getListId());
            
            return mapping.findForward("listConfiguration");
        } else {
            return super.findForward(mapping, form, request, response);
        }
    }
    
    public final class ListBean {
        private LinkedHashMap<Boolean, List<ListConfigurationVO>> groups = new LinkedHashMap<Boolean, List<ListConfigurationVO>>();

        public ListBean() {
            groups.put(Boolean.TRUE, new ArrayList<ListConfigurationVO>());
            groups.put(Boolean.FALSE, new ArrayList<ListConfigurationVO>());
        }
        
        public LinkedHashMap<Boolean, List<ListConfigurationVO>> getGroups() {
            return groups;
        }

        public void setGroups(LinkedHashMap<Boolean, List<ListConfigurationVO>> groups) {
            this.groups = groups;
        }

        public List<ListConfigurationVO> getGlobalList() {
            return groups.get(Boolean.TRUE);
        }

        public List<ListConfigurationVO> getUserList() {
            return groups.get(Boolean.FALSE);
        }
    }
    
}
