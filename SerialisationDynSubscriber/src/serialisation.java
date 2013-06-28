import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.dynamease.dyndemoandroapp.entity.DynSubscriber;

public class serialisation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<DynSubscriber> dl = new ArrayList<DynSubscriber>();
		dl.add(new DynSubscriber("A", "B"));
		dl.add(new DynSubscriber("A", "C"));
		dl.add(new DynSubscriber("Yves", "Nicolas"));
		dl.add(new DynSubscriber("C", "A"));
		dl.add(new DynSubscriber("antoine", "veuiller"));
		dl.add(new DynSubscriber("D", "E"));
		dl.add(new DynSubscriber("D", "F"));
		dl.add(new DynSubscriber("C", "H"));
		dl.add(new DynSubscriber("G", "R"));

		String foldername = "/home/dynamease/Documents/DynContacts/";

		ObjectOutputStream out = null;
		int id = 0;
		for (DynSubscriber ds : dl) {
			try {
				ds.setSubscriberId(id++);
				String filename = ds.getFullName() + ".dyndata";
				out = new ObjectOutputStream(new FileOutputStream(new File(
						foldername + filename)));
				out.writeObject(ds);
				out.flush();
				System.out.println("Wrote: "+foldername + filename);
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
