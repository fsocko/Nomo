package nomoApp;

/*Compatibility updates and development to Nomogram Application designed by R. Camara,
 *A. Marrero, and Professor Thomas Jones, University of Rochester, NY.
 * 
 * V1.1 (2016): Code refactorisation, updates for J8 compatibility. (Filip Socko)
 */

import java.awt.Dimension;

import javax.swing.JFrame;

import com.sun.javafx.tk.Toolkit;

public class NomoFrame {

	public static void main(String[] args) {
		  JFrame f = new JFrame();
		  f.addWindowListener(new java.awt.event.WindowAdapter() {
		       public void windowClosing(java.awt.event.WindowEvent e) {
		    	   System.exit(0);
		       };
		     });

		  NomoOrg2 nomo = new NomoOrg2();
		  
		  f.add(nomo);
		  nomo.setSize(800,800); // same size as defined in the HTML APPLET	  
		  nomo.setVisible(true);
		  f.setVisible(true);
		  f.pack();
		  nomo.init();
		  }
}

	