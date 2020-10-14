import ChessPieces.Colour;
import ChessPieces.Pawn;
import ChessPieces.Piece;
import GameLogic.ChessLogic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChessLogicTest extends BeforeEachTest {


    @Test
    public void movePawnTest(){
        ChessLogic chessLogic = new ChessLogic();
        Piece piece = chessLogic.getSpot(0,1).annaPiece();
        chessLogic.move(0,1,0,2);
        assertTrue(chessLogic.getSpot(0,2).annaPiece() instanceof Pawn);
        assertEquals(0, piece.annaX());
        assertEquals(2, piece.annaY());

    }





}
