package GameLogic;

import java.io.Serializable;

import ChessPieces.*;
import Misc.PeliHistoria;
import UI.Winner;
import UI.changePawn;
import Util.Coordinate;
import Util.Player;

public class ChessLogic implements Serializable{

	private ChessBoard chessBoard;
	private boolean[][] enPassantBoard;
	private int[][] enPassantBoardMove;
	private boolean WhiteKingIsOnCheck;
	private boolean BlackKingIsOnCheck;
	private PeliHistoria PH;
	private Player p1;
	private Player p2;
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
		PH = new PeliHistoria();
		turn=Colour.WHITE;

		p1 = new Player("White Player", Colour.WHITE, true);
		p2 = new Player("Black Player", Colour.BLACK, false);



	}

	public void move(Coordinate fromCoordinate, Coordinate toCoordinate){
		this.fromCoordinate	=	fromCoordinate;
		this.toCoordinate	=	toCoordinate;
		Spot toSpot	=	getSpot(toCoordinate);
		Spot fromSpot	=	getSpot(fromCoordinate);


		if(fromSpot !=null){ // katsotaan onko from paikkassa nappula
			if(checkTurn(fromCoordinate)){
				if(toSpot.annaPiece() !=null){ // katsotaan onko to paikassa nappula
					if(checkPiecesColour()){
						if(fromSpot.annaPiece().isAttackPossible(toCoordinate)){ // jos ovat eri, katsotaan onko attack mahdollista
							if(isNotOnTheWay(fromCoordinate, toCoordinate)){
								movePiece(fromCoordinate, toCoordinate);
							}
						}
					}
				}else{ //jos ei ole  nappula
					if(fromSpot.annaPiece().isMovePossible(toCoordinate)){//katostaan onko move mahdollista
						if(isNotOnTheWay(fromCoordinate, toCoordinate)){  //
							if(fromSpot.annaPiece().getName().matches("King") && Math.abs(toCoordinate.getXCoordinate()-fromCoordinate.getXCoordinate())>1){
								Castle(fromCoordinate, toCoordinate); // castling option
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



	private boolean checkPiecesColour(){ //metodi palautta true jos from ja to paikassa nappulat ovapalauttaa false jos  ovat eri
		if(getSpot(fromCoordinate).annaPiece().annaVari() == getSpot(toCoordinate).annaPiece().annaVari()){
			return false;
		}
		return true;
	}

	private boolean isNotOnTheWay(Coordinate fromCoordinate,Coordinate toCoordinate){ //tn metodin avulla katsotaan onko jotain nappuloita from ja

		//jos nappula on "Knight" metodi  palauttaa true, eli ei o
		if(getSpot(fromCoordinate).annaPiece().getName().matches("Knight")){
			return true;
		}

		int fromX = fromCoordinate.getXCoordinate();
		int fromY = fromCoordinate.getYCoordinate();
		int toX = toCoordinate.getXCoordinate();
		int toY = toCoordinate.getYCoordinate();

		if(fromX==toX){ // X akseli
			if(toY>fromY){
				for(int i=fromY+1; i<toY; i++){
					if(getSpot(fromX, i).annaPiece()!=null){
						return false;
					}
				}
			}else{
				for(int i=fromY-1; i>toY; i--){
					if(getSpot(fromX, i).annaPiece() != null){
						return false;
					}
				}
			}
		}else if(fromY==toY){ // Y askeli
			if(toX>fromX){
				for(int i=fromX+1; i<toX; i++){
					if(chessBoard.getSpotWithCoordinates(i,fromY).annaPiece()!=null){
						return false;
					}
				}
			}else{
				for(int i=fromX-1; i>toX; i--){
					if(chessBoard.getSpotWithCoordinates(i,fromY).annaPiece()!=null){
						return false;
					}
				}
			}


		// In diagonal (Suomeksi?)
		}else if(Math.abs(toX - fromX) == Math.abs(toY- fromY)){
			if(toX>fromX && toY>fromY){ //NE (North East)
				for(int i=1;i<toX-fromX; i++){
					if(chessBoard.getSpotWithCoordinates(fromX+i, fromY+i).annaPiece()!=null){
						return false;
					}
				}
			}else if(toX>fromX && toY<fromY){ //SE (South East)
				for(int i=1;i<toX-fromX; i++){
					if(chessBoard.getSpotWithCoordinates(fromX+i, fromY-i).annaPiece()!=null){
						return false;
					}
				}
			}else if(toX<fromX && toY>fromY){ //NW (North West)
				for(int i=1;i<fromX-toX; i++)
					if(chessBoard.getSpotWithCoordinates(fromX-i, fromY+i).annaPiece()!=null){
						return false;
					}
				}
			}else if(toX<fromX && toX<fromY){ //SW (South West)
				for(int i=1;i<fromX-toX; i++){
					if(chessBoard.getSpotWithCoordinates(fromX-i, fromY-i).annaPiece()!=null){
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

		if(NumberOfPiecesThatAreHittingTOXTOY == 0){
			return true;
		}
		return false;

	}

	private void Castle(Coordinate fromCoordinate, Coordinate toCoordinate){ // metodi tarkista onko castling mahdollista ja jos se on, suorittaa sen

		int fromX = fromCoordinate.getXCoordinate();
		int fromY = fromCoordinate.getYCoordinate();
		int toX = toCoordinate.getXCoordinate();
		int toY = toCoordinate.getYCoordinate();

		if(getSpot(fromCoordinate).annaPiece().annaFirstMove()){ // ensiin tytyy kastsoa, onko King first move true

			if(getSpot(fromCoordinate).annaPiece().annaVari()==Colour.WHITE){ //kun king on valkoinen

				if(fromX==4 && fromY==0 && toX==6 && toY==0 && chessBoard.getSpotWithCoordinates(7, 0).annaPiece().annaFirstMove()){ //  tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true

					if(nothingHits(new Coordinate(4, 0), Colour.BLACK) && nothingHits(new Coordinate(5 ,0), Colour.BLACK) && nothingHits(new Coordinate(6, 0), Colour.BLACK)){
						executeMove(new Coordinate(7,0), new Coordinate(5,0), null);
						movePiece(fromCoordinate,toCoordinate); //siirretaan king
						hitsKing(chessBoard.getSpotWithCoordinates(5, 0).annaPiece().annaVari(), new Coordinate(5, 0));
					}

				}else if(fromX==4 && fromY==0 && toX==2 && toY==0 && chessBoard.getSpotWithCoordinates(0, 0).annaPiece().annaFirstMove()){ // t tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true

					if(isNotOnTheWay(new Coordinate(0,0), new Coordinate(3,0))){

						if(nothingHits(new Coordinate(4, 0), Colour.BLACK) && nothingHits(new Coordinate(3 ,0), Colour.BLACK) && nothingHits(new Coordinate(2, 0), Colour.BLACK)){

							executeMove(new Coordinate(0,0), new Coordinate(3,0), null);
							movePiece(fromCoordinate,toCoordinate); //siirretn king
							hitsKing(getSpot(new Coordinate(3,0)).annaPiece().annaVari(), new Coordinate(3, 0));
						}

					}
				}
			}else{ // kun king on musta
				if(fromX==4 && fromY==7 && toX==6 && toY==7 && chessBoard.getSpotWithCoordinates(7, 7).annaPiece().annaFirstMove()){ // t tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true
					if(nothingHits(new Coordinate(4, 7), Colour.WHITE) && nothingHits(new Coordinate(5 ,7), Colour.WHITE) && nothingHits(new Coordinate(6, 7), Colour.WHITE)){
						executeMove(new Coordinate(7,7), new Coordinate(5,7), null);
						movePiece(fromCoordinate,toCoordinate); //siirretn king
						hitsKing(getSpot(new Coordinate(5,7)).annaPiece().annaVari(), new Coordinate(5, 7));
					}
				}else if(fromX==4 && fromY==7 && toX==2 && toY==7 && chessBoard.getSpotWithCoordinates(0, 7).annaPiece().annaFirstMove()){ //  tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true
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
			getSpot(toCoordinate.getXCoordinate(), fromCoordinate.getYCoordinate()).addPiece(null);
			if(getSpot(fromCoordinate).annaPiece()!=null){
				getSpot(toCoordinate.getXCoordinate(), fromCoordinate.getYCoordinate()).addPiece(temp);
			}else{
				moves++;
				}
		}

	}

	private boolean isEnPassant(Coordinate fromCoordinate, Coordinate toCoordinate){
		if(getSpot(fromCoordinate).annaPiece().getName().matches("Pawn") && Math.abs(fromCoordinate.getXCoordinate()-toCoordinate.getXCoordinate())==1 && Math.abs(fromCoordinate.getYCoordinate()-toCoordinate.getYCoordinate())==1){

			if(enPassantBoard[toCoordinate.getXCoordinate()][fromCoordinate.getYCoordinate()] && moves - enPassantBoardMove[toCoordinate.getXCoordinate()][fromCoordinate.getYCoordinate()] == 1){
					return true;

			}
		}
		return false;
	}

	private void hitsKing(Colour colour, Coordinate toCoordinate){

		Piece King = etsiKing(annaOppositeColour(colour));
		if(getSpot(toCoordinate).annaPiece()!=null){
			if(getSpot(toCoordinate).annaPiece().isAttackPossible(toCoordinate)){
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
				Piece piece = getSpot(i,j).annaPiece();
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
					Piece piece = getSpot(i,j).annaPiece();
					if(piece!=null){
						if(piece.annaVari()==turn){
							if(simulateMove(i,j)){
								didfind++;
							}
						}
					}
				}
			}
			if(didfind==0){
				if(turn==Colour.BLACK){
					boolean blackKingCheckMate = true;
					Winner.display("valkoiset");
					System.out.println("Check Mate Dude!");
				}else{
					boolean whiteKingCheckMate = true;
					Winner.display("mustat");
					System.out.println("Check Mate Dude!");
				}
			}
		}
	}

	private boolean simulateMove(int fromX, int fromY, Coordinate fromCoordinate){
     Spot  fromSpot = getSpot(fromCoordinate);
		Piece temp = fromSpot.annaPiece();
		Piece temp2;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){

				if(fromX==i && fromY==j){
					continue;
				}

				if(getSpot(i,j).annaPiece()!=null){
					Spot spot = getSpot(i,j);
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


								if(BlackKingIsOnCheck==false){
									preventFromCheck(turn);
									return true;
								}
							}else{
								if(WhiteKingIsOnCheck==false){
									preventFromCheck(turn);
									return true;
								}
							}
						}
					}
				}else{
					if(fromSpot.annaPiece().isMovePossible(new Coordinate(i,j))){

						if(isNotOnTheWay(new Coordinate(fromX, fromY), new Coordinate(i, j))){
							getSpot(i,j).addPiece(temp);
							getSpot(i,j).annaPiece().changeKoords(new Coordinate(i, j));
							fromSpot.addPiece(null);

							preventFromCheck(turn);

							getSpot(i,j).addPiece(null);
							fromSpot.addPiece(temp);
							fromSpot.annaPiece().changeKoords(new Coordinate(fromX, fromY));

							if(turn==Colour.BLACK){


								if(BlackKingIsOnCheck==false){
									preventFromCheck(turn);
									return true;
								}
							}else{


								if(WhiteKingIsOnCheck==false){
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
					pawnImprove(z, toCoordinate.getXCoordinate(), toCoordinate.getYCoordinate(), Colour.WHITE);
				}
			}else{
				if(toCoordinate.getYCoordinate()==0){
					int z = changePawn.display();
					pawnImprove(z, toCoordinate.getXCoordinate(), toCoordinate.getYCoordinate(), Colour.BLACK);
				}
			}
		}
	}

	private void pawnImprove(int type, int x, int y, Colour colour){
		if(type == 3)
			getSpot(x,y).addPiece(new Knight(colour, x, y));
		else if(type == 2)
			getSpot(x,y).addPiece(new Bishop(colour, x, y));
		else if(type == 4)
			getSpot(x,y).addPiece(new Rook(colour, x, y));
		else
			getSpot(x,y).addPiece(new Queen(colour, x, y));
	}

	private boolean checkTurn(Coordinate coordinate){ // metodi tarkista peli vuoron

		if(turn==getSpot(coordinate).annaPiece().annaVari()){

			return true;
		}
		return false;
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

	public Spot getSpot(int x,  int y){ //metodi palauttaa board
		return chessBoard.getSpotWithCoordinates(x,y) ;
	}



}
