import java.util.ArrayList;

public class Machine {
	public int index, speed;
	public double totalRuntime;
	private ArrayList<Task> assignedTasks;
	
	public Machine(int index, int speed){
		this.index = index;
		this.speed = speed;
		totalRuntime = 0;
		assignedTasks = new ArrayList<Task>();
	}
	
	public Machine(){
		index = 0;
		speed = 0;
		totalRuntime = 0;
		assignedTasks = new ArrayList<Task>();
	}
	
	private void computeTotalRuntime(){
		totalRuntime = 0; //Reset to avoid bad calculations
		for (Task t: assignedTasks) totalRuntime += t.runtime / speed;
	}
	
	public void assign(Task t) {
		assignedTasks.add(t);
	}
	
	public double getTotalRuntime(){
		computeTotalRuntime();
		return totalRuntime;
	}

	public ArrayList<Task> getTasks() {
		return assignedTasks;
	}
}
