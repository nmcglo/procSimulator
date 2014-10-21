import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class RunScheduler {

	static ArrayList<CPU> cpus;
	static ArrayList<Process> processes;
	
	public static void main (String args[]){
		
		int numCPUs = 4;
		int numOfProcs = 100;
		int ctxSwitchTime = 2;
		int numCPUBoundBursts = 8;
		int rrTimeSlice = 4;
		AlgorithmType algo = AlgorithmType.SJF;
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
