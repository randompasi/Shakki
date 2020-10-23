import ChessPieces.Colour;
import ChessPieces.Pawn;
import ChessPieces.Piece;

import Util.Coordinate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class PawnTest extends BeforeEachTest {



    @ParameterizedTest
    @EnumSource(Colour.class)
    public void startingLocationsTest(Colour colour) {
        chessBoard.createStartingPieces();

        for (int xCoord = 0; xCoord < 8; xCoord++) {
            if (colour == Colour.WHITE) {

                Piece target = chessBoard.getSpotWithCoordinates(xCoord, 1).annaPiece();
                assertTrue(target instanceof Pawn);

            } else {
                Piece target = chessBoard.getSpotWithCoordinates(xCoord, 6).annaPiece();
                assertTrue(target instanceof Pawn);
            }
        }


    }

    @ParameterizedTest
    @EnumSource(Colour.class)
    public void acceptableMovesTest(Colour colour){

        for (Coordinate fromCoordinate : coordinates ){
            Piece pawn = new Pawn(colour, fromCoordinate);
            pawn.changeKoords(fromCoordinate);

                for (Coordinate toCoornidate : coordinates) {
                    if (toCoornidate.getX() == fromCoordinate.getX() && toCoornidate.getY() - fromCoordinate.getY() == 1 && colour == Colour.WHITE) {
                        assertTrue(pawn.isMovePossible(toCoornidate));
                    } else if (toCoornidate.getX() == fromCoordinate.getX() && fromCoordinate.getY() - toCoornidate.getY() == 1 &&  colour == Colour.BLACK) {
                        try {
                            assertTrue(pawn.isMovePossible(toCoornidate));
                        } catch (Exception e) {

                            e.printStackTrace();
                        }finally {
                            pawn.isMovePossible(toCoornidate);
                        }
                    }

               }


      }

   }

}
