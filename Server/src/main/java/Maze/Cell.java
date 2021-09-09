package Maze;

public class Cell {
    private boolean rightWall = true;
    private boolean topWall = true;
    private boolean leftWall = true;
    private boolean bottomWall = true;

    public boolean isRightWall() {
        return rightWall;
    }

    public void setRightWall(boolean rightWall) {
        this.rightWall = rightWall;
    }

    public boolean isTopWall() {
        return topWall;
    }

    public void setTopWall(boolean topWall) {
        this.topWall = topWall;
    }

    public boolean isLeftWall() {
        return leftWall;
    }

    public void setLeftWall(boolean leftWall) {
        this.leftWall = leftWall;
    }

    public boolean isBottomWall() {
        return bottomWall;
    }

    public void setBottomWall(boolean bottomWall) {
        this.bottomWall = bottomWall;
    }
}