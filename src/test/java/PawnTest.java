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

//    @ParameterizedTest
//    @EnumSource(Colour.class)
//    public void acceptableMovesTest(Colour colour){
//
//        for (Coordinate fromCoordinate : coordinates ){
//            Piece pawn = new Pawn(colour, fromCoordinate.getXCoordinate(), fromCoordinate.getYCoordinate());
//            pawn.changeKoords(fromCoordinate.getXCoordinate(), fromCoordinate.getYCoordinate());
//
//                for (Coordinate toCoornidate : coordinates) {
//                    if (toCoornidate.getXCoordinate() == fromCoordinate.getXCoordinate() && toCoornidate.getYCoordinate() - fromCoordinate.getYCoordinate() == 1 && colour == Colour.WHITE) {
//                        assertTrue(pawn.isMovePossible(fromCoordinate.getXCoordinate(), fromCoordinate.getYCoordinate(), toCoornidate.getXCoordinate(), toCoornidate.getYCoordinate()));
//                    } else if (toCoornidate.getXCoordinate() == fromCoordinate.getXCoordinate() && fromCoordinate.getYCoordinate() - toCoornidate.getYCoordinate() == 1 &&  colour == Colour.BLACK) {
//                        assertTrue(pawn.isMovePossible(fromCoordinate.getXCoordinate(), fromCoordinate.getYCoordinate(), toCoornidate.getXCoordinate(), toCoornidate.getYCoordinate()));
//                    }
//
//                }
//
//
//        }
//
//    }

}
