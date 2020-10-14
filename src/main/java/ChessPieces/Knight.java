package ChessPieces;

import java.io.Serializable;

public class Knight extends Piece implements Serializable{

	final private String nimi="Knight";
	
	public Knight(Colour colour, int x, int y) {
		super(colour, x, y);
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
	
	public String getName(){
		return nimi;
	}
}
