public class Grid {
	
	//The grid is accessed in [row][column] style
	private Cell cells[][]; //1st dimension represents the width, 2nd dimension represents the height
	private Integer height;
	private Integer width;
	


	
	public Grid(Integer width, Integer height) {
		
		if(width < 1 || height < 1)
			throw new RuntimeException("Cannot create a grid with a dimension less than 1");
		
		this.height = height;
		this.width = width;
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				cells[i][j] = new Cell();
			}
		}
	}
	
	public Cell getCell(Integer row, Integer column) {
		if(row < 0 || row >= width || column < 0 || column >= height){
			throw new RuntimeException("Cannot return a cell from out of bounds."); 
		}
		
		return cells[row][column];
	}
	

}
