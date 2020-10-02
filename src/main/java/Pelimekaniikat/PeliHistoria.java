package Pelimekaniikat;

import DataObjects.Piece;

import java.io.Serializable;
import java.util.ArrayList;

public class PeliHistoria implements Serializable{
	
	private ArrayList<Piece> piece;
	private ArrayList<Integer> fromX;
	private ArrayList<Integer> fromY;
	private ArrayList<Integer> toX;
	private ArrayList<Integer> toY;
	
	public PeliHistoria(){
		piece=new ArrayList<Piece>();
		fromX=new ArrayList<Integer>();
		fromY=new ArrayList<Integer>();
		toX=new ArrayList<Integer>();
		toY=new ArrayList<Integer>();
	}
	
	public void lisaaHistoriaan(Piece piece, int fromX, int fromY, int toX, int toY){
		this.piece.add(piece);
		this.fromX.add(fromX);
		this.fromY.add(fromY);
		this.toX.add(toX);
		this.toY.add(toY);
	}
	
	public int annaVanhinFromX(){
		return this.fromX.get(this.fromX.size()-1);
	}
	public int annaVanhinFromY(){
		return this.fromY.get(this.fromY.size()-1);
	}
	public int annaVanhinToX(){
		return this.toX.get(this.toX.size()-1);
	}
	public int annaVanhinToY(){
		return this.toY.get(this.toY.size()-1);
	} 
	public Piece annaVanhinPiece(){
		return this.piece.get(this.piece.size()-1);
	}
	public int annaSize(){
		return piece.size();
	}
	
	
}
