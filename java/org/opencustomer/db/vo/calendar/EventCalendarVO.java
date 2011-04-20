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
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.db.vo.VersionedVO;

@Entity
@Table(name = "event_calendar")
@AttributeOverride(name = "id", column = @Column(name = "event_calendar_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class EventCalendarVO extends VersionedVO implements EntityAccess
{
    private static final long serialVersionUID = 3256728359722431795L;

    public static enum ParticipiantType
    {
        HOST,
        GUEST;
    }
    
    public static enum InvitationStatus
    {
        NEW,
        ACCEPTED,
        REJECTED,
        DELETED;
    }
    
    private EventVO event;

    private CalendarVO calendar;
    
    private ParticipiantType participiantType;
    
    private InvitationStatus invitationStatus;
    
    private Integer ownerUser;

    private Integer ownerGroup;

    private Access accessUser;

    private Access accessGroup;

    private Access accessGlobal;
    
    public EventCalendarVO()
    {        
    }
    
    public EventCalendarVO(EventVO event, CalendarVO calendar)
    {
        this.event = event;
        this.calendar = calendar;
    }
    
    @Column(name = "participiant_type")
    @Enumerated(EnumType.STRING)
    public ParticipiantType getParticipiantType()
    {
        return participiantType;
    }

    public void setParticipiantType(ParticipiantType participiantType)
    {
        this.participiantType = participiantType;
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
    @JoinColumn(name = "event_id")
    public EventVO getEvent()
    {
        return event;
    }

    public void setEvent(EventVO event)
    {
        this.event = event;
    }
    
    @ManyToOne
    @JoinColumn(name = "calendar_id")
    public CalendarVO getCalendar()
    {
        return calendar;
    }

    public void setCalendar(CalendarVO calendar)
    {
        this.calendar = calendar;
    }

    @Column(name = "owner_user")
    public Integer getOwnerUser()
    {
        return ownerUser;
    }

    public void setOwnerUser(Integer ownerUser)
    {
        this.ownerUser = ownerUser;
    }

    @Column(name = "owner_group")
    public Integer getOwnerGroup()
    {
        return ownerGroup;
    }

    public void setOwnerGroup(Integer ownerGroup)
    {
        this.ownerGroup = ownerGroup;
    }

    @Column(name = "access_user")
    @Enumerated(EnumType.STRING)
    public Access getAccessUser()
    {
        return accessUser;
    }

    public void setAccessUser(Access accessUser)
    {
        this.accessUser = accessUser;
    }

    @Column(name = "access_group")
    @Enumerated(EnumType.STRING)
    public Access getAccessGroup()
    {
        return accessGroup;
    }

    public void setAccessGroup(Access accessGroup)
    {
        this.accessGroup = accessGroup;
    }
    
    @Column(name = "access_global")
    @Enumerated(EnumType.STRING)
    public Access getAccessGlobal()
    {
        return accessGlobal;
    }

    public void setAccessGlobal(Access accessGlobal)
    {
        this.accessGlobal = accessGlobal;
    }
    
    public void toString(ToStringBuilder builder)
    {
        builder.append("participiantType", participiantType);
        builder.append("invitationStatus", invitationStatus);
        builder.append("event", event == null ? "" : event.getId());
        builder.append("calendar", calendar == null ? "" : calendar.getId());
        builder.append("ownerUser", ownerUser);
        builder.append("ownerGroup", ownerGroup);
        builder.append("accessUser", accessUser);
        builder.append("accessGroup", accessGroup);
        builder.append("accessGlobal", accessGlobal);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        boolean isEqual = false;

        if (obj == null)
            isEqual = false;
        else if (this == obj)
            isEqual = true;
        else if (!(obj instanceof EventCalendarVO))
            isEqual = false;
        else
        {
            EventCalendarVO castObj = (EventCalendarVO) obj;

            EqualsBuilder builder = new EqualsBuilder();

            builder.append(this.getCalendar().getId(), castObj.getCalendar().getId());
            builder.append(this.getEvent().getId(), castObj.getEvent().getId());

            isEqual = builder.isEquals();
        }

        return isEqual;
    }

    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        if(getCalendar() != null)
            builder.append(getCalendar().getId());
        else
            builder.append(0);
        
        if(getEvent() != null)
            builder.append(getEvent().getId());
        else
            builder.append(0);

        return builder.toHashCode();
    }
}
