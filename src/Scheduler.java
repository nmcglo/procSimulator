import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
/*
 * --- Operating Systems Homework 2 ---
 * Copyright (c) 2014, Mark Plagge -- plaggm
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
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

public class Scheduler {

    private int numProcs;
    private int numCPUs;
    private int contextSwitchTime;
    private int RRTimeSlice;
    private AlgorithmType algorithmType;
    private int totalTimeSpent;
    private ArrayList<Process> allProcesses;
    private ArrayList<CPU> cpus;

    public Scheduler(ArrayList<Process> procs, ArrayList<CPU> CPUs, int ctxSwitchTime, int rrTimeSlice, AlgorithmType atype) {
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

    public void addNewProcess(Process p) {
        p.setState(ProcessState.idle);
        this.numProcs++;
        this.allProcesses.add(p);
    }
//May not be used

    public void addCPU(CPU cpu) {
        this.numCPUs++;
        this.cpus.add(cpu);
    }
//May not be used

    public void moveToCPU(Process p) {
        p.switchContext(ProcessState.active);
//Find a ready CPU and give it the process
        for (int i = 0; i < cpus.size(); i++) {
            if (cpus.get(i).getProcess() == null) {
                cpus.get(i).addProcess(p);
            }
        }
    }
//May not be used

    public void moveFromCPUtoWait(Process p) {
//get the process that it's referencing from the process list
        for (int i = 0; i < allProcesses.size(); i++) {
            if (allProcesses.get(i).pid == p.pid) {
                p = allProcesses.get(i);
            }
        }
    }

    public void runSearchAlgorithm() {
        switch (algorithmType) {
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

    public void runShortestJobFirst() {
        SJFComparator sfjComparator = new SJFComparator();
        PriorityQueue<Process> readyQueue = new PriorityQueue<Process>(sfjComparator);
        ArrayList<Process> waitingList = new ArrayList<Process>();
        for (int i = 0; i < allProcesses.size(); i++) {
            if (allProcesses.get(i).pState == ProcessState.idle) {
                readyQueue.add(allProcesses.get(i));
            }
        }
        while (!(isCPUBoundDone(readyQueue, waitingList))) {
            this.tick();
            allProcesses.forEach(p -> p.tick());
            cpus.forEach(cpu -> cpu.tick());
            cpus.forEach(cpu -> {
                if (!(cpu.isIdle())) {
                    if (cpu.getProcess().getCurrentState() == ProcessState.IOWait
                            || cpu.getProcess().getCurrentState() == ProcessState.userWait
                            || cpu.getProcess().getCurrentState() == ProcessState.terminated) {
                        waitingList.add(cpu.getProcess());
                        cpu.rmProcess();
                    }
                }
            });
            if (!(waitingList.isEmpty())) {
                for (int i = 0; i < waitingList.size(); i++) {
                    Process proc = waitingList.get(i);
                    if (proc.getCurrentState() == ProcessState.idle) {
                        readyQueue.add(proc);
                        waitingList.remove(proc);
                    } else if (proc.getCurrentState() == ProcessState.terminated) {
                        waitingList.remove(proc);
                    }
                }
            }
            if (!(readyQueue.isEmpty())) {
                for (int j = 0; j < numCPUs; j++) {
                    CPU cpu = cpus.get(j);
                    if (cpu.isIdle() && !(readyQueue.isEmpty())) {
                        cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));
                    }
                }
            }
        }
        this.tick(); //Need extra tick or we won't see termination of last proc.
    }

    public void runShortestJobFirstPreemption() {
        SJFComparator sfjComparator = new SJFComparator();
        PriorityQueue<Process> readyQueue = new PriorityQueue<Process>(sfjComparator);
        ArrayList<Process> waitingList = new ArrayList<Process>();
        for (int i = 0; i < allProcesses.size(); i++) {
            if (allProcesses.get(i).pState == ProcessState.idle) {
                readyQueue.add(allProcesses.get(i));
            }
        }
        while (!(isCPUBoundDone(readyQueue, waitingList))) {
        	this.tick();
            allProcesses.forEach(p -> p.tick());
            cpus.forEach(cpu -> cpu.tick());
            cpus.forEach(cpu -> {
                if (!(cpu.isIdle())) {
                    if ((cpu.getProcess().getCurrentState() == ProcessState.IOWait
                            || cpu.getProcess().getCurrentState() == ProcessState.userWait
                            || cpu.getProcess().getCurrentState() == ProcessState.terminated)) {
                        waitingList.add(cpu.getProcess());
                        cpu.rmProcess();
                    }
                }
            });
            if (!(waitingList.isEmpty())) {
                for (int i = 0; i < waitingList.size(); i++) {
                    Process proc = waitingList.get(i);
                    if (proc.getCurrentState() == ProcessState.idle) {
                        readyQueue.add(proc);
                        waitingList.remove(proc);
                    } else if (proc.getCurrentState() == ProcessState.terminated) {
                        waitingList.remove(proc);
                    }
                }
            }
            if (!(readyQueue.isEmpty())) {
                for (int j = 0; j < numCPUs; j++) {
                    CPU cpu = cpus.get(j);
                    if (!(readyQueue.isEmpty())) {
                        if (cpu.isIdle()) {
                            cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));
                        } else if (cpu.getProcess().burstValue > readyQueue.peek().burstValue) {
                            readyQueue.add(cpu.getProcess().preempt());
                            cpu.rmProcess();
                            cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));
                        }
                    }
                }
            }
        }
        this.tick();//Sean's edit 
    }

///***** ROUND ROBIN CODE *******////
    public static ArrayList<Process> rrWaitProcs;
    public static ConcurrentLinkedQueue<Process> rrIdleProcs;
    public static int rrCT = 0;

    public void runRoundRobin() {
        rrWaitProcs = new ArrayList<>();
        rrIdleProcs = new ConcurrentLinkedQueue<>();
        PriorityQueue<Process> x;
        ArrayList<Process> y;
//load up the processes:
        allProcesses.parallelStream().forEach((px) -> {
            rrIdleProcs.add(px);
        });
        long isDone;
        long lp;

        lp = allProcesses.stream().filter(p -> p.isInteractive == false).count();
        System.out.println(lp);
        boolean runAgain;
        do {
            isDone = 0;
            runRR();
            for (Process p : allProcesses) {
                if (p.getCurrentState() == ProcessState.terminated && p.isInteractive == false) {
                    isDone++;
                }
            }

        } while (isDone != lp);
        this.tick(); //Sean's edit

    }
