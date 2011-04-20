package org.opencustomer.db.vo.system;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.framework.db.vo.BaseVO;

@Entity
@Table(name = "right_group")
@AttributeOverride(name = "id", column = @Column(name = "right_group_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class RightGroupVO extends BaseVO
{

    private static final long serialVersionUID = 3257570594269508401L;

    private String label;

    private String namekey;

    private int sort;

    private Set<RightVO> rights = new HashSet<RightVO>();

    private RightAreaVO rightArea;

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

    @OneToMany(mappedBy = "rightGroup", fetch = FetchType.EAGER)
    public Set<RightVO> getRights()
    {
        return rights;
    }

    public void setRights(Set<RightVO> rights)
    {
        this.rights = rights;
    }

    @ManyToOne
    @JoinColumn(name = "right_area_id")
    public RightAreaVO getRightArea()
    {
        return rightArea;
    }

    public void setRightArea(RightAreaVO rightArea)
    {
        this.rightArea = rightArea;
    }

    protected void toString(ToStringBuilder builder)
    {
        builder.append("namekey=" + namekey);
        builder.append("sort=" + sort);
    }
}
