import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RNG {
	//No more than 1000 tasks, 1 <= n <= 1000
	//No task longer than 10000, 1 <= t <= 10000
	//No more than 50 machines, 1 <= m <= 50
	//No machine faster than 20, 1 <= s <= 20
	
	public static void createInput(String fileName)
	{
		Random rand = new Random();
		
		int n = rand.nextInt(1000) + 1;
		int m = rand.nextInt(50) + 1;
		
		//To keep it simple for testing, use smaller sizes temporarily
		//int n = rand.nextInt(10) + 1;
		//int m = rand.nextInt(5) + 1;
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		try
		{
			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);
			
			//First line: Number of tasks
			bw.write(String.valueOf(n));
			bw.newLine();
			
			//Second line: Number of machines
			bw.write(String.valueOf(m));
			bw.newLine();
			
			//Third line: task runtimes
			for(int i = 0; i < n; i++){
				bw.write(String.valueOf(rand.nextInt(10000)+1) + " ");
			}
			bw.newLine();
			
			//Fourth line: machine speeds
			for(int i = 0; i < m; i++){
				bw.write(String.valueOf(rand.nextInt(20)+1) + " ");
			}
			bw.newLine();
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
}
