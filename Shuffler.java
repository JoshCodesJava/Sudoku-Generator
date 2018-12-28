import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Shuffler
{
	private static final Random rand = new Random();

	public static <T> void shuffle(List<T> elements)
	{
		for(int i = 0; i < elements.size(); i++)
		{
			Collections.shuffle(elements, rand);
		}
	}
}