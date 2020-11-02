import ChessPieces.Piece;
import GameLogic.ChessLogic;
import Util.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChessLogicTest extends BeforeEachTest {


    private ChessLogic chessLogic;

    @BeforeEach
    private void init(){
         chessLogic = new ChessLogic();
    }

    @Test
    public void moveWhitePawnTest(){

        Piece piece = chessLogic.getSpot(new Coordinate(0,1)).annaPiece();
        chessLogic.move(new Coordinate(0,1),new Coordinate(0,2));
        assertEquals(0, piece.getX());
        assertEquals(2, piece.getY());
        assertEquals(piece, chessLogic.getSpot(new Coordinate(0,2)).annaPiece());
    }

    @Test
    public void moveWhiteKnightTest(){
        Piece piece = chessLogic.getSpot(new Coordinate(1,0)).annaPiece();
        chessLogic.move(new Coordinate(1,0),new Coordinate(0,2));
        assertEquals(0, piece.getX());
        assertEquals(2, piece.getY());
        assertEquals(piece, chessLogic.getSpot(new Coordinate(0,2)).annaPiece());


    }

    @Test
    public void castlingTest(){
        for(int x = 1; x<4; x++){
            chessLogic.getSpot(new Coordinate(x,0)).addPiece(null);
        }
        Piece king = chessLogic.getSpot(new Coordinate(4,0)).annaPiece();
        Piece rook = chessLogic.getSpot(new Coordinate(0,0)).annaPiece();
        chessLogic.move(new Coordinate(4,0),new Coordinate(2,0));
        assertEquals(2, king.getX());
        assertEquals(0, king.getY());
        assertEquals(king, chessLogic.getSpot(new Coordinate(2,0)).annaPiece());
        assertEquals(3, rook.getX());
       assertEquals(0, rook.getY());
       assertEquals(rook, chessLogic.getSpot(new Coordinate(3,0)).annaPiece());


    }


    @Test
    public void enPassantTest(){
        Piece whitePawn = chessLogic.getSpot(new Coordinate(1,1)).annaPiece();
        chessLogic.move(new Coordinate(1,1),new Coordinate(1,3));//valkoinen tupla siirto
        chessLogic.move(new Coordinate(7,6),new Coordinate(7,5));//tyhja liike musra
        chessLogic.move(new Coordinate(1,3),new Coordinate(1,4));//yksi eteenpain valkoinen
        chessLogic.move(new Coordinate(0,6),new Coordinate(0,4));//musta 2 eteenpain
        chessLogic.move(new Coordinate(1,4),new Coordinate(0,5));
        assertEquals(0, whitePawn.getX(), whitePawn.getX()+" "+whitePawn.getY());
        assertEquals(5, whitePawn.getY(), "Y Coordinate");
        assertEquals(whitePawn, chessLogic.getSpot(new Coordinate(0,5)).annaPiece());
        assertEquals(0, whitePawn.getX(), whitePawn.getX()+" "+whitePawn.getY());
        assertEquals(null, chessLogic.getSpot(new Coordinate(0,4)).annaPiece(), "Blackpawn");



    }


    private void settingPiecesForCadtling(){

    }





}
