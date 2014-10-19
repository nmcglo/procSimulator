import java.util.Comparator;


public class PriorityComparator implements Comparator<Process>
{
	@Override
	public int compare(Process o1, Process o2) 
	{
		return (int) (o1.priority - o2.priority);
	}

}
