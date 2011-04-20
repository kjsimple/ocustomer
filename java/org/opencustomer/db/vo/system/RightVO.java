package org.opencustomer.db.vo.system;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.framework.db.vo.BaseVO;

@Entity
@Table(name = "rights")
@AttributeOverride(name = "id", column = @Column(name = "right_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class RightVO extends BaseVO
{
    private static final long serialVersionUID = 3257847684068291891L;

    public static enum Type {
        READ,
        WRITE,
        OTHER;
    }

    private String label;

    private String namekey;

    private Type type;

    private int sort;

    private RightGroupVO rightGroup;

    @Column(name = "label", updatable = false, nullable = false)
    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    @Column(name = "name_key", updatable = false, nullable = false)
    public String getNamekey()
    {
        return namekey;
    }

    public void setNamekey(String namekey)
    {
        this.namekey = namekey;
    }

    @Column(name = "sort", updatable = false, nullable = false)
    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    @Column(name = "right_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "right_group_id")
    public RightGroupVO getRightGroup()
    {
        return rightGroup;
    }

    public void setRightGroup(RightGroupVO rightGroup)
    {
        this.rightGroup = rightGroup;
    }

    protected void toString(ToStringBuilder builder)
    {
        builder.append("label=" + label);
        builder.append("namekey=" + namekey);
        builder.append("type=" + type);
        builder.append("sort=" + sort);

    }

}
