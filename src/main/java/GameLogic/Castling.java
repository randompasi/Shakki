package GameLogic;

import ChessPieces.Piece;
import ChessPieces.Spot;
import Util.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Castling {

    private ChessBoard chessBoard;
  List<Piece> rooks = new ArrayList<>();

    public Castling(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
        rooks.add(getSpot(new Coordinate(7,7)).annaPiece());
        rooks.add(getSpot(new Coordinate(0,7)).annaPiece());
        rooks.add(getSpot(new Coordinate(7,0)).annaPiece());
        rooks.add(getSpot(new Coordinate(0,0)).annaPiece());
    }


    public boolean isCastling(Piece king, Coordinate toCoordinate){
        return  isPieceKing(king) && isMoveCastling(king,toCoordinate) && isCastlingPossible(toCoordinate.getXCoordinate(), king);


    }
    private boolean isPieceKing(Piece king){
      return  king.getName().matches("King");
    }


    private boolean isMoveCastling(Piece king, Coordinate toCoordinate){
     return    Math.abs(toCoordinate.getXCoordinate()- king.annaX())==2 && toCoordinate.getYCoordinate()- king.annaY()==0;
    }


   private boolean isCastlingPossible(int toX, Piece king){


            for(Piece rook : rooks){
                if(rook.isFirstMove() && king.isFirstMove()){
                if(Math.abs(rook.annaX()-toX) <= 2){
                        return isPieceInWay(rook.annaX(), king);


                    }
                    }
            }
            return false;
    }

    private boolean isPieceInWay(int rookX, Piece king) {
        for (int i = rookX + 1 ; i < king.annaX() ; i++){
            if(chessBoard.getSpotWithCoordinates(i, king.annaY()).annaPiece() != null) return false;
        }
        return true;
    }



    private Spot getSpot(Coordinate coordinate){ //metodi palauttaa board
        return chessBoard.getSpotWithCoordinates(coordinate.getXCoordinate(),coordinate.getYCoordinate()) ;
    }

    public Piece getRookForCastling(int toX){
        for(Piece rook : rooks){
            if(Math.abs(rook.annaX()-toX) <= 2){
                return rook;
            }
        }
        return null;
    }
    public int  getRooksToCoordination(Piece rook){
        if(rook.annaX()==0)  return  3;
        else return 5;
    }

}
