package game;

public class LeaderBoardElement implements Comparable<LeaderBoardElement>{
    private String name;
    private int points;

    public LeaderBoardElement(String name) {
        this.name = name;
        points = 0;
    }

    public void increasePoints(int num) {
        points += num;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(LeaderBoardElement leaderBoard) {
        if (this.points == leaderBoard.points) {
            return this.name.compareTo(leaderBoard.name);
        }
        if (this.points < leaderBoard.points) {
            return -1;
        }
        return 1;
    }


    @Override
    public String toString() {
        return name + ' ' + points;
    }
}