//assuming that this method is called over and over again through
//a running loop (pseudo anonymous function haha)

    public void runRR() {
        this.tick();
        try {
            ///load and unload cpu:
            if (rrIdleProcs.size() > 0 && rrCT != RRTimeSlice) {
                for (CPU cp : cpus) {

                    //if cpu has proc, and it is done, move it to wait queue.
                    if (cp.getProcess() != null && cp.getProcess().isWaiting()) {
                        rrWaitProcs.add(cp.getProcess());
                        cp.rmProcess();
                    }
                    if (cp.getProcess() == null) {
                        //if cpu is empty, add a process.
                        cp.addProcess(rrIdleProcs.poll());
                        cp.getProcess().setState(ProcessState.active);
                    }
                }
            } else if (rrCT == RRTimeSlice) {
                for (CPU cp : cpus) {
                    //if cpu has a proc, and it is done, move it to wait queue.
                    if (cp.getProcess() != null) {
                        rrWaitProcs.add(cp.getProcess());
                        cp.rmProcess();

                    }
                    if (cp.getProcess() == null) {
                        cp.addProcess(rrIdleProcs.poll());
                        cp.getProcess().setState(ProcessState.active);
                    }

                }

            }
        } catch (Exception e) {
            //ddx -er
        }

        //Tick All Processes
        allProcesses.forEach((p) -> p.tick());
        cpus.parallelStream().forEach((c) -> c.tick());

        rrCT = (rrCT + 1) % RRTimeSlice;
        //move procs from wait to idle, if they are no longer waiting on IO/USER
        for (int i = 0; i < rrWaitProcs.size(); i++) {
            if (rrWaitProcs.get(i).isWaiting() == false || rrWaitProcs.get(i).getCurrentState() == ProcessState.idle) {
                rrIdleProcs.add(rrWaitProcs.remove(i));
            }
        }
    }

