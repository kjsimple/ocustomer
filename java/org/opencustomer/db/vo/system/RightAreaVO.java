package org.opencustomer.db.vo.system;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.framework.db.vo.BaseVO;

@Entity
@Table(name = "right_area")
@AttributeOverride(name = "id", column = @Column(name = "right_area_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class RightAreaVO extends BaseVO
{
    private static final long serialVersionUID = 3617007559538718773L;

    private String label;

    private String namekey;

    private int sort;

    private Set<RightGroupVO> rightGroups = new HashSet<RightGroupVO>();

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

    @OneToMany(mappedBy = "rightArea", fetch = FetchType.EAGER)
    public Set<RightGroupVO> getRightGroups()
    {

        return rightGroups;
    }

    public void setRightGroups(Set<RightGroupVO> rightGroups)
    {
        this.rightGroups = rightGroups;
    }

    protected void toString(ToStringBuilder builder)
    {
        builder.append("namekey=" + namekey);
        builder.append("sort=" + sort);
    }

}
