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

/**
 *
 * @author Mark Plagge -- plaggm
 */
public class CPUTuple {
    int pid;
    int time;

    public CPUTuple(int pid, int time)
    {
        this.pid = pid;
        this.time = time;
        this.proc = null;
    }
    public CPUTuple(int pid, int time, AbstractProcess p)
    {
        this.pid = pid;
        this.time = time;
        this.proc = p;
    }
    public int getPid() {
        return pid;
    }

    public int getTime() {
        return time;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setTime(int time) {
        this.time = time;
    }
    //Don't know if we want this:
    AbstractProcess proc;

    public AbstractProcess getProc() {
        return proc;
    }

    public void setProc(AbstractProcess proc) {
        this.proc = proc;
    }
    
    
} 
