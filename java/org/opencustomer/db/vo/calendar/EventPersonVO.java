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

package org.opencustomer.db.vo.calendar;

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
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.framework.db.vo.VersionedVO;

@Entity
@Table(name = "event_person")
@AttributeOverride(name = "id", column = @Column(name = "event_person_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class EventPersonVO extends VersionedVO
{
    private static final long serialVersionUID = 3256728359722431795L;

    public static enum InvitationStatus
    {
        NEW,
        ACCEPTED,
        REJECTED,
        DELETED;
    }
    
    private InvitationStatus invitationStatus;
    
    private EventVO event;
    
    private PersonVO person;

    public EventPersonVO() 
    {
        
    }
    
    public EventPersonVO(EventVO event, PersonVO person) 
    {
        this.event = event;
        this.person = person;
    }
    
    @Column(name = "invitation_status")
    @Enumerated(EnumType.STRING)
    public InvitationStatus getInvitationStatus()
    {
        return invitationStatus;
    }

    public void setInvitationStatus(InvitationStatus invitationStatus)
    {
        this.invitationStatus = invitationStatus;
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

    @ManyToOne
    @JoinColumn(name = "event_id")
    public EventVO getEvent()
    {
        return event;
    }

    public void setEvent(EventVO event)
    {
        this.event = event;
    }
    
    public void toString(ToStringBuilder builder)
    {
        builder.append("invitationStatus", invitationStatus);
        builder.append("event", event == null ? "" : event.getId());
        builder.append("person", person == null ? "" : person.getId());
    }
    
    @Override
    public boolean equals(Object obj)
    {
        boolean isEqual = false;

        if (obj == null)
            isEqual = false;
        else if (this == obj)
            isEqual = true;
        else if (!(obj instanceof EventPersonVO))
            isEqual = false;
        else
        {
            EventPersonVO castObj = (EventPersonVO) obj;

            EqualsBuilder builder = new EqualsBuilder();

            builder.append(this.getPerson().getId(), castObj.getPerson().getId());
            builder.append(this.getEvent().getId(), castObj.getEvent().getId());

            isEqual = builder.isEquals();
        }

        return isEqual;
    }

    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        if(getPerson() != null)
            builder.append(getPerson().getId());
        else
            builder.append(0);
        if(getEvent() != null)
            builder.append(getEvent().getId());
        else
            builder.append(0);

        return builder.toHashCode();
    }
}
