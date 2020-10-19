package ChessPieces;

import Util.Coordinate;

import java.io.Serializable;

public class Rook extends Piece implements Serializable{
	
	 final private String nimi="Rook";
	
	public Rook(Colour colour, int x, int y) {
		super(colour, x, y);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isMovePossible(int fromX, int fromY, int toX, int toY){
		if(toX == fromX){
			return true;
		}
		if(toY == fromY){
			return true;
		}
		return false;
	}
	public boolean isAttackPossible(int fromX, int fromY, int toX, int toY){
		return isMovePossible(fromX, fromY, toX, toY);
	}

	@Override
	public boolean isMovePossible(Coordinate toCoordinate) {
		return 	isMovePossible(x, y, toCoordinate.getXCoordinate(),toCoordinate.getYCoordinate());
	}

	@Override
	public boolean isAttackPossible(Coordinate toCoordinate) {
		return isMovePossible(toCoordinate);
	}
	
	public String getName(){
		return nimi;
	}

}
