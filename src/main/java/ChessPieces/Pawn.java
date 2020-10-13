package ChessPieces;

import java.io.Serializable;
import java.util.function.BiFunction;


public class Pawn extends Piece implements Serializable{

	
	//REMEMBER TO CHANGE THIS NAME ADRIAN! NO SPACE!
	final private String nimi="Pawn";
	private final BiFunction<Integer,Integer,Integer> subtraction = (a, b) -> (a - b);
	
	public Pawn(Colour colour, int x, int y) {
		super(colour, x, y);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isMovePossible(int fromX, int fromY, int toX, int toY){
		if(super.colour==Colour.WHITE){
			if(fromY==1){
				if(toX == fromX && toY-fromY == 2){
					return true;
				}
			}
				return basicMoveCheck(subtraction.apply(toY,fromY), toX);

		}else{
			if(fromY==6){
				if(toX == fromX && fromY-toY == 2){
					return true;

				}
			}
			if(toX == fromX && fromY - toY == 1){

				return  basicMoveCheck(subtraction.apply(fromY,toY),toX);
			}
		}
		return false;
	}

	private boolean basicMoveCheck(int subtraction, int toX){
		return toX == x && subtraction == 1;
	}

	
	public boolean isAttackPossible(int fromX, int fromY, int toX, int toY){
		if(super.colour==Colour.WHITE){
			if((toY-fromY == 1 && fromX-toX == 1) || (toY-fromY == 1 && toX-fromX ==  1)){
				return true;
			}
		}else{
			if((fromY-toY == 1 && fromX-toX == 1) || (fromY-toY == 1 && toX-fromX ==  1)){
				return true;
			}
		}
		return false;
	}
	
	public String getName(){
		return nimi;
	}
}
