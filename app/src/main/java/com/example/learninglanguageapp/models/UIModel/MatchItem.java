package com.example.learninglanguageapp.models.UIModel;


public class MatchItem {
    private String word;
    private String imageResource;
    private boolean isImage;
    private boolean isMatched;
    private String matchId; // ID để ghép đúng cặp

    public MatchItem(String word, String imageResource, boolean isImage, String matchId) {
        this.word = word;
        this.imageResource = imageResource;
        this.isImage = isImage;
        this.matchId = matchId;
        this.isMatched = false;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
}