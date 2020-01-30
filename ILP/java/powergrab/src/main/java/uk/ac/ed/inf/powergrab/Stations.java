package uk.ac.ed.inf.powergrab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;

public class Stations {
    private Position[] allPos;
    private ArrayList<Double> allPower;
    private ArrayList<Double> allCoins;
    private String link;            

    //class constructor
    public Stations(String link) {
        this.link = link;
    }

    /*
     * Connects to the server and downloads a geojson map, then converts it into
     * string then returns the features of the geoJson in
     */
    public ArrayList<Feature> connect() throws IOException {

        URL mapUrl = new URL(link);
        URLConnection connection = mapUrl.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setDoInput(true);

        connection.connect();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String mapSource = "";
        String a;
        while ((a = in.readLine()) != null) {
            mapSource += a + "\n";
        }
        in.close();

        ArrayList<Feature> features = (ArrayList<Feature>) FeatureCollection
                .fromJson(mapSource).features();
        // System.out.println(mapSource);
        return features;

    }

    /*
     * Initialises the 3 class variables with the properties and coordinates of the feature
     */
    public void init(ArrayList<Feature> features) {
        Position[] coords = new Position[features.size()];
        ArrayList<Double> power = new ArrayList<Double>();
        ArrayList<Double> coins = new ArrayList<Double>();

        for (int i = 0; i < features.size(); i++) {
            //type casts the geometry type to a Point type
            Point temp = (Point) features.get(i).geometry();  
            Position dummy = new Position(temp.latitude(), temp.longitude());
            coords[i] = dummy;
            power.add(features.get(i).getProperty("power").getAsDouble());
            coins.add(features.get(i).getProperty("coins").getAsDouble());

        }
        this.allPower = power;
        this.allCoins = coins;
        this.allPos = coords;

    }

  
    /*
     * Returns a valid formatted geoJson of type string
     * containing one lineString type feature with all the coordinates of the drone
     * and appends the already existing map features
     */
    public static String txtFile(ArrayList<Position> History,
            ArrayList<Feature> features, String date) {

        String str = "";
        str += "{\n" + "  \"type\": \"FeatureCollection\",\n"
                + "  \"date-generated\": \"" + date 
                + "\",\n" 
                + "  \"features\": [\n" 
                +"{\n" + "\"type\": \"Feature\",\n" 
                + "\"geometry\": {\n"
                + "\"type\": \"LineString\",\n" 
                + "\"coordinates\":  ";
        String temp = "[";
        
        for (int i = 0; i < History.size() - 1; i++) {
            
            //adds the coordinates of each point visited by the drone
            temp += "[" + History.get(i).longitude + ", "
                    + History.get(i).latitude + "] , ";
        }
        
        int tempSize = History.size() - 1;
        
        temp += "[" + History.get(tempSize).longitude + ", "    
                + History.get(tempSize).latitude + "]]  ";

        str += temp;
        str += " },\n" + "\"properties\": {\n"
                + "\"prop0\": \"value0\",\n"
                + "\"prop1\": 0.0\n" +
                "}\n" + 
                 "},\n";
        
        //adds the already exisiting features to the output string
        for (int x = 0; x < features.size() - 1; x++) {

            str += features.get(x).toJson() + ",";
        }
        //adds the least feature, to match the format of a GeoJSON file
        str += features.get(features.size() - 1).toJson() + "]}";

        return str;
    }

    
    //getters for the class variables
    public ArrayList<Double> getCoins() {
        return allCoins;
    }

    public ArrayList<Double> getPower() {
        return allPower;
    }

    public Position[] getPos() {
        return allPos;
    }

}
