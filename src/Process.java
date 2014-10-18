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
        System.out.println(p.getIdleTime());
    }

    private ScriptingContainer ruby;
    boolean hasRuby;
    ScriptEngine jruby;
    Object timingO;
    final int ctxSwitchLagTime;
    
    //IO / User Wait remaining time values:
    int ioWaitRem = 0;
    int usrWaitRem = 0;
    
 
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
        hasRuby = false;
        this.ctxSwitchLagTime = ctxSwitchLagTime;
        

    }
    /**
     * When entering the READY/IDLE queue, we have to set a random CPU time. 
     * This handles that.
     */
    void generateCPUTime(){
            if(hasRuby)
            {
               try {
                this.cpuTimeNeeded =  (int) ((Invocable)jruby).invokeMethod(timingO, "getBurstTime");
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
        
    }
    
    private void switchToWait(){
        
            if(this.burstNums == 0){
            this.pState = ProcessState.terminated;
            }
            this.pState = this.isInteractive? ProcessState.userWait : ProcessState.IOWait;
            stateTimeAdj(ProcessState.contextSwitch,ctxSwitchLagTime);
            stateTimeAdj(-2);            
      
    }
    
    /**
     * switchContext changes the current context of this process to the new one specified in the parameter.
     * It will manage CTX timings, as well as any sort of termination issues.
     * @param newContext 
     */
    public void switchContext(ProcessState newContext)
    {
        
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
          //  hasRuby=false;
            //no ruby here.   
        }
    }
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
        if(hasRuby)
           try {
               ticr(pState.toString());
        } catch (Exception ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            hasRuby = false;
            timings.replace(pState, timings.get(pState) + 1);
        }
        else
        timings.replace(pState, timings.get(pState) + 1);
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
    public int getTiming(ProcessState stq){
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
    public int getTotalWaitTime() {
        return getTiming(ProcessState.IOWait) + getTiming(ProcessState.userWait)
                + getTiming(ProcessState.contextSwitch) +getTiming(ProcessState.idle) ;
    }

    @Override
    public int remIoWait() {
        return ioWaitRem;
    }

    
    public int remCurrentCPUTime() {
        return burstValue;
    }
    

    @Override
    public int remUserWait() {
        return usrWaitRem;
    }
    
    /**
     * Total remaining wait time in this context. Automatically handles the 
     * system 
     * @return 
     */
    public int ttlWaitRem() {
        return remUserWait() + remIoWait();
    }
    
    
}       
