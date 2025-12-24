package com.example.learninglanguageapp.models.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "languages")
public class LanguageEntity {

    @PrimaryKey
    private int languageId;

    private String languageName;
    private String languageCode;
    private String flagIcon;

    // ===== Constructor =====
    public LanguageEntity(int languageId, String languageName, String languageCode, String flagIcon) {
        this.languageId = languageId;
        this.languageName = languageName;
        this.languageCode = languageCode;
        this.flagIcon = flagIcon;
    }

    // ===== Getter & Setter =====
    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getFlagIcon() {
        return flagIcon;
    }

    public void setFlagIcon(String flagIcon) {
        this.flagIcon = flagIcon;
    }
}
