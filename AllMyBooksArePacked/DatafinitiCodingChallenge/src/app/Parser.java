package app;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {

	private File input;
	private String url;
	private Document doc;
	
	public Parser(URL path) {
		
		try {
			this.input = new File(path.toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//URL of html file to resolve links against
		this.url = path.toString();
	}
	
	public Book Parse() {
		
		try {
			this.doc = Jsoup.parse(this.input, "UTF-8", this.url);
			
			Elements links = this.doc.select("div.buying > span > a");
			String author  = links.text().trim();
			
			Elements spans = this.doc.select("span#btAsinTitle");
			String title = spans.first().childNode(0).toString().trim();
			
			//Check if the book is a permanent copy or a rental to determine price
			Elements prices = this.doc.select("span#actualPriceValue > b.priceLarge");
			String price = prices.text().trim();
			if(price.compareTo("") == 0) {
				prices = this.doc.select("td.rightBorder.buyNewOffers > span.rentPrice");
				price = prices.text();
			}
			
			Elements details = this.doc.select("table#productDetailsTable li");
			
			String weight = "";
			String isbn = "";
			for(Element element : details) {
				
				if(element.children().first().text().trim().compareTo("Shipping Weight:") == 0) {
					
					weight = element.textNodes().get(0).text().trim().split(" ")[0].trim();
				} else if(element.children().first().text().trim().compareTo("ISBN-10:") == 0) {
					
					isbn = element.textNodes().get(0).text().trim();
				}
			}
			
			return new Book(title, author, price, weight, isbn);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}