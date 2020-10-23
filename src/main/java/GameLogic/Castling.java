package GameLogic;

import ChessPieces.Piece;
import ChessPieces.Spot;
import Util.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Castling {

    private final ChessBoard chessBoard;
  List<Piece> rooks = new ArrayList<>();

    public Castling(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
        rooks.add(getSpot(new Coordinate(7,7)).annaPiece());
        rooks.add(getSpot(new Coordinate(0,7)).annaPiece());
        rooks.add(getSpot(new Coordinate(7,0)).annaPiece());
        rooks.add(getSpot(new Coordinate(0,0)).annaPiece());
    }


    public boolean isCastling(Piece king, Coordinate toCoordinate){
        return  isPieceKing(king) && isMoveCastling(king,toCoordinate) && isCastlingPossible(toCoordinate.getX(), king);


    }
    private boolean isPieceKing(Piece king){
      return  king.getName().matches("King");
    }


    private boolean isMoveCastling(Piece king, Coordinate toCoordinate){
     return    Math.abs(toCoordinate.getX()- king.getX())==2 && toCoordinate.getY()- king.getY()==0;
    }


   private boolean isCastlingPossible(int toX, Piece king){


        for(Piece rook : rooks){
            if(rook.isFirstMove() && king.isFirstMove()){
                if(Math.abs(rook.getX()-toX) <= 2){
                        return isPieceInWay(rook.getX(), king);
                }
            }
        }
        return false;
    }

    private boolean isPieceInWay(int rookX, Piece king) {
        for (int i = rookX + 1; i < king.getX() ; i++){
            if(chessBoard.getSpotWithCoordinates(i, king.getY()).annaPiece() != null) return false;
        }
        return true;
    }



    public Coordinate getRooksFromCoordination(Coordinate toCooordinate){
        for(Piece rook : rooks){
            if(Math.abs(rook.getX()-toCooordinate.getX()) <= 2 && rook.isFirstMove() && rook.getY() == toCooordinate.getY()){
                return new Coordinate(rook.getX(), rook.getY());
            }
        }
        return null;
    }
    public int  getRooksToXCoordination(int fromX){
        if(fromX==0)  return  3;
        else return 5;
    }


    private Spot getSpot(Coordinate coordinate){
        return chessBoard.getSpotWithCoordinates(coordinate.getX(),coordinate.getY()) ;
    }

}
