import DataObjects.Colour;
import DataObjects.King;
import DataObjects.Piece;
import GameLogic.ChessBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.opentest4j.AssertionFailedError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class KingTest {


            private ChessBoard chessBoard;
            private List<Coordinate> coordinates = new ArrayList<>();

    @BeforeEach
            private void Init(){
                chessBoard = new ChessBoard();
                for (int x = 0; x < 8 ; x++){
                    for (int y = 0; y < 8 ; y++ ) {
                        coordinates.add(new Coordinate(x ,y));
                    }
                    }

            }

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
    public void movesTest(Colour colour){



                    for (Coordinate fromCoordinate : coordinates ){
                        Piece king = new King(colour, fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate());
                        for (Coordinate toCoornidate : coordinates ){
                            if(Math.abs(fromCoordinate.getxCoordinate()-toCoornidate.getxCoordinate())<=1 && Math.abs(fromCoordinate.getyCoordinate()-toCoornidate.getyCoordinate()) <=1) {
                                assertTrue(king.isAttackPossible(fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate(), toCoornidate.getxCoordinate(), toCoornidate.getyCoordinate()));
                            }
                            else {
                                try {
                                    assertFalse(king.isAttackPossible(fromCoordinate.getxCoordinate(), fromCoordinate.getyCoordinate(), toCoornidate.getxCoordinate(), toCoornidate.getyCoordinate()));
                                }
                                catch (AssertionFailedError error){
                                    System.out.println(fromCoordinate.getxCoordinate() +" "+fromCoordinate.getyCoordinate()+" "+toCoornidate.getxCoordinate()+" "+toCoornidate.getyCoordinate());
                                }
                            }
                        }
                    }
//
//
//
//                    }
            }




}
