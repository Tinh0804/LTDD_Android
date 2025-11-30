package com.example.learninglanguageapp.models;
import java.util.List;

public class UnitWithLessons {
    private Unit unit;
    private List<Lesson> lessons;
    public UnitWithLessons(Unit unit, List<Lesson> lessons) {
        this.unit = unit;
        this.lessons = lessons;
    }
    public Unit getUnit() {
        return unit;
    }
    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    public List<Lesson> getLessons() {
        return lessons;
    }
    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}