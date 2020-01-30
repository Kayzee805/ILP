Class

**<u>Stations Class:</u>** Connects to server and downloads the Geojson map and converts it into type String. Then retrieves the features of that geoJson map and stores the different properties and coordinates of the features in three different List. An index in one list will correspond to  an index in the others, all relating to the same feature.

<u>Instance Methods</u>

​		Stations(String link): Initialises the instance variable link with the String variable passed to the constructor.

​		*Connect*() : Opens a communication link to the URL of type string, then through the use of a bufferedReader, reads the contents of the webpage then saves the JSON as a type String. Then extracts and returns a List of type Feature from a formatted JSON string.

​		*init*(ArrayList<Feature> features) : Loops through a List of Feature then initialises 3 instance variables with the Properties (coins and power) and the Coordinates of each Feature, which has been used to create a Position object. An index in all the 3 variables will refer to the variables/properties of the same feature.

​		*txtFile*(ArrayList<Position> History, Arraylist<Feature> features, String date) : Creates a valid formatted JSON String, that contains a feature of type LineString with all the coordinates from a specific List and also, appends to it all the Features from a list of Feature. Then this formatted string is returned.

​		*getCoins()*:  returns the instance variable allCoins of the class

​		*getPower()*: returns the instance variable allPower of the class

​		*getPos()*: returns the instance variable allPos of the class

<u>Instance Variables</u>

​		*Position [] allPos*: stores a list of type Position, which represents the coordinates of each feature

​		*Arraylist<Double> allPower*: stores a list of type Double, which represnts the property power of each feature

​		*ArrayList<Double> allCoins:* stores a list of type Double, which represents the property coin of each feature



**<u>Direction Class</u>**: An enum class that defines a collection of constants of type Direction and contains a instance variable which returns an opposite direction of a given direction.

<u>Instance variable</u>

​		oppositeDirection: A HashMap with the keys and values both having the type Direction. Contains 16 keys each with a value that refers to the Direction opposite to it.

<u>**Position Class**</u>: A class where Objects type Position is initialised with methods to allow you to check if position is within game area or calculate the position after moving in a certain direction. 

<u>Instance methods</u>

​		Position(double y, double x): initialises the two instance variables of the class

​		nextPosition(Direction d): moves a position in a certain direction then returns the new Position

​		inPlayArea(): Checks if the drone is within the game area.

​		calcMove(double a): Calculates moves in two directions, x and y plane , with a radius of r. 

<u>Instance variables</u>

​		latitude: stores the latitude, y-axis, of the position

​		longitude: stores the longitude, x- axis, of the position

​		xMin, xMax: stores the minimum and maximum boundaries of the map in the x direction. the variables are final, so cannot be changed

​		yMin, yMax: stores the minimum and maximum boundaries of the map in the y direction. the variables are final, so cannot be changed

<u>**App class**</u>: Only function of this class is to run the application through the main method.

​		main(): When called, it creates objects of different classes and executes the movements of the drones then outputs two files, a txt file containing details of every move and a valid formatted geojson file. 

<u>**Drone Class**</u>: Holds the basic characteristics that all types of drones will share.

<u>Instance methods</u>

​		Drone(Position a, Arraylist<Double> b, ArrayList<Double> c, Position[] d, int e): initialises the instance variables of the drone, the random number generator and adds the initial Position of the drone to the instance variable History.

​		indexOfStations(): initialises the two instance variable of type List<Integer> with the index of the positive and negative stations in the map.

​		totalCoins(): returns the total number of positive coins that can be collected from the map

​		calcDistance(Position a, Position b): returns the distance between two positions which is calculated using the Euclidean distance formula.

​		gameOver(): If the drone can not make any more moves, it returns true else false;

​		move(Direction d): moves the drone in a certain Direction, and transfers coins from the nearest station if in range. It also keeps track of the movement of the drone and it's direction.

​		closestStation(): returns the index of an closest station in range, else it returns the maximum value an Integer can hold

​		transfer(): if a station exists in range, it connects to it and transfers all the coins and power from the station to the drone. Then gives the station a new value of coins and power, the difference from transfering.

​		moveLog(Position a, Position b): logs the movements of the drones and it's coins, power and the direction it moved in by storing it in a String variable. Then appends the variable to the instance variable output.

​		getHistory(): Returns all the positions that the drone has visited whilst moving

​		getCoin(): returns the Drone's current coins

​		getPower(): returns the Drone's current power

​		getMoves(): Returns the amount of moves the drone has made so far

​		getCurrent(): Returns the current Position of the drone

​		getOutput(): Returns the log of the drone's movements and it's coins, power and the directions it moved in.



<u>Instance Variables</u>

​		Position current: Holds the current position of the drone

​		ArrayList<Direction> allDirections: holds the 16 direction that the drone can move in, and this variable is final, so cannot be changed.

​		Random rand: the random number generator that'll produces random numbers that some drones use help them choose a direction of movement.

​		double power: holds the current power of the drone

​		double coins: holds the current coins of the drone

​		ArrayList<Position> history: stores all the positions that the drone has visited.

​		ArrayList<Double> allCoins: each index in the array corresponds to a power station and how much coins it contains.

​		ArrayList<Double> allPower: each index in the array corresponds to a power station and how much powerit contains.

