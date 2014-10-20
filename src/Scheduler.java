import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;


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
	
	
	public Scheduler(AlgorithmType atype)
	{
		this.numProcs = 0;
		this.contextSwitchTime = 0;
		this.RRTimeSlice = 0;
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
	public void addNewProcess(Process p)
	{
		p.setState(ProcessState.idle);
		this.numProcs++;
		this.allProcesses.add(p);
	}
	
	public void addCPU(CPU cpu)
	{
		this.numCPUs++;
		this.cpus.add(cpu);
	}
	
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
	
	public void runShortestJobFirst()
	{
		
		SJFComparator sfjComparator = new SJFComparator();
		PriorityQueue<Process> readyQueue = new PriorityQueue<Process>(sfjComparator);
		ArrayList<Process> waitingList = new ArrayList<Process>();
		for(int k = 0; k < numCPUs; k++){
			cpus.add(new CPU(k));
		}
		for (int i = 0; i < allProcesses.size(); i++)
		{
			if (allProcesses.get(i).pState == ProcessState.idle)
			{
				readyQueue.add(allProcesses.get(i));
			}	
		}
		while(!(readyQueue.isEmpty()) && !(waitingList.isEmpty())){
			allProcesses.forEach(p -> p.tick());
			cpus.forEach(cpu -> cpu.tick());
			cpus.forEach(cpu->{
				if(cpu.getProcess() != null && 
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
						allProcesses.get(allProcesses.indexOf(proc)).setState(ProcessState.terminated);
						waitingList.remove(proc);
					}
					
				});
			}
			if(!(readyQueue.isEmpty())){
				cpus.forEach(cpu ->{
					if(cpu.getProcess() == null){
						cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));
					}
				});
			}
		}
	}
	
	public void runShortestJobFirstPreemption()
	{
		
		SJFComparator sfjComparator = new SJFComparator();
		PriorityQueue<Process> readyQueue = new PriorityQueue<Process>(sfjComparator);
		ArrayList<Process> waitingList = new ArrayList<Process>();
		for(int k = 0; k < numCPUs; k++){
			cpus.add(new CPU(k));
		}
		for (int i = 0; i < allProcesses.size(); i++)
		{
			if (allProcesses.get(i).pState == ProcessState.idle)
			{
				readyQueue.add(allProcesses.get(i));
			}	
		}	
		while(!(readyQueue.isEmpty()) && !(waitingList.isEmpty())){
			allProcesses.forEach(p -> p.tick());
			cpus.forEach(cpu -> cpu.tick());
			cpus.forEach(cpu->{
				if(cpu.getProcess() != null && 
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
						allProcesses.get(allProcesses.indexOf(proc)).setState(ProcessState.terminated);
						waitingList.remove(proc);
					}
					
				});
			}
			if(!(readyQueue.isEmpty())){
				cpus.forEach(cpu ->{
					if(cpu.getProcess() == null){
						cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));
					}
					else if (cpu.getProcess().remCurrentCPUTime() > readyQueue.peek().remCurrentCPUTime()){
						readyQueue.add(cpu.getProcess().preempt());
						cpu.rmProcess();
						cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));
					}
				});
			}
		}
	}
	
	public void runRoundRobin()
	{
		ArrayList<Process> readyQueue = new ArrayList<Process>();
		//No priority needed
		for (int i = 0; i < allProcesses.size(); i++)
		{
			if (allProcesses.get(i).pState == ProcessState.idle)
			{
				readyQueue.add(allProcesses.get(i));
			}
		}
	}
	
	public void runPreemptivePriority()
	{
		PriorityComparator pComparator = new PriorityComparator();
		PriorityQueue<Process> readyQueue = new PriorityQueue<Process>(pComparator);
		ArrayList<Process> waitingList = new ArrayList<Process>();
		for(int k = 0; k < numCPUs; k++)
		{
			cpus.add(new CPU(k));
		}
		for (int i = 0; i < allProcesses.size(); i++)
		{
			if (allProcesses.get(i).pState == ProcessState.idle)
			{
				readyQueue.add(allProcesses.get(i));
			}	
		}
		while(!(readyQueue.isEmpty()) && !(waitingList.isEmpty())){
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
						allProcesses.get(allProcesses.indexOf(proc)).setState(ProcessState.terminated);
						waitingList.remove(proc);
					}
					
				});
			}
			if(!(readyQueue.isEmpty())){
				cpus.forEach(cpu ->{
					if(cpu.getProcess() == null){
						cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));
					}
					else if (cpu.getProcess().remCurrentCPUTime() > readyQueue.peek().remCurrentCPUTime()){
						readyQueue.add(cpu.getProcess().preempt());
						cpu.rmProcess();
						cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));
					}
				});
			}
		}
		
		
		
		
		
		
	}
	
	
	

	

}
