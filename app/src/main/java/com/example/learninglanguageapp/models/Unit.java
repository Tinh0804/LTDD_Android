package com.example.learninglanguageapp.models;

import java.io.Serializable;
import java.util.List;

public class Unit implements Serializable {
    private int unitId;
    private String unitName;
    private int orderIndex;
    private boolean isLocked;
    private List<Lesson> lessons; 

    public Unit() {}

    public int getUnitId() { return unitId; }
    public void setUnitId(int unitId) { this.unitId = unitId; }
    public String getUnitName() { return unitName; }
    public void setUnitName(String unitName) { this.unitName = unitName; }
    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }
    public boolean isLocked() { return isLocked; }
    public void setLocked(boolean locked) { isLocked = locked; }
    public List<Lesson> getLessons() { return lessons; }
    public void setLessons(List<Lesson> lessons) { this.lessons = lessons; }
}
