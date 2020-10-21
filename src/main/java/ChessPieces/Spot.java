package ChessPieces;

import java.io.Serializable;

public class Spot implements Serializable{
	
	
	private final int x;
	private final int y;
	private Piece piece;


	public Spot(int x, int y, Piece piece){
		this.x=x;
		this.y=y;
		this.piece=piece;
	}

	
	public void addPiece(Piece piece){
		this.piece=piece;
	}
	
	public Piece annaPiece(){
		return piece;
	}
}
