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

package org.opencustomer.db.vo.crm;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.framework.db.vo.VersionedVO;

@Entity
@Table(name = "address")
@AttributeOverride(name = "id", column = @Column(name = "address_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class AddressVO extends VersionedVO
{
    private static final long serialVersionUID = 3906645319163656497L;

    public static enum Type {
        MAIN,
        POSTAL,
        BILLING,
        DELIVERY;
    }

    private Type type;

    private String name;

    private String street;

    private String postbox;

    private String zip;

    private String city;

    private PersonVO person;

    private CompanyVO company;

    private int version;
    
    public AddressVO()
    {

    }

    public AddressVO(CompanyVO company, Type type)
    {
        setCompany(company);
        setType(type);
    }

    @Column(name = "city")
    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    @ManyToOne
    @JoinColumn(name = "company_id")
    public CompanyVO getCompany()
    {
        return company;
    }

    public void setCompany(CompanyVO company)
    {
        this.company = company;
    }

    @Column(name = "name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "person_id")
    public PersonVO getPerson()
    {
        return person;
    }

    public void setPerson(PersonVO person)
    {
        this.person = person;
    }

    @Column(name = "postbox")
    public String getPostbox()
    {
        return postbox;
    }

    public void setPostbox(String postbox)
    {
        this.postbox = postbox;
    }

    @Column(name = "street")
    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    @Column(name = "address_type")
    @Enumerated(EnumType.STRING)
    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    @Column(name = "zip")
    public String getZip()
    {
        return zip;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }

    protected void toString(ToStringBuilder builder)
    {
        builder.append("type", type);
        builder.append("name", name);
        builder.append("street", street);
        builder.append("postbox", postbox);
        builder.append("zip", zip);
        builder.append("city", city);
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean isEqual = false;

        if (obj == null)
            isEqual = false;
        else if (this == obj)
            isEqual = true;
        else if (!(obj instanceof AddressVO))
            isEqual = false;
        else
        {
            AddressVO castObj = (AddressVO) obj;

            EqualsBuilder builder = new EqualsBuilder();

            builder.append(this.getCompany(), castObj.getCompany());
            builder.append(this.getType(), castObj.getType());

            isEqual = builder.isEquals();
        }

        return isEqual;
    }

    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(getCompany());
        builder.append(getType());

        return builder.toHashCode();
    }

}
