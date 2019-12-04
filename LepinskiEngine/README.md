# OODFinal2

This is an extension of the previous VisionProject (see https://github.com/mlepinski/VisionProject2)
This repository is a work in progress. There may be a few bugs. Additionally, not all ModelTypes are implemented.
The smaller repository (github.com/mlepinski/OODFinal) has only the files related to the player team interfaces.
This repository includes the entire game engine for testing purposes.

**********

The new version of this project involves both a hiding team and a searching team.
(The hiding team places the coins into the maze and the hunting team tries to find them)

**Note: This game has significant complexity, but you can ignore a lot of this complexity if you choose**
In particular, you don't have to use any of the new robots and if you ignore the obstacles, your code should still work. 
(Of course, making smart decisions related to obstacles will probably help you get more coins!)

**********

This project adds three new complications:
1) Obstacles (aka Traps) set up by the hiding team
2) A new super-valuable type of coin
3) More types of robots

The game now works in the following way:
A) The GameEngine generates a maze and hands it to the hiding team
B) The hiding team is asked to place COINS into the maze
C) The hiding team is asked to place OBSTACLES into the maze
D) The GameEngine generates a random starting location on the edge of the Maze
   The searching team will always start in the middle of one of the four outer edges
E) The searching team selects which robots they want on their team
F) The searching team searches the maze, just like in the prior Vision Project. 
G) The teams switch roles and we start back at Step A

**Your job is to implement the PlayerTeam interface. 
Please see PlayerTeam.java for a description of the functions that you need to implement.**

There are two types of Coins:
1) Gold Coins are worth 1 point. There are many of these in the Maze
2) Diamond Coins are worth 10 points, but there are only two in the entire Maze

There are three types of Obstacles:
1) STONE turns a square into solid stone and blocks all movement
   When a STONE obstacle is place, that square is disconnected from the rest of the Maze
   Placing a STONE obstacle causes the game engine to change the available directions that can be moved from neighboring locations
   That is, the Location above (North of) the STONE obstacle is changed so that you can no longer move South.
2) DARK prevents robots from seeing out of this space
   A robot standing in a DARK location can only see the DARK location and nothing else
   A robot standing near a DARK Location may see the DARK location, but can't see past the DARK location
   That is, DAKR makes it hard to see things on the other side of the DARK location
3) SLOW causes robots entering the location to skip their next turn
   Robots can enter the SLOW location without problem, but they are then not able to execute a command on the next turn
   This obstacle simulates sand, vegitation, or other obastacles that make it difficult to pass through the location
   
There are types of Robots: (**Note:** Not all ModelTypes are currently implemented)
1) CoinBot can pick up any type of Coin and can see one square in every direction (walls do not block vision)
2) ScoutBot cannot pick up any Coin, but ScoutBot can see all squares within a distance of three from the ScoutBot
   (walls do not block the ScoutBot vision)
3) GhostBot can pick up only Diamond Coins (not Gold). GhostBot can see one square in every direction (walls do not block vision)
   GhostBot can move through walls and STONE obstacles. GhostBot is not limited by the available directions at a location
4) EscortBot cannot pick up coins. EscortBot can see two squares in every direction (walls do not block vision)
   SLOW obstacles in the same square as EscortBot or in adjacent squares have no affect on any robot.
5) VisionBot cannot pick up coins. VisionBot cannot move. 
   VisionBot can see every Location in the Maze that contains a Coin (walls and distance do not matter)
6) FastBot can only pick up Gold Coins. FastBot can see one square away, but cannot see through walls.
   FastBot can execute two commands every turn. (Only FastBot can use CommandFastMove)
7) BasicBot can pick up Gold Coins. BasicBot can see three squares, but cannot see through walls. 
   BasicBot cannot see through walls, but can see around corners.
 
