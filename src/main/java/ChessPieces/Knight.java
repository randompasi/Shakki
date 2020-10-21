package ChessPieces;

import Util.Coordinate;

import java.io.Serializable;

public class Knight extends Piece implements Serializable{

	final private String nimi="Knight";


	public Knight(Colour colour, Coordinate coordinate) {
		super(colour, coordinate);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isMovePossible(int fromX, int fromY, int toX, int toY){
		
		//twoHorizontallyOneVertically()
		//oneHorizontallytwoVertically


		if(Math.abs(toX-fromX)==1 && Math.abs(toY-fromY)==2){
			return true;
		}
		if(Math.abs(toX-fromX)==2 && Math.abs(toY-fromY)==1){
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
