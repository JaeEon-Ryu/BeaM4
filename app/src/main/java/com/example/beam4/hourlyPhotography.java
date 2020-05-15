package com.example.beam4;

class hourlyPhotography {

    private String timeString;
    private String timeIndex;

    public hourlyPhotography(String timeString, String timeIndex) {
        this.timeString = timeString;
        this.timeIndex = timeIndex;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public void setTimeIndex(String timeIndex) {
        this.timeIndex = timeIndex;
    }

    public String getTimeIndex() {
        return this.timeIndex;
    }

    public String getTimeString() {
        return this.timeString;
    }

}