///END ROUND ROBIN **********************************************************
    public void runPreemptivePriority() {
        PriorityComparator pComparator = new PriorityComparator();
        PriorityQueue<Process> readyQueue = new PriorityQueue<Process>(pComparator);
        ArrayList<Process> waitingList = new ArrayList<Process>();
        for (int i = 0; i < allProcesses.size(); i++) {
            if (allProcesses.get(i).pState == ProcessState.idle) {
                readyQueue.add(allProcesses.get(i));
            }
        }
        while (!(isCPUBoundDone(readyQueue, waitingList))) {
//Check for AGING
        	this.tick(); //Sean's edit (10/23/14, 8:57am)
            readyQueue.forEach(proc -> {
                if (proc.getPriority() > 0) {
                    if (proc.timeInIdleQueue() > 1200) {
                        proc.priority--;
                        System.out.print("[time " + totalMS + "] ");
                        System.out.println("Increased priority of CPU-bound process ID " + proc.getPid() + " to " + proc.getPriority() + " due to aging");
                    }
                }
            });
            
            allProcesses.forEach(p -> p.tick());
            cpus.forEach(cpu -> cpu.tick());
            cpus.forEach(cpu -> {
                if (!(cpu.isIdle())) {
                    if ((cpu.getProcess().getCurrentState() == ProcessState.IOWait
                            || cpu.getProcess().getCurrentState() == ProcessState.userWait
                            || cpu.getProcess().getCurrentState() == ProcessState.terminated)) {
                        waitingList.add(cpu.getProcess());
                        cpu.rmProcess();
                    }
                }
            });
            if (!(waitingList.isEmpty())) {
                for (int i = 0; i < waitingList.size(); i++) {
                    Process proc = waitingList.get(i);
                    if (proc.getCurrentState() == ProcessState.idle) {
                        readyQueue.add(proc);
                        waitingList.remove(proc);
                    } else if (proc.getCurrentState() == ProcessState.terminated) {
                        proc.switchContext(ProcessState.terminated);
                        waitingList.remove(proc);
                    }
                }
            }
            if (!(readyQueue.isEmpty())) {
                for (int j = 0; j < numCPUs; j++) {
                    CPU cpu = cpus.get(j);
                    if (!(readyQueue.isEmpty())) {
                        if (cpu.isIdle()) {
                            cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));
                        } else if (cpu.getProcess().priority > readyQueue.peek().priority) {
                            readyQueue.add(cpu.getProcess().preempt());
                            cpu.rmProcess();
                            cpu.addProcess(readyQueue.poll().switchContext(ProcessState.active));
                        }
                    }
                }
            }
        }
        this.tick(); //Sean's edit 
    }

    public boolean isCPUBoundDone(PriorityQueue<Process> readyQueue, ArrayList<Process> waitingList) {
        boolean done = true;
        Iterator<Process> readyIter = readyQueue.iterator();
        while (readyIter.hasNext()) {
            if (!(readyIter.next().isInteractive())) { //not interactive means CPU bound
                done = false;
                return done;
            }
        }
        Iterator<Process> waitingIter = waitingList.iterator();
        while (waitingIter.hasNext()) {
            if (!(waitingIter.next().isInteractive())) {
                done = false;
                return done;
            }
        }
        //Checks if there's a CPU-bound process in a CPU (still executing).
        Iterator<CPU> cpuIter = cpus.iterator();
        while(cpuIter.hasNext()){
        	CPU cpu = cpuIter.next();
        	if(!(cpu.isIdle())){
        		if(!(cpu.getProcess().isInteractive())){
        			done = false;
        			return done;
        		}
        	}
        }
        return done;
    }
    
    long totalMS = 0;

    public void tick() {
        String w = "[time ";
        String r = "ms] ";
        allProcesses.stream().forEach((p) -> {
            try {
                String val = p.announce();
                int crappyVariable = val.length();
                if (val.equalsIgnoreCase("null")) {
                    throw new NullPointerException();
                }

                System.out.println(w + totalMS + r + val);
            } catch (NullPointerException e) {
            }
        });
        totalMS++;
    }

}

//ADD TICK 2
