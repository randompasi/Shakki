import DataObjects.Colour;
import DataObjects.King;
import DataObjects.Piece;
import Pelimekaniikat.ChessBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class KingTest {


            private ChessBoard chessBoard;

            @BeforeEach
            private void Init(){
                chessBoard = new ChessBoard();
            }

    @ParameterizedTest
    @EnumSource(Colour.class)
    public void KingsStartingLocations(Colour colour){
                if(colour == Colour.WHITE){
                    Piece actual = new King(colour, 4, 0);
                    Piece target = chessBoard.getSpotWithCoordinates(4,0).annaPiece();
                        assertTrue( actual instanceof King);

                }
                else{
                    Piece actual = new King(colour, 4, 7);
                    Piece target = chessBoard.getSpotWithCoordinates(4,7).annaPiece();
                    assertTrue( actual instanceof King);


                }


            }




}
