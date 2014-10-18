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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import org.jruby.embed.ScriptingContainer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptEngine;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


 


public class Process extends AbstractProcess {

    public static void main(String[] args) {
        Process p = new Process(1, false, 1,2,3,4);
        p.tick();
        p.switchContext(ProcessState.active);
        while(p.remCurrentCPUTime() != 0)
            p.tick();
        System.out.println("Done");
 
    }

    private ScriptingContainer ruby;
    boolean hasRuby;
    ScriptEngine jruby;
    Object timingO;
    final int ctxSwitchLagTime;
    
    //IO / User Wait remaining time values:
    long ioWaitRem = 0;
    long usrWaitRem = 0;
    
 
    /**
     * 
     * @param pid -- Process ID
     * @param isInteractive -- Is this an interactive process?
     * @param numberOfBursts -- Number of bursts needed
     * @param priority -- The priority for the process.
     * @param startTime 
     * @param ctxSwitchLagTime -- A lag time for process context switching.
     */
    
    public Process(int pid, boolean isInteractive, int numberOfBursts, int priority, int startTime, int ctxSwitchLagTime) {
        super(pid, isInteractive, numberOfBursts, priority);
       
        //set up tming info:
        /* Due to dec. to not use Ruby here, I used a hashmap instead. Code left
        as a warning or tutorial on jruby integration. */
        createRuby(null);
        //hasRuby = false;
        this.ctxSwitchLagTime = ctxSwitchLagTime;
        //And initiate the state of this machine:
        this.switchContext(ProcessState.idle);
        

    }
    /**
     * When entering the READY/IDLE queue, we have to set a random CPU time. 
     * This handles that.
     */
    void generateCPUTime(){
            if(hasRuby)
            {
               try {
                this.cpuTimeNeeded = (long) ((Invocable)jruby).invokeMethod(timingO, "getBurstTime");
                   System.out.println("DEBUG - CPU TIME IS:" + this.cpuTimeNeeded);
            } catch (ScriptException | NoSuchMethodException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
    }
    /**
     * When entering the wait times, we have to set a random wait time.
     * This handles that.
     */
    void generateWaitTime() {
        if(hasRuby)
        {
            int wt;
            int rt;
            try {
                wt = (int) ((Invocable)jruby).invokeMethod(timingO, "getIOTime");
                rt = (int) ((Invocable)jruby).invokeMethod(timingO, "getIntTime");
            } catch (ScriptException | NoSuchMethodException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                Random d = new Random();
                wt = d.nextInt(2999) + 1000;
                rt = d.nextInt(3199) + 1200;
            }
            
            this.ioWaitRem  = wt;
            this.usrWaitRem = rt;
            if(isInteractive)
                ioWaitRem = 0;
            else
                usrWaitRem = 0;
            
        }
    }
    
    private void ctxOverhead()
    {
        stateTimeAdj(ProcessState.contextSwitch,ctxSwitchLagTime);
        stateTimeAdj(0 - ctxSwitchLagTime);     
    }

    private void switchToWait(){
        
            if(this.burstNums == 0){
            this.pState = ProcessState.terminated;
            }
            this.pState = this.isInteractive? ProcessState.userWait : ProcessState.IOWait;
            //generate random times for the new process states and times and things:
            generateWaitTime();
            //add overhead (CTX SWITCH) to the new process state:
            ctxOverhead();      
    }
    
    private void switchToIdle(){
        
        //Not much to double check - just add the CTX switch time overhead and then
        //migrate.
        this.pState = ProcessState.idle;
        this.generateCPUTime(); // create a CPU time for the process.
        //is there a ctx switch for this?
    }
    
    /**
     * switchContext changes the current context of this process to the new one specified in the parameter.
     * It will manage CTX timings, as well as any sort of termination issues.
     * @param newContext 
     */
    public final void switchContext(ProcessState newContext)
    {
        switch(newContext){
            case idle: switchToIdle();
                break;
            case IOWait:
            case userWait:
                switchToWait();
                
                break;
            default:
                this.pState = newContext;
        }
        
    }
    
    
   
    void stateChange(int val)
    {
        
    }
    public void ticr(String itm) throws ScriptException, NoSuchMethodException
    {
           // jruby.eval("$timings.tickTime(\"" + itm + "\")");
        ((Invocable)jruby).invokeMethod(timingO, "tickTime",itm);

    }
    public int getTr(String itm) throws ScriptException, NoSuchMethodException
    {
        return ( (Integer) ((Invocable)jruby).invokeMethod(timingO, "getTiming",itm));
    }
    private void createRuby(ArrayList<String> timings)
    {
        hasRuby = true;
       
 
        try {
            System.setProperty("org.jruby.embed.localvariable.behavior", "persistent");
            URL url = getClass().getResource("processTimer.rb");
            File rbf = new File(url.getPath());
           
            jruby = new ScriptEngineManager().getEngineByName("jruby");   
            
           timingO = jruby.eval(new BufferedReader(new FileReader(rbf)));
           if(timings == null)
               ((Invocable)jruby).invokeMethod(timingO, "createDefault");
           else
           {
               jruby.put("$tList", timings.toArray());
               ((Invocable)jruby).invokeMethod(timingO,"createCustomInt");
           }

            
        } catch (FileNotFoundException|NoSuchMethodException | ScriptException ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            //hasRuby=false;
            //no ruby here.   
        }
    }
    /**
     * IsDone tells you if the process is done.
     * @return -True if the process is terminated.
     */
    @Override
    public boolean isDone() {
        return (burstValue + burstNums) == 0;
    }
    
    /**
     * Does the tick methods. Will also move this process into a wait state and 
     * possibly move the process into a IO/User wait. Also handles context. 
     * HOWEVER: This process doesn't notify the scheduler when the state is ready 
     * to change.
     */
    @Override
    public void tick() {
        if(isDone()) {
            return;
        } 
        
        if(hasRuby)
        {
                try {
                    ticr(pState.toString()); // increment our times

             } catch (ScriptException | NoSuchMethodException ex) {
                 Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                 hasRuby = false;
                 //timings.replace(pState, timings.get(pState) + 1);
                 //TODO: add some non-ruby dependant alternatives to the try statement,
                 //or die.
             }

             //next, see if we need to move out of our current context:
             if(ttlWaitRem() == 0 && (pState == ProcessState.userWait|| pState == ProcessState.IOWait ))
             {
                       //initiate a change to the idle state.
                 switchToIdle();
             }
             if(remCurrentCPUTime() == 0 && (pState== ProcessState.active))
             {
                 switchToWait();
             }
        }
        stateTimeAdj(1);    
        
        
    }
    /**
     * adjusts the process' current time, for the current context.
     * @param value 
     */
    void stateTimeAdj(int value)
    {
        this.timings.replace(pState, timings.get(pState) + value);
    }
    /**
     * adjusts the process' current time, for an arbitrary context.
     * @param p <- string name of the state
     * @param value <-value to adjust
     */
    void stateTimeAdj(String p, Integer value)
    {
        ProcessState ps = ProcessState.valueOf(p);
        stateTimeAdj(ps,value);
        
    }
     /**
     * adjusts the process' current time, for an arbitrary context.
     * @param p <- a processState to change.
     * @param value <-value to adjust
     */
    void stateTimeAdj(ProcessState p, Integer value)
    {
        this.timings.replace(p, timings.get(p) + value);
    }
   
    
    /**
     * GetTiming. Returns the total time spent at context (ProcessState) stq
     * @param stq The processState you are interested in.
     * @return 
     */
    @Override
    public long getTiming(ProcessState stq){
        if(hasRuby)
            try {
                return (getTr(stq.toString()));
                
        } catch (ScriptException|NoSuchMethodException ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            hasRuby = false;
            
        } 
        
        return timings.get(stq);
        
 
    }
    @Override
    public long getTotalWaitTime() {
        return getTiming(ProcessState.IOWait) + getTiming(ProcessState.userWait)
                + getTiming(ProcessState.contextSwitch) +getTiming(ProcessState.idle) ;
    }

    @Override
     long remIoWait() {

            return ioWaitRem;
    }

    /**
     * Gives the remaining time in MS for this CPU's burst speed.
     * @return CPU TIME NEEDED TO FINISH PROCESS
     */
    public long remCurrentCPUTime() {
       
        try {
            
            long x = (long)((Invocable)jruby).invokeMethod(timingO, "getCBurst");
            return  x;
        } catch (ScriptException | NoSuchMethodException ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            
        return burstValue;
    }
    

    @Override
     long remUserWait() {
        return usrWaitRem;
    }
    
    /**
     * Total remaining wait time in this context. Automatically handles the 
     * system 
     * @return 
     * @throws javax.script.ScriptException 
     * @throws java.lang.NoSuchMethodException 
     */
    public long ttlWaitRem() {
        if(hasRuby)
           try {
               return (long) ((Invocable)jruby).invokeMethod(timingO, "getCWait");
        } catch (ScriptException | NoSuchMethodException ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return remUserWait() + remIoWait();
    }
    
    
}       
