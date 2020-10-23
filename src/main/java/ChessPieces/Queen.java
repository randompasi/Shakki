package ChessPieces;

import Util.Coordinate;

import java.io.Serializable;

public class Queen extends Piece implements Serializable{

	final private String nimi="Queen";
	


	public Queen(Colour colour, Coordinate coordinate) {
		super(colour, coordinate);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isMovePossible(int fromX, int fromY, int toX, int toY){
		if(fromX == toX){
			return true;
		}
		if(fromY == toY){
			return true;
		}
		if(Math.abs(toX - fromX) == Math.abs(toY- fromY)){
			return true;
		}
		return false;
	}
	@Override
	public boolean isMovePossible(Coordinate toCoordinate) {
		return 	isMovePossible(fromX, fromY, toCoordinate.getX(),toCoordinate.getY());
	}

	@Override
	public boolean isAttackPossible(Coordinate toCoordinate) {
		return isMovePossible(toCoordinate);
	}

	public String getName(){
		return nimi;
	}
	
}
