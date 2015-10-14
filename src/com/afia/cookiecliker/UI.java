package com.afia.cookiecliker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class UI extends JFrame implements ActionListener, ListSelectionListener {

	//Final variables
	private static final long serialVersionUID = 4627286953421658112L;
	private static final int WIDTH = 350;
	private static final int HEIGHT = 300;

	//Objects
	private static Clicker cc = new Clicker();

	//Components
	private JList<String> buildingSelection;
	private static JTextArea statsDisplay;
	private JButton generateCookies;
	private BufferedImage img;

	/**
	 * Intializes the game
	 */
	public UI() {
		super( "Cookie Clicker" );
		initComponents();
		initFrame();

		cc.start();
	}

	/**
	 * Creates an instance of this GUI and starts the updater thread
	 */
	public static void main(String[] args) {		
		@SuppressWarnings("unused")
		UI ccg = new UI();

		Updater updater = new Updater();
		updater.start();
	}



	/**
	 * Purchases the selected building
	 */
	private void attemptPurchase() {
		String buildingWithNumbers = this.buildingSelection.getSelectedValue();
		String buildingWithoutNumbers = buildingWithNumbers.substring( 0, buildingWithNumbers.indexOf("-") - 1 );
		Building building = Building.valueOf( buildingWithoutNumbers );

		boolean b = cc.purchaseBuilding( building );
		if( b ) {
			JOptionPane.showMessageDialog( this, "You have purchased a " + building.toString() + "!", 
					"Successfully purchased!", JOptionPane.INFORMATION_MESSAGE );
		}
		else {
			JOptionPane.showMessageDialog( this, "You do not have enough cookies to purchase this building!", 
					"Not enough cookies!", JOptionPane.INFORMATION_MESSAGE );
		}
	}

	/**
	 * Refreshes the list model
	 * @return The refreshed model
	 */
	private DefaultListModel<String> refreshModel() {
		DefaultListModel<String> model = new DefaultListModel<String>();

		for( Building b : Building.values() ) {
			model.addElement( b.toString() + " - " + cc.getBuildingCount( b ) );
		}

		return model;
	}

	/**
	 * Sets up the components
	 */
	private final void initComponents() {
		//Image
		loadImage();
		this.generateCookies = new JButton( new ImageIcon( this.img ) );
		this.generateCookies.setBackground( Color.white );
		this.generateCookies.addActionListener( this );


		//Text area
		statsDisplay = new JTextArea( 5, 30 );
		statsDisplay.setText( "Cookies: 0" );
		statsDisplay.setEditable( false );

		//JList
		this.buildingSelection = new JList<String>( refreshModel() );
		this.buildingSelection.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		this.buildingSelection.setLayoutOrientation( JList.HORIZONTAL_WRAP );
		this.buildingSelection.setPreferredSize( new Dimension( 330, 100 ) );
		this.buildingSelection.addListSelectionListener( this );

		super.add( UI.statsDisplay );
		super.add( this.buildingSelection );
		super.add( this.generateCookies );
	}

	/**
	 * Loads the image from the web
	 */
	private void loadImage() {
		try {
			URL url = new URL("http://icons.iconarchive.com/icons/oxygen-icons.org/oxygen/64/Apps-preferences-web-browser-cookies-icon.png");
			this.img = ImageIO.read( url );
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog( this, "Invalid URL!", "Error!", JOptionPane.ERROR_MESSAGE );
		} catch (IOException e) {
			JOptionPane.showMessageDialog( this, "Error loading image!", "Error!", JOptionPane.ERROR_MESSAGE );
		}
	}

	/**
	 * Sets up the frame
	 */
	private final void initFrame() {
		super.setSize( WIDTH, HEIGHT );
		super.setResizable( false );
		super.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		super.setLocationRelativeTo( null );
		super.setLayout( new FlowLayout() );
		super.setVisible( true );
		super.getContentPane().setBackground( Color.WHITE );
	}

	/**
	 * Manages button actions
	 */
	@Override public void actionPerformed(ActionEvent ae) {
		if( ae.getSource() == this.generateCookies ) {
			cc.generateCookie();
		}
	}

	/**
	 * Manage list selection events
	 */
	@Override public void valueChanged(ListSelectionEvent lse) {
		if( lse.getSource() == this.buildingSelection ) {
			if( ! lse.getValueIsAdjusting() ) {
				if( ! this.buildingSelection.isSelectionEmpty() ) {
					attemptPurchase();
					this.buildingSelection.setModel( refreshModel () );
				}
			}
		}
	}

	/**
	 * Updates score
	 * @author Afia
	 *
	 */
	private static class Updater extends Thread implements Runnable {
		public void run() {
			while( true ) {
				statsDisplay.setText( "Cookies: " + cc.getAbsoluteCookieCount() );
				statsDisplay.append( System.lineSeparator() + "Cookies per second: " + cc.getCookiesPerSecond() );

				try {
					sleep( 100 );
				} 
				catch (InterruptedException e) {
					JOptionPane.showMessageDialog( null, "An error has occured with the runtime!", "Error!", JOptionPane.ERROR_MESSAGE );
					System.out.println( e );
					break;
				}
			}
		}
	}

}
