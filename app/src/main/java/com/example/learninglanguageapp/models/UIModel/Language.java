package com.example.learninglanguageapp.models.UIModel;

public class Language {
    private String name;
    private int languageId;
    private int flagResId;


    public Language(String name, int flagResId,int languageId) {
        this.name = name;
        this.flagResId = flagResId;
        this.languageId = languageId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public String getName() {
        return name;
    }

    public int getFlagResId() {
        return flagResId;
    }
}
