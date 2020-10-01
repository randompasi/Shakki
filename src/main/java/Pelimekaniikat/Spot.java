package Pelimekaniikat;

import java.io.Serializable;

public class Spot implements Serializable{
	
	
	private int x;
	private int y;
	private Piece piece; 
	
	public Spot(int x, int y,Piece piece){
		this.x=x;
		this.y=y;
		this.piece=piece;
	}
	
	public int annaX(){
		return x;
	}
	
	public int annaY(){
		return y;
	}
	
	public void addPiece(Piece piece){
		this.piece=piece;
	}
	
	public Piece annaPiece(){
		return piece;
	}
}
