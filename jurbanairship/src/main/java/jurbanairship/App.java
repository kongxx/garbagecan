package jurbanairship;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App
{
//    public static void main( String[] args )
//    {
//        System.out.println( "Hello World!" );
//		System.out.println(new File("/").getAbsolutePath());
//		System.out.println(new File(".").getAbsolutePath());
//    }

	private static App app = new App();

	private List<String> ids = new ArrayList<String>();

	private App() {

	}

	public static App getInstance() {
		return app;
	}

	public List<String> getIds() {
		return ids;
	}

	public void addId(String id) {
		ids.add(id);
	}

	public void removeId(String id) {
		ids.remove(id);
	}

}
