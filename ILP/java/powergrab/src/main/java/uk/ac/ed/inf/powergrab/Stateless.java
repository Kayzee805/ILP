package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Random;

//to do: LinkedHashMap isnt sorted in dice for some reason
//use linked list

public class Stateless extends Drone {

    public Stateless(Position start, ArrayList<Double> allCoins,
            ArrayList<Double> allPower, Position[] allPos, int seed) {

        // invokes the methods and variables of teh Drone class
        super(start, allCoins, allPower, allPos, seed);
    }

    // Returns the position of the station and it's index in the 3 array var
    /*
     * Returns the positon of the station and its index in the 3 array Variables
     * allCoins, allPower and allPos where, the station is within 0.00055
     * degrees from the drone 0.0003 for the movement and 0.00025 for the
     * station's range This is similar to a big Circle
     */
    private LinkedHashMap<Position, Integer> inBigRange(
            ArrayList<Integer> index) {
        LinkedHashMap<Position, Integer> result = new LinkedHashMap<Position, Integer>();
        for (int i = 0; i < index.size(); i++) {
            double distance = calcDistance(getCurrent(), allPos[index.get(i)]);
            if (distance <= (Math.pow(0.00025, 1) + Math.pow(0.0003, 1))) {

                result.put(allPos[index.get(i)], index.get(i));
            }
        }
        return result;
    }

    /*
     * Checks if the positions of the stations are within 0.00025degrees of the
     * drone This is a small circle
     */
    private LinkedHashMap<Direction, Integer> inSmallRange(
            LinkedHashMap<Position, Integer> positions) {
        LinkedHashMap<Direction, Integer> result = new LinkedHashMap<Direction, Integer>();
        double distance = Double.MAX_VALUE;
        if (positions.size() > 0) {
            // only executes the below if there are at least 1 drone within the
            // big circle
            for (Direction x : allDirections) {
                Position temp = getCurrent().nextPosition(x);
                if (temp.inPlayArea()) {
                    for (Position y : positions.keySet()) {
                        distance = calcDistance(y, temp);
                        // checks for the value to stop duplicates when choosing
                        // a move later
                        if (distance <= 0.00025 && !result.containsKey(x)) {
                            result.put(x, positions.get(y));
                            // System.out.println(result.get(x) +" and the
                            // distance = "+ distance);

                        }
                    }
                }
            }

        }
        return result;

    }

    // returns list with only positives, if negative and positive in one
    // direction, checks if the positive one is closer or nah
    /*
     * Returns the direction and the station of the index, which contains a
     * station, where the closest station is positive
     */
    private LinkedHashMap<Direction, Integer> positiveMovesOnly() {
        LinkedHashMap<Direction, Integer> pos = inSmallRange(
                inBigRange(indexPositives));
        LinkedHashMap<Direction, Integer> neg = inSmallRange(
                inBigRange(indexNegatives));
            /*
        if(getMoves() ==0) {
            System.out.println(pos.keySet());
            System.out.println(neg.keySet());
        }*/
        LinkedHashMap<Direction, Integer> result = new LinkedHashMap<Direction, Integer>();
        for (Direction x : pos.keySet()) {

            if (neg.containsKey(x)) {
                if (!closeNegative(x, pos.get(x), neg.get(x))) {
                    /*
                     * so when a negative and positive is in the same direction
                     * checks if the positive is closer to the drone or not
                     * if it is, adds to the list to be returned
                     */

                    result.put(x, pos.get(x));
                }
            } else {
                //if no negatives, adds to the list to be returned
                result.put(x, pos.get(x));

            }
        }
        // System.out.println(result);
        return result;

    }

    /*
     * returns the index of the station, that is closest to the given position
     */
    public int closestStationStateless(Position temp) {

        int index = Integer.MAX_VALUE;

        double minDistance = Double.MAX_VALUE;
        // loops through all the stations
        for (int i = 0; i < allPos.length; i++) {
            double distance = calcDistance(temp, allPos[i]);
            // and checks if the station **is** within 0.00025 degrees
            if (distance < minDistance && distance <= 0.00025) {
                minDistance = distance;
                index = i; // index of the station
            }

        }

        return index;
    }

    /*
     * Chooses a direction for the drone where, if surrounded by multiple
     * positive stations, it chooses the one with the higest coin and makes sure
     * that if the drone moves to that direction it'll charge to that station.
     * returns null if no valid move with a positive station.
     */

