package Pelimekaniikat;

import DataObjects.*;



public class ChessBoard {


    private Spot[][] board ;

    public ChessBoard() {

        board = new Spot[8][8];

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
    }


    public Spot getSpotWithCoordinates(int x, int y){
        return  this.board[x][y];
    }
}
