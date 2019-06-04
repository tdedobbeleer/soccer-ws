package com.soccer.ws.dto;

/**
 * Created by u0090265 on 10/10/15.
 */
public class AccountStatisticDTO {
    private AccountDTO account;
    private int goals;
    private int assists;
    private int played;
    private int motm;

    public AccountStatisticDTO() {}

    public AccountStatisticDTO(AccountDTO account, int goals, int assists, int played, int motm) {
        this.account = account;
        this.goals = goals;
        this.assists = assists;
        this.played = played;
        this.motm = motm;
    }

    public AccountStatisticDTO(AccountDTO account) {
        this.account = account;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getMotm() {
        return motm;
    }

    public void setMotm(int motm) {
        this.motm = motm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountStatisticDTO that = (AccountStatisticDTO) o;

        if (goals != that.goals) return false;
        if (assists != that.assists) return false;
        if (played != that.played) return false;
        if (motm != that.motm) return false;
        return account != null ? account.equals(that.account) : that.account == null;
    }

    @Override
    public int hashCode() {
        int result = account != null ? account.hashCode() : 0;
        result = 31 * result + goals;
        result = 31 * result + assists;
        result = 31 * result + played;
        result = 31 * result + motm;
        return result;
    }

    @Override
    public String toString() {
        return "AccountStatisticDTO{" +
                "account=" + account +
                ", goals=" + goals +
                ", assists=" + assists +
                ", played=" + played +
                ", motm=" + motm +
                '}';
    }
}
