package com.example.learninglanguageapp.models.UIModel;


public class Level {
    private String name;
    private String desc;
    private int icon;
    private int id;

    public Level(String name, String desc, int icon,int id) {
        this.name = name;
        this.desc = desc;
        this.icon = icon;
        this.id  = id;
    }

    public String getName() { return name; }
    public String getDesc() { return desc; }
    public int getIcon() { return icon; }

    public int getId() { return id; }
}
