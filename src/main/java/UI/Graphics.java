package UI;


import DataObjects.Colour;
import DataObjects.Piece;
import GameLogic.*;
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
	private ChessLogic CB;
	private Color blue = Color.BLUE;
	private final Rectangle[][] taulu = new Rectangle[8][8];
	private boolean vaihto = true;
	private int moveX = 0;
	private int moveY = 0;
	private int toX = 0;
	private int toY = 0;
	
	public Graphics() throws IOException {
		CB = new ChessLogic();
		createChessBoard();
		
	}
	
	public Graphics(ChessLogic CB) throws IOException {
		this.CB=CB;
		createChessBoard();
	}




	public ChessLogic getCB() {
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
            	if(CB.getSpot(col, row) != null && CB.getSpot(col, row).annaPiece() != null){
            		ImageView imgView = new ImageView(chooseImage(CB.getSpot(col, row).annaPiece()));
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
                				CB.move(moveX, moveY, toX, toY);
                				
                				createChessBoard();
            				}
            				}
            			}
            	}
            }

	
	public Image chooseImage(Piece piece) throws IOException {
		Image image;
		String resourceName = piece.getColour().toString() + piece.getName();

				if(isWhitePiece(piece)){
				image =	loadImage(resourceLocation(piece, resourceName));

				}
				else{
					image =	loadImage(resourceLocation(piece, resourceName));
				}
				return image;
	}

	private URL resourceLocation(Piece piece, String resourceName){

		return getClass().getResource("/images/"+ resourceName + ".gif");

	}

	private boolean isWhitePiece(Piece piece){
		Colour vari = piece.annaVari();
		return vari == Colour.WHITE;
	}


	private Image loadImage(URL url)  {
		return  new Image(url.toExternalForm());
	}
	

}
