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
package scInterfaces;

import stateEnums.ProcessState;

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
    
    //Calculated Values (xxxTime is time spent in a state)
    int idleTime;
    int ioWaitTime;
    int activeTime;
    int ctxSwitchTime;
    int startTime;
    int userWaitTime;
    
    
    private ProcessState pState;
    
    private static final String pidStr = "Process ID: ";
    private static final String sttStr = "|| Start Stime: ";
    private static final String twtStr = "|| Total Wait Time: ";
    private static final String tioStr = "|| Total IO Waiting Time: ";
    
    
    
    
    
    public boolean isInteractive()
    {
        return this.isInteractive;  
    }

    public int getActiveTime() {
        return activeTime;
    }

    public int getBurstValue() {
        return burstValue;
    }

    public int getUserWait() {
        return userWait;
    }

    public int getUserWaitTime() {
        return userWaitTime;
    }
    

    public int getCpuTimeNeeded() {
        return cpuTimeNeeded;
    }

    public int getCtxSwitchTime() {
        return ctxSwitchTime;
    }

    public int getIdle() {
        return idleTime;
    }

    public int getIoWait() {
        return ioWait;
    }

    public int getPid() {
        return pid;
    }

    public int getvWait() {
        return vWait;
    }
    
    public int getStartTime() {
        return startTime;
    }


    public int getPriority() {
        return priority;
    }

    public int getIoWaitTime() {
        return ioWaitTime;
    }
       
    public int getTotalWaitTime() {
        return getIoWait() + getIdle() + getvWait();
    }
    
    public int remIoWait() {
        return getIoWait() - getIoWaitTime();
    }
    public int remActiveTime() {
        return getBurstValue() - getActiveTime();
    }
    public int remUserWait() {
        return getUserWait()- getUserWaitTime();
    }
    
    
    @Override
    public String toString() {
        return pidStr + this.getPid() + sttStr + this.getStartTime() + twtStr + 
                getTotalWaitTime()+ tioStr + getvWait();
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
