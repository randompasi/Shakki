package ChessPieces;

import java.io.Serializable;

public class King extends Piece implements Serializable{

	final private String nimi="King";
	
	public King(Colour colour, int x, int y) {
		super(colour, x, y);
	}
	
	public boolean isMovePossible(int fromX, int fromY, int toX, int toY){
		//System.out.println(fromX +" "+fromY+" "+toX+" "+toY);
		//normal move - any direction, distance 1
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
	
	public String getName(){
		return nimi;
	}
}









