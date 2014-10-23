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

public class CPU extends AbstractCPU{
    boolean isCtxSwitch;
    long oldP = 0;
    long totalTicks = 0;

    public CPU(int ID){
    	super(ID);
    }
    

    @Override
    public void addProcess(Process p) {
        if(isCtxSwitch == true)
        {
            
            isCtxSwitch = false;
            ///print msg:
            //[time 4279ms] Context switch (swapping out process ID 4 for process ID 17)
            
            System.out.println("[time "+ totalTicks +"ms] Context switch (swapping out process ID " + oldP + "for process ID " + p.getPid()+")");  
                        oldP = p.getPid();
                    }
        this.process = p;
    }

    @Override
    public void rmProcess() {
        isCtxSwitch = true;
    	this.process = null;
    }
    
    @Override
    public void tick() {
    	totalTicks++;
        if(process == null)
            this.idleTime ++;
        else
            this.usageTime++;
    }
    
    public int getIdleTime() {
       return this.idleTime;
    }
    
    public boolean isIdle(){
        return super.isIdle();
    }

	public Process getProcess() {
		return this.process;
	}

	public int getUsageTime() {
		return this.usageTime;
	}

	public void setUsageTime(int usageTime) {
		this.usageTime = usageTime;
	}

	public int getCPUID() {
		return this.CPUID;
	}

	public void setCPUID(int cPUID) {
		this.CPUID = cPUID;
	}

	public void setIdleTime(int idleTime) {
		this.idleTime = idleTime;
	}
    
}
