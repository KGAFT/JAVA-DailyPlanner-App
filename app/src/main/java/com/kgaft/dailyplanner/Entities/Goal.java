package com.kgaft.dailyplanner.Entities;

public class Goal {
    private String mainInfo;

    private String hoursInfo;
    private String minutesInfo;
    private String dayOfWeek;
    private boolean isInMainTimeTable = false;
    private boolean isTodayCompleted = false;


    private int id = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(String mainInfo) {
        this.mainInfo = mainInfo;
    }

    public boolean isTodayCompleted() {
        return isTodayCompleted;
    }

    public void setTodayCompleted(boolean todayCompleted) {
        isTodayCompleted = todayCompleted;
    }

    public String getHoursInfo() {
        return hoursInfo;
    }

    public void setHoursInfo(String hoursInfo) {
        this.hoursInfo = hoursInfo;
    }

    public String getMinutesInfo() {
        return minutesInfo;
    }

    public void setMinutesInfo(String minutesInfo) {
        this.minutesInfo = minutesInfo;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public boolean isInMainTimeTable() {
        return isInMainTimeTable;
    }

    public void setInMainTimeTable(boolean inMainTimeTable) {
        isInMainTimeTable = inMainTimeTable;
    }
}
