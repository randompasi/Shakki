package ChessPieces;

import Util.Coordinate;

import java.io.Serializable;

public class Bishop extends Piece implements Serializable{

	final private String nimi="Bishop";


	public Bishop(Colour colour, Coordinate coordinate) {
		super(colour, coordinate);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean isMovePossible(Coordinate toCoordinate){

		if(Math.abs(toCoordinate.getX() - fromX) == Math.abs(toCoordinate.getY() - fromY)){
            return true;
		}
		return false;
	}

	@Override
	public boolean isAttackPossible(Coordinate toCoordinate) {
		return isMovePossible(toCoordinate);
	}

	public String getName(){
		return nimi;
	}
}
