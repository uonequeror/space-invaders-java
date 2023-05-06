package main;
public class Counter {
    private int points;
    public Counter() {
        this.points = 0;
    }
    public int getPoints() {
        return points;
    }
    public void addPoints(int amount) {
        points += amount;
    }
    public void setPoints(int scorePoints) {
        points = scorePoints;
    }
}
