package EMOJI;

import java.util.*; 
// TODO: updated ScoutPathOption based on the constructor

public class ScoutBotPathOptionGenerator implements PathOptionGenerator {
	public Maze maze;
	private Pathfinder pathfinder;
	private int height;
	private int width;
	public RegionAnalyzer regionAnalyzer;
	public List<Region> centersList;
	public List<PathOption> pathList;

	public ScoutBotPathOptionGenerator(Maze maze) {
		this.maze = maze;
		this.height = maze.tiles[0].length;
		this.width = maze.tiles.length;
		this.pathfinder = new BreadthFirstSearchPathFinder(maze);
		this.regionAnalyzer = new RegionAnalyzer(maze);

	}

	public List<PathOption> generatePathOptions(int x, int y, int turns){
		pathList = new ArrayList<PathOption>();
		regionAnalyzer.calculateRegions(x,y,0,0,height,width);
		centersList = regionAnalyzer.getRegions();
		List<Coin> hiddenCoins = regionAnalyzer.findAllHiddenCoins();
		for (Region region : centersList) {
			PathOption option = new ScoutPathOption(pathfinder.findPath(maze.tiles[x][y], region.center), turns, hiddenCoins, maze);
            option.countPoints();// implement count points in ScoutPathOption

            if (option.path.size() > 0) {
            	pathList.add(option);
            }else{
            	//else call NearestPathFinder() to find the nearst tile to the center
            	pathList.addAll(NearestPathFinder(region.center,maze.tiles[x][y],turns, hiddenCoins));
            }
            //System.out.println(tile.getX() + " " + tile.getY() + " " + option);
        }
        //System.out.println(pathList);
        
        Collections.sort(pathList);
        for (PathOption pathOption : pathList) {
        	System.out.println(pathOption);
        }
        return pathList;
    }




	private List<PathOption> NearestPathFinder(Tile center, Tile start, int turns, List<Coin> hiddenCoins){
	    	// try the center borders first, and then the diagonals and the second level of borders
	    	// the limit of searching is -4/+4  tiles
		List<PathOption> bordersPathList = new ArrayList<PathOption>();
		int[][] BorderSHIFT = {{0, 1}, {1, 0}, {0, -1}, {-1, 0} };
		int[][] DiagonalSHIFT = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1} };
		int x = center.getX();
		int y = center.getY();
		int minX = center.getX()-4;
		int minY = center.getY()-4;
		int maxX = center.getX()+4;
		int maxY = center.getX()+4;

		while(bordersPathList.isEmpty()){
			int iterationCounter = 1;
	        	// do border shifts
			for (int[] shift : BorderSHIFT){
				x = center.getX();
				y = center.getY();
				x= x+ (shift[0]* iterationCounter);
				y= y+ (shift[1]* iterationCounter);			        						        		
				if (x >= minX && x < maxX && y >= minY && y < maxY){
					PathOption option2 = new ScoutPathOption(pathfinder.findPath(start, maze.tiles[x][y]), turns, hiddenCoins, maze);
					option2.countPoints();
					if (option2.path.size() > 0){
						bordersPathList.add(option2);
					}else{
			        			// do diagonal shifts
						for (int[] shift2 : DiagonalSHIFT){
							x = center.getX();
							y = center.getY();
							x= x+ (shift2[0]* iterationCounter);
							y= y+ (shift2[1]* iterationCounter);			        					
							if (x >= minX && x < maxX && y >= minY && y < maxY){
								option2 = new ScoutPathOption(pathfinder.findPath(start, maze.tiles[x][y]), turns, hiddenCoins, maze);
								option2.countPoints();
								if (option2.path.size() > 0)bordersPathList.add(option2);

							}

						}
					}
					iterationCounter++;
				}

				return bordersPathList;


			}
		}
	}
}