​		Position [] allPos: each index in the array corresponds to a power station and the object Position which holds it's longitude and latitude values

​		int moves : an Integer value of the number of moves the drone has made.

​		int NEG: stores the number of negative stations the drone has visited

​		int POS: stores the number of positive stations the drone has visited

​		int counter: stores a number that keeps track of the stations for a  Stateful drone

​		Direction prevD: stores the previous Direction that the drone moved in, only used by some Drones

​		int repeatCounter: stores the amount of time the drone repeats a move, backwards then forwards/ vice versa

​		Direction chosenDirection: stores the direction that the drone has decided to move in

​		int seed: stores the seed for the random number generator

​		ArrayList<Integer> indexPositives: stores the indexes of all the positive power stations in the map

ArrayList<Integer> indexNegative: stores the indexes of all the negative power stations in the map



<u>**Stateless Class**</u>: executes the movement of the drone and moves to a positive station if in range, else makes it so the drone moves in a random direction whilst avoiding negative stations

<u>instance Methods</u>

​		Stateless2(Position a, ArrayList<Double> b, ArrayList<DOuble> c, Position[] d, int e): calls the constructor, methods and the properties of the parent Drone class and initialies a drone of type Stateless.

​		inBigRange(ArrayList<Integer> index): Scans a big circle of radius 0.00025+0.0003 then returns a Map containing the Position and the index of the stations within the circle.

​		inSmallRnage(HashMap<Position, Integer> a): Loops through a Map of Position and indexes of stations and checks if the station is within 0.00025 from all the directions. It then returns a Map of the direction the station is in and the stations Index.

​		positiveMovesOnly():  Loops through possible moves the drone can make and returns the directions with the indexes of the stations, which is the closest to the move and has more than 0coins, so hasn't been visited before

​		bestMove(HashMap<Direction, Integer> possibleMoves): Returns the direction of the station which is in range and has the highest coins, compared to the other stations in range.

​		bestNegMove(HashMap<Direction, Integer> possibleMoves): Returns the direction which contains a negative station which will cause the drone to lose the least amount of coins. 

​		listWithoutNegatives(ArrayList<DIrection> directions,HashMap<Direction, Integer> allNeg ): returns a List of Directions that are in range, not negative and is in the play area of the map.

​		closeNegative(Direction d, int pos, int neg): compares the two station indexes, usually one positive and one negative. It returns true if the negative station is closer and false if the positive station is closer if the drone moved in a certain direction

​		moving(): finds the best possible move for the drone from it's current position. Moves to a direction where the positive is the closest station or moves to a direction which will cause the drone to lose the least coins, if surrounded by all negative stations or moves to a random direction which does not contain any negative and is valid.

​		execute(): Executes the movement of the drones until the game is over and whilst moving, logs each move made by the drone and saves it to the parent instance variable, output.



**<u>Stateful Class</u>** : executes the movement of the drone so it tries to visit as much positive power stations as it can whilst avoiding negative stations when possible. 

<u>instance Methods</u>

​		Stateful2(...):calls the constructor, methods and the properties of the parent Drone class and initialies a drone of type Stateful.

​		nextCounter(Position a): Checks if another positive station is in range of the drone **and** closer than the station the counter variable is referring to. Returns true if it is, else false. 

​		shortestMove(): Returns the direction that moves the drone closest to the station the counter variable refers to. It also, takes into account of cases where the drone repeats moves multiple times or takes too long to connect to the next station and makes a move regardless of negatives.

​		inRangeVisited(Position a): Returns true if the drone is in range of a station that has already been visited before else, false.

​		inRangeNegative2(Position a): checks if any negative stations in range then compares the distance with the distance from the drone to the station counter variable refers to. If positive is closer and in range then returns false, else true. If the positive is not in range, then returns true if it found a negative station within range of the drone.

​		inRnageNegative(Position a): checks if any negative stations are in range of the drone, then returns true if it found any else false.

​		fillUpGraph(): Initialises the 2D double graph instance variable then fills up the array with the distances between positive power stations.

​		findRoute(): returns the index of the station closest to another station or a position, then appends the index to a list of already visited List and updates the variable startIndex to the index of the closest station.

​		allRoute(): Fills up the instance Variable route with a list of station indexes, which connects the initial position to its closest positive station then closest station to that station. Then repeats until the size of route variable is the same as the number of positive stations in the map.

​		possibleMoves(): Returns a list of directions that contains valid moves and moves that are not in range of a negative station.

​		moving(): Moves the drone by one step in a direction that brings it one step closer to the station counter is referring to and if it has visited all the positive stations, it moves in a random direction whilst avoiding negatives.

​		execute(): Executes the movement of the drones until the game is over and whilst moving, logs each move made by the drone and saves it to the parent instance variable, output.

<u>instance Variables</u>

​		int startIndex: Stores the index of a power station.

​		ArrayList<integer> visited: stores the indexes of all visited stations

​		int dummyCounter: a counter that counts the number of moves since the drone last conncted to a power station.

​		double[][] [] [] graph: once initilased, stores the matrix of n by n, where n is the size of positive stations in the map. graph[i] [j] will store the  distance between the station with the i'th and j'th index.

​		ArrayList<Integer> route: Once filled, stores the indexes of positive station that the drone will try to move in order.

​		int counter: an integer that guides the drone to a specific power station in the map.