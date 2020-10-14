package Util;

public class Coordinate {

    /**
     * need to make changes so everything uses this class for transfering coornidates
     */


    private int xCoordinate;

    public Coordinate(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }



    public int getYCoordinate() {
        return yCoordinate;
    }



    private int yCoordinate;
}
