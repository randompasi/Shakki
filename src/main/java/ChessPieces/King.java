package ChessPieces;

import Util.Coordinate;

import java.io.Serializable;
import java.util.function.BiFunction;

public class King extends Piece implements Serializable{

	final private String nimi="King";
	private BiFunction<Coordinate,Coordinate, Integer> pythagora = (coordinate, coordinate2) ->  (int)Math.sqrt(Math.pow(coordinate.getX()-coordinate2.getX(), 2) + Math.pow(coordinate.getY()-coordinate2.getY(), 2));


	public King(Colour colour, Coordinate coordinate) {
		super(colour, coordinate);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isMovePossible(Coordinate toCoordinate){

		if(1 == pythagora.apply(new Coordinate(fromX, fromY), toCoordinate))return true;

		return isCastling(toCoordinate) ;

	}

	private boolean isCastling(Coordinate toCoordinate){
	return 	Math.abs(toCoordinate.getX()- fromX)==2 && toCoordinate.getY()- fromY==0;

	}


	@Override
	public boolean isAttackPossible(Coordinate toCoordinate) {
		return isMovePossible(toCoordinate);
	}

	public String getName(){
		return nimi;
	}
}









