import java.util.List;

public class RobotApp {
	
	
	
	public static Grid grid;
	
	public static void main(String[] args) {
		

	}
	
	public static Grid generateGrid(int gridSize, List<Position> obstacles, List<Position> dirt, Position robotPosition, Robot.Direction orientation) {
		Grid newGrid = new Grid(gridSize, gridSize);
		
		for(Position t : obstacles){
			if(t.x < 0 || t.x >= gridSize || t.y < 0 || t.y >= gridSize){
				throw new RuntimeException("One of your obstacles is out of bounds");
			}
			
			Cell cellRef = newGrid.getCell(t.x, t.y);
			cellRef.setObstructed(true);
			
		}
		
		for(Position t : dirt){
			if(t.x < 0 || t.x >= gridSize || t.y < 0 || t.y >= gridSize){
				throw new RuntimeException("One of your dirt piles is out of bounds");
			}
			
			Cell cellRef = newGrid.getCell(t.x, t.y);
			cellRef.setDirty(true);
		}
		
		return newGrid;
	}
	
	
}
