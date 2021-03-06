import GameLogic.ChessBoard;
import Util.Coordinate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

public class BeforeEachTest {

    protected ChessBoard chessBoard;
    protected static List<Coordinate> coordinates = new ArrayList<>();

    @BeforeEach
    private void Init(){
        chessBoard = new ChessBoard();


    }

    @BeforeAll
    protected static void initCoordinates(){
        for (int x = 0; x < 8 ; x++){
            for (int y = 0; y < 8 ; y++ ) {
                coordinates.add(new Coordinate(x ,y));
            }
        }
    }
}
