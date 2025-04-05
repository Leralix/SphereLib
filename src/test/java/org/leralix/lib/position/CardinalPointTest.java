package org.leralix.lib.position;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CardinalPointTest {


    @ParameterizedTest
    @ValueSource(floats = {-135, 180, -180, 135.1f})
    void testNorth(float yaw){
        assertEquals(CardinalPoint.NORTH, CardinalPoint.getCardinalPoint(yaw));
    }

    @ParameterizedTest
    @ValueSource(floats = {-134.9f, -90, -45})
    void testEast(float yaw){
        assertEquals(CardinalPoint.EAST, CardinalPoint.getCardinalPoint(yaw));
    }


    @ParameterizedTest
    @ValueSource(floats = {-44.9f, 0, 45})
    void testSouth(float yaw){
        assertEquals(CardinalPoint.SOUTH, CardinalPoint.getCardinalPoint(yaw));
    }

    @ParameterizedTest
    @ValueSource(floats = {45.1f, 90, 134.9f})
    void testWest(float yaw){
        assertEquals(CardinalPoint.WEST, CardinalPoint.getCardinalPoint(yaw));
    }



}