import ChessPieces.Colour;
import ChessPieces.Pawn;
import ChessPieces.Piece;
import ChessPieces.Spot;
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
        assertEquals(0, piece.annaX());
        assertEquals(2, piece.annaY());
        assertEquals(piece, chessLogic.getSpot(new Coordinate(0,2)).annaPiece());
    }

    @Test
    public void moveWhiteKnightTest(){
        Piece piece = chessLogic.getSpot(new Coordinate(1,0)).annaPiece();
        chessLogic.move(new Coordinate(1,0),new Coordinate(0,2));
        assertEquals(0, piece.annaX());
        assertEquals(2, piece.annaY());
        assertEquals(piece, chessLogic.getSpot(new Coordinate(0,2)).annaPiece());


    }

    @Test
    public void castlingTest(){
        for(int x = 5; x<7; x++){
            chessLogic.getSpot(new Coordinate(x,0)).addPiece(null);
        }
        Piece king = chessLogic.getSpot(new Coordinate(4,0)).annaPiece();
        Piece rook = chessLogic.getSpot(new Coordinate(7,0)).annaPiece();
        chessLogic.move(new Coordinate(4,0),new Coordinate(6,0));
        assertEquals(6, king.annaX());
        assertEquals(0, king.annaY());
        assertEquals(king, chessLogic.getSpot(new Coordinate(6,0)).annaPiece());
        assertEquals(5, rook.annaX());
       assertEquals(0, rook.annaY());
       assertEquals(rook, chessLogic.getSpot(new Coordinate(5,0)).annaPiece());


    }


    @Test
    public void enPassantTest(){
        Piece whitePawn = chessLogic.getSpot(new Coordinate(1,1)).annaPiece();
        chessLogic.move(new Coordinate(1,1),new Coordinate(1,3));//valkoinen tupla siirto
        chessLogic.move(new Coordinate(7,6),new Coordinate(7,5));//tyhja liike musra
        chessLogic.move(new Coordinate(1,3),new Coordinate(1,4));//yksi eteenpain valkoinen
        chessLogic.move(new Coordinate(0,6),new Coordinate(0,4));//musta 2 eteenpain
        chessLogic.move(new Coordinate(1,4),new Coordinate(0,5));
        assertEquals(0, whitePawn.annaX(), whitePawn.annaX()+" "+whitePawn.annaY());
        assertEquals(5, whitePawn.annaY(), "Y Coordinate");
        assertEquals(whitePawn, chessLogic.getSpot(new Coordinate(0,5)).annaPiece());
        assertEquals(0, whitePawn.annaX(), whitePawn.annaX()+" "+whitePawn.annaY());
        assertEquals(null, chessLogic.getSpot(new Coordinate(0,4)).annaPiece(), "Blackpawn");



    }


    private void settingPiecesForCadtling(){

    }





}
