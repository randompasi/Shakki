package ChessPieces;

import Util.Coordinate;

import java.io.Serializable;
import java.util.function.BiFunction;


public class Pawn extends Piece implements Serializable{


	private BiFunction<Coordinate,Coordinate, Integer> pythagora = (coordinate, coordinate2) ->  (int)Math.sqrt(Math.pow(coordinate.getX()-coordinate2.getX(), 2) + Math.pow(coordinate.getY()-coordinate2.getY(), 2));
	final private String nimi="Pawn";


	public Pawn(Colour colour, Coordinate coordinate) {
		super(colour, coordinate);
	}





	private boolean basicMoveCheck(int subtraction, int toX){
		return toX == fromX && subtraction == 1;
	}
	private int absoluteDifference(int from, int to){
		return Math.abs(from-to);
	}


	public boolean isAttackPossible(int fromX, int fromY, int toX, int toY){
		if((Math.abs(toY-fromY) == 1 && fromX-toX == 1) || (Math.abs(toY-fromY) == 1 && toX-fromX ==  1))return true;
		return false;
	}

	@Override
	public boolean isMovePossible(Coordinate toCoordinate) {
		if (isMovingBackward(toCoordinate)) return false;

		int distance = 1;
		if (firstMove) distance = 2;
			return toCoordinate.getX() == fromX && isDistance(toCoordinate, distance);

	}

	private boolean isMovingBackward(Coordinate toCoordinate){
		if(colour == Colour.BLACK) return toCoordinate.getY() > fromY;
			else return toCoordinate.getY() < fromY;
	}

	@Override
	public boolean isAttackPossible(Coordinate toCoordinate) {
		return isAttackPossible(fromX, fromY, toCoordinate.getX(),toCoordinate.getY());
	}

	public String getName(){
		return nimi;
	}

	private boolean isDistance(Coordinate toCoordinate, int targetDistance){
	int realDistance =	pythagora.apply(toCoordinate, new Coordinate(fromX,fromY));

	return realDistance != 0 && realDistance <= targetDistance;
	}
}