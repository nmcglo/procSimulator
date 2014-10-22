import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;


/* 
 *  --- Operating Systems Homework 2 --- 
 * Copyright (c) 2014, Mark Plagge -- plaggm
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
public class Scheduler 
{
	private int numProcs;
	private int numCPUs;
	private int contextSwitchTime;
	private int RRTimeSlice;
	private AlgorithmType algorithmType;
	private int totalTimeSpent;
	
	private ArrayList<Process> allProcesses;
	private ArrayList<CPU> cpus;
	
	
	public Scheduler(ArrayList<Process> procs, ArrayList<CPU> CPUs, int ctxSwitchTime, int rrTimeSlice, AlgorithmType atype)
	{
		this.allProcesses = procs;
		this.numProcs = allProcesses.size();
		this.cpus = CPUs;
		this.numCPUs = cpus.size();
		this.contextSwitchTime = ctxSwitchTime;
		this.RRTimeSlice = rrTimeSlice;
		this.algorithmType = atype;
		this.totalTimeSpent = 0;
                
                    
	}
	
	//GETTERS AND SETTERS****************************************************************
	public int getNumProcs() {
		return numProcs;
	}

	public void setNumProcs(int numProgs) {
		this.numProcs = numProgs;
	}

	public int getContextSwitchTime() {
		return contextSwitchTime;
	}

	public void setContextSwitchTime(int contextSwitchTime) {
		this.contextSwitchTime = contextSwitchTime;
	}

	public int getRRTimeSlice() {
		return RRTimeSlice;
	}

	public void setRRTimeSlice(int rRTimeSlice) {
		RRTimeSlice = rRTimeSlice;
	}

	public int getTotalTimeSpent() {
		return totalTimeSpent;
	}

	public void setTotalTimeSpent(int totalTimeSpent) {
		this.totalTimeSpent = totalTimeSpent;
	}
	
	public AlgorithmType getAlgorithmType() {
		return algorithmType;
	}

	public void setAlgorithmType(AlgorithmType algorithmType) {
		this.algorithmType = algorithmType;
	}

	public List<Process> getAllProcesses() {
		return allProcesses;
	}

	public void setAllProcesses(ArrayList<Process> allProcesses) {
		this.allProcesses = allProcesses;
	}

	public List<CPU> getCpus() {
		return cpus;
	}

	public void setCpus(ArrayList<CPU> cpus) {
		this.cpus = cpus;
	}
	
	public int getNumCPUs() {
		return numCPUs;
	}

	public void setNumCPUs(int numCPUs) {
		this.numCPUs = numCPUs;
	}

	
	
	//BEGIN OTHER METHODS*****************************************************
	//May not be used
	public void addNewProcess(Process p)
	{
		p.setState(ProcessState.idle);
		this.numProcs++;
		this.allProcesses.add(p);
	}
	//May not be used
	public void addCPU(CPU cpu)
	{
		this.numCPUs++;
		this.cpus.add(cpu);
	}
	//May not be used
	public void moveToCPU(Process p)
	{
		p.switchContext(ProcessState.active);
		
		//Find a ready CPU and give it the process
		for(int i = 0; i < cpus.size(); i++)
		{
			if (cpus.get(i).getProcess() == null)
				cpus.get(i).addProcess(p);
		}
	}
	//May not be used
	public void moveFromCPUtoWait(Process p)
	{
		
		//get the process that it's referencing from the process list
		for(int i = 0; i < allProcesses.size(); i++)
		{
			if (allProcesses.get(i).pid == p.pid)
				p = allProcesses.get(i);	
		}
	}
	
	
	
	public void runSearchAlgorithm()
	{
		switch (algorithmType)
		{
		case SJF:
			runShortestJobFirst();
			break;
		case SJFE:
			runShortestJobFirstPreemption();
			break;
		case RR:
			runRoundRobin();
			break;
		case PP:
			runPreemptivePriority();
			break;
		}
		
	}
	/* Changes made to rSJF: 
	 * 1. changed forEach to regular for loops for 2nd and 3rd big if statements.
	 * 2. edited isIdle in CPU.
	 * 3. changed member variables in AbstractCPU to protected, not private
	 * 4. removed member variables in CPU, thus only being in AbstractCPU now
	 * 5. added else statement in tick() in Process. */
	public void runShortestJobFirst()
	{
		
		SJFComparator sfjComparator = new SJFComparator();
		PriorityQueue<Process> readyQueue = new PriorityQueue<Process>(sfjComparator);
		ArrayList<Process> waitingList = new ArrayList<Process>();
		for (int i = 0; i < allProcesses.size(); i++)
		{
			if (allProcesses.get(i).pState == ProcessState.idle)
			{
				readyQueue.add(allProcesses.get(i));
			}	
		}
		while(!(isCPUBoundDone(readyQueue, waitingList))){
			
			allProcesses.forEach(p -> p.tick());
			cpus.forEach(cpu -> cpu.tick());
			cpus.forEach(cpu->{
				if(!(cpu.isIdle())){
					if (cpu.getProcess().getCurrentState() == ProcessState.IOWait ||
						cpu.getProcess().getCurrentState() == ProcessState.userWait ||
						cpu.getProcess().getCurrentState() == ProcessState.terminated){
								
						waitingList.add(cpu.getProcess());
						cpu.rmProcess();
					}
				}
				
			});
			if(!(waitingList.isEmpty())){
				for(int i = 0; i < waitingList.size(); i++){ 		//a change
					Process proc = waitingList.get(i);				//a change
					if(proc.getCurrentState() == ProcessState.idle){
						readyQueue.add(proc);
						waitingList.remove(proc);
					}
					else if(proc.getCurrentState() == ProcessState.terminated){
						proc.switchContext(ProcessState.terminated);
						System.out.println(proc.getPid()+" has terminated");
						waitingList.remove(proc);
					}
					
				}
			}
			if(!(readyQueue.isEmpty())){
				for(int j = 0; j < numCPUs; j++){					//a change
					CPU cpu = cpus.get(j); 							//a change
					if(cpu.isIdle() && !(readyQueue.isEmpty())){ 	//a change
						cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));
					}
				}
			}
		}
		System.out.println("DONE!");
	}
	
	public void runShortestJobFirstPreemption()
	{
		
		SJFComparator sfjComparator = new SJFComparator();
		PriorityQueue<Process> readyQueue = new PriorityQueue<Process>(sfjComparator);
		ArrayList<Process> waitingList = new ArrayList<Process>();
		for (int i = 0; i < allProcesses.size(); i++)
		{
			if (allProcesses.get(i).pState == ProcessState.idle)
			{
				readyQueue.add(allProcesses.get(i));
			}	
		}	
		while(!(isCPUBoundDone(readyQueue, waitingList))){
			allProcesses.forEach(p -> p.tick());
			cpus.forEach(cpu -> cpu.tick());
			cpus.forEach(cpu->{
				if(!(cpu.isIdle())){ 
					if((cpu.getProcess().getCurrentState() == ProcessState.IOWait ||
						cpu.getProcess().getCurrentState() == ProcessState.userWait ||
						cpu.getProcess().getCurrentState() == ProcessState.terminated)){
					
						waitingList.add(cpu.getProcess());
						cpu.rmProcess();
					}
				}
			});
			if(!(waitingList.isEmpty())){
				for(int i = 0; i < waitingList.size(); i++){ 		
					Process proc = waitingList.get(i);		
					if(proc.getCurrentState() == ProcessState.idle){
						readyQueue.add(proc);
						waitingList.remove(proc);
					}
					else if(proc.getCurrentState() == ProcessState.terminated){
						proc.switchContext(ProcessState.terminated);
						System.out.println(proc.getPid() + " has terminated");
						waitingList.remove(proc);
					}
					
				}
			}
			if(!(readyQueue.isEmpty())){
				for(int j = 0; j < numCPUs; j++){					
					CPU cpu = cpus.get(j); 
					if(!(readyQueue.isEmpty())){
						if(cpu.isIdle()){
							System.out.println(readyQueue.toString()+" is entering CPU");
							cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));	
						}
						else if (cpu.getProcess().burstValue > readyQueue.peek().burstValue){
							readyQueue.add(cpu.getProcess().preempt());
							System.out.println(cpu.getProcess().getPid() + " was preempted");
							cpu.rmProcess();
							System.out.println(readyQueue.toString()+" is entering CPU");
							cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));
						}
					}
				}
			}
		}
	}
	public static ConcurrentLinkedQueue<Process> rrWaitProcs;
        public static ConcurrentLinkedQueue<Process> rrIdleProcs;
        public static int rrCT = 0;
	public void runRoundRobin()
	{
          
            rrWaitProcs = new ConcurrentLinkedQueue<>();
            rrIdleProcs = new ConcurrentLinkedQueue<>();
            //load up the processes:
            allProcesses.parallelStream().forEach((px) -> {
                rrIdleProcs.add(px);
            });
            
        }
              //assuming that this method is called over and over again through
            //a running loop (pseudo anonymous function haha)
            public void runRR() {
            for(CPU cp : cpus)
            {
                if(cp.getProcess().remCurrentCPUTime() == 0) // process is done.
                {
                    rrWaitProcs.add(cp.getProcess());
                    cp.rmProcess();
                }
                if(cp.isIdle())
                {
                    cp.addProcess(rrIdleProcs.poll());
                }
                //and do the time slice thing
            }
            if(rrCT == RRTimeSlice)
            {
                //slice of time has happened. Preempt the cpu procs:
                for(CPU cp:cpus)
                {
                    rrWaitProcs.add(cp.getProcess());
                    cp.rmProcess();
                    cp.addProcess(rrWaitProcs.poll());
                    
                }
            }
            //clean up the Process stuff - move readies out of wait:
            rrWaitProcs.stream().filter((p) -> (!p.isWaiting())).map((p) -> {
                rrIdleProcs.add(p);
                return p;
            }).forEach((p) -> {
                rrWaitProcs.remove(p);
            });
            
            //Tick All Processes
            allProcesses.forEach((p) -> p.tick());
            cpus.parallelStream().forEach((c) -> c.tick());
            rrCT = (rrCT + 1) % RRTimeSlice;
            
           
		
	}
	
	
	public void runPreemptivePriority()
	{
		PriorityComparator pComparator = new PriorityComparator();
		PriorityQueue<Process> readyQueue = new PriorityQueue<Process>(pComparator);
		ArrayList<Process> waitingList = new ArrayList<Process>();
		for (int i = 0; i < allProcesses.size(); i++)
		{
			if (allProcesses.get(i).pState == ProcessState.idle)
			{
				readyQueue.add(allProcesses.get(i));
			}	
		}
		
		
		
		//need to check if all CPU bound processes are complete, not if
		//ready queue is empty.
		while(!(isCPUBoundDone(readyQueue, waitingList))){
			readyQueue.forEach(proc ->
			{ 
				if(proc.getPriority() > 0)
				{
					if(proc.getTiming(ProcessState.idle) > 1200)
					{
						proc.priority--;
					}	
				}
			});
			
			allProcesses.forEach(p -> p.tick());
			cpus.forEach(cpu -> cpu.tick());
			cpus.forEach(cpu->{
				if(cpu.isIdle() && 
				(cpu.getProcess().getCurrentState() == ProcessState.IOWait ||
				cpu.getProcess().getCurrentState() == ProcessState.userWait ||
				cpu.getProcess().getCurrentState() == ProcessState.terminated)){
					
					waitingList.add(cpu.getProcess());
					cpu.rmProcess();
				}
			});
			if(!(waitingList.isEmpty())){
				waitingList.forEach(proc -> {
					
					if(proc.getCurrentState() == ProcessState.idle){
						readyQueue.add(proc);
						waitingList.remove(proc);
					}
					else if(proc.getCurrentState() == ProcessState.terminated){
						proc.switchContext(ProcessState.terminated);
						waitingList.remove(proc);
					}
					
				});
			}
			if(!(readyQueue.isEmpty())){
				cpus.forEach(cpu ->{
					if(cpu.isIdle()){
						cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));
					}
					else if (cpu.getProcess().priority > readyQueue.peek().priority){
						readyQueue.add(cpu.getProcess().preempt());
						cpu.rmProcess();
						cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));
					}
				});
			}
			
			
		}
			
	}
	
	public boolean isCPUBoundDone(PriorityQueue<Process> readyQueue, ArrayList<Process> waitingList){
		boolean done = true;
		
		Iterator<Process> readyIter = readyQueue.iterator();
		while(readyIter.hasNext()){
			if(!(readyIter.next().isInteractive())){ //not interactive means CPU bound
				done = false;
				return done;
			}
		}
		Iterator<Process> waitingIter = waitingList.iterator();
		while(waitingIter.hasNext()){
			if(!(waitingIter.next().isInteractive())){
				done = false;
				return done;
			}
		}
		return done;
	}

}
