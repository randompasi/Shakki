import ChessPieces.Colour;
import Util.Coordinate;
import ChessPieces.King;
import ChessPieces.Piece;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;



import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class KingTest extends  BeforeEachTest {




    @ParameterizedTest
    @EnumSource(Colour.class)
    public void startingLocationsTest(Colour colour){
                chessBoard.createStartingPieces();
                if(colour == Colour.WHITE){
                    Piece target = chessBoard.getSpotWithCoordinates(4,0).annaPiece();
                        assertTrue( target instanceof King);

                }
                else{
                    Piece target = chessBoard.getSpotWithCoordinates(4,7).annaPiece();
                    assertTrue( target instanceof King);


                }


    }

    @ParameterizedTest
    @EnumSource(Colour.class)
    public void acceptableMovesTest(Colour colour){

                    for (Coordinate fromCoordinate : coordinates ){
                        Piece king = new King(colour, fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate());
                        king.changeKoords(fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate());
                        for (Coordinate toCoornidate : coordinates ){
                            if(Math.abs(fromCoordinate.getxCoordinate()-toCoornidate.getxCoordinate())<=1 && Math.abs(fromCoordinate.getyCoordinate()-toCoornidate.getyCoordinate()) <=1) {
                                assertTrue(king.isAttackPossible(fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate(), toCoornidate.getxCoordinate(), toCoornidate.getyCoordinate()));
                            }

                        }
                    }

            }


    @ParameterizedTest
    @EnumSource(Colour.class)
    public void notAcceptableMovesTest(Colour colour){

        for (Coordinate fromCoordinate : coordinates ){
            Piece king = new King(colour, fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate());
            king.changeKoords(fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate());
            for (Coordinate toCoornidate : coordinates ){
                if(Math.abs(fromCoordinate.getxCoordinate()-toCoornidate.getxCoordinate()) > 1 && Math.abs(fromCoordinate.getyCoordinate()-toCoornidate.getyCoordinate()) > 1) {
                    assertFalse(king.isMovePossible(fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate(), toCoornidate.getxCoordinate(), toCoornidate.getyCoordinate()));
                }

            }
        }

    }
    @ParameterizedTest
    @EnumSource(Colour.class)
    public void castlingTest(Colour colour){
        Piece king;

        if(colour == Colour.WHITE) {
            king = new King(colour, 4, 0);
            assertTrue(king.isAttackPossible(4,0,6,0));
            assertTrue(king.isAttackPossible(4,0,2,0));
        }else{
            king = new King(colour, 4, 7);
            assertTrue(king.isAttackPossible(4,7,6,7));
            assertTrue(king.isAttackPossible(4,7,2,7));

        }




    }



}
