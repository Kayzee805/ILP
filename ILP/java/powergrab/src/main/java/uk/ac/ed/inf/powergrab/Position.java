package uk.ac.ed.inf.powergrab;

public class Position {

    public final double r = 0.0003;

    public double latitude;
    public double longitude;
    
    //the boundaries of the map can not be changed
    public final double xMin = -3.192473;
    public final double xMax = -3.184319;
    public final double yMin = 55.942617;
    public final double yMax = 55.946233;

    public Position(double latitude, double longitude) { // latitude is y and
                                                         // longitude is x
        this.latitude = latitude;
        this.longitude = longitude;

    }

   
    //carryes out the movement of the drone in one direction
    public Position nextPosition(Direction direction) {
    

        double[] coords;
        // 0,22.5,45,67.5,90, 112.5, 135, 157.5, 180

        switch (direction) {

        case N:
            coords = CalcMove(90.0);
            return new Position(latitude + coords[0], longitude);

        case NNE:
            coords = CalcMove(67.5);
            return new Position(latitude + coords[0], longitude + coords[1]);

        case NE:
            coords = CalcMove(45);
            return new Position(latitude + coords[0], longitude + coords[1]);

        case ENE:
            coords = CalcMove(22.5);
            return new Position(latitude + coords[0], longitude + coords[1]);

        case E:
            coords = CalcMove(0);
            return new Position(latitude + coords[0], longitude + coords[1]);

        case ESE:
            coords = CalcMove(337.5);
            return new Position(latitude + coords[0], longitude + coords[1]);

        case SE:
            coords = CalcMove(315);
            return new Position(latitude + coords[0], longitude + coords[1]);

        case SSE:
            coords = CalcMove(292.5);
            return new Position(latitude + coords[0], longitude + coords[1]);

        case S:
            coords = CalcMove(270);
            return new Position(latitude + coords[0], longitude);

        case SSW:
            coords = CalcMove(247.5);
            return new Position(latitude + coords[0], longitude + coords[1]);

        case SW:
            coords = CalcMove(225);
            return new Position(latitude + coords[0], longitude + coords[1]);

        case WSW:
            coords = CalcMove(202.5);
            return new Position(latitude + coords[0], longitude + coords[1]);

        case W:
            coords = CalcMove(180);
            return new Position(latitude, longitude + coords[1]);

        case WNW:
            coords = CalcMove(157.5);
            return new Position(latitude + coords[0], longitude + coords[1]);

        case NW:
            coords = CalcMove(135);
            return new Position(latitude + coords[0], longitude + coords[1]);

        case NNW:
            coords = CalcMove(112.5);
            return new Position(latitude + coords[0], longitude + coords[1]);

        default: // So in case of null? or the direction is maybe invalid
            return new Position(latitude, longitude); // The same position is
                                                      // returned, i.e. the
                                                      // drone doesn't move

        }

    }

    //checks if the position is in bounds or not
    public boolean inPlayArea() {
        if (this.latitude >= yMax || this.latitude <= yMin
                || this.longitude >= xMax || this.longitude <= xMin) {
            return false;
        } else { 
            return true;
        }
    }

    //returns the additional distance to be added to the drone, if it moves
    //in a certain angle
    private double[] CalcMove(double angle) {
        double x = this.r * Math.cos(Math.toRadians(angle)); 
        double y = this.r * Math.sin(Math.toRadians(angle)); 
        double[] result = new double[] { y, x }; 
        
        return result;
    }

}
