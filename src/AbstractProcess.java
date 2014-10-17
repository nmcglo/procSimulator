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


import java.util.HashMap;
import stateEnums.ProcessState;
import static stateEnums.ProcessState.Active;
import static stateEnums.ProcessState.CTXSwitch;
import static stateEnums.ProcessState.IOWait;
import static stateEnums.ProcessState.Idle;
import static stateEnums.ProcessState.Terminated;
import static stateEnums.ProcessState.UserWait;

/**
 *
 * @author Mark
 */
public abstract class AbstractProcess {
    int pid;
    boolean isInteractive;
    int cpuTimeNeeded;
    
    //Initial values (set via constructor)
    int vWait;
    int ioWait;
    /** burstValue is the total CPU time needed. **/
    int burstValue;
    int priority;
    int userWait;
    
    int startTime;
    //Calculated Values (xxxTime is time spent in a state)

    ProcessState pState;
  
    private static final String pidStr = "Process ID: ";
    private static final String sttStr = "|| Start Stime: ";
    private static final String twtStr = "|| Total Wait Time: ";
    private static final String tioStr = "|| Total IO Waiting Time: ";
    HashMap<ProcessState, Integer> timings;
         
         

    public AbstractProcess(int pid, boolean isInteractive,int burstValue, int priority,  int startTime ) {
        this.pid = pid;
        this.isInteractive = isInteractive;
        this.cpuTimeNeeded = 0;
        this.burstValue = burstValue;
        this.priority = priority;
        this.startTime = startTime;
        this.pState = ProcessState.Idle;
        //set up timing table:
                timings = new HashMap<>(6);
        int it = 0;
        timings.put(Idle, it);
        timings.put(UserWait, it);
        timings.put(IOWait, it);
        timings.put(Active, it);
        timings.put(Terminated, it);
        timings.put(CTXSwitch, it);
    }
    public boolean isInteractive()
    {
        return this.isInteractive;  
    }
    //Timing getter:
    public int getTiming(ProcessState stq){
        return timings.get(stq);
    } 
    public int getTiming(String sq) {
        return timings.get(ProcessState.valueOf(sq));
        
    }
    //return timings (current process times):
      public int getActiveTime() {
        return getTiming(Active);
    }
   
    public int getIoWaitTime() {
        return getTiming(IOWait);
    }
    public int getUserWaitTime() {
        return getTiming(UserWait);
    }
    public int getCtxSwitchTime() {
        return getTiming(CTXSwitch);
    }
    public int getIdleTime() {
        return getTiming(Idle);
    }

    
    public int getRemainingBursts()
    {
        return this.burstValue;
    }
    public int getBurstValue() {
        return burstValue;
    }
    //process remaining time(calculated values)
    public abstract int getTotalWaitTime();
    public abstract int remIoWait();
    public abstract int remActiveTime();
    public abstract int remUserWait();
    
    
    
    //Process stat methods
    public int getStartTime() {
        return startTime;
    }

    public int getPriority() {
        return priority;
    }
    public int getPid() {
        return pid;
    }


       

    
    
    @Override
    public String toString() {
        return pidStr + this.getPid() + sttStr + this.getStartTime() + twtStr + 
                getTotalWaitTime()+ tioStr + getIoWaitTime();
    }
    /** Setters Below
     * @param state **/
    public void setState(ProcessState state){
        this.pState = state;
    }
    //Abstract (need to implement) methods:
    
    abstract public void tick();
    abstract public boolean isDone();
    
    
    
}
