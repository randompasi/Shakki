package GameLogic;

import ChessPieces.*;



public class ChessBoard {


    private Spot[][] board ;

    public ChessBoard() {

        board = new Spot[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Spot(i, j, null);

            }
        }



    }


    public void createStartingPieces(){
        for (int i = 0; i < 8; i++) {
            board[i][1].addPiece(new Pawn(Colour.WHITE, i, 1));
        }

        for(int i=0;i<8;i++){
            board[i][6].addPiece(new Pawn(Colour.BLACK, i, 6));
        }
        createOneSide(Colour.BLACK, 7);
        createOneSide(Colour.WHITE, 0);
    }



    private void createOneSide(Colour colour, int yCoordinate){

            board[0][yCoordinate].addPiece( new Rook(colour, 0, yCoordinate));
            board[1][yCoordinate].addPiece( new Knight(colour, 1, yCoordinate));
            board[2][yCoordinate].addPiece( new Bishop(colour, 2, yCoordinate));
            board[3][yCoordinate].addPiece( new Queen(colour,3,yCoordinate));
            board[4][yCoordinate].addPiece( new King(colour,4,yCoordinate));
            board[5][yCoordinate].addPiece( new Bishop(colour, 5, yCoordinate));
            board[6][yCoordinate].addPiece( new Knight(colour, 6, yCoordinate));
            board[7][yCoordinate].addPiece( new Rook(colour, 7, yCoordinate));

        }


    public Spot getSpotWithCoordinates(int x, int y){
        return  this.board[x][y];
    }
}
