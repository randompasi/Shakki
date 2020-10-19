package ChessPieces;

import Util.Coordinate;

import java.io.Serializable;

public abstract class Piece implements Serializable{



	protected Colour colour;
	protected int x;
	protected int y;
	protected boolean firstMove;
	
	public Piece(Colour colour,int x, int y){
		this.colour=colour;
		this.x=x;
		this.y=y;
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
	public void asetaX(int x){
		this.x=x;
	}
	public void asetaY(int y){
		this.y=y;
	}
	public boolean annaFirstMove(){
		return firstMove;
	}
	public void changeKoords(int x, int y){
		this.x=x;
		this.y=y;
		firstMove=false;
	}
	public Colour getColour() { return colour; }
	public abstract String getName();
	public abstract boolean isMovePossible(int fromX, int fromY, int toX, int toY);
	public abstract boolean isAttackPossible(int fromX, int fromY, int toX, int toY);
	public abstract boolean isMovePossible(Coordinate coordinate);
	public abstract boolean isAttackPossible(Coordinate coordinate);
	
}
