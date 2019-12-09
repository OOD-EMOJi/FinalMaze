# Vision: Hiding and Searching 🙀🌟👀🙃👻

## Install and run

### If you don't already have a copy of the maze game

1. Clone this repository.
1. Open your terminal at the folder containing this repository and run
```` 
$ javac LepinskiEngine/GameEngine.java
$ java LepinskiEngine.GameEngine
````

### If you already have a maze set up

1. Download the `EMOJI` package and place in a folder alongside your `LepinskiEngine` directory
1. Add the line `import EMOJI.*;` to the beginning of `LepinskiEngine/GameEngine.java`
1. Modify the constructor of `LepinskiEngine/GameEngine.java`:
    1. To use our hiding team: modify line 44 and set `other_team = new EMOJiHiding();`
    1. To use our searching team: modify line 43 and set `the_team = new EMOJISearchingTeam1();`
1. Open your terminal at the folder containing this repository and run
```` 
$ javac LepinskiEngine/GameEngine.java
$ java LepinskiEngine.GameEngine
````

## Strategy 

### Hiding obstacles and coins

Our Hiding Team `EMOJiHiding.java` hides coins based on three basic principles (what we collectively like to call the *Don't Put All Your Eggs in One Basket! Laws*):

1. No square should hold more than one coin.
1. It should take as many turns as possible to get both diamond coins.
1. Most coins should not be accessible without hitting at least one obstacle.

### Searching for coins

Our Searching Team `EMOJISearchingTeam1.java` consists of these robots:

1. One VisionBot **"Goggles"** does what a VisionBot does.
2. One CoinBot **"Wanderer"** doesn't actively hunt for coins. She travels the maze aimlessly (though tries to avoid obstacles), picking up coins as she sees them.
3. One GhostBot **"Spooks"** hunts for diamond coins. If he has turns to spare after collecting all the diamond coins, he spends the rest of his time exploring.
4. Two FastBots **"Sonic" and "Tails"** divide and conquer to collect all the gold coins they can before time runs out.
