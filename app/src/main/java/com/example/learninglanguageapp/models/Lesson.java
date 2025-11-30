package com.example.learninglanguageapp.models;

import java.io.Serializable;
import java.util.List;

public class Lesson  implements Serializable {
    private int lessonId;
    private int unitId;
    private String lessonName;
    private int orderIndex;
    private int experienceReward;
    private boolean unlockRequired;
    private String unitName;

    public Lesson() {}

    public int getLessonId() { return lessonId; }
    public void setLessonId(int lessonId) { this.lessonId = lessonId; }
    public int getUnitId() { return unitId; }
    public void setUnitId(int unitId) { this.unitId = unitId; }
    public String getLessonName() { return lessonName; }
    public void setLessonName(String lessonName) { this.lessonName = lessonName; }
    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }
    public int getExperienceReward() { return experienceReward; }
    public void setExperienceReward(int experienceReward) { this.experienceReward = experienceReward; }
    public boolean isUnlockRequired() { return unlockRequired; }
    public void setUnlockRequired(boolean unlockRequired) { this.unlockRequired = unlockRequired; }
    public String getUnitName() { return unitName; }
    public void setUnitName(String unitName) { this.unitName = unitName; }
}