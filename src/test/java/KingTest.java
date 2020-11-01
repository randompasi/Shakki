import ChessPieces.Colour;
import Util.Coordinate;
import ChessPieces.King;
import ChessPieces.Piece;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;


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
                        Piece king = new King(colour, fromCoordinate);
                        king.changeKoords(fromCoordinate);
                        for (Coordinate toCoornidate : coordinates ){
                          if( 1 == (int)Math.sqrt(Math.pow(fromCoordinate.getX()-toCoornidate.getX(), 2) + Math.pow(fromCoordinate.getY()-toCoornidate.getY(), 2)))
                                assertTrue(king.isAttackPossible(toCoornidate));

                        }
                    }

            }


    @ParameterizedTest
    @EnumSource(Colour.class)
    public void notAcceptableMovesTest(Colour colour){

        for (Coordinate fromCoordinate : coordinates ){
            Piece king = new King(colour, fromCoordinate);
            king.changeKoords(fromCoordinate);
            for (Coordinate toCoornidate : coordinates ){
                if(Math.abs(fromCoordinate.getX()-toCoornidate.getX()) > 1 && Math.abs(fromCoordinate.getY()-toCoornidate.getY()) > 1) {
                    assertFalse(king.isMovePossible(toCoornidate));
                }

            }
        }

    }
    @ParameterizedTest
    @EnumSource(Colour.class)
    public void castlingTest(Colour colour){
        Piece king;

        if(colour == Colour.WHITE) {
            king = new King(colour, new Coordinate(4, 0));
            assertTrue(king.isAttackPossible(new Coordinate(6,0)));
            assertTrue(king.isAttackPossible(new Coordinate(2,0)));
        }else{
            king = new King(colour, new Coordinate(4, 7));
            assertTrue(king.isAttackPossible(new Coordinate(6,7)));
            assertTrue(king.isAttackPossible(new Coordinate(2,7)));

        }




    }



}
