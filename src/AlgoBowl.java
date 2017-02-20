import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AlgoBowl {
	private static final String INPUTFILE1 = "Inputs\\input.txt";
	private static final String OUTPUTFILE1 = "Outputs\\output.txt";
	
	//Machines and Tasks store in original order, sortedMachines and sortedTasks store sorted order.
	public static ArrayList<Machine> machines, sortedMachines;
	public static ArrayList<Task> tasks, sortedTasks;
	
	public static int numTasks, numMachines;
	public static double maxTotalRuntime;
	
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
	
	public static void assignTasks(){
		//Implement the algorithm here
	}
	
	public static void writeOutput(String fileName) throws IOException{
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		try
		{
			fw = new FileWriter(fileName,true);
			bw = new BufferedWriter(fw);
			
			//First line: maximum total runtime
			bw.write(String.format("%.2f", maxTotalRuntime));	
			bw.newLine();
			
			//Following lines: Assigned task IDs for each machine
			for(int i = 0; i < machines.size(); i++){
				ArrayList<Task> temp = machines.get(i).assignedTasks;
				for(int j = 0; j < temp.size(); j++){
					bw.write(temp.get(j).index + " ");
				}
				bw.newLine();
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		} 
		finally 
		{
			try{
				if(bw != null)
					bw.close();
				if(fw != null)
					fw.close();
			}
			catch (IOException ex) 
			{
				ex.printStackTrace();
			}
		}	
	}
	
	public static void findMaxTotalRuntime(){
		maxTotalRuntime = sortedMachines.get(0).getTotalRuntime();
		for(int i = 1; i < sortedMachines.size(); i++){
			if(sortedMachines.get(i).getTotalRuntime() > maxTotalRuntime){
				maxTotalRuntime = sortedMachines.get(i).getTotalRuntime();
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		readInput(INPUTFILE1);
		sortMachines();
		sortTasks();
		
		assignTasks();		
		findMaxTotalRuntime();
		
		
		
	}
}
