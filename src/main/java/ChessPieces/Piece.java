package ChessPieces;

import Util.Coordinate;

import java.io.Serializable;

public abstract class Piece implements Serializable{



	protected Colour colour;
	protected int x;
	protected int y;
	protected boolean firstMove;

	public Piece(Colour colour,Coordinate coordinate){
		this.colour=colour;
		this.x=coordinate.getXCoordinate();
		this.y=coordinate.getYCoordinate();
		firstMove=true;
	}
	
	public int annaX(){
		return x;
	}
	public int annaY(){
		return y;
	}
	public Colour annaVari(){
		return colour;
	}
	public boolean isFirstMove(){
		return firstMove;
	}
	public void changeKoords(Coordinate coordinate){
		this.x=coordinate.getXCoordinate();
		this.y=coordinate.getYCoordinate();
		firstMove=false;
	}
	public Colour getColour() { return colour; }
	public abstract String getName();
	public abstract boolean isMovePossible(Coordinate coordinate);
	public abstract boolean isAttackPossible(Coordinate coordinate);
	
}
