package ChessPieces;

import Util.Coordinate;
import java.io.Serializable;
import java.util.function.BiFunction;


public class Pawn extends Piece implements Serializable{

	/**
	 * Pythagora theorem distance between to points
	 */
	private BiFunction<Coordinate,Coordinate, Integer> pythagora = (coordinate, coordinate2) ->  (int)Math.sqrt(Math.pow(coordinate.getX()-coordinate2.getX(), 2) + Math.pow(coordinate.getY()-coordinate2.getY(), 2));
	final private String nimi="Pawn";


	public Pawn(Colour colour, Coordinate coordinate) {
		super(colour, coordinate);
	}


	@Override
	public boolean isMovePossible(Coordinate toCoordinate) {
		if (!isMovingForward(toCoordinate)) return false;

		int distance = 1;
		if (firstMove) distance = 2;
			return toCoordinate.getX() == fromX && isDistance(toCoordinate, distance);

	}

	private boolean isMovingForward(Coordinate toCoordinate){
		if(colour == Colour.BLACK) return toCoordinate.getY() < fromY;
			else return toCoordinate.getY() > fromY;
	}

	@Override
	public boolean isAttackPossible(Coordinate toCoordinate) {
		if (!isMovingForward(toCoordinate)) return false;
		return toCoordinate.getY() != fromY && isDistance(toCoordinate, 1);
	}

	private boolean isDistance(Coordinate toCoordinate, int targetDistance){
	int realDistance =	pythagora.apply(toCoordinate, new Coordinate(fromX,fromY));

	return realDistance != 0 && realDistance <= targetDistance;
	}


	public String getName(){
		return nimi;
	}
}