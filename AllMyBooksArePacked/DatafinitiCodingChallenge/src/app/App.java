package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class App {
	
	public static void main(String[] args) throws MalformedURLException {
	
		//Assumes the data directory will be in the same folder as the exported jar file
		String dataDirectory = System.getProperty("user.dir");
		dataDirectory+="/data/";
		
		File dir = new File(dataDirectory);
		File[] directoryListing = dir.listFiles();
		
		/* When using files from a resource package in Eclipse:
		URL directory = App.class.getResource("data/");
		File dir = new File(directory.getFile());
		File[] directoryListing = dir.listFiles();
		*/
		
		Book[] books = null;
		
		if (directoryListing != null) {
			
			books = new Book[directoryListing.length];
			for(int i = 0; i < directoryListing.length; i ++) {
				
				Parser htmlParser = new Parser(directoryListing[i].toURI().toURL());
				books[i] = htmlParser.Parse();
			}
			
		} else {
			
			System.err.println("Couldn't find data directory");
		    System.exit(1);
		}
		
		Packer packer = new Packer(books);
		ArrayList<Box> boxes = packer.packBooks();
		
		String boxList="";
		for(Box box : boxes) {
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(box);
			boxList+=json + "\n";
		}
		
		try(PrintWriter out = new PrintWriter("output.json")) {
		    out.println(boxList);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}