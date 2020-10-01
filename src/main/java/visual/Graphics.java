package visual;


import Pelimekaniikat.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import java.io.IOException;
import java.net.URL;


public class Graphics extends Panel {
	
	private final int size = 8;
	private GridPane chessBoard = new GridPane();
	private ChessBoard CB;
	private Color blue = Color.BLUE;
	private final Rectangle[][] taulu = new Rectangle[8][8];
	private boolean vaihto = true;
	private int moveX = 0;
	private int moveY = 0;
	private int toX = 0;
	private int toY = 0;
	
	public Graphics() throws IOException {
		CB = new ChessBoard();
		createChessBoard();
		
	}
	
	public Graphics(ChessBoard CB) throws IOException {
		this.CB=CB;
		createChessBoard();
	}




	public ChessBoard getCB() {
		return CB;
	}


	public GridPane getChessBoard() {
		return chessBoard;
	}
	
	
	public void  createChessBoard() throws IOException {
		for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Rectangle square = new Rectangle();
                taulu[row][col] = square;
                Color color;
                if ((row + col) % 2 == 0)
                	color = Color.BISQUE;
                
                else 
                	color = Color.BROWN;
                
                square.setFill(color);
                
                //Luodaan listener jokaiseen neliöön
                square.setOnMouseClicked(e -> {
					try {
						loydaoikeaRuutu(square);
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				});
                chessBoard.add(square, col, row);
                
                square.widthProperty().bind(chessBoard.widthProperty().divide(size));
                square.heightProperty().bind(chessBoard.heightProperty().divide(size));
            	if(CB.annaStringBoard(col, row) != null && CB.annaStringBoard(col, row).annaPiece() != null){
            		ImageView imgView = new ImageView(chooseImage(CB.annaStringBoard(col, row)));
            		chessBoard.add(imgView,col ,row );
       
            		imgView.fitWidthProperty().bind(square.widthProperty()); 
            		imgView.fitHeightProperty().bind(square.heightProperty());
            		imgView.setOnMouseClicked(e -> {
						try {
							loydaoikeaRuutu(square);
						} catch (IOException ioException) {
							ioException.printStackTrace();
						}
					});
            		}
            }
		}
	}
	
            public void loydaoikeaRuutu(Rectangle square) throws IOException {
            	square.setFill(blue);
            	for(int i=0 ; i < taulu.length; i++){
            		for(int j=0; j <taulu[i].length; j++){
            			if(square == taulu[i][j]){
            				if(vaihto){
            				vaihto = false;
            				moveX = j;
            				moveY = i;		
            				}
            				else{
            					vaihto=true;
                				toX = j;
                				toY = i;
                				System.out.println(moveX+","+moveY+","+toX+","+toY);
                				CB.move(moveX, moveY, toX, toY);
                				
                				createChessBoard();
            				}
            				}
            			}
            	}
            }

	
	public Image chooseImage(Spot spot) throws IOException {
		Image image;

				if(isWhitePiece(spot)){
				image =	loadImage(getClass().getResource(parser(spot.annaPiece(), "W")));

				}
				else{
					image =	loadImage(getClass().getResource(parser(spot.annaPiece(), "B")));
				}
				return image;
	}

	private String parser(Piece piece, String color){
		String url;

		if(piece instanceof Bishop)
			url = "/images/"+ color +"B.gif";

		else if(piece instanceof King)
			url = "/images/"+ color +"K.gif";


		else if(piece instanceof Knight)
			url = "/images/"+ color +"N.gif";

		else if(piece instanceof Rook)
			url = "/images/"+ color +"R.gif";

		else if(piece instanceof Queen)
			url = "/images/"+ color +"Q.gif";

		else
			url = "/images/"+ color +"P.gif";

		return url;
	}

	private boolean isWhitePiece(Spot spot){
		Colour vari = spot.annaPiece().annaVari();
		return vari == Colour.WHITE;
	}


	private Image loadImage(URL url)  {
		return  new Image(url.toExternalForm());
	}
	

}
