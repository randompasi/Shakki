package Pelimekaniikat;

import java.io.Serializable;

public class Rook extends Piece implements Serializable{
	
	private String nimi="Rook";
	
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
	
	public String annaNimi(){
		return nimi;
	}

}
