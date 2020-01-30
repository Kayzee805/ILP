 \1. Software architecture description. This section provides a description of the software architecture of your application. Your application is made up of a collection of Java classes; explain why you identified these classes as being the right ones for your application. Identify class hierarchical relationships between classes: which classes are subclasses of others 

basically decisions that i made early on that would be too costly to change at the later stages of the design.

so stuff like, me deciding on the way to refer to the properties of the stations

·    Explain why I choose to store the stations in the Stations class

·    Drone class and why, something like stateless and stateful shares the same data but the way they move and how they use the data is different

·    Say stateless and stateful are subclasses of drone

Stations Class

I choose the Station class to be the class where the maps would be downloaded from the server. This was done as, all the data fetched from the server will be related to the power stations.

Drone class

The Stateful and the Stateless classes are the subclasses of the Drone class. This was done as, the Stateful and the Stateless class shares the same characteristics and data as the Drone class. The only difference is on how they make use of the data to move around the map. 



<u>Stations</u>

Before I even started coding, the my initial thoughts on the architecture of the application was to have a class such that the object of the class would contain all the details of a station, such as the Coordinates, coins and power. However, as this would require us to create 50 objects of that class and it'd mean that I would need to call that object anytime I want to access any of it's variables. As this would have made looping through a variable of that class a bit awkward, I decided not to do this and have a class with 3 array type variables that would store the variables that each station can have, so coordinates, which is used to create a new object of class Position, coins and power. 

This decision made iterating through the coins or the power of the station or calling a variable of the station very easy to understand and use, as an index in one array type of variable would represent the  variables of the same station in other arrays as well. The class which carries out these tasks in called "Stations" and it makes calling or iterating through the variables of the stations easy. 

<u>Drone</u>

As both the stateful and stateless type drones share the same basic characteristics, movement styles (Charging to closest station after moving) and only differ in the way their ability to use the geoJson map to map their movements. I decided to create a Drone class to be the Parent class of the stateful and stateless class. This would mean that the Stateful and Stateless class are the subclasses of the Drone class therefore, will inherit all the properties of the Drone class. Doing this meant that I did not have to create the same method twice, one for each class and they could just share the method they inherit from the Drone class.

 