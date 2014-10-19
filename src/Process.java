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
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
<<<<<<< HEAD
=======
import org.jruby.embed.ScriptingContainer;
>>>>>>> FETCH_HEAD
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.jruby.embed.ScriptingContainer;
/**
 * Process represents a process running in the simulation. 
 * When created, the process will be in an idle state. 
 * When switchContext is called, moving the process to active, one burstnum is removed.
 * If burstnums are zero and the burstTime is zero, the process will auto-switch to terminated. THIS ADDS CONTEXT SWITCH TIME as of now.
 * The process will auto-switch to a wait time if the process time is done, adding context switch overhead.
 * Further ticks will remove time waiting for the user. Once those run out, the process will self-switch to idle (waiT)
 * 
 * @author Mark Plagge -- plaggm
 */
public class Process extends AbstractProcess {

    public static void runner(Process p) {
        int counter = 0;
   
        
        p.tick(); //an idle tick to test the idle ticker.
        p.switchContext(ProcessState.active);
        while(p.isDone() == false)
        {
            //CPU TIME.
            while (p.remCurrentCPUTime() != 0) {
                p.tick();
                
                counter++;
            }
            //WAIT TIME:
              System.out.println(p.getCurrentState());
            while(!p.isWaiting())
            {
                p.tick();
            }
            System.out.println("Burst Times Left: " + p.remBurstTimes());
            System.out.println("is done? " + p.isDone());
          
            p.switchContext(ProcessState.active);
            System.out.println("is done? " + p.isDone());
        }

       
        System.out.println("Done");
        System.out.println(counter);
        System.out.println(p.toString());
        System.out.println(p.getActiveTime());
        System.out.println(p.remBurstTimes());
        p.switchContext(ProcessState.active);
        p.tick();
        System.out.println(p.toString());
        System.out.println(p.getActiveTime());
        System.out.println(p.remBurstTimes());

        System.out.println("-------WAIT TIMES. Idle: " + p.getIdleTime() + "IO WAIT"
                + p.getIoWaitTime() + "USR Wait" + p.getUserWaitTime() + "CTX SWITCH"
                + p.getCtxSwitchTime());
        System.out.println("--- Rem Burts: " + p.remCurrentCPUTime());
        System.out.println("--- IS DONE: " + p.isDone());

    }

<<<<<<< HEAD
    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
=======
    public static void main(String[] args) {
        Process p = new Process(1, false, 1,2,3,4);
        p.tick();
        p.switchContext(ProcessState.active);
        while(p.remCurrentCPUTime() != 0)
            p.tick();
        System.out.println("Done");
 
>>>>>>> FETCH_HEAD
    }

    private ScriptingContainer ruby;
    boolean hasRuby;
    ScriptEngine jruby;
    Object receiver;
    final int ctxSwitchLagTime;

    //IO / User Wait remaining time values:
    long ioWaitRem = 0;
    long usrWaitRem = 0;
<<<<<<< HEAD

=======
    
 
>>>>>>> FETCH_HEAD
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
<<<<<<< HEAD
=======
        
>>>>>>> FETCH_HEAD

    }

    /**
     * When entering the READY/IDLE queue, we have to set a random CPU time.
     * This handles that.
     */
