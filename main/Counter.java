package main;
public class Counter {
    private int points;
    private int count;
    public Counter() {
        this.points = 0;
        this.count = 0;
    }
    public int getPoints() {
        return points;
    }
    public void addPoints(int amount) {
        points += amount;
    }
    public void resetCount(){ count = 0; }
    public void increaseCount(int amount){
        count += amount;
    }
    public int getCount(){
        return count;
    }
}
