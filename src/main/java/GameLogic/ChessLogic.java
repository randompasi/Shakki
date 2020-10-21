package GameLogic;

import java.io.Serializable;

import ChessPieces.*;
import UI.Winner;
import UI.changePawn;
import Util.Coordinate;
import Util.Player;

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

	public void move(Coordinate fromCoordinate, Coordinate toCoordinate){
		this.fromCoordinate	=	fromCoordinate;
		this.toCoordinate	=	toCoordinate;
		Spot toSpot	=	getSpot(toCoordinate);
		Spot fromSpot	=	getSpot(fromCoordinate);


		if(fromSpot.annaPiece() !=null){ // katsotaan onko from paikkassa nappula
			if(checkTurn(fromCoordinate)){
				if(toSpot.annaPiece() !=null){ // katsotaan onko to paikassa nappula
					if(isDifferentColour()){
						if(fromSpot.annaPiece().isAttackPossible(toCoordinate)){ // jos ovat eri, katsotaan onko attack mahdollista
							if(isNotOnTheWay(fromCoordinate, toCoordinate)){
								movePiece(fromCoordinate, toCoordinate);
							}
						}
					}
				}else{ //jos ei ole  nappula
					if(fromSpot.annaPiece().isMovePossible(toCoordinate)){//katostaan onko move mahdollista
						if(isNotOnTheWay(fromCoordinate, toCoordinate)){  //
							if(isCastling(fromSpot.annaPiece())){
								Castling(fromCoordinate, toCoordinate); // castling option
							}else{
								movePiece(fromCoordinate, toCoordinate);
							}
						}
					}else{
						enPassant(fromCoordinate, toCoordinate);
					}
				}
			}else{
				if(turn==Colour.WHITE){
					System.out.println("It is "+p1.annaNimi()+"'s turn\n");
				}else{
					System.out.println("It is "+p2.annaNimi()+"'s turn\n");
				}
			}
		}
	}

	private boolean isCastling(Piece piece){
	return	piece.getName().matches("King") && Math.abs(toCoordinate.getXCoordinate()-fromCoordinate.getXCoordinate())==2 && toCoordinate.getYCoordinate()-fromCoordinate.getYCoordinate()==0;
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
			preventFromCheck( getSpot(toCoordinate).annaPiece().annaVari());
			if(!((getSpot(toCoordinate).annaPiece().annaVari()==Colour.BLACK && BlackKingIsOnCheck) || (getSpot(toCoordinate).annaPiece().annaVari()==Colour.WHITE && WhiteKingIsOnCheck))){
				if(causesNoCheck(getSpot(toCoordinate).annaPiece().annaVari())){
				enPassantMove(fromCoordinate, toCoordinate, moves); //tarkastellaan "en passant"
				hitsKing(getSpot(toCoordinate).annaPiece().annaVari(), toCoordinate);
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
					if(chessBoard.getSpotWithCoordinates(i,j).annaPiece().annaVari()!=colour){
						if(chessBoard.getSpotWithCoordinates(i,j).annaPiece().isAttackPossible(new Coordinate(King.annaX(), King.annaY()))){//<------------
							if(isNotOnTheWay(new Coordinate(i, j), new Coordinate(King.annaX(), King.annaY()))){
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
					if(chessBoard.getSpotWithCoordinates(i,j).annaPiece().annaVari()!=colour){
						if(chessBoard.getSpotWithCoordinates(i,j).annaPiece().isAttackPossible(new Coordinate(i,j))){//<------------
							if(isNotOnTheWay(new Coordinate(i, j), new Coordinate(King.annaX(), King.annaY()))){
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
		return getSpot(fromCoordinate).annaPiece().annaVari() != getSpot(toCoordinate).annaPiece().annaVari();
	}

	private boolean isNotOnTheWay(Coordinate fromCoordinate,Coordinate toCoordinate){ //tn metodin avulla katsotaan onko jotain nappuloita from ja

		//jos nappula on "Knight" metodi  palauttaa true, eli ei o
		if(getSpot(fromCoordinate).annaPiece().getName().matches("Knight")){
			return true;
		}


		if(fromCoordinate.getXCoordinate()==toCoordinate.getXCoordinate()){ // X akseli
			if(toCoordinate.getYCoordinate()>fromCoordinate.getYCoordinate()){
				for(int i=fromCoordinate.getYCoordinate()+1; i<toCoordinate.getYCoordinate(); i++){
					if(getSpot(new Coordinate(fromCoordinate.getXCoordinate(), i)).annaPiece()!=null){
						return false;
					}
				}
			}else{
				for(int i=fromCoordinate.getYCoordinate()-1; i>toCoordinate.getYCoordinate(); i--){
					if(getSpot(new Coordinate(fromCoordinate.getXCoordinate(), i)).annaPiece() != null){
						return false;
					}
				}
			}
		}else if(fromCoordinate.getYCoordinate()==toCoordinate.getYCoordinate()){ // Y askeli
			if(toCoordinate.getXCoordinate() > fromCoordinate.getXCoordinate()){
				for(int i=fromCoordinate.getXCoordinate()+1; i<toCoordinate.getXCoordinate(); i++){
					if(chessBoard.getSpotWithCoordinates(i,fromCoordinate.getYCoordinate()).annaPiece()!=null){
						return false;
					}
				}
			}else{
				for(int i=fromCoordinate.getXCoordinate()-1; i>toCoordinate.getXCoordinate(); i--){
					if(chessBoard.getSpotWithCoordinates(i,fromCoordinate.getYCoordinate()).annaPiece()!=null){
						return false;
					}
				}
			}


		// In diagonal (Suomeksi?)
		}else if(Math.abs(toCoordinate.getXCoordinate() - fromCoordinate.getXCoordinate()) == Math.abs(toCoordinate.getYCoordinate()- fromCoordinate.getYCoordinate())){
			if(toCoordinate.getXCoordinate()>fromCoordinate.getXCoordinate() && toCoordinate.getYCoordinate()>fromCoordinate.getYCoordinate()){ //NE (North East)
				for(int i=1;i<toCoordinate.getXCoordinate()-fromCoordinate.getXCoordinate(); i++){
					if(chessBoard.getSpotWithCoordinates(fromCoordinate.getXCoordinate()+i, fromCoordinate.getYCoordinate()+i).annaPiece()!=null){
						return false;
					}
				}
			}else if(toCoordinate.getXCoordinate()>fromCoordinate.getXCoordinate() && toCoordinate.getYCoordinate()<fromCoordinate.getYCoordinate()){ //SE (South East)
				for(int i=1;i<toCoordinate.getXCoordinate()-fromCoordinate.getXCoordinate(); i++){
					if(chessBoard.getSpotWithCoordinates(fromCoordinate.getXCoordinate()+i, fromCoordinate.getYCoordinate()-i).annaPiece()!=null){
						return false;
					}
				}
			}else if(toCoordinate.getXCoordinate()<fromCoordinate.getXCoordinate() && toCoordinate.getYCoordinate()>fromCoordinate.getYCoordinate()){ //NW (North West)
				for(int i=1;i<fromCoordinate.getXCoordinate()-toCoordinate.getXCoordinate(); i++)
					if(chessBoard.getSpotWithCoordinates(fromCoordinate.getXCoordinate()-i, fromCoordinate.getYCoordinate()+i).annaPiece()!=null){
						return false;
					}
				}
			}else if(toCoordinate.getXCoordinate()<fromCoordinate.getXCoordinate() && toCoordinate.getXCoordinate()<fromCoordinate.getYCoordinate()){ //SW (South West)
				for(int i=1;i<fromCoordinate.getXCoordinate()-toCoordinate.getXCoordinate(); i++){
					if(chessBoard.getSpotWithCoordinates(fromCoordinate.getXCoordinate()-i, fromCoordinate.getYCoordinate()-i).annaPiece()!=null){
						return false;
					}
				}
			}

		return true;
	}
	private boolean nothingHits(Coordinate toCoordinate, Colour colour){

		int NumberOfPiecesThatAreHittingTOXTOY = 0;

		for(int i=7;i>=0;i--){
			for(int j=0;j<8;j++){
				if(chessBoard.getSpotWithCoordinates(i, j).annaPiece()!=null){
					if(chessBoard.getSpotWithCoordinates(i, j).annaPiece().annaVari()==colour){
						if(chessBoard.getSpotWithCoordinates(i, j).annaPiece().isAttackPossible(toCoordinate)){//<------------
							if(isNotOnTheWay(new Coordinate(i,j), toCoordinate)){
								NumberOfPiecesThatAreHittingTOXTOY++;
							}
						}
					}
				}
			}
		}

		return NumberOfPiecesThatAreHittingTOXTOY == 0;

	}

	private void Castling(Coordinate fromCoordinate, Coordinate toCoordinate){ // metodi tarkista onko castling mahdollista ja jos se on, suorittaa sen



		if(getSpot(fromCoordinate).annaPiece().isFirstMove()){ // ensiin tytyy kastsoa, onko King first move true

			if(getSpot(fromCoordinate).annaPiece().annaVari()==Colour.WHITE){ //kun king on valkoinen

				if(fromCoordinate.getXCoordinate()==4 && fromCoordinate.getYCoordinate()==0 && toCoordinate.getXCoordinate()==6 && toCoordinate.getYCoordinate()==0 && chessBoard.getSpotWithCoordinates(7, 0).annaPiece().isFirstMove()){ //  tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true

					if(nothingHits(new Coordinate(4, 0), Colour.BLACK) && nothingHits(new Coordinate(5 ,0), Colour.BLACK) && nothingHits(new Coordinate(6, 0), Colour.BLACK)){
						executeMove(new Coordinate(7,0), new Coordinate(5,0), null);
						movePiece(fromCoordinate,toCoordinate); //siirretaan king
						hitsKing(chessBoard.getSpotWithCoordinates(5, 0).annaPiece().annaVari(), new Coordinate(5, 0));
					}

				}else if(fromCoordinate.getXCoordinate()==4 && fromCoordinate.getYCoordinate()==0 && toCoordinate.getXCoordinate()==2 && toCoordinate.getYCoordinate()==0 && chessBoard.getSpotWithCoordinates(0, 0).annaPiece().isFirstMove()){ // t tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true

					if(isNotOnTheWay(new Coordinate(0,0), new Coordinate(3,0))){

						if(nothingHits(new Coordinate(4, 0), Colour.BLACK) && nothingHits(new Coordinate(3 ,0), Colour.BLACK) && nothingHits(new Coordinate(2, 0), Colour.BLACK)){

							executeMove(new Coordinate(0,0), new Coordinate(3,0), null);
							movePiece(fromCoordinate,toCoordinate); //siirretn king
							hitsKing(getSpot(new Coordinate(3,0)).annaPiece().annaVari(), new Coordinate(3, 0));
						}

					}
				}
			}else{ // kun king on musta
				if(fromCoordinate.getXCoordinate()==4 && fromCoordinate.getYCoordinate()==7 && toCoordinate.getXCoordinate()==6 && toCoordinate.getYCoordinate()==7 && chessBoard.getSpotWithCoordinates(7, 7).annaPiece().isFirstMove()){ // t tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true
					if(nothingHits(new Coordinate(4, 7), Colour.WHITE) && nothingHits(new Coordinate(5 ,7), Colour.WHITE) && nothingHits(new Coordinate(6, 7), Colour.WHITE)){
						executeMove(new Coordinate(7,7), new Coordinate(5,7), null);
						movePiece(fromCoordinate,toCoordinate); //siirretn king
						hitsKing(getSpot(new Coordinate(5,7)).annaPiece().annaVari(), new Coordinate(5, 7));
					}
				}else if(fromCoordinate.getXCoordinate()==4 && fromCoordinate.getYCoordinate()==7 && toCoordinate.getXCoordinate()==2 && toCoordinate.getYCoordinate()==7 && chessBoard.getSpotWithCoordinates(0, 7).annaPiece().isFirstMove()){ //  tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true
					if(isNotOnTheWay(new Coordinate(0,7), new Coordinate(3,7))){
						if(nothingHits(new Coordinate(4, 7), Colour.WHITE) && nothingHits(new Coordinate(3 , 7), Colour.WHITE) && nothingHits(new Coordinate(2, 7), Colour.WHITE)){
							executeMove(new Coordinate(0,7), new Coordinate(3,7), null);
							movePiece(fromCoordinate,toCoordinate); //siirretn king
							hitsKing(getSpot(new Coordinate(3,7)).annaPiece().annaVari(), new Coordinate(3,7));
						}
					}
				}
			}
		}
	}

	private void enPassantMove(Coordinate fromCoordinate, Coordinate toCoordinate,int moves){ //metodi katso onko movePiece "en passant" tapanen
		if(getSpot(toCoordinate).annaPiece().getName().matches("Pawn") && Math.abs(fromCoordinate.getYCoordinate()-toCoordinate.getYCoordinate())==2){
			enPassantBoard[toCoordinate.getXCoordinate()][toCoordinate.getYCoordinate()]=true; //asettaa "enPassantBoard" boolean taulukkoon kordinaatti misson tapahtunut "en passant"
			enPassantBoardMove[toCoordinate.getXCoordinate()][toCoordinate.getYCoordinate()]=moves; //asettaa "enPassantBoardMove" int taulukkoon kuinka mones move se oli
		}

	}

	private void enPassant(Coordinate fromCoordinate, Coordinate toCoordinate){ //metodi toteutaa "en passant"

		Piece temp=getSpot(fromCoordinate).annaPiece();
		if(isEnPassant(fromCoordinate,toCoordinate)){
			movePiece(fromCoordinate, toCoordinate);
			getSpot(new Coordinate(toCoordinate.getXCoordinate(), fromCoordinate.getYCoordinate())).addPiece(null);
			if(getSpot(fromCoordinate).annaPiece()!=null){
				getSpot(new Coordinate(toCoordinate.getXCoordinate(), fromCoordinate.getYCoordinate())).addPiece(temp);
			}else{
				moves++;
				}
		}

	}

	private boolean isEnPassant(Coordinate fromCoordinate, Coordinate toCoordinate){
		if(getSpot(fromCoordinate).annaPiece().getName().matches("Pawn") && Math.abs(fromCoordinate.getXCoordinate()-toCoordinate.getXCoordinate())==1 && Math.abs(fromCoordinate.getYCoordinate()-toCoordinate.getYCoordinate())==1){

			return enPassantBoard[toCoordinate.getXCoordinate()][fromCoordinate.getYCoordinate()] && moves - enPassantBoardMove[toCoordinate.getXCoordinate()][fromCoordinate.getYCoordinate()] == 1;
		}
		return false;
	}

	private void hitsKing(Colour colour, Coordinate toCoordinate){

		Piece King = etsiKing(annaOppositeColour(colour));
		assert King != null;
		if(getSpot(toCoordinate).annaPiece()!=null){
			if(getSpot(toCoordinate).annaPiece().isAttackPossible(new Coordinate(King.annaX(), King.annaY()))){
				if(isNotOnTheWay(toCoordinate, new Coordinate(King.annaX(), King.annaY()))){
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
					if(piece.getName().matches("King") && piece.annaVari()==colour){
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
						if(piece.annaVari()==turn){
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
		int fromX = fromCoordinate.getXCoordinate();
		int fromY = fromCoordinate.getYCoordinate();
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
					if(fromSpot.annaPiece().isAttackPossible(new Coordinate(i,j)) && turn!=spot.annaPiece().annaVari()){
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
			if(piece.annaVari()==Colour.WHITE){
				if(toCoordinate.getYCoordinate()==7){
					int z = changePawn.display();
					pawnImprove(z, toCoordinate, Colour.WHITE);
				}
			}else{
				if(toCoordinate.getYCoordinate()==0){
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

		return turn == getSpot(coordinate).annaPiece().annaVari();
	}

	private void setTurn(){ //metodi asettaa peli vuoron
		if(turn==Colour.WHITE){
			turn=Colour.BLACK;
		}else{
			turn=Colour.WHITE;
		}
	}

	public Spot getSpot(Coordinate coordinate){ //metodi palauttaa board
		return chessBoard.getSpotWithCoordinates(coordinate.getXCoordinate(),coordinate.getYCoordinate()) ;
	}

//	public Spot getSpot(int x,  int y){ //metodi palauttaa board
//		return chessBoard.getSpotWithCoordinates(x,y) ;
//	}



}
