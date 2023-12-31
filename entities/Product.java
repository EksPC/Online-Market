package entities;

import java.io.Serializable;

/**
 * This class represents a market product described by:
 * id, name, price.
 * It is possible that a {@code MyClient} already purchased the product:
 * 	in this case there's also the ownerName field;
 * 
 * @see MyClient
 * */
public class Product implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String ownerName = "";
	private double price;
	
	
	public Product(String id, String name, double price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getId() {
		return this.id;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public void setPrice(double newPrice) {
		this.price = newPrice;
	}
	
	public void setOwnerName(String name) {
		this.ownerName = name;
	}
	
	public String getOwnerName() {
		return this.ownerName;
	}
}
