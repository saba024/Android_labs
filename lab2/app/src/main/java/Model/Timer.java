package Model;

public class Timer {
    private int id;
    private String name;
    private String prepareTime;
    private String duration;
    private String restTime;
    private String cycleNumber;
    private String setNumber;
    private String pause;
    private String color;

    public Timer() {}

    public Timer(int id, String name, String prepareTime, String duration, String restTime,
                 String cycleNumber, String setNumber, String pause, String color)
    {
        this.id = id;
        this.name = name;
        this.prepareTime = prepareTime;
        this.duration = duration;
        this.restTime = restTime;
        this.cycleNumber = cycleNumber;
        this.setNumber = setNumber;
        this.pause = pause;
        this.color = color;
    }

    public Timer(String name, String prepareTime, String duration, String restTime,
                 String cycleNumber, String setNumber, String pause, String color)
    {
        this.name = name;
        this.prepareTime = prepareTime;
        this.duration = duration;
        this.restTime = restTime;
        this.cycleNumber = cycleNumber;
        this.setNumber = setNumber;
        this.pause = pause;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getCycleNumber() {
        return cycleNumber;
    }

    public String getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public String getPause() {
        return pause;
    }

    public String getRestTime() {
        return restTime;
    }

    public String getSetNumber() {
        return setNumber;
    }

    public String getColor() {
        return color;
    }

    public String getPrepareTime() {
        return prepareTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCycleNumber(String cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setPause(String pause) {
        this.pause = pause;
    }

    public void setPrepareTime(String prepareTime) {
        this.prepareTime = prepareTime;
    }

    public void setRestTime(String restTime) {
        this.restTime = restTime;
    }

    public void setSetNumber(String setNumber) {
        this.setNumber = setNumber;
    }
}
