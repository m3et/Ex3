# The Maze Of Waze
- Ex3/Ex4 OOP

<img src="https://github.com/m3et/Ex3/blob/master/screen%20shot/Screen%20Shot%202020-01-19%20at%2019.46.15.png">

### Introduction:

In This Project:
- develop the game "The Maze Of Waze"
which is based on assignment data and algorithmic data that underlies Assignment 2.
- Create *KML* Files - which record the game and can be viewed on Google Earth.

This project is based on building a real-time game of robots (objects) on a weighted graph and collecting fruits (objects) of numerical value ("coins") placed on the graph sides.\
**Game time:** 30-60 seconds.\
**Purpose of the game:** Obtain a maximum number of fruits that the robots collected together.

### How to play the game:

1. Choose level to play (0-23 options)
<img src="https://github.com/m3et/Ex3/blob/master/screen%20shot/Screen%20Shot%202020-01-19%20at%2019.59.16.png">
2. Choose mode to play
<img src="https://github.com/m3et/Ex3/blob/master/screen%20shot/Screen%20Shot%202020-01-19%20at%2019.59.41.png">

Two options:
- **Manually** -
The robot can be moved by pressing a button only on the neighbor nodes.

- **Automatic** -
The robot will move by using an algorithm.

### Fruit class
- Represents the fruits to be collected.

### Robot class
- Represents the players - robots in the game.

### Graph_Algo Class
- Represents the graph algorithms.

### DGraph class
- Represents the data structure of the graph.

### AutoDrive class
- Represents the game algorithm which is used on the Automated game.

### ManualDrive class
-Represents all the methods be used in the manual game

### PlayGround class
- Represents a game scenario that is given.
- Imported jar file -Game_Server.

### MyGameGUI class
- Represents a Graphical וnterface of the game.

### KML_Logger class
- Creates a KML file for each game.

### DB class
- uses the Data-base
