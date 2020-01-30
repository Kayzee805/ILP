package uk.ac.ed.inf.powergrab;


import java.util.LinkedHashMap;


public enum Direction {
    N,NNE,NE,ENE,E,ESE,SE,SSE,S,SSW,SW,WSW,W,WNW,NW,NNW;
	
    
    //A map where the value is the opposite direction of the key, so 180 degrees of a direction
	public final static LinkedHashMap<Direction, Direction> oppositeDirection = new LinkedHashMap<Direction, Direction>(){{
		put(Direction.N, Direction.S);
		put(Direction.NNE, Direction.SSW);
		put(Direction.NE, Direction.SW);
		put(Direction.ENE, Direction.WSW);
		put(Direction.E, Direction.W);
		put(Direction.ESE, Direction.WNW);
		put(Direction.SE, Direction.NW);
		put(Direction.SSE, Direction.NNW);
		put(Direction.S, Direction.N);
		put(Direction.SSW, Direction.NNE);
		put(Direction.SW, Direction.NE);
		put(Direction.WSW, Direction.ENE);
		put(Direction.W,Direction.E);
		put(Direction.WNW, Direction.ESE);
		put(Direction.NW, Direction.SE);
		put(Direction.NNW, Direction.SSE);
	}};
}
