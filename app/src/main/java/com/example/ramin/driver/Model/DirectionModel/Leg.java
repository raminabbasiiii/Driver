package com.example.ramin.driver.Model.DirectionModel;

import java.util.List;

public class Leg {

    private String summary;
    private LegsDistance distance;
    private LegsDuration duration;
    private List<Step> steps = null;

    public String getSummary() {
        return summary;
    }

    public LegsDistance getDistance() {
        return distance;
    }

    public LegsDuration getDuration() {
        return duration;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setDistance(LegsDistance distance) {
        this.distance = distance;
    }

    public void setDuration(LegsDuration duration) {
        this.duration = duration;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
