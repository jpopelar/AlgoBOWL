import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AlgoBowl {
	private static final String INPUTFILE1 = "Inputs\\input.txt";
	
	public static ArrayList<Machine> machines, sortedMachines;
	public static ArrayList<Task> tasks, sortedTasks;
	public static int numTasks;
	public static int numMachines;
	
	public static void readInput(String fileName) throws IOException
	{
		machines = new ArrayList<Machine>();
		sortedMachines = new ArrayList<Machine>();
		tasks = new ArrayList<Task>();
		sortedTasks = new ArrayList<Task>();
		numTasks = 0;
		numMachines = 0;
		
		Task theTask;
		Machine theMachine;
		
		Scanner s = null;
		String[] theLine;
		try
		{
			s = new Scanner(new FileReader(fileName));
			numTasks = Integer.parseInt(s.nextLine());		//read num of tasks
			numMachines = Integer.parseInt(s.nextLine());	//read num of machines
			
			theLine = s.nextLine().split(" ");	//The line of task runtimes
			for(int i = 0; i < numTasks; i++){
				theTask = new Task(i, Integer.parseInt(theLine[i]));
				tasks.add(theTask);
				sortedTasks.add(theTask);
			}
			
			theLine = s.nextLine().split(" ");	//The line of machine speeds
			for(int i = 0; i < numMachines; i++){
				theMachine = new Machine(i, Integer.parseInt(theLine[i]));
				machines.add(theMachine);
				sortedMachines.add(theMachine);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}			
	}

	public static void sortMachines(){
		//Bubble sort
		for(int i = 0; i < sortedMachines.size(); i++){
			for(int j = 1; j < sortedMachines.size(); j++){
				if(sortedMachines.get(j).speed < sortedMachines.get(j-1).speed){
					Machine temp = sortedMachines.get(j-1);
					sortedMachines.set(j-1, sortedMachines.get(j));
					sortedMachines.set(j, temp);
				}
			}
		}
	}
	
	public static void sortTasks(){
		//Bubble sort
		for(int i = 0; i < sortedTasks.size(); i++){
			for(int j = 1; j < sortedTasks.size(); j++){
				if(sortedTasks.get(j).runtime < sortedTasks.get(j-1).runtime){
					Task temp = sortedTasks.get(j-1);
					sortedTasks.set(j-1, sortedTasks.get(j));
					sortedTasks.set(j, temp);
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		readInput(INPUTFILE1);
		sortMachines();
		sortTasks();

		
		
		
	}
}
