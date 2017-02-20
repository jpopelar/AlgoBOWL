import java.util.ArrayList;

public class Machine {
	public int index, speed;
	public double totalRuntime;
	public ArrayList<Task> assignedTasks;
	
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
	
	public void computeTotalRuntime(){
		for(int i = 0; i < assignedTasks.size(); i++){
			totalRuntime = totalRuntime + assignedTasks.get(i).runtime / speed;
		}
	}
	
	public double getTotalRuntime(){
		computeTotalRuntime();
		return totalRuntime;
	}
}
