package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Drone {
    private Position current;
    //list of all possible 16 directions
    public static final ArrayList<Direction> allDirections = new ArrayList<Direction>(
            Arrays.asList(Direction.N, Direction.NNE, Direction.NE,
                    Direction.ENE, Direction.E, Direction.ESE, Direction.SE,
                    Direction.SSE, Direction.S, Direction.SSW, Direction.SW,
                    Direction.WSW, Direction.W, Direction.WNW, Direction.NW,
                    Direction.NNW));
    public static Random rand;

    private double power;
    private double coins;
    //record of all the positions the drone has visited
    private ArrayList<Position> history = new ArrayList<Position>(); 

    public ArrayList<Double> allCoins;
    public ArrayList<Double> allPower;
    public Position[] allPos;
    private int moves = 0;
    public int NEG = 0;
    public int POS = 0;
    public int counter = 0;
    public Direction prevD;
    //where the logs of the drone will be stored
    public String output = "";
    public Direction chosenDirection;

    private int seed;
    public static ArrayList<Integer> indexPositives;
    public static ArrayList<Integer> indexNegatives;
    //dummy counter is the number of moves made without charging to a non zero station
    public int dummyCounter = 0;

    //initialises all the variables
    public Drone(Position start, ArrayList<Double> allCoins,
            ArrayList<Double> allPower, Position[] allPos, int seed) {

        this.power = 250.0;
        this.coins = 0.0;
        this.current = start;
        this.allCoins = allCoins;
        this.allPower = allPower;
        this.allPos = allPos;
        this.seed = seed;

        history.add(start);

        //initalises the pseudo number generator
        rand = new Random(seed);

    }

    // initialises the indexes of all the positive & negative stations in two arrays
    public void indexOfStations() {
        ArrayList<Integer> positives = new ArrayList<Integer>();
        ArrayList<Integer> negatives = new ArrayList<Integer>();

        for (int i = 0; i < allCoins.size(); i++) {
            if (allCoins.get(i) >= 0) {
                //as no station can have one negative and one positive property
                positives.add(i);
            } else {
                negatives.add(i);
            }
        }
        indexPositives = positives;
        indexNegatives = negatives;
    }

    //a method that outputs the total number of coins in the map
    public double totalCoins() {
        double totalCoins = 0;

        for (int i = 0; i < indexPositives.size(); i++) {
            totalCoins += allCoins.get(indexPositives.get(i));
        }
        return totalCoins;
    }

    
    
    //Euclidean distance of one position to the other
    public double calcDistance(Position a, Position b) {

        double y = Math.pow(a.longitude - b.longitude, 2);
        double x = Math.pow(a.latitude - b.latitude, 2);
        return (Math.pow(y + x, 0.5));

    }

    //boolean of whether or not the drone can make any more moves
    public boolean gameOver() {
        if (power < 1.25 || moves == 250) {
            return true; // game is over
        } else {
            return false; // game not done yet
        }

    }

    //moves the drone by one move and charges it if posssible
    public void move(Direction D) {
        if (!gameOver()) {
            // System.out.println("direction moved at move :"+ moves+" is "+ D);
            if (moves == 0) {
                prevD = D;
            }
            current = current.nextPosition(D);
            
            prevD = D;
            //adds the position to the history of positions
            history.add(current);
            power -= 1.25;
            moves++;
            transfer();

        }
       

    }

    //returns the index of the closest station from the drone
    public int closestStation() {

        int index = Integer.MAX_VALUE;

        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < allPos.length; i++) {
            double distance = calcDistance(current, allPos[i]);
         
            if (distance < minDistance && distance <= 0.00025) { 
                minDistance = distance;
                index = i; // index of the station
            }

        }

        return index;

    }

    //transfers coins and powers from a station to the drone, if possible
    private void transfer() {
        int stationIndex = closestStation();
        if (stationIndex != Integer.MAX_VALUE) {
            double stationCoins = allCoins.get(stationIndex);
            double stationPower = allPower.get(stationIndex);
            double newCoins = 0;
            double newPower = 0;
          
   
            if (coins + stationCoins < 0) {
                coins = 0;
                newCoins = stationCoins - coins;

            } else {

                coins += stationCoins;
                newCoins = 0;

            }
            if (power + stationPower < 0) {
                power = 0;
                newPower = stationPower - power;
            } else {
                power += stationPower;
                newPower = 0;
            }

            //dummyCounter doesn't change unless the drone charges to a station with non-zero
            //coins or power
            if (stationCoins == 0) {
                dummyCounter++;
             //   System.out.println("already visited station");
            } else {
                dummyCounter = 0;   
            }
            if (stationCoins > 0) {
                POS++;
                // System.out.println("move "+ moves +" is positive and the
                // counter is "+counter);

            }
            if (stationCoins < 0) {
                System.out.println("its a negative station at move " + moves);
                NEG++;
            }

            //extracts the coins from the station and sets them with their new value
            allCoins.set(stationIndex, newCoins);
            allPower.set(stationIndex, newPower);
        }

        else {
            dummyCounter++;
        }

    }

    //Logs each move made by the drone in a specific format
    public void moveLog(Position previous, Position next) {
        String addThis = "";
        if (getMoves() < 250) {
            addThis = "" + previous.latitude + ", " + previous.longitude + ", "
                    + chosenDirection + ", " + next.latitude + ", "
                    + next.longitude + ", ";
            //printing the coins and powers to 6decimal places
            String coinString = String.format("%.6f , %.6f \n", getCoin(),
                    getPower());
            addThis += coinString;
        } else {
            
            addThis = "" + previous.latitude + ", " + previous.longitude + ", "
                    + chosenDirection + ", " + next.latitude + ", "
                    + next.longitude + ", ";
            String coinString = String.format("%.6f , %.6f", getCoin(),
                    getPower());
            addThis += coinString;
            
        }
        output += addThis;
    }
    public int randomNumber() {
        int random= rand.nextInt(16);
    //    System.out.println(moves+"    "+random);
        return random;
    }

    public ArrayList<Position> getHistory() {
        return history;
    }

    public double getCoin() {
        return coins;
    }

    public double getPower() {
        return power;
    }

    public int getMoves() {
        return moves;
    }

    public Position getCurrent() {
        return current;
    }

    public String getOutput() {
        return output;
    }

}
