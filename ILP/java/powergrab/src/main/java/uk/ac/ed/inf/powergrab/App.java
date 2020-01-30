package uk.ac.ed.inf.powergrab;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Logger;

import com.mapbox.geojson.Feature;

public class App {

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();


        String link = "http://homepages.inf.ed.ac.uk/stg/powergrab/" + args[2]
                + "/" + args[1] + "/" + args[0] + "/powergrabmap.geojson";
        String date = args[0] + "-" + args[1] + "-" + args[2];
        int seed = Integer.parseInt(args[5]);

        String type = args[6];
     
        double latitude = Double.parseDouble(args[3]);
        ;
        double longitude = Double.parseDouble(args[4]);

        Position start = new Position(latitude, longitude);
  
        Stations stationList = new Stations(link);
        ArrayList<Feature> features = stationList.connect();

        stationList.init(features);
        ArrayList<Double> allPower = stationList.getPower();
        ArrayList<Double> allCoins = stationList.getCoins();
        Position[] allPos = stationList.getPos();

        //text for the geoJson file
        String geoJson = "";
        String path = "";
        
        //text for the txt file
        String text = "";
     //   System.out.println("Type: " + args[6] + " Date: " + date);
        //Initialises the drone the executes the movements of the drone
        //the movements are then returned in a geojson file and text file

        if (type.equals("stateless")) {
            Stateless droneA = new Stateless(start, allCoins, allPower,
                    allPos, seed);
            droneA.indexOfStations();
            double total = droneA.totalCoins();

            droneA.execute();

            geoJson = Stations.txtFile(droneA.getHistory(), features, date);
         //   System.out.println("Total amount of collectable coins = " + total);

           // System.out.println("Final Coins: " + droneA.getCoin()
             //       + " \nFinal Power: " + droneA.getPower());
            double percentage = (droneA.getCoin() / total) * 100.0;
            System.out.println("Coins collected = " + percentage + "%");
            text = droneA.getOutput();
            path += "stateless-" + date;

        } else if (type.equals("stateful")) {
            Stateful droneB = new Stateful(start, allCoins, allPower, allPos,
                    seed);

            droneB.indexOfStations();
        //    System.out.println("Total amount of collectable coins = "
          //          + droneB.totalCoins());
            double total = droneB.totalCoins();
            droneB.fillUpGraph();
            droneB.allRoute();
            droneB.execute();

            geoJson = Stations.txtFile(droneB.getHistory(), features, date);
           // System.out.println("Final Coins: " + droneB.getCoin()
             //       + " \nFinal Power: " + droneB.getPower());
            double percentage = (droneB.getCoin() / total) * 100.0;
            System.out.println("Coins collected = " + percentage + "%");
            text = droneB.getOutput();
            path += "stateful-" + date;
        } else {
            throw new IllegalArgumentException("Drone type not valid");
        }
        
        //outputs the two files to the folder of the following path.
        String filePath = "c:/Users/kayze/Desktop/3rdyear/ILP/evaluator/"
                + path;
        PrintWriter geoWriter = new PrintWriter(filePath + ".geojson");
        geoWriter.println(geoJson);
        geoWriter.close();
        PrintWriter textWriter = new PrintWriter(filePath + ".txt");
        textWriter.println(text);
        textWriter.close();

        System.out.println(geoJson);
        
        //just to check teh timers

        long end = System.currentTimeMillis();
        NumberFormat formatter = new DecimalFormat("#0.00000");

        System.out.print("\nExecution time is "
                + formatter.format((end - startTime) / 1000d) + " seconds");
      
    }
}
