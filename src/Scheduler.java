import java.util.List;
import java.util.Queue;

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
	
	private List<Process> allProcesses;
	private List<CPU> cpus;
	private Queue<Process> readyQueue;
	private Queue<Process> waitQueue; //IOWAIT AND USERWAIT
	
	
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

	public void setAllProcesses(List<Process> allProcesses) {
		this.allProcesses = allProcesses;
	}

	public List<CPU> getCpus() {
		return cpus;
	}

	public void setCpus(List<CPU> cpus) {
		this.cpus = cpus;
	}

	public Queue<Process> getReadyQueue() {
		return readyQueue;
	}

	public void setReadyQueue(Queue<Process> readyQueue) {
		this.readyQueue = readyQueue;
	}

	public Queue<Process> getWaitQueue() {
		return waitQueue;
	}

	public void setWaitQueue(Queue<Process> waitQueue) {
		this.waitQueue = waitQueue;
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
		this.numProcs++;
		this.allProcesses.add(p);
		this.readyQueue.add(p);
	}
	
	public void addCPU(CPU cpu)
	{
		this.numCPUs++;
		this.cpus.add(cpu);
	}
	
	public void moveToCPU()
	{
		Process p = readyQueue.poll();
		p.switchContext(ProcessState.active);
		
		//Find a ready CPU and give it the process
		for(int i = 0; i < cpus.size(); i++)
		{
			if (cpus.get(i).getProcess() == null)
				cpus.get(i).addProcess(p);
		}
	}
	
	public void moveFromCPU(int pid)
	{
		//get the process that it's referencing from the process list
		
		
		
	}
	
	
	
	
	

}
