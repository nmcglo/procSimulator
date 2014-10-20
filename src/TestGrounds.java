import java.util.PriorityQueue;


public class TestGrounds {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		
		PriorityComparator pComparator = new PriorityComparator();
		PriorityQueue<Process> readyQueue = new PriorityQueue<Process>(pComparator);
		
		Process p1 = new Process(1, false, 8, 0, 0, 2);
		Process p2 = new Process(2, false, 8, 0, 0, 2);
		Process p3 = new Process(3, false, 8, 3, 0, 2);
		Process p4 = new Process(4, false, 8, 2, 0, 2);
		Process p5 = new Process(5, false, 8, 1, 0, 2);
		
		readyQueue.add(p1);
		readyQueue.add(p2);
		readyQueue.add(p3);
		readyQueue.add(p4);
		readyQueue.add(p5);
		
		
		
		System.out.println(readyQueue);
	}

}
