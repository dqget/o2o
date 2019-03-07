package com.lovesickness.o2o.entity;

import java.util.Date;

public class Area {
    private Long areaId;//ID
    private String areaName;//名称
    //private String areaDesc;
    private Integer priority;//权重
    private Date createTime;//创建时间
    private Date lastEditTime;//更新时间

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    //	public String getAreaDesc() {
//		return areaDesc;
//	}
//	public void setAreaDesc(String areaDesc) {
//		this.areaDesc = areaDesc;
//	}
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    @Override
    public String toString() {
        return "Area [areaId=" + areaId + ", areaName=" + areaName + ", priority=" + priority + ", createTime="
                + createTime + ", lastEditTime=" + lastEditTime + "]";
    }

}