<<<<<<< HEAD
    void generateCPUTime() {
        
        /*
        if (hasRuby) {
            try {
                this.burstValue = (long) ((Invocable) jruby).invokeMethod(receiver, "getBurstTime");
               
                  // ((Invocable)jruby).invokeMethod(receiver,"setNewBurst");

=======
    void generateCPUTime(){
            if(hasRuby)
            {
               try {
                this.cpuTimeNeeded = (long) ((Invocable)jruby).invokeMethod(timingO, "getBurstTime");
                   System.out.println("DEBUG - CPU TIME IS:" + this.cpuTimeNeeded);
>>>>>>> FETCH_HEAD
            } catch (ScriptException | NoSuchMethodException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        //alarm
        this.burstValue = 1;
    }

    /**
     * When entering the wait times, we have to set a random wait time. This
     * handles that.
     */
    void generateWaitTime() {
<<<<<<< HEAD

        Random d = new Random();
        long wt = d.nextInt(2999) + 1000;
        long rt = d.nextInt(3199) + 1200;

        this.ioWaitRem = wt;
        this.usrWaitRem = rt;
        if (isInteractive) {
            ioWaitRem = 0;
        } else {
            usrWaitRem = 0;
        }

    }

    /**
     * helper that adds context overhead time.
     */
    private void ctxOverhead() {
        stateTimeAdj(ProcessState.contextSwitch, ctxSwitchLagTime);
        stateTimeAdj(0 - ctxSwitchLagTime); // remove some of the current time.
        //then adjust the proper wait and other time.
        ioWaitRem = ioWaitRem == 0 ? 0 : ioWaitRem - 3;
        usrWaitRem = usrWaitRem == 0 ? 0 : usrWaitRem - 3;
    }

    private void switchToWait() {

        if (isDone()) {
            this.pState = ProcessState.terminated;
        }
        this.pState = this.isInteractive ? ProcessState.userWait : ProcessState.IOWait;
        //generate random times for the new process states and times and things:
        generateWaitTime();
        //add overhead (CTX SWITCH) to the new process state:
        ctxOverhead();
=======
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
>>>>>>> FETCH_HEAD
    }

    private void switchToIdle() {

        //Not much to double check - just add the CTX switch time overhead and then
        //migrate.
        burstNums --; // we will have one fewer burst times
        this.pState = ProcessState.idle;
        this.generateCPUTime(); // create a CPU time for the process.
        //is there a ctx switch for this?
    }

    void switchToActive() {

        ctxOverhead();
        if (isDone()) {
            this.pState = ProcessState.terminated;
        } else {
            
            this.pState = ProcessState.active;
        }
    }

    /**
     * switchContext changes the current context of this process to the new one
     * specified in the parameter. It will manage CTX timings, as well as any
     * sort of termination issues.
     *
     * @param newContext
     */
<<<<<<< HEAD
    public final void switchContext(ProcessState newContext) {
        switch (newContext) {
            case idle:
                switchToIdle();
=======
    public final void switchContext(ProcessState newContext)
    {
        switch(newContext){
            case idle: switchToIdle();
>>>>>>> FETCH_HEAD
                break;
            case IOWait:
            case userWait:
                switchToWait();
<<<<<<< HEAD

                break;
            case active:
                switchToActive();
=======
                
>>>>>>> FETCH_HEAD
                break;
            default:
                this.pState = newContext;
        }
<<<<<<< HEAD

    }

    void stateChange(int val) {

=======
        
    }
    
    
   
    void stateChange(int val)
    {
        
>>>>>>> FETCH_HEAD
    }

    public void ticr(String itm) throws ScriptException, NoSuchMethodException {
        // jruby.eval("$timings.tickTime(\"" + itm + "\")");
        ((Invocable) jruby).invokeMethod(receiver, "tickTime", itm);

    }

    public long getTr(String itm) throws ScriptException, NoSuchMethodException {
        return ((long) ((Invocable) jruby).invokeMethod(receiver, "getTiming", itm));
    }

    private void createRuby(ArrayList<String> timings) {
        hasRuby = true;

        try {
            System.setProperty("org.jruby.embed.localvariable.behavior", "persistant");
            URL url = getClass().getResource("processTimer.rb");
            File rbf = new File(url.getPath());
<<<<<<< HEAD
            System.out.println(url);
            jruby = new ScriptEngineManager().getEngineByName("jruby");
            receiver = jruby.eval(new BufferedReader(new FileReader(rbf)));
=======
           
            jruby = new ScriptEngineManager().getEngineByName("jruby");   
            
           timingO = jruby.eval(new BufferedReader(new FileReader(rbf)));
           if(timings == null)
               ((Invocable)jruby).invokeMethod(timingO, "createDefault");
           else
           {
               jruby.put("$tList", timings.toArray());
               ((Invocable)jruby).invokeMethod(timingO,"createCustomInt");
           }
>>>>>>> FETCH_HEAD

            if (timings == null) {
                ((Invocable) jruby).invokeMethod(receiver, "createDefault");
            } else {
                jruby.put("$tList", timings.toArray());
                ((Invocable) jruby).invokeMethod(receiver, "createCustomInt");
            }

            ScriptingContainer cont = new ScriptingContainer();

        } catch (FileNotFoundException | NoSuchMethodException | ScriptException ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            //hasRuby=false;
            //no ruby here.   
        }
    }
<<<<<<< HEAD

    /**
     * IsDone tells you if the process is done.
     *
=======
    /**
     * IsDone tells you if the process is done.
>>>>>>> FETCH_HEAD
     * @return -True if the process is terminated.
     */
    @Override
    public boolean isDone() {

        return (burstValue + burstNums) == 0;
    }

    /**
     * Does the tick methods. Will also move this process into a wait state and
     * possibly move the process into a IO/User wait. Also handles context.
     * HOWEVER: This process doesn't notify the scheduler when the state is
     * ready to change.
     */
    @Override
    public void tick() {
<<<<<<< HEAD
        if (isDone()) {
            this.switchContext(ProcessState.terminated);
            return;
        }
        else {
            if (hasRuby) {
                try {
                    ticr(pState.toString()); // increment our times
                } catch (ScriptException | NoSuchMethodException ex) {
                    Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                    hasRuby = false;
                    timings.replace(pState, timings.get(pState) + 1);
                    //TODO: add some non-ruby dependant alternatives to the try statement,
                    //or die.
                }
            }
             switch (pState) {
                case active:
                    this.burstValue--;
                    break;
                case IOWait:
                    this.ioWaitRem--;
                    break;
                case userWait:
                    this.usrWaitRem--;
                    break;
            }
                //next, see if we need to move out of our current context:
            //if the cpu time out, we switch to a wait speech
            
            if(burstValue == 0 && pState == ProcessState.active)
            {
                if(burstValue == 0 && burstNums == 0)
                    this.pState = ProcessState.terminated;
                //switch to waiting!
                switchToWait(); //should add some wait time, and then switch context.
                
            }
            else if (ttlWaitRem() == 0 && (pState == ProcessState.userWait || pState == ProcessState.IOWait)) {
                //initiate a change to the idle state.
                switchContext(ProcessState.idle);
                
            }
            //switch to idle
           
            }
        
            //stateTimeAdj(1);    
            //and update our remaning burst/wait times:
           
        }

    

=======
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
>>>>>>> FETCH_HEAD
    /**
     * adjusts the process' current time, for the current context.
     *
     * @param value
     */
    void stateTimeAdj(int value) {
        stateTimeAdj(pState, value);
    }

    /**
     * adjusts the process' current time, for an arbitrary context.
     *
     * @param p <- string name of the state @param value <-valu
     * e to adjust
     */
    void stateTimeAdj(String p, Integer value) {
        ProcessState ps = ProcessState.valueOf(p);
        stateTimeAdj(ps, value);

    }

    /**
     * adjusts the process' current time, for an arbitrary context.
     *
     * @param p <- a processState to change. @param value <-value
     * to adjust
     */
    void stateTimeAdj(ProcessState p, Integer value) {
        try {
            rStateTimeAdj(p, value);
        } catch (ScriptException | NoSuchMethodException ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            jStateTimeAdj(p, value);
        }
    }

    void jStateTimeAdj(ProcessState p, Integer value) {
        this.timings.replace(p, timings.get(p) + value);
    }

    void rStateTimeAdj(ProcessState p, Integer value) throws ScriptException, NoSuchMethodException {
        for (int i = 1; i < value; i++) {
            ticr(p.toString());
        }
    }

    /**
     * GetTiming. Returns the total time spent at context (ProcessState) stq
     *
     * @param stq The processState you are interested in.
     * @return
     */

    @Override
<<<<<<< HEAD
    public long getTiming(ProcessState stq) {
        if (hasRuby) {
=======
    public long getTiming(ProcessState stq){
        if(hasRuby)
>>>>>>> FETCH_HEAD
            try {
                return (getTr(stq.toString()));

            } catch (ScriptException | NoSuchMethodException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                hasRuby = false;

            }
        }

        return timings.get(stq);

    }

    @Override
    public long getTotalWaitTime() {
        return getTiming(ProcessState.IOWait) + getTiming(ProcessState.userWait)
                + getTiming(ProcessState.contextSwitch) + getTiming(ProcessState.idle);
    }

    @Override
     long remIoWait() {

<<<<<<< HEAD
        return ioWaitRem;
=======
            return ioWaitRem;
>>>>>>> FETCH_HEAD
    }

    /**
     * Gives the remaining time in MS for this CPU's burst speed.
<<<<<<< HEAD
     *
     * @return CPU TIME NEEDED TO FINISH PROCESS
     */
     public long remCurrentCPUTime() {

=======
     * @return CPU TIME NEEDED TO FINISH PROCESS
     */
    public long remCurrentCPUTime() {
       
        try {
            
            long x = (long)((Invocable)jruby).invokeMethod(timingO, "getCBurst");
            return  x;
        } catch (ScriptException | NoSuchMethodException ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            
>>>>>>> FETCH_HEAD
        return burstValue;
    }

    @Override
     long remUserWait() {
        return usrWaitRem;
    }

    /**
<<<<<<< HEAD
     * Total remaining wait time in this context. Automatically handles the
     * system
     *
     * @return

     */
    public long ttlWaitRem() {

=======
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
        
        
>>>>>>> FETCH_HEAD
        return remUserWait() + remIoWait();
    }

    /**
     * This returns the number of bursts left for this process to finish.
     *
     * @return
     */
    public long remBurstTimes() {
        return this.burstNums;
    }

    /**
     * total time this process has been running (CPU bound)
     * @return 
     */
    public long totalActiveTime() {
        return getTiming(ProcessState.active);
    }

    /**
     * total time this process has been idle (ready/wait)
     * @return 
     */
    public long totalIdleTime() {

        return getTiming(ProcessState.idle);
    }
    /**
     * The total time this process has been waiting on user or IO.
     * @return 
     */
    public long totalWaitTime() {
        return getTotalWaitTime();
    }
    
    public boolean isWaiting() {
        return (remIoWait() + remUserWait()) != 0;
    }

    public ProcessState getCurrentState(){
            return this.pState;
}

    @Override
    public long getTiming(String t) {
        return getTiming(ProcessState.valueOf(t));
    }
}
