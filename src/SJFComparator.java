import java.util.Comparator;


public class SJFComparator implements Comparator<Process> 
{

	@Override
	public int compare(Process o1, Process o2)
	{
		return o1.burstValue - o2.burstValue;
	}
	
}
