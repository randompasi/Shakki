package GameLogic;

import ChessPieces.Colour;

import java.io.Serializable;

public class Player implements Serializable{

	private String nimi;
	private Colour colour;
	private boolean turn;
	
	public Player(String nimi,Colour colour,boolean turn){
		this.nimi=nimi;
		this.colour=colour;
		this.turn=turn;
	}
	
	public String annaNimi(){
		return nimi;
	}
	
	public Colour annaColour(){
		return colour;
	}
	public void asetaTurn(boolean turn){
		this.turn=turn;
	}
	public boolean annaTurn(){
		return turn;
	}
}
