package com.afia.cookiecliker;

import static java.lang.Math.floor;
import static java.lang.Math.pow;

import java.util.HashMap;

import javax.swing.JOptionPane;

/**
 * The main thread behind Clicker
 * @author Afia
 *
 */
public class Clicker extends Thread implements Runnable {
	
	//Count for each type of building
	private HashMap<Building, Integer> buildingCount = new HashMap<Building, Integer>();
	
	//Game variables
	private double cookieCount;
	private double cookiesPerSecond;
	
	/**
	 * Sets up the hash map
	 */
	public Clicker() {
		for( Building b : Building.values() ) {
			this.buildingCount.put(b, 0);
		}
		
		this.cookieCount = cookiesPerSecond = 0.0;
	}
	
	/**
	 * Autmatically generates cookies
	 */
	public void run() {
		while( true ) {
			this.cookieCount += cookiesPerSecond;
			try {
				sleep( 1000 );
			} 
			catch (InterruptedException e) {
				JOptionPane.showMessageDialog( null, "An error has occured with the runtime!", "Error!", JOptionPane.ERROR_MESSAGE );
				System.out.println( e );
				break;
			}
		}
	}
	
	/**
	 * Returns the number of cookies
	 * @return The rounded down number of cookies
	 */
	public long getAbsoluteCookieCount() {
		return (long)( floor( this.cookieCount ) );
	}
	
	/**
	 * Determines the amount of cookies being generated per second as a whole
	 * @return The number of cookies generated per second
	 */
	private double determineCookiesPerSecond() {
		double current = 0.0;
		
		for( Building b : Building.values() ) {
			current += ( (double)this.buildingCount.get(b) * b.getCookiesPerSecond() );
		}
		
		return current;
	}
	
	/**
	 * Purchases a specific building
	 * @param buildingToPurchase The building the player wishes to purchase
	 * @return True if the building was purchased successfully. False otherwise.
	 */
	public boolean purchaseBuilding( Building buildingToPurchase ) {
		int currentNumberOfBuildings = this.buildingCount.get( buildingToPurchase );
		long costOfNewBuilding = (long)(buildingToPurchase.getIntialCost() * pow(1.15, currentNumberOfBuildings) );
		
		if( getAbsoluteCookieCount() >= costOfNewBuilding ) {
			this.buildingCount.put( buildingToPurchase, this.buildingCount.get( buildingToPurchase ) + 1 );
			this.cookieCount -= costOfNewBuilding;
			this.cookiesPerSecond = determineCookiesPerSecond();
			return true;
		}
		return false;
	}
	
	/**
	 * Adds 1 cookie to the count. Used for user clicks
	 */
	public void generateCookie() {
		this.cookieCount++;
	}
	
	/**
	 * The number of cookies per second
	 * @return The number of cookies generated per second.
	 */
	public double getCookiesPerSecond() {
		return this.cookiesPerSecond;
	}
	
	/**
	 * Returns the number of this type of building owned
	 * @param building The building to check
	 * @return The number of buildings of this type
	 */
	public int getBuildingCount( Building building ) {
		return this.buildingCount.get( building );
	}
}