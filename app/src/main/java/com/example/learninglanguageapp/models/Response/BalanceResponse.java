package com.example.learninglanguageapp.models.Response;


public class BalanceResponse {
    private int diamonds;
    private int hearts;

    public BalanceResponse() {
    }

    public BalanceResponse(int diamonds, int hearts) {
        this.diamonds = diamonds;
        this.hearts = hearts;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }
}