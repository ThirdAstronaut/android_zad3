package lab.swim.pwr.android_zad3;

/**
 * Created by Rafał on 2018-03-16.
 */

public class Walk {

    private String name;
    private String duration;
    private String numOfSteps;

    public Walk(String name, String duration, String numOfSteps) {
        this.name = name;
        this.duration = duration;
        this.numOfSteps = numOfSteps;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public String getNumOfSteps() {
        return numOfSteps;
    }
}
