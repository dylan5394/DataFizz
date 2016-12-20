package app;

import com.google.gson.annotations.SerializedName;

public class Book {

	private String title;
	private String author;
	private String price;
	private String shipping_weight;
	private transient double shippingWeightDouble;
	@SerializedName("isbn-10")
	private String isbn10;
	
	public Book(String title, String author, String price, String shippingWeight, String isbn10) {
		
		this.title = title;
		this.author = author;
		this.price = price + " USD";
		this.shippingWeightDouble = Double.parseDouble(shippingWeight);
		this.shipping_weight = shippingWeight + " pounds";
		this.isbn10 = isbn10;
	}

	//Scale weight by a factor of 10 to remove decimals so it works with knapsack algorithm
	public int getWeight() {
		return (int)(10*shippingWeightDouble);
	}

	public String getTitle() {
		return title;
	}
}
