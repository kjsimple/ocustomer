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

package org.opencustomer.db.vo.crm.custom;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.framework.db.vo.BaseVO;

public final class CompanyListVO extends BaseVO
{
    private static final long serialVersionUID = 3256721775604674617L;

    private String companyName;

    private String companyStateName;

    private String companyStateNameKey;

    private String companyTypeName;

    private String companyTypeNameKey;

    private String sectorName;

    private String sectorNameKey;

    private String categoryName;

    private String categoryNameKey;

    private String ratingName;

    private String ratingNameKey;

    private Date lastContactDate;
    
    
    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getCompanyStateName()
    {
        return companyStateName;
    }

    public void setCompanyStateName(String companyStateName)
    {
        this.companyStateName = companyStateName;
    }

    public String getCompanyTypeName()
    {
        return companyTypeName;
    }

    public void setCompanyTypeName(String companyTypeName)
    {
        this.companyTypeName = companyTypeName;
    }

    public Date getLastContactDate()
    {
        return lastContactDate;
    }

    public void setLastContactDate(Date lastContactDate)
    {
        this.lastContactDate = lastContactDate;
    }

    public String getSectorName()
    {
        return sectorName;
    }

    public void setSectorName(String sectorName)
    {
        this.sectorName = sectorName;
    }

    public final String getCompanyStateNameKey()
    {
        return companyStateNameKey;
    }

    public final void setCompanyStateNameKey(String companyStateNameKey)
    {
        this.companyStateNameKey = companyStateNameKey;
    }

    public final String getCompanyTypeNameKey()
    {
        return companyTypeNameKey;
    }

    public final void setCompanyTypeNameKey(String companyTypeNameKey)
    {
        this.companyTypeNameKey = companyTypeNameKey;
    }

    public final String getSectorNameKey()
    {
        return sectorNameKey;
    }

    public final void setSectorNameKey(String sectorNameKey)
    {
        this.sectorNameKey = sectorNameKey;
    }

    public final String getCategoryName()
    {
        return categoryName;
    }

    public final void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public final String getCategoryNameKey()
    {
        return categoryNameKey;
    }

    public final void setCategoryNameKey(String categoryNameKey)
    {
        this.categoryNameKey = categoryNameKey;
    }

    public final String getRatingName()
    {
        return ratingName;
    }

    public final void setRatingName(String ratingName)
    {
        this.ratingName = ratingName;
    }
    
    public final String getRatingNameKey()
    {
        return ratingNameKey;
    }
    
    public final void setRatingNameKey(String ratingNameKey)
    {
        this.ratingNameKey = ratingNameKey;
    }

    protected void toString(ToStringBuilder builder)
    {
        builder.append("companyName", companyName);
        builder.append("companyStateName", companyStateName);
        builder.append("companyStateNameKey", companyStateNameKey);
        builder.append("companyTypeName", companyTypeName);
        builder.append("companyTypeNameKey", companyTypeNameKey);
        builder.append("sectorName", sectorName);
        builder.append("sectorNameKey", sectorNameKey);
        builder.append("sectorName", categoryName);
        builder.append("sectorNameKey", categoryNameKey);
        builder.append("sectorName", ratingName);
        builder.append("sectorNameKey", ratingNameKey);
        builder.append("lastContactDate", lastContactDate);
    }

}
