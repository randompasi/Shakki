package ChessPieces;

import Util.Coordinate;

import java.io.Serializable;

public class King extends Piece implements Serializable{

	final private String nimi="King";
	
	public King(Colour colour, int x, int y) {
		super(colour, x, y);
	}

	public King(Colour colour, Coordinate coordinate) {
		super(colour, coordinate);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isMovePossible(int fromX, int fromY, int toX, int toY){

		if(Math.abs(fromX-toX)<=1 && Math.abs(fromY-toY) <=1){
			return true;
		}
		else {
			return isCastling(fromX, fromY, toX, toY) ;
		}
	}

	private boolean isCastling(int fromX, int fromY, int toX, int toY){
		if(super.colour==Colour.WHITE){
			if((super.firstMove && fromX == 4 && fromY == 0 && toY==0 && toX == 6) || ((super.firstMove && fromX == 4 && fromY == 0 && toY==0 && toX == 2)) ){
				return true;
			}
		}else{
			if((super.firstMove && fromX == 4 && fromY == 7 && toY==7 && toX == 6) || ((super.firstMove && fromX == 4 && fromY == 7 && toY==7 && toX == 2)) ){
				return true;
			}
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









