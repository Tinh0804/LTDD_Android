package com.example.learninglanguageapp.models;

import java.io.Serializable;
import java.util.List;

public class Unit implements Serializable {
    private int unitId;
    private int courseId;
    private String unitName;
    private int orderIndex;
    private boolean unlockRequired;
    private List<Lesson> lessons; // Danh sách các bài học trong Unit

    public Unit() {}

    public Unit(int unitId, int courseId, String unitName, int orderIndex) {
        this.unitId = unitId;
        this.courseId = courseId;
        this.unitName = unitName;
        this.orderIndex = orderIndex;
        this.unlockRequired = false;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public boolean isUnlockRequired() {
        return unlockRequired;
    }

    public void setUnlockRequired(boolean unlockRequired) {
        this.unlockRequired = unlockRequired;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
