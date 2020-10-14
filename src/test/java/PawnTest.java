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
            Piece pawn = new Pawn(colour, fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate());
            pawn.changeKoords(fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate());

                for (Coordinate toCoornidate : coordinates) {
                    if (toCoornidate.getxCoordinate() == fromCoordinate.getxCoordinate() && toCoornidate.getyCoordinate() - fromCoordinate.getyCoordinate() == 1 && colour == Colour.WHITE) {
                        assertTrue(pawn.isMovePossible(fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate(), toCoornidate.getxCoordinate(), toCoornidate.getyCoordinate()));
                    } else if (toCoornidate.getxCoordinate() == fromCoordinate.getxCoordinate() && fromCoordinate.getyCoordinate() - toCoornidate.getyCoordinate() == 1 &&  colour == Colour.BLACK) {
                        assertTrue(pawn.isMovePossible(fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate(), toCoornidate.getxCoordinate(), toCoornidate.getyCoordinate()));
                    }

                }


        }

    }


//
//    @ParameterizedTest
//    @EnumSource(Colour.class)
//    public void notAcceptableMovesTest(Colour colour){
//
//        for (Coordinate fromCoordinate : coordinates ){
//            Piece king = new King(colour, fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate());
//            king.changeKoords(fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate());
//            for (Coordinate toCoornidate : coordinates ){
//                if(Math.abs(fromCoordinate.getxCoordinate()-toCoornidate.getxCoordinate()) > 1 && Math.abs(fromCoordinate.getyCoordinate()-toCoornidate.getyCoordinate()) > 1) {
//                    assertFalse(king.isAttackPossible(fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate(), toCoornidate.getxCoordinate(), toCoornidate.getyCoordinate()));
//                }
//
//            }
//        }
//
//    }
}
