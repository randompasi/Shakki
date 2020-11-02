package GameLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import ChessPieces.*;
import UI.Winner;
import UI.changePawn;
import Util.Coordinate;
import Util.Player;
import javafx.util.Pair;

public class ChessLogic implements Serializable{

	private final ChessBoard chessBoard;
	private final boolean[][]  enPassantBoard;
	private final int[][] enPassantBoardMove;
	private boolean WhiteKingIsOnCheck;
	private boolean BlackKingIsOnCheck;
	private final Player p1;
	private final Player p2;
	private Colour turn;
	private int moves;
	private Coordinate fromCoordinate;
	private Coordinate toCoordinate;
	private Castling castling;


	public ChessLogic(){


		chessBoard = new ChessBoard();
		chessBoard.createStartingPieces();
		moves=1;
		enPassantBoard=new boolean[8][8];
		enPassantBoardMove=new int[8][8];
		turn=Colour.WHITE;

		p1 = new Player("White Player", Colour.WHITE, true);
		p2 = new Player("Black Player", Colour.BLACK, false);



	}

	public void move(Coordinate fromCoordinate, Coordinate toCoordinate) {
		this.fromCoordinate = fromCoordinate;
		this.toCoordinate = toCoordinate;
		this.castling = new Castling(chessBoard);
		Spot fromSpot = getSpot(fromCoordinate);


		if (fromSpot.annaPiece() == null) return;


		if (isAttackMove(toCoordinate) || isMove(toCoordinate)) {
				if (castling.isCastling(fromSpot.annaPiece(), toCoordinate)) {
					Castling(fromCoordinate, toCoordinate); // castling option
				} else {
					movePiece(fromCoordinate, toCoordinate);
				}

		} else {
			enPassant(fromCoordinate, toCoordinate);
		}

	}




	private boolean isAttackMove(Coordinate toCoordinate){
		return  getSpot(toCoordinate).annaPiece() !=null && checkTurn(fromCoordinate) && isDifferentColour() &&  getSpot(toCoordinate).annaPiece().isAttackPossible(toCoordinate) && isNotOnTheWay(fromCoordinate,toCoordinate);
	}
	private boolean isMove(Coordinate toCoordinate){
		return checkTurn(fromCoordinate) && getSpot(fromCoordinate).annaPiece().isMovePossible(toCoordinate) && isNotOnTheWay(fromCoordinate,toCoordinate);
	}


	private void executeMove(Coordinate fromCoordinate, Coordinate toCoordinate, Piece piece){
		getSpot(toCoordinate).addPiece(getSpot(fromCoordinate).annaPiece());  // jos ei ole, to paikassa on from nappula
		getSpot(fromCoordinate).addPiece(piece); // ja from paikassa on nyt null nappula
		if(piece!=null){
			getSpot(fromCoordinate).annaPiece().changeKoords(fromCoordinate);
		}
		getSpot(toCoordinate).annaPiece().changeKoords(toCoordinate); //aseta nappulan uudet koordinatit ja laittaa firstMove falseksi
	}

	private void movePiece(Coordinate fromCoordinate, Coordinate toCoordinate){ //metodi liikkuu nappuloita

			Piece temp = getSpot(toCoordinate).annaPiece();
			executeMove(fromCoordinate, toCoordinate, null);
			preventFromCheck( getSpot(toCoordinate).annaPiece().getColour());
			if(!((getSpot(toCoordinate).annaPiece().getColour()==Colour.BLACK && BlackKingIsOnCheck) || (getSpot(toCoordinate).annaPiece().getColour()==Colour.WHITE && WhiteKingIsOnCheck))){
				if(causesNoCheck(getSpot(toCoordinate).annaPiece().getColour())){
				enPassantMove(fromCoordinate, toCoordinate, moves); //tarkastellaan "en passant"
				hitsKing(getSpot(toCoordinate).annaPiece().getColour(), toCoordinate);
				setTurn(); // asetetaan peli vuoro
				moves++; // li
				pawnAtEnd(toCoordinate);//pawn at 0 or 7
				//PH.lisaaHistoriaan(toSpot.annaPiece(),fromX, fromY, toX, toY); //lisen liike Peli Historiaan
				kingCheckMate();
				}else{
					System.out.println("/////////////////  WRONG MOVE, KING WILL BE ON CHECK  /////////////////\n");
					executeMove(fromCoordinate, toCoordinate, temp);
				}
			}else{
				System.out.println("/////////////////  WRONG MOVE, KING IS ON CHECK  /////////////////\n");
				executeMove(fromCoordinate, toCoordinate, temp);
			}

	}

