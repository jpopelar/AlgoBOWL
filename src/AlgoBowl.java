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
	
	public static void avgLoadBalanceHeuristic() throws IOException{
		//Goal: each processor takes a share of the work based on its speed
		
		//First calculate the total time units needed for all tasks
		double totTime = 0;
		for (Task t: tasks) totTime += t.runtime;
		
		//Then calculate the total speed of all processors
		int totSpeed = 0;
		for (Machine m: machines) totSpeed += m.speed;
		
		//Now, assume we have totSpeed machines all of speed 1
		//How much work would each machine need to do on average in a perfect world?
		double avgWork = totTime / totSpeed;
		
		//The heuristic then goes something like this:
		//For each task, starting with the longest:
		for (int i = sortedTasks.size()-1; i >= 0; i--) {
			boolean taskAssigned = false; //Switches on when task is assigned in inner loop
			int nextBest = sortedMachines.size()-1; //Default to fastest processor
			

			//  Find the fastest processor whose load is below avgWork
			for (int j = sortedMachines.size()-1; j >= 0; j--) {
				//  If that assignment would put its load above avgWork, bookmark that processor and examine the next one
				if ((sortedMachines.get(j).getTotalRuntime() + (sortedTasks.get(i).runtime / sortedMachines.get(j).speed)) >= avgWork
						&& sortedMachines.get(j).getTotalRuntime() < avgWork //Second clause avoids overloading already overloaded machines
						&& nextBest < j) //Third clause ensures we overload the faster processors first
					nextBest = j;
				//  If we can assign the task to a processor and keep its load below avgWork, then make the assignment
				else if (sortedMachines.get(j).getTotalRuntime() + (sortedTasks.get(i).runtime / sortedMachines.get(j).speed) < avgWork) {
					sortedMachines.get(j).assign(sortedTasks.get(i));
					break;
				}	
			}
		
			//If we didn't find a machine with free room, assign the task to the next best one (overload it)
			if (!taskAssigned) sortedMachines.get(nextBest).assign(sortedTasks.get(i));
		}
		//Complexity: O(n*k) (worst case we have to examine each processor for each task)
		
		//End by printing the output file!
		writeOutput("avgLoadBalance.txt");
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
				ArrayList<Task> temp = machines.get(i).getTasks();
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
	
	public static boolean verifier(String inputFile, String outputFile) throws IOException{
		readInput(inputFile);
		double result = 0;
		int machineID, taskID;
		double maxRuntime;
		double currentRuntime = 0;
		
		//Task ID starts with 1
		Scanner s = null;
		String[] theLine;
		try
		{
			s = new Scanner(new FileReader(outputFile));
			result = Double.parseDouble(s.nextLine());		//read maximum runtime in result
			
			//Read the first machine
			machineID = 0;
			theLine = s.nextLine().split(" ");	//The line of task runtimes
			for(int i = 0; i < theLine.length; i++){
				taskID = Integer.parseInt(theLine[i])-1;
				currentRuntime = currentRuntime + tasks.get(taskID).runtime / machines.get(machineID).speed;
			}
			maxRuntime = currentRuntime;
			
			//Read the next machine if it exists
			while(s.hasNextLine()){
				machineID++;
				theLine = s.nextLine().split(" ");
				currentRuntime = 0;
				
				for(int i = 0; i < theLine.length; i++){
					taskID = Integer.parseInt(theLine[i])-1;
					currentRuntime = currentRuntime + tasks.get(taskID).runtime / machines.get(machineID).speed;
				}
				
				if(maxRuntime < currentRuntime){
					maxRuntime = currentRuntime;
				}
			}
			
			//Return results, using a 1% tolerance
			if(Math.abs(maxRuntime - result) / maxRuntime <= 0.01){
				return true;
			}
			else{
				return false;
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally {s.close();}
		
		return false;		
	}
	
	public static void main(String[] args) throws IOException {
		int option = 0;		//If 0, make new input. If 1, run algorithm with existing input. If 2 or else, run verifier.
		
		if(option == 2){
			//Make new input
			RNG.createInput(INPUTFILE1);
		}
		else if(option == 1){
			//Run algorithm with existing input
			readInput(INPUTFILE1);
			sortMachines();
			sortTasks();
			
			assignTasks();		
			findMaxTotalRuntime();
		}
		else{
			//Run verifier
			System.out.println(verifier(INPUTFILE1, OUTPUTFILE1));
		}		
		
	}
}
