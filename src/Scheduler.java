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
	int numProgs;
	int contextSwitchTime;
	int numP;
	int RRTimeSlice;
	String algorithmType;
	int totalTimeSpent;
	
	public Scheduler(String atype)
	{
		this.numProgs = 0;
		this.contextSwitchTime = 0;
		this.numP = 0;
		this.RRTimeSlice = 0;
		this.algorithmType = atype;
		this.totalTimeSpent = 0;
	}
	
	
	
	public List<Process> allProcs()
	{
		
		return null;
	}
	
	public Queue<Process> waiting()
	{
		
		return null;
	}
	
	public List<CPU> cpus()
	{
		
		return null;
	}
	
	public List<Process> userWait()
	{
		
		return null;
	}
	
	public List<Process> IOWait()
	{
		
		return null;
	}
	
	public List<Process> completeProcesses()
	{
		
		return null;
	}
	
	

}
