package com.example.learninglanguageapp.models;

import java.io.Serializable;
import java.util.List;

public class Unit implements Serializable {
    private int unitId;
    private int courseId;
    private String unitName;
    private int orderIndex;
    private boolean isLocked;
    private int totalLessons; // Thêm
    private int completedLessons; // Thêm

    public Unit() {}

    public Unit(int unitId, int courseId, String unitName, int orderIndex, boolean isLocked, int totalLessons, int completedLessons) {
        this.unitId = unitId;
        this.courseId = courseId;
        this.unitName = unitName;
        this.orderIndex = orderIndex;
        this.isLocked = isLocked;
        this.totalLessons = totalLessons;
        this.completedLessons = completedLessons;
    }

    public int getUnitId() {
        return unitId;
    }
    public int getCourseId() {
        return courseId;
    }
    public String getUnitName() {
        return unitName;
    }
    public int getOrderIndex() {
        return orderIndex;
    }
    public boolean isLocked() {
        return isLocked;
    }
    public int getTotalLessons() {
        return totalLessons;
    }
    public int getCompletedLessons() {
        return completedLessons;
    }
    public void setUnitId(int unitId) { this.unitId = unitId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public void setUnitName(String unitName) { this.unitName = unitName; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }
    public void setLocked(boolean locked) { isLocked = locked; }
    public void setTotalLessons(int totalLessons) { this.totalLessons = totalLessons; }
    public void setCompletedLessons(int completedLessons) { this.completedLessons = completedLessons; }
}