    private Direction bestMove(
            LinkedHashMap<Direction, Integer> possibleMoves) {
        Direction result = null;
        double maxCoin = 0;
        if (possibleMoves.size() > 0) {
            for (Direction x : possibleMoves.keySet()) {
                // scans the drone's area if it moved in the x'th direction
                Position temp = getCurrent().nextPosition(x);
                double coin = allCoins.get(possibleMoves.get(x));
                int closest = closestStationStateless(temp);

                // this will check if the station is the closest station to the
                // drone
                if (possibleMoves.get(x) == closest) {
                    if (coin > maxCoin) {
                        maxCoin = coin;
                        result = x;
                    }
                }
            }
            // System.out.println(maxCoin +" at move " + getMoves());
        }
        // System.out.println(result +" coins = "+maxCoin);

        /*
         * if(result!= null) { System.out.println(result+" moves = "+getMoves()
         * +"    "+possibleMoves.keySet()); }
         */

        return result;
    }

    /*
     * In the rare case, where the drone is only in range of negative moves it
     * chooses the negative station with the highest coins so, the drone loses
     * the least amount of coins
     */
    private Direction bestNegMove(
            LinkedHashMap<Direction, Integer> possibleMoves) {
        Direction result = null;
        double maxCoins = Double.MIN_VALUE;
        if (possibleMoves.size() > 0) {
            for (Direction d : possibleMoves.keySet()) {
                if (allCoins.get(possibleMoves.get(d)) > maxCoins) {
                    maxCoins = allCoins.get(possibleMoves.get(d));
                    result = d;
                }

            }
        }

        return result;

    }

    /*
     * method for the drone when making random moves Returns a List of
     * directions which prevents the drone from connecting to a negative station
     * or going out of bounds
     */
    private ArrayList<Direction> listWithoutNegatives(
            ArrayList<Direction> directions,
            LinkedHashMap<Direction, Integer> allNeg) {
        ArrayList<Direction> result = new ArrayList<Direction>();

        for (int i = 0; i < directions.size(); i++) {
            Position temp = getCurrent().nextPosition(directions.get(i));
            if (!allNeg.containsKey(directions.get(i)) && temp.inPlayArea()) {

                result.add(directions.get(i));
            }
        }
        return result;
    }

    /*
     * Compares two stations, mainly a positive and a negative if teh negative
     * is closer returns true else false
     */
    private boolean closeNegative(Direction d, int pos, int neg) {

        Position temp = getCurrent().nextPosition(d);

        /*
         * calculates the distance from a position where the drone has moved in
         * the d Direction
         */
        double posDistance = calcDistance(temp, allPos[pos]);
        double negDistance = calcDistance(temp, allPos[neg]);
        if (posDistance > negDistance) {
            return true;
        } else {
            return false;
        }

    }

    /*
     * Executes the movement of the drone for one move
     */

    private void moving() {
        LinkedHashMap<Direction, Integer> possibleMoves = positiveMovesOnly();
        Direction bestPositiveMove = bestMove(possibleMoves);

        // checks if the drone can move to a positive station or not, if so
        // moves there
        if (bestPositiveMove != null) {
            move(bestPositiveMove);
            // the direction is saved for one move, until it changes next move
            // it is sent to the method that'll log the movements of the drone
            chosenDirection = bestPositiveMove;
            // System.out.println(getMoves() +" direction = "+bestPositiveMove);

        }
        /*
         * Executes the following if no valid positive station in range
         */
        else {
            LinkedHashMap<Direction, Integer> negSmallRange = inSmallRange(
                    inBigRange(indexNegatives));

            // computes the list of directions with positive and valid moves
            ArrayList<Direction> test = listWithoutNegatives(allDirections,
                    negSmallRange); //

            // in case only negative moves are available, calls the method to
            // choose the direction with the higest coins.
            if (test.size() == 0) {
                Direction d = bestNegMove(negSmallRange);
                move(d);
                chosenDirection = d;
                // tho, this section is hardly ever used by the drone, it is
                // here
                // in case of an extreme case

            } else {
                // else it'll choose a random direction
                int size = test.size();

                int random = Integer.MAX_VALUE;
                while (random >= size) {
                    random = randomNumber();
                }

                Direction chosen = test.get(random);
                move(chosen);
                chosenDirection = chosen;

                // System.out.println(getMoves()+" and direction = "+ chosen);
            }
        }
    }

    // exectues the entire movement of the drone until it can no longer move.
    public void execute() {
        double total = totalCoins();

        while (!gameOver()) {
            Position previous = getCurrent();
            // moves the drone one step at a time, until the game is over
            moving();
            // logs each movement of the drone to be output-ted
            moveLog(previous, getCurrent());
        }
        /*
        System.out
                .println("The game is over and the Drone will stop moving now");

        // below are just to see the results of the drone's life cycle
        System.out.println(
                getCoin() + " out of " + total + " coins has been collected");
                */
    //    System.out.println("Positives visited = " + POS);
        System.out.println("Negatives visited = " + NEG);

    }
}
