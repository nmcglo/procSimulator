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


/**
 *
 * @author Mark Plagge -- plaggm
 * @author Sean Daly -- dalys2
 * 
 */

public abstract class AbstractCPU {
    private Process process;
    private int idleTime;
    private int usageTime;
    private int CPUID;
    
    public AbstractCPU(int ID){
    	process = null;
    	idleTime = 0;
    	usageTime = 0;
    	CPUID = ID;
    }
    
    public boolean isIdle(){
        return process != null;
    }
    
    public  abstract void addProcess(Process p);
    public	abstract Process getProcess();
    public  abstract void rmProcess();
    public  abstract void tick();
	public	abstract int getIdleTime();
	public	abstract void setIdleTime(int idleTime);
	public	abstract int getUsageTime();
	public	abstract void setUsageTime(int usageTime);
	public	abstract int getCPUID();
	public	abstract void setCPUID(int cPUID);
    
}
