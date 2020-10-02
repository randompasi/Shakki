package DataObjects;

import DataObjects.Colour;
import DataObjects.Piece;

import java.io.Serializable;

public class Bishop extends Piece implements Serializable{

	final private String nimi="Bishop";
	
	public Bishop(Colour colour, int x, int y) {
		super(colour, x, y);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isMovePossible(int fromX, int fromY, int toX, int toY){
		//diagonal move
		if(Math.abs(toX - fromX) == Math.abs(toY - fromY)){
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
