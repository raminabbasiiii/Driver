package com.example.ramin.driver.Model.DirectionModel;

import java.util.List;

public class Step {

    private String name;
    private String instruction;
    private StepsDistance distance;
    private StepsDuration duration;
    private String polyline;
    private String maneuver;
    private List<Double> startLocation = null;

    public String getName() {
        return name;
    }

    public String getInstruction() {
        return instruction;
    }

    public StepsDistance getDistance() {
        return distance;
    }

    public StepsDuration getDuration() {
        return duration;
    }

    public String getPolyline() {
        return polyline;
    }

    public String getManeuver() {
        return maneuver;
    }

    public List<Double> getStartLocation() {
        return startLocation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setDistance(StepsDistance distance) {
        this.distance = distance;
    }

    public void setDuration(StepsDuration duration) {
        this.duration = duration;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }

    public void setStartLocation(List<Double> startLocation) {
        this.startLocation = startLocation;
    }
}
