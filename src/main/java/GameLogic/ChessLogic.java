package GameLogic;

import java.io.Serializable;

import DataObjects.*;
import UI.Winner;
import UI.changePawn;

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
	private Spot fromSpot;
	private Spot toSpot;


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

	public void move(int fromX, int fromY,int toX,int toY){
      this.fromSpot	=	getSpot(fromX, fromY);
		this.toSpot	=	getSpot(toX, toY);

		if(fromSpot !=null){ // katsotaan onko from paikkassa nappula
			if(checkTurn(fromX, fromY)){
				if(toSpot.annaPiece() !=null){ // katsotaan onko to paikassa nappula
					if(checkPiecesColour(fromX,fromY,toX,toY)){ // jos on katsotaan sen v�ri 
						if(fromSpot.annaPiece().isAttackPossible(fromX, fromY, toX, toY)){ // jos ovat eri, katsotaan onko attack mahdollista
							if(isNotOnTheWay(fromX, fromY, toX, toY)){ // jos on mahdollista, katsotaan onko to ja from v�lill�  jotain nappula
								movePiece(fromX, fromY, toX, toY);
							}
						}
					}
				}else{ //jos ei ole  nappula
					if(fromSpot.annaPiece().isMovePossible(fromX, fromY, toX, toY)){//katostaan onko move mahdollista
						if(isNotOnTheWay(fromX, fromY, toX, toY)){  // jos on mahdollista, katsotaan onko to ja from v�lill� jotain nappula
							if(fromSpot.annaPiece().getName().matches("King") && Math.abs(toX-fromX)>1){
								Castle(fromX, fromY, toX, toY); // castling option
							}else{
								movePiece(fromX, fromY, toX, toY);
							}
						}
					}else{
						enPassant(fromX, fromY, toX, toY);
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
	
	private void executeMove(int fromX,int fromY,int toX,int toY, Piece piece){
		toSpot.addPiece(fromSpot.annaPiece());  // jos ei ole, to paikassa on from nappula
		fromSpot.addPiece(piece); // ja from paikassa on nyt null nappula
		if(piece!=null){
			fromSpot.annaPiece().changeKoords(fromX, fromY);
		}
		toSpot.annaPiece().changeKoords(toX, toY); //aseta nappulan uudet koordinatit ja laittaa firstMove falseksi
	}

	private void movePiece(int fromX, int fromY,int toX,int toY){ //metodi liikkuu nappuloita

			Piece temp = toSpot.annaPiece();
			
			executeMove(fromX, fromY, toX, toY, null);
			preventFromCheck( toSpot.annaPiece().annaVari());
			if(!((toSpot.annaPiece().annaVari()==Colour.BLACK && BlackKingIsOnCheck) || (toSpot.annaPiece().annaVari()==Colour.WHITE && WhiteKingIsOnCheck))){
				if(causesNoCheck(toSpot.annaPiece().annaVari())){
				enPassantMove(fromX, fromY, toX, toY, moves); //tarkastellaan "en passant"
				hitsKing(toSpot.annaPiece().annaVari(), toX, toY);
				setTurn(); // asetetaan peli vuoro
				moves++; // lis�t��n move
				pawnAtEnd(toX, toY);//pawn at 0 or 7
				PH.lisaaHistoriaan(toSpot.annaPiece(),fromX, fromY, toX, toY); //lis�minen liike Peli Historiaan
				kingCheckMate();
				}else{
					System.out.println("/////////////////  WRONG MOVE, KING WILL BE ON CHECK  /////////////////\n");
					executeMove(toX, toY, fromX, fromY, temp);
				}
			}else{
				System.out.println("/////////////////  WRONG MOVE, KING IS ON CHECK  /////////////////\n");
				executeMove(toX, toY, fromX, fromY, temp);
			}
			
	}
	
	private boolean causesNoCheck(Colour colour){

		Piece King = etsiKing(colour);
		
		for(int i=7;i>=0;i--){
			for(int j=0;j<8;j++){
				if(chessBoard.getSpotWithCoordinates(i,j).annaPiece()!=null){
					if(chessBoard.getSpotWithCoordinates(i,j).annaPiece().annaVari()!=colour){
						if(chessBoard.getSpotWithCoordinates(i,j).annaPiece().isAttackPossible(i, j, King.annaX(), King.annaY())){//<------------
							if(isNotOnTheWay(i, j, King.annaX(), King.annaY())){
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
						if(chessBoard.getSpotWithCoordinates(i,j).annaPiece().isAttackPossible(i, j, King.annaX(), King.annaY())){//<------------
							if(isNotOnTheWay(i, j, King.annaX(), King.annaY())){
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

	

	private boolean checkPiecesColour(int fromX, int fromY,int toX,int toY){ //metodi palautta true jos from ja to paikassa nappulat ovat eriv�risi�, ja palauttaa false jos  ovat eri
		if(fromSpot.annaPiece().annaVari() == toSpot.annaPiece().annaVari()){
			return false;
		}
		return true;
	}
	
	private boolean isNotOnTheWay(int fromX, int fromY,int toX,int toY){ //t�m�n metodin avulla katsotaan onko jotain nappuloita from ja to v�lill�
	
		//jos nappula on "Knight" metodi  palauttaa true, eli ei ole mit��n
		if(fromSpot.annaPiece().getName().matches("Knight")){
			return true;
		}
			
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
	private boolean nothingHits(int toX, int toY, Colour colour){
		
		int NumberOfPiecesThatAreHittingTOXTOY = 0;
		
		for(int i=7;i>=0;i--){
			for(int j=0;j<8;j++){
				if(chessBoard.getSpotWithCoordinates(i, j).annaPiece()!=null){
					if(chessBoard.getSpotWithCoordinates(i, j).annaPiece().annaVari()==colour){
						if(chessBoard.getSpotWithCoordinates(i, j).annaPiece().isAttackPossible(i, j, toX, toY)){//<------------
							if(isNotOnTheWay(i, j, toX, toY)){
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
	
	private void Castle(int fromX, int fromY,int toX,int toY){ // metodi tarkista onko castling mahdollista ja jos se on, suorittaa sen
		
		if(fromSpot.annaPiece().annaFirstMove()){ // ensiin t�ytyy kastsoa, onko King first move true
			
			if(fromSpot.annaPiece().annaVari()==Colour.WHITE){ //kun king on valkoinen
				
				if(fromX==4 && fromY==0 && toX==6 && toY==0 && chessBoard.getSpotWithCoordinates(7, 0).annaPiece().annaFirstMove()){ // t�ss� tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true
					
					if(nothingHits(4, 0, Colour.BLACK) && nothingHits(5 ,0, Colour.BLACK) && nothingHits(6, 0, Colour.BLACK)){
						executeMove(7, 0, 5, 0, null);
						movePiece(fromX, fromY, toX, toY); //siirret��n king
						hitsKing(chessBoard.getSpotWithCoordinates(5, 0).annaPiece().annaVari(), 5, 0);
					}
					
				}else if(fromX==4 && fromY==0 && toX==2 && toY==0 && chessBoard.getSpotWithCoordinates(0, 0).annaPiece().annaFirstMove()){ // t�ss� tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true
					
					if(isNotOnTheWay(0, 0, 3, 0)){	
						
						if(nothingHits(4, 0, Colour.BLACK) && nothingHits(3 ,0, Colour.BLACK) && nothingHits(2, 0 , Colour.BLACK)){
							
							executeMove(0, 0, 3, 0, null);
							movePiece(fromX, fromY, toX, toY); //siirret��n king
							hitsKing(chessBoard.getSpotWithCoordinates(3, 0).annaPiece().annaVari(), 3, 0);
						}
						
					}
				}
			}else{ // kun king on musta
				if(fromX==4 && fromY==7 && toX==6 && toY==7 && chessBoard.getSpotWithCoordinates(7, 7).annaPiece().annaFirstMove()){ // t�ss� tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true
					if(nothingHits(4, 7, Colour.WHITE) && nothingHits(5 ,7 , Colour.WHITE) && nothingHits(6, 7, Colour.WHITE)){
						executeMove(7, 7, 5, 7, null);
						movePiece(fromX, fromY, toX, toY); //siirret��n king
						hitsKing(chessBoard.getSpotWithCoordinates(5, 7).annaPiece().annaVari(), 5, 7);
					}
				}else if(fromX==4 && fromY==7 && toX==2 && toY==7 && chessBoard.getSpotWithCoordinates(0, 7).annaPiece().annaFirstMove()){ // t�ss� tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true
					if(isNotOnTheWay(0, 7, 3, 7)){
						if(nothingHits(4, 7, Colour.WHITE) && nothingHits(3 , 7, Colour.WHITE) && nothingHits(2, 7 , Colour.WHITE)){
							executeMove(0, 7, 3, 7, null);
							movePiece(fromX, fromY, toX, toY); //siirret��n king
							hitsKing(chessBoard.getSpotWithCoordinates(3, 7).annaPiece().annaVari(), 3, 7);
						}
					}
				}
			}
		}
	}

	private void enPassantMove(int fromX,int fromY,int toX,int toY,int moves){ //metodi katso onko movePiece "en passant" tapanen
		
		if(toSpot.annaPiece().getName().matches("Pawn ") && Math.abs(fromY-toY)==2){
			enPassantBoard[toX][toY]=true; //asettaa "enPassantBoard" boolean taulukkoon kordinaatti miss� on tapahtunut "en passant"
			enPassantBoardMove[toX][toY]=moves; //asettaa "enPassantBoardMove" int taulukkoon kuinka mones move se oli 
		}
		
	}

	private void enPassant(int fromX, int fromY,int toX,int toY){ //metodi toteutaa "en passant" 
		
		Piece temp=getSpot(toX,fromY).annaPiece();
		
		if(fromSpot.annaPiece().getName().matches("Pawn ") && Math.abs(fromX-toX)==1 && Math.abs(fromY-toY)==1){
			
			if(enPassantBoard[toX][fromY] && moves - enPassantBoardMove[toX][fromY] == 1){
				
					movePiece(fromX, fromY, toX, toY);
					getSpot(toX, fromY).addPiece(null);
					if(fromSpot.annaPiece()!=null){
						getSpot(toX, fromY).addPiece(temp);
					}else{
						moves++;
					}
			}
		}
	}
	
	private void hitsKing(Colour colour, int toX,int toY){
		
		Piece King = etsiKing(annaOppositeColour(colour));
		if(toSpot.annaPiece()!=null){
			if(toSpot.annaPiece().isAttackPossible(toX, toY, King.annaX(), King.annaY())){
				if(isNotOnTheWay(toX, toY, King.annaX(), King.annaY())){
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
	
	private boolean simulateMove(int fromX, int fromY){
		
		Piece temp = fromSpot.annaPiece();
		Piece temp2;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				
				if(fromX==i && fromY==j){
					continue;
				}
				
				if(getSpot(i,j).annaPiece()!=null){
					Spot spot = getSpot(i,j);
					if(fromSpot.annaPiece().isAttackPossible(fromX, fromY, i, j) && turn!=spot.annaPiece().annaVari()){
						if(isNotOnTheWay(fromX, fromY, i, j)){
							temp2=spot.annaPiece();
							spot.addPiece(temp);
							spot.annaPiece().changeKoords(i, j);
							fromSpot.addPiece(null);
							preventFromCheck(turn);

							spot.addPiece(temp2);
							spot.annaPiece().changeKoords(i, j);
							fromSpot.addPiece(temp);
							fromSpot.annaPiece().changeKoords(fromX, fromY);
							
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
					if(fromSpot.annaPiece().isMovePossible(fromX, fromY, i, j)){
						
						if(isNotOnTheWay(fromX, fromY, i, j)){
							getSpot(i,j).addPiece(temp);
							getSpot(i,j).annaPiece().changeKoords(i, j);
							fromSpot.addPiece(null);
							
							preventFromCheck(turn);

							getSpot(i,j).addPiece(null);
							fromSpot.addPiece(temp);
							fromSpot.annaPiece().changeKoords(fromX, fromY);
							
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
	
	private void pawnAtEnd(int x, int y){
		Piece piece = getSpot(x,y).annaPiece();
		if(piece.getName().matches("Pawn ")){
			if(piece.annaVari()==Colour.WHITE){
				if(y==7){
					int z = changePawn.display();
					pawnImprove(z, x, y, Colour.WHITE);
				}
			}else{
				if(y==0){
					int z = changePawn.display();
					pawnImprove(z, x, y, Colour.BLACK);
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
	
	private boolean checkTurn(int fromX,int fromY){ // metodi tarkista peli vuoron
		
		if(turn==fromSpot.annaPiece().annaVari()){
			
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
	
	public Spot getSpot(int x, int y){ //metodi palauttaa board
		return chessBoard.getSpotWithCoordinates(x,y) ;
	}

	
	
}
