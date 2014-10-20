import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 *
 * @author mark
 */
public class RoundRobin {
    ArrayList<CPU> Processors;
    ConcurrentLinkedQueue<Process> idleProcs;
    ArrayList<Process> waitProcs;
    ArrayList<Process> theProcs;
    
    
    
    public RoundRobin(int numOfProcs, int numOfCpus, int ioBurstNum, int ctxSwitchTime){
        theProcs = new ArrayList<>();
        waitProcs = new ArrayList<>();
        Processors = new ArrayList<>(numOfProcs);
          
        Processors.forEach(new Consumer<CPU>() {
            int x = numOfCpus;
            public void accept(CPU cpu) {
                cpu = new CPU(x);
                x--;
            }
        });
        //generate a list of 80/20 interactive/io procs:
        ArrayList<Boolean> mt80 = new ArrayList<>(numOfProcs);
        int v1 = (int) (numOfProcs * .80);
        int v2 = numOfProcs - v1;
        
        for(int i = 0; i < v1; i ++)
            mt80.add(true);
        for(int i = 0; i < v2; i ++)
            mt80.add(false);
        
        Collections.shuffle(mt80);
        
        int x = 0;
        idleProcs = new ConcurrentLinkedQueue<Process>();
        while(mt80.size() != 0)
        {
            Process pc  = new Process(x,mt80.remove(0),ioBurstNum,getPrior(),0,ctxSwitchTime);
            idleProcs.add(pc);
            theProcs.add(pc);
            
        }
        //setup is complete. .. run the thing!

    }
    
    public static int getPrior() {
        return 3;
    }
    
    public boolean stillRun()
    {
        boolean r = false;

        if(theProcs.parallelStream().filter(pc->(pc.isInteractive == false && (!pc.isDone()) )).count() > 0)
            r = true;
        return r;
    }
    public Process getProc()
    {
        return idleProcs.poll();
    }
    public void runRR()
    {
        while(stillRun())
        {
           for(CPU cp :Processors)
           {
               if(cp.getProcess().remCurrentCPUTime() == 0)
               {
                   waitProcs.add(cp.getProcess());
                   cp.rmProcess();
               }
               if(cp.isIdle())
                   cp.addProcess(getProc());
               
                   
           }
           //ticks:
           theProcs.forEach(p->p.tick());
           Processors.forEach(p->p.tick());
           for(Process p : waitProcs)
           {
               if(p.ttlWaitRem() == 0)
               { idleProcs.add(p);
                waitProcs.remove(p);
           }
           
        }
    }
    }
}