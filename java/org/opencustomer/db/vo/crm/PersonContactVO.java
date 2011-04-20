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
import org.opencustomer.framework.db.vo.BaseVO;

@Entity
@Table(name = "person_contact")
@AttributeOverride(name = "id", column = @Column(name = "person_contact_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class PersonContactVO extends BaseVO
{
    private static final long serialVersionUID = 3258689909941679409L;

    public static enum Type {
        NONE,
        SENDER,
        TO,
        CC,
        BCC;
    }

    private Type relationType = Type.NONE;

    private PersonVO person;

    private CompanyVO company;
    
    private ContactVO contact;
    
    public PersonContactVO()
    {

    }

    public PersonContactVO(PersonVO person, ContactVO contact)
    {
        this();
        setPerson(person);
        setCompany(person.getCompany());
        setContact(contact);
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
    @JoinColumn(name = "contact_id")
    public ContactVO getContact()
    {
        return contact;
    }

    public void setContact(ContactVO contact)
    {
        this.contact = contact;
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

    @Column(name = "relation_type")
    @Enumerated(EnumType.STRING)
    public Type getRelationType()
    {
        return relationType;
    }

    public void setRelationType(Type relationType)
    {
        this.relationType = relationType;
    }

    protected void toString(ToStringBuilder builder)
    {
        builder.append("person", person == null ? "null" : person.getId());
        builder.append("company", company == null ? "null" : company.getId());
        builder.append("contact", contact == null ? "null" : contact.getId());
        builder.append("relationType", relationType);
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean isEqual = false;

        if (obj == null)
            isEqual = false;
        else if (this == obj)
            isEqual = true;
        else if (!(obj instanceof PersonContactVO))
            isEqual = false;
        else
        {
            PersonContactVO castObj = (PersonContactVO) obj;

            EqualsBuilder builder = new EqualsBuilder();

            builder.append(this.getPerson().getId(), castObj.getPerson().getId());
            builder.append(this.getContact().getId(), castObj.getContact().getId());

            isEqual = builder.isEquals();
        }

        return isEqual;
    }

    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(getPerson().getId());
        builder.append(getContact().getId());

        return builder.toHashCode();
    }
}
