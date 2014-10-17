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
import java.util.ArrayList;
import org.jruby.Main;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import stateEnums.ProcessState;
import static stateEnums.ProcessState.Idle;
import org.jruby.*;
 


public class Process extends AbstractProcess {

   

    private ScriptingContainer ruby;
    boolean hasRuby;
    ScriptEngine jruby;
    Object timingO;
    public static void main(String[] args) throws ScriptException {
        Process p = new Process(1, true, 2, 4, 5);
       Process q = new Process(1, true, 2, 4, 5);
        
    }
    public void ticr(String itm) throws ScriptException, NoSuchMethodException
    {
           // jruby.eval("$timings.tickTime(\"" + itm + "\")");
        ((Invocable)jruby).invokeMethod(timingO, "tickTime(\"" + itm + "\")");

    }
    public int getTr(String itm) throws ScriptException, NoSuchMethodException
    {
        return ( (Integer) ((Invocable)jruby).invokeMethod(timingO, "getTiming(\"" + itm + "\")"));
    }
    public Process(int pid, boolean isInteractive, int burstValue, int priority, int startTime) {
        super(pid, isInteractive, burstValue, priority, startTime);
        
        //set up tming info:
        /* Due to dec. to not use Ruby here, I used a hashmap instead. Code left
        as a warning or tutorial on jruby integration. */
        createRuby(null);
         

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
            hasRuby=false;
            //no ruby here.   
        }
    }
    @Override
    public boolean isDone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
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
    @Override
    public int getTotalWaitTime() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int remIoWait() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int remActiveTime() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int remUserWait() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
       
  

 
 class rbTimings{
     
 }



}
