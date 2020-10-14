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

        Piece piece = chessLogic.getSpot(0,1).annaPiece();
        chessLogic.move(new Coordinate(0,1),new Coordinate(0,2));
        assertEquals(0, piece.annaX());
        assertEquals(2, piece.annaY());
        assertEquals(piece, chessLogic.getSpot(0,2).annaPiece());
    }

    @Test
    public void moveWhiteKnightTest(){
        Piece piece = chessLogic.getSpot(1,0).annaPiece();
        chessLogic.move(new Coordinate(1,0),new Coordinate(0,2));
        assertEquals(0, piece.annaX());
        assertEquals(2, piece.annaY());
        assertEquals(piece, chessLogic.getSpot(0,2).annaPiece());


    }





}