	private boolean causesNoCheck(Colour colour){

		Piece King = etsiKing(colour);
		assert King != null;
		for(int i=7;i>=0;i--){
			for(int j=0;j<8;j++){
				if(chessBoard.getSpotWithCoordinates(i,j).annaPiece()!=null){
					if(chessBoard.getSpotWithCoordinates(i,j).annaPiece().getColour()!=colour){
						if(chessBoard.getSpotWithCoordinates(i,j).annaPiece().isAttackPossible(new Coordinate(King.getX(), King.getY()))){//<------------
							if(isNotOnTheWay(new Coordinate(i, j), new Coordinate(King.getX(), King.getY()))){
								return false;
							}
						}
					}
				}
			}
		}

		return true;
	}

	private void preventFromCheck(Colour colour){

		Piece King = etsiKing(colour);
		assert King != null;
		int NumberOfPiecesThatAreHittingKing = 0;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				if(chessBoard.getSpotWithCoordinates(i,j).annaPiece()!=null){
					if(chessBoard.getSpotWithCoordinates(i,j).annaPiece().getColour()!=colour){
						if(chessBoard.getSpotWithCoordinates(i,j).annaPiece().isAttackPossible(new Coordinate(i,j))){//<------------
							if(isNotOnTheWay(new Coordinate(i, j), new Coordinate(King.getX(), King.getY()))){
								NumberOfPiecesThatAreHittingKing++;
							}
						}
					}
				}
			}
		}
		if(NumberOfPiecesThatAreHittingKing==0){
			if(colour==Colour.WHITE){
				WhiteKingIsOnCheck=false;
			}else{
				BlackKingIsOnCheck=false;
			}
		}
	}



	private boolean isDifferentColour(){
		return getSpot(fromCoordinate).annaPiece().getColour() != getSpot(toCoordinate).annaPiece().getColour();
	}

	private boolean isNotOnTheWay(Coordinate fromCoordinate,Coordinate toCoordinate){ //tn metodin avulla katsotaan onko jotain nappuloita from ja

		//jos nappula on "Knight" metodi  palauttaa true, eli ei o
		if(getSpot(fromCoordinate).annaPiece().getName().matches("Knight")){
			return true;
		}


					if(isPieces(fromCoordinate, toCoordinate)) {
						return false;




		// In diagonal (Suomeksi?)
		}else if(Math.abs(toCoordinate.getX() - fromCoordinate.getX()) == Math.abs(toCoordinate.getY()- fromCoordinate.getY())){
			if(toCoordinate.getX()>fromCoordinate.getX() && toCoordinate.getY()>fromCoordinate.getY()){ //NE (North East)
				for(int i = 1; i<toCoordinate.getX()-fromCoordinate.getX(); i++){
					if(chessBoard.getSpotWithCoordinates(fromCoordinate.getX()+i, fromCoordinate.getY()+i).annaPiece()!=null){
						return false;
					}
				}
			}else if(toCoordinate.getX()>fromCoordinate.getX() && toCoordinate.getY()<fromCoordinate.getY()){ //SE (South East)
				for(int i = 1; i<toCoordinate.getX()-fromCoordinate.getX(); i++){
					if(chessBoard.getSpotWithCoordinates(fromCoordinate.getX()+i, fromCoordinate.getY()-i).annaPiece()!=null){
						return false;
					}
				}
			}else if(toCoordinate.getX()<fromCoordinate.getX() && toCoordinate.getY()>fromCoordinate.getY()){ //NW (North West)
				for(int i = 1; i<fromCoordinate.getX()-toCoordinate.getX(); i++)
					if(chessBoard.getSpotWithCoordinates(fromCoordinate.getX()-i, fromCoordinate.getY()+i).annaPiece()!=null){
						return false;
					}
				}
			}else if(toCoordinate.getX()<fromCoordinate.getX() && toCoordinate.getX()<fromCoordinate.getY()){ //SW (South West)
				for(int i = 1; i<fromCoordinate.getX()-toCoordinate.getX(); i++){
					if(chessBoard.getSpotWithCoordinates(fromCoordinate.getX()-i, fromCoordinate.getY()-i).annaPiece()!=null){
						return false;
					}
				}
			}

		return true;
	}

	private boolean isPieces(Coordinate fromCoordinate, Coordinate toCoordinate){

		  for(Coordinate coordinate : coordinatesBetween(fromCoordinate, toCoordinate)){
				if (isPiece(coordinate)) {
					return true;
				}
		}

		return false;
	}


	private boolean isPiece(Coordinate coordinate){
		return getSpot(coordinate).annaPiece() != null;
	}

	private List<Coordinate> coordinatesBetween(Coordinate fromCoordinate, Coordinate toCoordinate){
		List<Coordinate> coordinatesBetween = new ArrayList<>();
		Pair<Integer,Integer> distance = distance(fromCoordinate, toCoordinate);
			for (int i = distance.getKey() + 1; i < distance.getValue(); i++) {
				coordinatesBetween.add(createCoordinate(fromCoordinate, toCoordinate, i));
			}

		coordinatesBetween.removeAll(Collections.singleton(null));
		return coordinatesBetween;
	}

	private Pair<Integer, Integer> distance(Coordinate fromCoordinate, Coordinate toCoordinate){
		if (fromCoordinate.getX() == toCoordinate.getX()) return new Pair<>(fromCoordinate.getY(),toCoordinate.getY());
		else if(fromCoordinate.getY() == toCoordinate.getY()) return new Pair<>(fromCoordinate.getX(),toCoordinate.getX());
		else return new Pair<>(0,1);
	}

	private Coordinate createCoordinate(Coordinate fromCoordinate, Coordinate toCoordinate, int i){
		if(fromCoordinate.getX() == toCoordinate.getX()) return  new Coordinate(fromCoordinate.getX(), i);
		else if(fromCoordinate.getY() == toCoordinate.getY()) return  new Coordinate(i, fromCoordinate.getY());
		else return null;
	}





	private void Castling(Coordinate kingFrom, Coordinate kingTo){

							Coordinate rookFrom =	castling.getRooksFromCoordination(kingTo);
							int rookToX = castling.getRooksToXCoordination(rookFrom.getX());
							Coordinate rookTo = new Coordinate(rookToX,rookFrom.getY());
							executeMove(rookFrom, rookTo, null);
							movePiece(kingFrom,kingTo);
							hitsKing(getSpot(rookTo).annaPiece().getColour(), rookTo);
	}

	private void enPassantMove(Coordinate fromCoordinate, Coordinate toCoordinate,int moves){ //metodi katso onko movePiece "en passant" tapanen
		if(getSpot(toCoordinate).annaPiece().getName().matches("Pawn") && Math.abs(fromCoordinate.getY()-toCoordinate.getY())==2){
			enPassantBoard[toCoordinate.getX()][toCoordinate.getY()]=true; //asettaa "enPassantBoard" boolean taulukkoon kordinaatti misson tapahtunut "en passant"
			enPassantBoardMove[toCoordinate.getX()][toCoordinate.getY()]=moves; //asettaa "enPassantBoardMove" int taulukkoon kuinka mones move se oli
		}

	}

	private void enPassant(Coordinate fromCoordinate, Coordinate toCoordinate){ //metodi toteutaa "en passant"

		Piece temp=getSpot(fromCoordinate).annaPiece();
		if(isEnPassant(fromCoordinate,toCoordinate)){
			movePiece(fromCoordinate, toCoordinate);
			getSpot(new Coordinate(toCoordinate.getX(), fromCoordinate.getY())).addPiece(null);
			if(getSpot(fromCoordinate).annaPiece()!=null){
				getSpot(new Coordinate(toCoordinate.getX(), fromCoordinate.getY())).addPiece(temp);
			}else{
				moves++;
				}
		}

	}

	private boolean isEnPassant(Coordinate fromCoordinate, Coordinate toCoordinate){
		if(getSpot(fromCoordinate).annaPiece().getName().matches("Pawn") && Math.abs(fromCoordinate.getX()-toCoordinate.getX())==1 && Math.abs(fromCoordinate.getY()-toCoordinate.getY())==1){

			return enPassantBoard[toCoordinate.getX()][fromCoordinate.getY()] && moves - enPassantBoardMove[toCoordinate.getX()][fromCoordinate.getY()] == 1;
		}
		return false;
	}

	private void hitsKing(Colour colour, Coordinate toCoordinate){

		Piece King = etsiKing(annaOppositeColour(colour));
		assert King != null;
		if(getSpot(toCoordinate).annaPiece()!=null){
			if(getSpot(toCoordinate).annaPiece().isAttackPossible(new Coordinate(King.getX(), King.getY()))){
				if(isNotOnTheWay(toCoordinate, new Coordinate(King.getX(), King.getY()))){
					if(colour==Colour.WHITE){
						BlackKingIsOnCheck=true;
					}else{
						WhiteKingIsOnCheck=true;
					}
				}
			}
		}

	}


	private Colour annaOppositeColour(Colour colour){

		if(colour==Colour.WHITE){
			return Colour.BLACK;
		}else{
			return Colour.WHITE;
		}

	}




	private Piece etsiKing(Colour colour){

		for(int i = 0; i<8; i++){
			for(int j = 0; j<8; j++){
				Piece piece = getSpot(new Coordinate(i,j)).annaPiece();
				if(piece != null){
					if(piece.getName().matches("King") && piece.getColour()==colour){
						return piece;
					}
				}
			}
		}
		return null;
	}


	private void kingCheckMate(){
		int didfind=0;
		if(turn==Colour.BLACK && BlackKingIsOnCheck || turn==Colour.WHITE && WhiteKingIsOnCheck){
			for(int i=0;i<8;i++){
				for(int j=0;j<8;j++){
					Coordinate loopCoordination = new Coordinate(i,j);
					Piece piece = getSpot(loopCoordination).annaPiece();
					if(piece!=null){
						if(piece.getColour()==turn){
							if(simulateMove(loopCoordination)){
								didfind++;
							}
						}
					}
				}
			}
			if(didfind==0){
				if(turn==Colour.BLACK){
					Winner.display("valkoiset");
				}else{
					Winner.display("mustat");
				}
				System.out.println("Check Mate Dude!");
			}
		}
	}

	private boolean simulateMove(Coordinate fromCoordinate){
		int fromX = fromCoordinate.getX();
		int fromY = fromCoordinate.getY();
     Spot  fromSpot = getSpot(new Coordinate(fromX,fromY));
		Piece temp = fromSpot.annaPiece();
		Piece temp2;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				Coordinate loopCoordination = new Coordinate(i,j);

				if(fromX==i && fromY==j){
					continue;
				}

				if(getSpot(loopCoordination).annaPiece()!=null){
					Spot spot = getSpot(loopCoordination);
					if(fromSpot.annaPiece().isAttackPossible(new Coordinate(i,j)) && turn!=spot.annaPiece().getColour()){
						if(isNotOnTheWay(new Coordinate(fromX, fromY), new Coordinate(i, j))){
							temp2=spot.annaPiece();
							spot.addPiece(temp);
							spot.annaPiece().changeKoords(new Coordinate(i, j));
							fromSpot.addPiece(null);
							preventFromCheck(turn);

							spot.addPiece(temp2);
							spot.annaPiece().changeKoords(new Coordinate(i, j));
							fromSpot.addPiece(temp);
							fromSpot.annaPiece().changeKoords(new Coordinate(fromX, fromY));

							if(turn==Colour.BLACK){


								if(!BlackKingIsOnCheck){
									preventFromCheck(turn);
									return true;
								}
							}else{
								if(!WhiteKingIsOnCheck){
									preventFromCheck(turn);
									return true;
								}
							}
						}
					}
				}else{
					if(fromSpot.annaPiece().isMovePossible(new Coordinate(i,j))){

						if(isNotOnTheWay(new Coordinate(fromX, fromY), new Coordinate(i, j))){
							getSpot(loopCoordination).addPiece(temp);
							getSpot(loopCoordination).annaPiece().changeKoords(new Coordinate(i, j));
							fromSpot.addPiece(null);

							preventFromCheck(turn);

							getSpot(loopCoordination).addPiece(null);
							fromSpot.addPiece(temp);
							fromSpot.annaPiece().changeKoords(new Coordinate(fromX, fromY));

							if(turn==Colour.BLACK){


								if(!BlackKingIsOnCheck){
									preventFromCheck(turn);
									return true;
								}
							}else{


								if(!WhiteKingIsOnCheck){
									preventFromCheck(turn);
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private void pawnAtEnd(Coordinate toCoordinate){
		Piece piece = getSpot(toCoordinate).annaPiece();
		if(piece.getName().matches("Pawn ")){
			if(piece.getColour()==Colour.WHITE){
				if(toCoordinate.getY()==7){
					int z = changePawn.display();
					pawnImprove(z, toCoordinate, Colour.WHITE);
				}
			}else{
				if(toCoordinate.getY()==0){
					int z = changePawn.display();
					pawnImprove(z, toCoordinate, Colour.BLACK);
				}
			}
		}
	}

	private void pawnImprove(int type, Coordinate toCoordinate, Colour colour){
		if(type == 3)
			getSpot(toCoordinate).addPiece(new Knight(colour, toCoordinate));
		else if(type == 2)
			getSpot(toCoordinate).addPiece(new Bishop(colour, toCoordinate));
		else if(type == 4)
			getSpot(toCoordinate).addPiece(new Rook(colour, toCoordinate));
		else
			getSpot(toCoordinate).addPiece(new Queen(colour, toCoordinate));
	}

	private boolean checkTurn(Coordinate coordinate){ // metodi tarkista peli vuoron

		return turn == getSpot(coordinate).annaPiece().getColour();
	}

	private void setTurn(){ 
		if(turn==Colour.WHITE){
			turn=Colour.BLACK;
		}else{
			turn=Colour.WHITE;
		}
	}

	public Spot getSpot(Coordinate coordinate){ //metodi palauttaa board
		return chessBoard.getSpotWithCoordinates(coordinate.getX(),coordinate.getY()) ;
	}




}
