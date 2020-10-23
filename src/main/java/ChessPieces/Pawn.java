package ChessPieces;

import Util.Coordinate;

import java.io.Serializable;


public class Pawn extends Piece implements Serializable{

	
	//REMEMBER TO CHANGE THIS NAME ADRIAN! NO SPACE!
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

		if(firstMove){
			if(toCoordinate.getX() == fromX && absoluteDifference(fromY, toCoordinate.getY()) == 2){
				return true;
			}
		}
		return basicMoveCheck(absoluteDifference(fromY, toCoordinate.getY()), toCoordinate.getX());
		}

	@Override
	public boolean isAttackPossible(Coordinate toCoordinate) {
		return isAttackPossible(fromX, fromY, toCoordinate.getX(),toCoordinate.getY());
	}
	
	public String getName(){
		return nimi;
	}
}
