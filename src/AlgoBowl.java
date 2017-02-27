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
		/*	
		In the limit as tasks become smaller and more divisible relative to the total workload, each machine should perform an amount of work proportional to its fraction of the total work-power.
		Use this as a limit to fill the machines.
		
		Sum the work cost over all tasks (TotalWork). This is the unit of work to be performed.
		
		In the smooth-task limit, each machine should perform ( TotalWork * (Machine.speed/ sum(Machine.speed)) ) work.
		TargetMachineCapacity = ( TotalWork * (Machine.speed/ sum(Machine.speed)) )
		MachineCapacity = TargetMachineCapacity - sum( machine.assignedTasks)*Machine.speed 
		
		While numberRemainingTasks > 0
		
			Case 1: max(tasks) larger than max( MachineCapacity ):
				// Heuristic: Assign the task to the machine with the largest MachineCapacity to minimize over fill
				Assign task with max(Task.runtime) to machine with max(MachineCapacity)
			
			Case 2: no tasks larger than max( MachineCapacity ):
				// Heuristic: Large tasks will be harder to place. Attempt to place them first
				// Heuristic: Attempt to get as close as possible to the balanced load case. eg. all machines are at exactly TargetMachineCapacity
				Assign task with max(Task.runtime) to the machine with min( Task.runtime - MachineCapacity )
		
		End While
	*/
	
	int totalWork = 0;
	int sumMachineSpeeds = 0;
	double[] targetMachineCapacity = new double[machines.size()];
	double[] machineCapacity = new double[machines.size()];
	
	//Find totalWork
	for(int i=0; i<tasks.size(); i++) {
		totalWork = totalWork + tasks.get(i).runtime;			
	}
	
	//Sum Machine Speeds
	for(int i=0; i<machines.size(); i++) {
		sumMachineSpeeds = sumMachineSpeeds + machines.get(i).speed;
	}
	
	//Find the target capacity for each machine
	for(int i=0; i<machines.size(); i++) {
		targetMachineCapacity[i] = totalWork * ((double) machines.get(i).speed / sumMachineSpeeds );
		//Set the initial capacity to the target capacity
		machineCapacity[i] = targetMachineCapacity[i];
	}
	
	for(int i=sortedTasks.size()-1; i>=0; i--) {
		// max machineCapacity
		double maxMC = 0;
		int maxMIndex = 0;
		for(int j=0; j<machineCapacity.length; j++) {
			if(machineCapacity[j] > maxMC) {
				maxMIndex = j;
				maxMC = machineCapacity[j];
			}
		}
		
		// case 1
		if( sortedTasks.get(i).runtime > maxMC) {
			Machine temp = machines.get(maxMIndex);
			temp.assignedTasks.add(sortedTasks.get(i));
			machines.set(maxMIndex, temp);
			//update machineCapacity
			machineCapacity[maxMIndex] = machineCapacity[maxMIndex] - sortedTasks.get(i).runtime;
			
			
		// case 2
		} else { 
			//min of difference between machineCapacity and largest task duration
			double taskCapacityDiff = Double.MAX_VALUE;
			int diffIndex=0;
			double tempDiff;
			for(int j=0; j<machineCapacity.length;j++){
				tempDiff = machineCapacity[j] - tasks.get(i).runtime;
				if( ( tempDiff < taskCapacityDiff) && ( tempDiff > 0 )) {
					taskCapacityDiff = machineCapacity[j] - tasks.get(i).runtime;
					diffIndex = j;
				}
			}
			// assign to machine
			Machine temp = machines.get(diffIndex);
			temp.assignedTasks.add(sortedTasks.get(i));
			machines.set(diffIndex, temp);
			//update machineCapacity
			machineCapacity[diffIndex] = machineCapacity[diffIndex] - sortedTasks.get(i).runtime;
		}
	}
		
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
					sortedMachines.get(j).assignedTasks.add(sortedTasks.get(i));
					break;
				}	
			}
		
			//If we didn't find a machine with free room, assign the task to the next best one (overload it)
			if (!taskAssigned) sortedMachines.get(nextBest).assignedTasks.add(sortedTasks.get(i));
		}
		//Complexity: O(n*k) (worst case we have to examine each processor for each task)
		

	}
	
	public static void writeOutput(String fileName) throws IOException{
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		try
		{
			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);
			
			//First line: maximum total runtime
			bw.write(String.format("%.2f", maxTotalRuntime));	
			bw.newLine();
			
			//Following lines: Assigned task IDs for each machine (Task ID starts with 1)
			for(int i = 0; i < machines.size(); i++){
				if(machines.get(i).assignedTasks.isEmpty() == false){
					ArrayList<Task> temp = machines.get(i).assignedTasks;
					for(int j = 0; j < temp.size(); j++){
						bw.write((temp.get(j).index + 1) + " ");
					}
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
		double maxRuntime = 0;
		double currentRuntime = 0;
		
		//Task ID starts with 1
		Scanner s = null;
		String temp;
		String[] theLine;
		try
		{
			s = new Scanner(new FileReader(outputFile));
			result = Double.parseDouble(s.nextLine());		//read maximum runtime in result
			
			//Read the first machine
			machineID = 0;
			temp = s.nextLine();
			theLine = temp.split(" ");	//The line of task runtimes
			if(temp.isEmpty() == false){
				for(int i = 0; i < theLine.length; i++){
					taskID = Integer.parseInt(theLine[i])-1;
					currentRuntime = currentRuntime + tasks.get(taskID).runtime / machines.get(machineID).speed;
				}
				maxRuntime = currentRuntime;
			}			
			
			//Read the next machine if it exists
			while(s.hasNextLine()){
				machineID++;
				temp = s.nextLine();
				theLine = temp.split(" ");
				currentRuntime = 0;
				
				if(temp.isEmpty() == false){
					for(int i = 0; i < theLine.length; i++){
						taskID = Integer.parseInt(theLine[i])-1;
						currentRuntime = currentRuntime + tasks.get(taskID).runtime / machines.get(machineID).speed;
					}
					
					if(maxRuntime < currentRuntime){
						maxRuntime = currentRuntime;
					}
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
		return false;		
	}
	
	public static void main(String[] args) throws IOException {
		int option = 2;		//0: Make new input. 1: Chamal's algorithm. 2: Jared's algorithm. Else: Verifier.
		
		if(option == 0){
			RNG.createInput(INPUTFILE1);
		}
		else if(option == 1){
			readInput(INPUTFILE1);
			sortMachines();
			sortTasks();
			
			assignTasks();	
			
			findMaxTotalRuntime();
			writeOutput(OUTPUTFILE1);
			
			System.out.println(verifier(INPUTFILE1, OUTPUTFILE1));
		}
		else if(option == 2){
			readInput(INPUTFILE1);
			sortMachines();
			sortTasks();
			
			avgLoadBalanceHeuristic();
			
			findMaxTotalRuntime();
			writeOutput(OUTPUTFILE1);
			
			System.out.println(verifier(INPUTFILE1, OUTPUTFILE1));
		}
		else{
			//Run verifier
			System.out.println(verifier(INPUTFILE1, OUTPUTFILE1));
		}		
	}
}
