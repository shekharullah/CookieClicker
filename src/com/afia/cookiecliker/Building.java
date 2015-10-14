package com.afia.cookiecliker;

/**
 * The various buildings available
 * @author Afia
 *
 */
public enum Building {
	
	CURSOR(15, 0.1), GRANDMA(100, 0.5), FARM(500, 2), FACTORY(3000, 10), MINE(10000, 40), SHIPMENT(40000, 100), ALCHEMY_LAB(200000, 400), 
	PORTAL(1666666, 6666), TIME_MACHINE(123456789, 98765), ANTIMATER_CONDENSER(3999999999L, 999999);
	
	private long initialCost;
	private double cookiesPerSecond;
	
	/**
	 * Creates a building
	 * @param initalCost The amount of a cookies that a building initially costs
	 * @param cookiesPerSecond The amount of cookies this building generates per second.
	 */
	Building( long initalCost, double cookiesPerSecond ) {
		this.initialCost = initalCost;
		this.cookiesPerSecond = cookiesPerSecond;
	}
	
	public long getIntialCost() {
		return this.initialCost;
	}
	
	public double getCookiesPerSecond() {
		return this.cookiesPerSecond;
	}
}
