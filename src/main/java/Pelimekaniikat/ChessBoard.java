package Pelimekaniikat;

import java.io.Serializable;

import visual.Winner;
import visual.changePawn;

public class ChessBoard implements Serializable{

	private Spot[][] board ;
	private boolean[][] enPassantBoard;
	private int[][] enPassantBoardMove;
	private boolean WhiteKingIsOnCheck;
	private boolean BlackKingIsOnCheck;
	private boolean WhiteKingCheckMate;
	private boolean BlackKingCheckMate;
	private PeliHistoria PH;
	private Player p1;
	private Player p2;
	private Colour turn;
	private int moves;
	
	/**
	 * NOTES : 
	 * WHAT IF PAWN ON 0 OR 7?
	 * WHAT IF 3 SAME MOVES
	 * WHAT IF CASTLING ROOK HITS KING
	 * WHAT IF PIECE HITS WHILE CASTLING
	 * WHAT IF EN PASSANT AND CHECK
	 * REMEMBER TO CHANGE "Pawn " to "Pawn"
	 */
	public ChessBoard(Spot[][] board, boolean[][] enPassantBoard, int[][] enPassantBoardMove, boolean WhiteKingIsOnCheck, boolean BlackKingIsOnCheck, boolean WhiteKingCheckMate,
			boolean BlackKingCheckMate, PeliHistoria PH, Player p1, Player p2, Colour turn, int moves){
		
		this.board=board;
		this.enPassantBoard=enPassantBoard;
		this.enPassantBoardMove=enPassantBoardMove;
		this.WhiteKingIsOnCheck=WhiteKingIsOnCheck;
		this.BlackKingIsOnCheck=BlackKingIsOnCheck;
		this.WhiteKingCheckMate=WhiteKingCheckMate;
		this.BlackKingCheckMate=BlackKingCheckMate;
		this.PH=PH;
		this.p1=p1;
		this.p2=p2;
		this.turn=turn;
		this.moves=moves;
		
	}
	
	
	public ChessBoard(){//ChessBoard(Spot[][] board)
	
		moves=1;
		board = new Spot[8][8];
		enPassantBoard=new boolean[8][8];
		enPassantBoardMove=new int[8][8];
		PH = new PeliHistoria();
		turn=Colour.WHITE;
		
		p1 = new Player("White Player", Colour.WHITE, true);
		p2 = new Player("Black Player", Colour.BLACK, false);
		
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				board[i][j]=new Spot(i, j, null);
			}
		}
		
		//Luodaan valkoiset nappulat
		
		for(int i=0;i<8;i++){
			board[i][1].addPiece(new Pawn(Colour.WHITE, i, 1));
		}
		
		board[0][0].addPiece( new Rook(Colour.WHITE, 0, 0));
		board[7][0].addPiece( new Rook(Colour.WHITE, 7, 0));
		
		board[1][0].addPiece( new Knight(Colour.WHITE, 1, 0));
		board[6][0].addPiece( new Knight(Colour.WHITE, 6, 0));
		
		board[2][0].addPiece( new Bishop(Colour.WHITE, 2, 0));
		board[5][0].addPiece( new Bishop(Colour.WHITE, 5, 0));
		
		board[3][0].addPiece( new Queen(Colour.WHITE,3,0));
		
		board[4][0].addPiece( new King(Colour.WHITE,4,0));
		
		//Luodaan mustat nappulat
		
		for(int i=0;i<8;i++){
			board[i][6].addPiece(new Pawn(Colour.BLACK, i, 6));
		}
		
		board[0][7].addPiece( new Rook(Colour.BLACK, 0, 7));
		board[7][7].addPiece( new Rook(Colour.BLACK, 7, 7));
		
		board[1][7].addPiece( new Knight(Colour.BLACK, 1, 7));
		board[6][7].addPiece( new Knight(Colour.BLACK, 6, 7));
		
		board[2][7].addPiece( new Bishop(Colour.BLACK, 2, 7));
		board[5][7].addPiece( new Bishop(Colour.BLACK, 5, 7));
		
		board[3][7].addPiece( new Queen(Colour.BLACK,3,7));
		
		board[4][7].addPiece( new King(Colour.BLACK,4,7));
		
		giveMeStatus();  // tilannen tulostus
	}
	public boolean annaBlackKingCheckMate(){
		return BlackKingCheckMate;
	}
	
	public boolean annaWhiteKingCheckMate(){
		return WhiteKingCheckMate;
	}
	
	public void move(int fromX, int fromY,int toX,int toY){ 
		if(board[fromX][fromY].annaPiece()!=null){ // katsotaan onko from paikkassa nappula	
			if(checkTurn(fromX, fromY)){
				if(board[toX][toY].annaPiece()!=null){ // katsotaan onko to paikassa nappula
					if(checkPiecesColour(fromX,fromY,toX,toY)){ // jos on katsotaan sen v�ri 
						if(board[fromX][fromY].annaPiece().isAttackPossible(fromX, fromY, toX, toY)){ // jos ovat eri, katsotaan onko attack mahdollista
							if(isNotOnTheWay(fromX, fromY, toX, toY)){ // jos on mahdollista, katsotaan onko to ja from v�lill�  jotain nappula
								movePiece(fromX, fromY, toX, toY);
							}
						}
					}
				}else{ //jos ei ole  nappula
					if(board[fromX][fromY].annaPiece().isMovePossible(fromX, fromY, toX, toY)){//katostaan onko move mahdollista
						if(isNotOnTheWay(fromX, fromY, toX, toY)){  // jos on mahdollista, katsotaan onko to ja from v�lill� jotain nappula
							if(board[fromX][fromY].annaPiece().annaNimi().matches("King") && Math.abs(toX-fromX)>1){
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
		giveMeStatus();
	}	
	
	private void executeMove(int fromX,int fromY,int toX,int toY, Piece piece){
		board[toX][toY].addPiece(board[fromX][fromY].annaPiece());  // jos ei ole, to paikassa on from nappula
		board[fromX][fromY].addPiece(piece); // ja from paikassa on nyt null nappula
		if(piece!=null){
			board[fromX][fromY].annaPiece().changeKoords(fromX, fromY);
		}
		board[toX][toY].annaPiece().changeKoords(toX, toY); //aseta nappulan uudet koordinatit ja laittaa firstMove falseksi
	}

	private void movePiece(int fromX, int fromY,int toX,int toY){ //metodi liikkuu nappuloita

			Piece temp = board[toX][toY].annaPiece();
			
			executeMove(fromX, fromY, toX, toY, null);
			preventFromCheck(board[toX][toY].annaPiece().annaVari());
			if(!((board[toX][toY].annaPiece().annaVari()==Colour.BLACK && BlackKingIsOnCheck) || (board[toX][toY].annaPiece().annaVari()==Colour.WHITE && WhiteKingIsOnCheck))){
				if(causesNoCheck(board[toX][toY].annaPiece().annaVari())){
				enPassantMove(fromX, fromY, toX, toY, moves); //tarkastellaan "en passant"
				hitsKing(board[toX][toY].annaPiece().annaVari(), toX, toY);
				setTurn(); // asetetaan peli vuoro
				moves++; // lis�t��n move
				pawnAtEnd(toX, toY);//pawn at 0 or 7
				PH.lisaaHistoriaan(board[toX][toY].annaPiece(),fromX, fromY, toX, toY); //lis�minen liike Peli Historiaan
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
				if(board[i][j].annaPiece()!=null){
					if(board[i][j].annaPiece().annaVari()!=colour){
						if(board[i][j].annaPiece().isAttackPossible(i, j, King.annaX(), King.annaY())){//<------------
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
				if(board[i][j].annaPiece()!=null){
					if(board[i][j].annaPiece().annaVari()!=colour){
						if(board[i][j].annaPiece().isAttackPossible(i, j, King.annaX(), King.annaY())){//<------------
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
	
	
	public void giveMeStatus(){ //tilannen tulostus
		for(int i=7;i>=0;i--){
			for(int j=0;j<8;j++){
				if(board[j][i].annaPiece()==null){
					System.out.print(" null | ");
				}else{
					System.out.print(board[j][i].annaPiece().annaNimi()+" | ");
				}
			}
			System.out.println();
			System.out.println("________________________________________________________________");
		}
		System.out.println();
		System.out.println();
	}
	
	private boolean checkPiecesColour(int fromX, int fromY,int toX,int toY){ //metodi palautta true jos from ja to paikassa nappulat ovat eriv�risi�, ja palauttaa false jos  ovat eri
		if(board[fromX][fromY].annaPiece().annaVari() == board[toX][toY].annaPiece().annaVari()){
			return false;
		}
		return true;
	}
	
	private boolean isNotOnTheWay(int fromX, int fromY,int toX,int toY){ //t�m�n metodin avulla katsotaan onko jotain nappuloita from ja to v�lill�
	
		//jos nappula on "Knight" metodi  palauttaa true, eli ei ole mit��n
		if(board[fromX][fromY].annaPiece().annaNimi().matches("Knight")){
			return true;
		}
			
		if(fromX==toX){ // X akseli
			if(toY>fromY){
				for(int i=fromY+1; i<toY; i++){
					if(board[fromX][i].annaPiece()!=null){
						return false;
					}
				}
			}else{
				for(int i=fromY-1; i>toY; i--){
					if(board[fromX][i].annaPiece()!=null){
						return false;
					}
				}
			}
		}else if(fromY==toY){ // Y askeli
			if(toX>fromX){
				for(int i=fromX+1; i<toX; i++){
					if(board[i][fromY].annaPiece()!=null){
						return false;
					}
				}
			}else{
				for(int i=fromX-1; i>toX; i--){
					if(board[i][fromY].annaPiece()!=null){
						return false;
					}
				}
			}
			

		// In diagonal (Suomeksi?)
		}else if(Math.abs(toX - fromX) == Math.abs(toY- fromY)){
			if(toX>fromX && toY>fromY){ //NE (North East)
				for(int i=1;i<toX-fromX; i++){
					if(board[fromX+i][fromY+i].annaPiece()!=null){
						return false;
					}
				}
			}else if(toX>fromX && toY<fromY){ //SE (South East)
				for(int i=1;i<toX-fromX; i++){
					if(board[fromX+i][fromY-i].annaPiece()!=null){
						return false;
					}
				}
			}else if(toX<fromX && toY>fromY){ //NW (North West)
				for(int i=1;i<fromX-toX; i++){
					if(board[fromX-i][fromY+i].annaPiece()!=null){
						return false;
					}
				}
			}else if(toX<fromX && toX<fromY){ //SW (South West)
				for(int i=1;i<fromX-toX; i++){
					if(board[fromX-i][fromY-i].annaPiece()!=null){
						return false;
					}
				}
			}		
		}	
		return true;
	}
	private boolean nothingHits(int toX, int toY, Colour colour){
		
		int NumberOfPiecesThatAreHittingTOXTOY = 0;
		
		for(int i=7;i>=0;i--){
			for(int j=0;j<8;j++){
				if(board[i][j].annaPiece()!=null){
					if(board[i][j].annaPiece().annaVari()==colour){
						if(board[i][j].annaPiece().isAttackPossible(i, j, toX, toY)){//<------------
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
		
		if(board[fromX][fromY].annaPiece().annaFirstMove()){ // ensiin t�ytyy kastsoa, onko King first move true
			
			if(board[fromX][fromY].annaPiece().annaVari()==Colour.WHITE){ //kun king on valkoinen
				
				if(fromX==4 && fromY==0 && toX==6 && toY==0 && board[7][0].annaPiece().annaFirstMove()){ // t�ss� tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true
					
					if(nothingHits(4, 0, Colour.BLACK) && nothingHits(5 ,0, Colour.BLACK) && nothingHits(6, 0, Colour.BLACK)){
						executeMove(7, 0, 5, 0, null);
						movePiece(fromX, fromY, toX, toY); //siirret��n king
						hitsKing(board[5][0].annaPiece().annaVari(), 5, 0);
					}
					
				}else if(fromX==4 && fromY==0 && toX==2 && toY==0 && board[0][0].annaPiece().annaFirstMove()){ // t�ss� tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true
					
					if(isNotOnTheWay(0, 0, 3, 0)){	
						
						if(nothingHits(4, 0, Colour.BLACK) && nothingHits(3 ,0, Colour.BLACK) && nothingHits(2, 0 , Colour.BLACK)){
							
							executeMove(0, 0, 3, 0, null);
							movePiece(fromX, fromY, toX, toY); //siirret��n king
							hitsKing(board[3][0].annaPiece().annaVari(), 3, 0);
						}
						
					}
				}
			}else{ // kun king on musta
				if(fromX==4 && fromY==7 && toX==6 && toY==7 && board[7][7].annaPiece().annaFirstMove()){ // t�ss� tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true
					if(nothingHits(4, 7, Colour.WHITE) && nothingHits(5 ,7 , Colour.WHITE) && nothingHits(6, 7, Colour.WHITE)){
						executeMove(7, 7, 5, 7, null);
						movePiece(fromX, fromY, toX, toY); //siirret��n king
						hitsKing(board[5][7].annaPiece().annaVari(), 5, 7);
					}
				}else if(fromX==4 && fromY==7 && toX==2 && toY==7 && board[0][7].annaPiece().annaFirstMove()){ // t�ss� tarkistetaan on from ja to paikassa tietyt koordinatit ja onko tornilla first move true
					if(isNotOnTheWay(0, 7, 3, 7)){
						if(nothingHits(4, 7, Colour.WHITE) && nothingHits(3 , 7, Colour.WHITE) && nothingHits(2, 7 , Colour.WHITE)){
							executeMove(0, 7, 3, 7, null);
							movePiece(fromX, fromY, toX, toY); //siirret��n king
							hitsKing(board[3][7].annaPiece().annaVari(), 3, 7);
						}
					}
				}
			}
		}
	}

	private void enPassantMove(int fromX,int fromY,int toX,int toY,int moves){ //metodi katso onko movePiece "en passant" tapanen
		
		if(board[toX][toY].annaPiece().annaNimi().matches("Pawn ") && Math.abs(fromY-toY)==2){ 
			enPassantBoard[toX][toY]=true; //asettaa "enPassantBoard" boolean taulukkoon kordinaatti miss� on tapahtunut "en passant"
			enPassantBoardMove[toX][toY]=moves; //asettaa "enPassantBoardMove" int taulukkoon kuinka mones move se oli 
		}
		
	}

	private void enPassant(int fromX, int fromY,int toX,int toY){ //metodi toteutaa "en passant" 
		
		Piece temp=board[toX][fromY].annaPiece();
		
		if(board[fromX][fromY].annaPiece().annaNimi().matches("Pawn ") && Math.abs(fromX-toX)==1 && Math.abs(fromY-toY)==1){ 
			
			if(enPassantBoard[toX][fromY] && moves - enPassantBoardMove[toX][fromY] == 1){
				
					movePiece(fromX, fromY, toX, toY);
					board[toX][fromY].addPiece(null);
					if(board[fromX][fromY].annaPiece()!=null){
						board[toX][fromY].addPiece(temp);
					}else{
						moves++;
					}
			}
		}
	}
	
	private void hitsKing(Colour colour, int toX,int toY){
		
		Piece King = etsiKing(annaOppositeColour(colour));
		if(board[toX][toY].annaPiece()!=null){
			if(board[toX][toY].annaPiece().isAttackPossible(toX, toY, King.annaX(), King.annaY())){
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
				if(board[i][j].annaPiece() != null){
					if(board[i][j].annaPiece().annaNimi().matches("King") && board[i][j].annaPiece().annaVari()==colour){
						return board[i][j].annaPiece();
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
					if(board[i][j].annaPiece()!=null){
						if(board[i][j].annaPiece().annaVari()==turn){
							if(simulateMove(i,j)){
								didfind++;
							}
						}
					}
				}
			}
			if(didfind==0){
				if(turn==Colour.BLACK){
					BlackKingCheckMate = true;
					Winner.display("valkoiset");	
					System.out.println("Check Mate Dude!");
				}else{
					WhiteKingCheckMate = true;
					Winner.display("mustat");
					System.out.println("Check Mate Dude!");
				}
			}
		}
	}
	
	private boolean simulateMove(int fromX, int fromY){
		
		Piece temp = board[fromX][fromY].annaPiece();
		Piece temp2;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				
				if(fromX==i && fromY==j){
					continue;
				}
				
				if(board[i][j].annaPiece()!=null){
					if(board[fromX][fromY].annaPiece().isAttackPossible(fromX, fromY, i, j) && turn!=board[i][j].annaPiece().annaVari()){
						if(isNotOnTheWay(fromX, fromY, i, j)){
							temp2=board[i][j].annaPiece();
							board[i][j].addPiece(temp);
							board[i][j].annaPiece().changeKoords(i, j);
							board[fromX][fromY].addPiece(null);
							preventFromCheck(turn);
							
							board[i][j].addPiece(temp2);
							board[i][j].annaPiece().changeKoords(i, j);
							board[fromX][fromY].addPiece(temp);
							board[fromX][fromY].annaPiece().changeKoords(fromX, fromY);
							
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
					if(board[fromX][fromY].annaPiece().isMovePossible(fromX, fromY, i, j)){
						
						if(isNotOnTheWay(fromX, fromY, i, j)){
							board[i][j].addPiece(temp);
							board[i][j].annaPiece().changeKoords(i, j);
							board[fromX][fromY].addPiece(null);
							
							preventFromCheck(turn);
							
							board[i][j].addPiece(null);
							board[fromX][fromY].addPiece(temp);
							board[fromX][fromY].annaPiece().changeKoords(fromX, fromY);
							
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
		if(board[x][y].annaPiece().annaNimi().matches("Pawn ")){
			if(board[x][y].annaPiece().annaVari()==Colour.WHITE){
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
			board[x][y].addPiece(new Knight(colour, x, y));
		else if(type == 2)
			board[x][y].addPiece(new Bishop(colour, x, y));
		else if(type == 4)
			board[x][y].addPiece(new Rook(colour, x, y));
		else
			board[x][y].addPiece(new Queen(colour, x, y));
	}
	
	private boolean checkTurn(int fromX,int fromY){ // metodi tarkista peli vuoron
		
		if(turn==board[fromX][fromY].annaPiece().annaVari()){
			
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
	
	public Spot annaStringBoard(int x, int y){ //metodi palauttaa board 
		return board[x][y];
	}
	
	
	
}
