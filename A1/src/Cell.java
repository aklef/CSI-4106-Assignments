
public class Cell {
	private Boolean visited;
	private Boolean closed;
	private Boolean dirty;
	private Boolean obstructed;
	
	public Cell() {
		visited = false;
		closed = false;
		dirty = false;
		obstructed = false;
	}
	
	public Cell(Boolean isVisited, Boolean isClosed, Boolean isDirty, Boolean isObstructed) {
		visited = isVisited;
		closed = isClosed;
		dirty = isDirty;
		obstructed = isObstructed;
	}
	
	public Boolean getVisited() {
		return visited;
	}
	public void setVisited(Boolean visited) {
		this.visited = visited;
	}
	
	
	public Boolean getClosed() {
		return closed;
	}
	public void setClosed(Boolean closed) {
		this.closed = closed;
	}
	
	
	public Boolean getDirty() {
		return dirty;
	}
	public void setDirty(Boolean dirty) {
		this.dirty = dirty;
	}
	
	
	public Boolean getObstructed() {
		return obstructed;
	}
	public void setObstructed(Boolean obstructed) {
		this.obstructed = obstructed;
	}
	
}
