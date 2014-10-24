import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.lang.Exception;
public class RunScheduler {

	static ArrayList<CPU> cpus;
	static ArrayList<Process> processes;
	
	public static void main (String args[]){
		//runs the scheduler for all of the types of schedulers
		//check args:
		
		if(args.length > 0)
		try{	
				numCPUs = Integer.parseInt(args[0]);
				numOfProcs = Integer.parseInt(args[1]);
				ctxSwitchTime = Integer.parseInt(args[2]);
				numCPUBoundBursts = Integer.parseInt(args[3]);
				rrTimeSlice = Integer.parseInt(args[4]);
		}
		
		catch(Exception e)
		{
			System.out.println("When using command line arguments, you should use the form:");
			System.out.println("nCPUS nPROCS CTXSwitchTime CPUBursts rrTimeSlice");
			System.out.println("All or none of these are required.");
			System.out.println("****PRESS ENTER TO CONTINUE****");
			Scanner k2 = new Scanner(System.in);
			k2.nextLine();
			k2.close();
		}

		String[] types = {"RR","SJF","SJFE","PP"};
		Scanner keyboard = new Scanner(System.in);
		for(String tp : types)
		{
			System.out.println("Starting run for " + tp + " scheduler algorithm.");
			System.out.println("************************************************");
			runSched(tp);
			System.out.println("\n\n************************************************");
			System.out.println(tp + "<- run is complete.");
			System.out.println("Press <ENTER> to continue.");
			keyboard.nextLine();	
		}
		keyboard.close();
	}
	public static int numCPUs = 4;
	public static int numOfProcs = 12;
	public static int ctxSwitchTime = 2;
	public static int numCPUBoundBursts = 8;
	public static int rrTimeSlice = 200;


	public static void runSched(String tp)
	{

		AlgorithmType algo = AlgorithmType.valueOf(tp);
		cpus = new ArrayList<CPU>(numCPUs);
		processes = new ArrayList<Process>(numOfProcs);
		generateProcesses(numOfProcs, numCPUBoundBursts, ctxSwitchTime, algo);
		generateCPUs(numCPUs);
		Scheduler scheduler = new Scheduler(processes, cpus, ctxSwitchTime, rrTimeSlice, algo);
		scheduler.runSearchAlgorithm();	
	}
	
	public static void generateProcesses(int numProcs, int numCPUBoundBursts, int ctxSwitchTime, AlgorithmType algo){
		//courtesy of Mark with tweaks by Sean
		//generate a list of 80/20 interactive/io procs:
		
        ArrayList<Boolean> mt80 = new ArrayList<>(numProcs);
        int v1 = (int) (numProcs * .80);
        int v2 = numProcs - v1;
        
        for(int i = 0; i < v1; i ++)
            mt80.add(true);
        for(int i = 0; i < v2; i ++)
            mt80.add(false);
        
        Collections.shuffle(mt80);
        
        int x = 0;
        while(mt80.size() != 0)
        {        	
        	int priority = randInt(0,4);
        	Process pc = new Process(x,mt80.remove(0),numCPUBoundBursts,priority,0,ctxSwitchTime);
        	processes.add(pc);
        	x += 1;
        }
	}
	
	public static void generateCPUs(int numCPUs){
		
		for(int k = 0; k < numCPUs; k++){
			cpus.add(new CPU(k));
		}
		
	}
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
}
