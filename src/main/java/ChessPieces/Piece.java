package ChessPieces;

import Util.Coordinate;

import java.io.Serializable;

public abstract class Piece implements Serializable{



	protected Colour colour;
	protected int fromX;
	protected int fromY;
	protected boolean firstMove;

	public Piece(Colour colour,Coordinate coordinate){
		this.colour=colour;
		this.fromX =coordinate.getX();
		this.fromY =coordinate.getY();
		firstMove=true;
	}
	
	public int getX(){
		return fromX;
	}
	public int getY(){
		return fromY;
	}
	public boolean isFirstMove(){
		return firstMove;
	}
	public void changeKoords(Coordinate coordinate){
		this.fromX =coordinate.getX();
		this.fromY =coordinate.getY();
		firstMove=false;
	}
	public Colour getColour() { return colour; }
	public abstract String getName();
	public abstract boolean isMovePossible(Coordinate coordinate);
	public abstract boolean isAttackPossible(Coordinate coordinate);
	
}
