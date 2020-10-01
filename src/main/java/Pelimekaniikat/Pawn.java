package Pelimekaniikat;

import java.io.Serializable;

public class Pawn extends Piece implements Serializable{

	
	//REMEMBER TO CHANGE THIS NAME ADRIAN! NO SPACE!
	final private String nimi="Pawn";
	
	
	public Pawn(Colour colour, int x, int y) {
		super(colour, x, y);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isMovePossible(int fromX, int fromY, int toX, int toY){
		if(super.colour==Colour.WHITE){
			if(fromY==1){
				if(toX == fromX && toY-fromY == 2){
					return true;
				}
			}
			if(toX == fromX && toY - fromY == 1){
				return true;
			}
		}else{
			if(fromY==6){
				if(toX == fromX && fromY-toY == 2){
					return true;
				}
			}
			if(toX == fromX && fromY - toY == 1){
				return true;
			}
		}
		return false;
	}
	
	public boolean isAttackPossible(int fromX, int fromY, int toX, int toY){
		if(super.colour==Colour.WHITE){
			if((toY-fromY == 1 && fromX-toX == 1) || (toY-fromY == 1 && toX-fromX ==  1)){
				return true;
			}
		}else{
			if((fromY-toY == 1 && fromX-toX == 1) || (fromY-toY == 1 && toX-fromX ==  1)){
				return true;
			}
		}
		return false;
	}
	
	public String annaNimi(){
		return nimi;
	}
}
