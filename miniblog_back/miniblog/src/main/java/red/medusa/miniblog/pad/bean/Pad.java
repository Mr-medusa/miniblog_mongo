package red.medusa.miniblog.pad.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Pad implements Serializable {
    private String id;
    private String parentId;
    private String name;
    private String type;
    private Boolean isShow = true;
    private Boolean isChecked = false;
    private Boolean isStretch = true;
    private Boolean isDefault = false;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date updateTime;
    private List<Pad> children;

    private Integer order;

    public Pad() {
    }

    public Pad(String id, String parentId, String name, String type, Boolean isShow, Boolean isChecked, Boolean isStretch, Date createTime, Date updateTime, Integer order,Boolean isDefault,List<Pad> children) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
        this.isShow = isShow;
        this.isChecked = isChecked;
        this.isStretch = isStretch;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.order = order;
        this.isDefault = isDefault;
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean show) {
        isShow = show;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean checked) {
        isChecked = checked;
    }

    public Boolean getIsStretch() {
        return isStretch;
    }

    public void setIsStretch(Boolean stretch) {
        isStretch = stretch;
    }

    public List<Pad> getChildren() {
        return children;
    }

    public void setChildren(List<Pad> children) {
        this.children = children;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Integer getOrder() {
        if (order == null) {
            return Integer.MAX_VALUE;
        }
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Pad{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", isShow=" + isShow +
                ", isChecked=" + isChecked +
                ", isStretch=" + isStretch +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", children=" + children +
                ", order=" + order +
                '}';
    }
}
