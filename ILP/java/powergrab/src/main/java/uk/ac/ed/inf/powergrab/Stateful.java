package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Random;

//Is a subclass of the Drone class
public class Stateful extends Drone {
    private static int startIndex;

    // holds the indexes of already visited stations, for the route
    private ArrayList<Integer> visited = new ArrayList<Integer>();

    // 2d array of distance from one station to the other
    private double[][] graph;
    // the route, similar to bus stops, for the drone to move in
    private static ArrayList<Integer> route = new ArrayList<Integer>();
    private int counter = 0;

    public Stateful(Position start, ArrayList<Double> allCoins,
            ArrayList<Double> allPower, Position[] allPos, int seed) {

        // calls the methods and varaibles of the done class
        super(start, allCoins, allPower, allPos, seed);
    }

    /*
     * checks if the station that the counter is within charging range of the
     * drone, so the closest station to the drone
     * 
     */

    private boolean nextCounter(Position temp) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < indexPositives.size(); i++) {
            if (indexPositives.get(i) != route.get(counter)) {
                double distance = calcDistance(temp,
                        allPos[indexPositives.get(i)]);
                if (distance < min && distance <= 0.00025
                        && allCoins.get(indexPositives.get(i)) != 0) {
                    min = distance;
                }
            }
        }
        // compares the distances of two stations from the drone
        double pos = calcDistance(temp, allPos[route.get(counter)]);
        if (pos < min) {
            return false;
        } else {

            return true;
        }

    }

    /*
     * Returns the direction, that reduces the distance from the drone to the
     * station, whilst avoiding negatives however, exceptions are made from time
     * to time
     */
    private Direction shortestMove() {

        double minDistance = Double.MAX_VALUE;
        Direction result = null;
        for (Direction d : allDirections) {
            Position temp = getCurrent().nextPosition(d);
            double distance = calcDistance(temp, allPos[route.get(counter)]);

            if (temp.inPlayArea() && distance < minDistance) {
                // if a negative is blocking the drone from moving
                // and the drone cannot go pass it, it decides to go through it
                if (dummyCounter > 31) {
                    minDistance = distance;
                    result = d;

                }
                // in case the drone can't charge to the staition due to another
                // already charged station, it takes a step back then trys again
                else if (dummyCounter > 20 && dummyCounter < 32) {

                    if (!inRangeVisited(temp) && !inRangeNegative(temp)) {
                        minDistance = distance;
                        result = d;
                    }
                } else {

                    // checks for directions that are not in range of negatives
                    // and that the drone is not repeating a move
                    if (!inRangeNegative(temp) && !nextCounter(temp)
                            && dummyCounter > 6
                            && Direction.oppositeDirection.get(prevD) != d) {
                        minDistance = distance;
                        result = d;
                    }
                    // if the drone just finished charging, it is allowed to
                    // make a
                    // few moves which would cause it to go back where it came
                    // from
                    else if (!inRangeNegative(temp) && !nextCounter(temp)
                            && dummyCounter < 7) {
                        minDistance = distance;
                        result = d;
                    }
                }

            }
        }
        // in case its null and prevents drone from moving, it'll choose the
        // direction thats the shortest to the station were going to
        // regardless of negatives, as long as it's in bounds.
        if (result == null) {
            double minDistance2 = Double.MAX_VALUE;
            for (Direction d : allDirections) {
                Position temp = getCurrent().nextPosition(d);
                double pos = calcDistance(temp, allPos[route.get(counter)]);
                if (temp.inPlayArea()) {
                    if (pos < minDistance2) {
                        minDistance = pos;
                        result = d;
                    }
                }
            }
        }

        // System.out.println(result);
        return result;

    }

    // returns true if the drone is in range of an already visited station
    private boolean inRangeVisited(Position temp) {
        if (counter < route.size()) {
            for (int i = 0; i < allPos.length; i++) {
                double dummy = calcDistance(temp, allPos[i]);
                // makes sure the drone doesn't compare it with the station
                // its trying to get to
                if (dummy <= 0.00025 && allCoins.get(i) == 0
                        && i != route.get(counter)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Returns true if the closest station to the drone is a negative

    private boolean inRangeNegative(Position temp) {

        boolean found = false;
        double pos = calcDistance(temp, allPos[route.get(counter)]);

        double min = Double.MAX_VALUE;
        if (counter < route.size()) {
            for (int i = 0; i < indexNegatives.size(); i++) {
                if (indexNegatives.get(i) != route.get(counter)) {
                    double distance = calcDistance(temp,
                            allPos[indexNegatives.get(i)]);

                    if (distance < min && distance <= 0.00025
                            && allCoins.get(indexNegatives.get(i)) != 0) {
                        min = distance;
                        found = true;
                    }
                }
            }
        }
        if (pos <= 0.00025 && pos < min) {
            return false;
        }
        return found;
    }

    /*
     * Initialises the variable then fills it up with the distances from one
     * station to the other.
     */
    public void fillUpGraph() {

        int i = 0;
        int size = indexPositives.size();
        graph = new double[size][size];

        while (i < size) {
            for (int j = i; j < size; j++) {
                if (i == j) {
                    graph[i][j] = 0;
                    graph[j][i] = 0;
                } else {
                    Position temp1 = allPos[indexPositives.get(i)];
                    Position temp2 = allPos[indexPositives.get(j)];
                    graph[i][j] = calcDistance(temp1, temp2);
                    graph[j][i] = graph[i][j];
                }

            }
            i++;
        }
    }

    // finds the closest station from the station with the index startIndex
    private int findRoute() {

        int returnIndex = Integer.MAX_VALUE;
        double distance = Double.MAX_VALUE;
        int removeIndex = Integer.MAX_VALUE;

        // For the very first move, it checks from the initial move
        // startIndex will be initialised after the first closest station has
        // been found
        if (route.size() == 0) {
            for (int i = 0; i < indexPositives.size(); i++) {
                double dummyDistance = calcDistance(getCurrent(),
                        allPos[indexPositives.get(i)]);
                if (dummyDistance < distance) {

                    distance = dummyDistance;
                    returnIndex = indexPositives.get(i);
                    removeIndex = i;
                }
            }
        } else {
            //for the rest of the stations
            for (int i = 0; i < indexPositives.size(); i++) {

                if (!visited.contains(i)) {
                    double dummyDistance = graph[startIndex][i];
                    if (dummyDistance < distance) {
                        distance = dummyDistance;
                        returnIndex = indexPositives.get(i);
                        removeIndex = i;
                       
                    }
                }
            }
        }
        
        //adds the index to the list of already visited stations
        visited.add(removeIndex);
        //updates the position of the drone, to check from
        startIndex = removeIndex;

        return returnIndex;

    }

    //finds the entire route for the drone to move in
    public void allRoute() {

        while (route.size() < indexPositives.size()) {

            route.add(findRoute());
            // System.out.println("PosStation size = "+ posStation.size() +"
        }

    }
    //checks if the closest station from the drone is negative or positive
    private boolean closestNegative(Position temp) {
        ArrayList<Direction> result = new ArrayList<Direction>();
        int index = -1;
        double min = Double.MAX_VALUE;

        boolean found = false;
        for (int i = 0; i < allPos.length; i++) {
            double distance = calcDistance(temp, allPos[i]);
            if (distance <= 0.00025 && distance < min) {
                index = i;
                min = distance;
            }
        }

        if (index != -1) {
            if (allCoins.get(index) >= 0) {
                return false;
            }
            return true;
        }
        return false;

    }


    //list of directions that are valid and the closest station is non-negative
    private ArrayList<Direction> possibleMoves() {
        ArrayList<Direction> result = new ArrayList<Direction>();

        for (Direction x : allDirections) {
            Position temp = getCurrent().nextPosition(x);
            if (temp.inPlayArea() && !closestNegative(temp)) {
                result.add(x);

            }

        }

        // System.out.println("Moves :"+ moves+ " possible moves are "+ result);
        return result;
    }

    //Moves the drone once
    private void moving() {

        //each direction is chosen until, all the positive stations are visited
        if (counter < route.size()) {
            Direction d = shortestMove();
            move(d);
            //saves the direction to be logged
            chosenDirection = d;

            // System.out.println("move : "+ moves+ " in the direction :"+ d + "
            
            //if the station it's going to charges, it moves to the next one
            if (allCoins.get(route.get(counter)) == 0) {
                counter++;
            }
            
            //if the drone still hasn't reached a station in 35 moves, it moves 
            //to the next station as it is not worth trying more
            if (dummyCounter == 35) {
                counter++;
                dummyCounter = 0;
                System.out.println("skips at move " + getMoves());
            }

        } else {
            //if all stations visited, it moves in a random direction
            ArrayList<Direction> possibleDirections = possibleMoves();
            int size = possibleDirections.size();
            int random = Integer.MAX_VALUE;
            while(random>= size) {
                random = randomNumber();
            }
            Direction d = possibleDirections.get(random);
            move(d);
            chosenDirection = d;

            // System.out.println("Its a RANDOM move at move: "+moves +" ="+
            // current.latitude +", "+ current.longitude);

        }

    }

    //Exectues the movements of the drone till game over
    public void execute() {
        // System.out.println("total number of iteratiions possible in route =
        // "+route.size());

        while (!gameOver()) {
            Position previous = getCurrent();
            moving();
            //logs the movements of the drone
            moveLog(previous, getCurrent());

        }
      //  System.out.println("positives found = " + POS);
        System.out.println("negatives found = " + NEG);
        
    }

}
