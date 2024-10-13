// Lynn Tao
// May 10, 2024
// Finds the original order of cards that allows you to make it in numerical order after shuffling it in a certain pattern
// Go to line 25 to adjust the zoom if the window is to big/small for your screen!!!

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import javax.swing.*;

public class Problem3 extends JPanel implements Runnable, MouseListener, MouseMotionListener {

	JPanel ogDeck, newDeck;
	Image background1, background2, background3, background4, placeholder, offScreenImage;

	Graphics offScreenBuffer;
	JTextField t;
	JButton b;

	ArrayList<Image> bottomCards;
	Deque <Image> currentPile;

	static double scale = 0.8; // number from 0-1 for the scale of the zoom (but keep it more than 0.7 for good graphics!!!)
	static int numCards = 0;
	
	// length and width of each card
	final static int LEN = 120;
	final static int WID = 80;
	
	// how much to change x and y has changed from original position
	static double moveX = 0;
	static double moveY = 0;

	// horizontal/vertical movement needed to shift card to correct position
	static int diffX = 0;
	static int diffY = 0;
	
	// number of slots filled on bottom
	static int filled = 0;

	// number of moves
	static int moveCount = 0;

	// location of top card in file
	static int pileX = 0;
	static int pileY = 0;

	// x values of each of the bottom cards
	static int[] bottomX;

	// whether card moving is in progress
	static boolean moving = false;
	
	// background
	static int backgroundOption = 1;

	public Problem3() {
		
		bottomCards = new ArrayList<>();
		
		// load background images
		background1 = Toolkit.getDefaultToolkit ().getImage ("images/background.png");
		background2 = Toolkit.getDefaultToolkit ().getImage ("images/background2.png");
		background3 = Toolkit.getDefaultToolkit ().getImage ("images/background3.png");
		background4 = Toolkit.getDefaultToolkit ().getImage ("images/background4.png");
		placeholder = Toolkit.getDefaultToolkit ().getImage ("images/placeholder.png");
		resetCards();

		// size and location
		setPreferredSize(new Dimension((int)(scale*1600), (int)(scale*960)));
		setLocation(0, 0);

		// mouse listeners
		addMouseListener(this);
		addMouseMotionListener(this);

		// thread for animation movements
		Thread thread = new Thread(this);
		thread.start();

	}

	// Description: does the graphics you see on the window
	// Parameters: Graphics g
	// Return: void
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// set up the offscreen buffer for less flickering
		if (offScreenBuffer == null)
		{
			offScreenImage = createImage (this.getWidth (), this.getHeight ());
			offScreenBuffer = offScreenImage.getGraphics ();
		}

		// set up length/width of the card with the scale
		int length = (int)(scale*Math.min(LEN*10.0/numCards, LEN));
		int width = (int)(scale*Math.min(WID*10.0/numCards, WID));

		// original background
		if (backgroundOption == 1) {
			offScreenBuffer.drawImage(background1, 0, 0, (int)(scale*1600), (int)(scale*960), this);
		}

		// hover on first button background
		else if (backgroundOption == 2){
			offScreenBuffer.drawImage(background2, 0, 0, (int)(scale*1600), (int)(scale*960), this);
		}

		// hover on second button background
		else if (backgroundOption == 3){
			offScreenBuffer.drawImage(background3, 0, 0, (int)(scale*1600), (int)(scale*960), this);
		}
		
		// hover on third button background
		else {
			offScreenBuffer.drawImage(background4, 0, 0, (int)(scale*1600), (int)(scale*960), this);
		}

		
		
		// BOTTOM ROW OF CARDS
		
		// x and y values of each card
		int x = (int)(scale*30);
		int y = (int)(scale*800);

		
		for (int i = 0; i < numCards; i++) {
			offScreenBuffer.drawImage(bottomCards.get(i), x, y, length, width, this);
			bottomX[i] = x;			
			x += (int)(scale*Math.min(200, 1650.0/(numCards+2)));

		}

		
		// ORIGINAL PILE OF CARDS
		
		// x and y values of each card
		x = (int)(scale*30+ filled*5);
		y = (int)(scale*(700-numCards*Math.min(70, 750.0/(numCards+2))+ filled * Math.min(70, 750.0/(numCards+2))));
		
		int count = 0;
		
		// make copy so no concurrent exception!
		Deque<Image> pile = new LinkedList<>(currentPile);
		
		for (Image i : pile) {
			count++;

			// if not the top card in the pile
			if (count != currentPile.size()) {
				offScreenBuffer.drawImage(i, x, y, length, width, this);
				x += (int)(scale*5);
				y += (int)(scale*Math.min(70, 750.0/(numCards+2)));
			}

		}

		// for the top card in the pile:
		pileX = x;
		pileY = y;

		// not moving -> display in normal position
		if (!moving) {
			offScreenBuffer.drawImage(currentPile.peekLast(), x, y, length, width, this);
		}

		// moving -> display in where it should be
		else {
			offScreenBuffer.drawImage(currentPile.peekLast(), pileX+(int)Math.round(moveX), pileY+(int)Math.round(moveY), length, width, this);
		}

		// put off screen buffer onto g
		g.drawImage (offScreenImage, 0, 0, this);
		

	}

	public static void main(String[] args) {

		JFrame frame = new JFrame("Cards!");

		Problem3 p = new Problem3();
		p.loadPile(); // load the cards (0 by default though)

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(p);
		frame.pack();
		frame.setVisible(true);


	}

	// Description: resets all values to default when numCards is reset
	// Parameters: none
	// Return: void, but changes the global vars
	public void reset() {
		moveX = 0;
		moveY = 0;

		diffX = 0;
		diffY = 0;
		filled = 0;

		pileX = 0;
		pileY = 0;

		moveCount = 0;
		
		bottomX = new int[numCards];

		moving = false;
	}

	// Description: loads the cards into the currentPile deque
	// Parameters: none
	// Return: none, but modifies currentPile
	public void loadPile() {
		// load all cards into arraylist
		ArrayList<Image> cards = new ArrayList<>();

		for (int i = numCards; i >= 1; i--) {
			cards.add(Toolkit.getDefaultToolkit ().getImage ("images/card" + i + ".png"));
		}

		// put the cards into the deque
		currentPile = new LinkedList<>();

		for (Image i : cards) {
			currentPile.offerLast(i); // add card to last pos
			currentPile.offerLast(currentPile.pollFirst()); // move first card in pile to bottom
		}
		
		// undo the moving for the last card
		currentPile.offerFirst(currentPile.pollLast());

	}

	// Description: resets the "sorted" cards at the bottom of the screen to all be placeholders
	// Parameters: none
	// Return: none, but modifies bottomCards
	public void resetCards() {
		bottomCards.removeAll(bottomCards);
		bottomX = new int[numCards];
		for (int i = 0; i < numCards; i++) {
			bottomCards.add(Toolkit.getDefaultToolkit().getImage("images/placeholder.png"));
		}
	}

	// Description: dialog box that asks for number of cards
	// Parameters: none
	// Return: int that represents the valid number entered (-1 to exit)
	public int showDialog () {
		int numCards = 0;
		while(true) {
			String input = JOptionPane.showInputDialog(this, "Enter number of cards");

			// not empty or null
			if (input != null && input.length() != 0) {
				try {
					numCards = Integer.parseInt(input);
					
					// out of range
					if (numCards <= 0 || numCards > 25) {
						throw new NumberFormatException();
					}
					break;
				}

				catch (NumberFormatException e) {

				}
			}

			// cancel/x clicked -> return -1
			else {
				return -1;
			}
		}
		return numCards;
	}

	// Description: updates variables when a card is moved
	// Parameters: none
	// Return: none (void)
	public void moveCard() {
		moveCount++;
		if (moveCount %2 == 1) {
			filled++;
		}
		moving = true;
	}

	// Description: overriden for the MouseListener, controls what happens when user clicks on screen
	// Parameters: MouseEvent e is the mouse action that occured
	// Return: none (void) but calls other methods and changes stuff
	public void mouseClicked(MouseEvent e) {
		// x and y pos of where mouse was clicked
		int x = e.getX();
		int y = e.getY();

		// move button
		if (x >= (int)(scale*890) && x <= (int)(scale*1520) && y >= (int)(scale*30) && y <= (int)(scale*230)) {
			// move the card if it is not already moving and slots haven't been all filled
			if (!moving && filled < numCards)
				moveCard(); 
		}

		// new button
		else if (x >= (int)(scale*890) && x <= (int)(scale*1520) && y >= (int)(scale*270) && y <= (int)(scale*470)) {
			// ask for user input for number of cards
			int temp = showDialog();

			// if user inputs -1, do nothing
			if (temp != -1) {
				// set numCards to the input and reset everything
				numCards = temp;
				this.reset();
				this.resetCards();
				this.loadPile();

				this.removeAll();
				this.repaint();
			}
		}
		
		// exit button
		else if (x >= (int)(scale*890) && x <= (int)(scale*1520) && y >= (int)(scale*505) && y <= (int)(scale*705)) {
			System.exit(0);
		}



	}

	@Override
	public void mousePressed(MouseEvent e) {
		//		System.out.println(e.getY());

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//		System.out.println("e");
		//		getNumCards();


	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// Description: used for the thread 
	// Parameters: none
	// Return: none (void) but controls what happens each time thread is gone through
	public void run() {
		while(true) {
			// if card moving is in progress
			if (moving) {
				// moving card to bottom
				if (moveCount %2 == 1) {

					// how much in x and y direction card needs to move to next slot
					diffX = bottomX[filled-1] - pileX;
					diffY = (int)(scale*800) - pileY;

					// angle card needs to move at
					double angle = Math.atan2(diffY*1.0, diffX*1.0);

					// speeds in x and y direction that card needs to move
					double xv = scale*16.0*Math.cos(angle);
					double yv = scale*16.0*Math.sin(angle);

					// adjust how much card moves in those directions
					moveX += xv;
					moveY += yv;
					
					// if reach bottom of screen -> stop the movement and set the bottomCards to be the actual card instead of placeholder
					if (moveY >= diffY) {
						moving = false;
						bottomCards.set(filled-1, currentPile.pollLast());
						moveX = 0;
						moveY = 0;
					}
				}

				// moving card to end of deck
				else {
					
					// how much in x and y direction card needs to move to next slot
					diffX = ((int)(scale*-5)) * (numCards-filled-1);
					diffY = -(int)(scale*(Math.min(70, 750/(numCards+2)) * (numCards-filled-1)));
					
					// angle card needs to move at
					double angle = Math.atan2(diffY*1.0, diffX*1.0);

					// speeds in x and y direction that card needs to move
					double xv = scale*8.0*Math.cos(angle);
					double yv = scale*8.0*Math.sin(angle);

					// adjust how much card moves in those directions
					moveX += Math.round(xv);
					moveY += Math.round(yv);
					
					// if reach end of deck -> stop the movement and move the first card in pile to the end
					if (moveY <= diffY) {
						moving = false;
						currentPile.offerFirst(currentPile.pollLast());
						moveX = 0;
						moveY = 0;
					}
				}
				repaint();
			}

			try {
				Thread.sleep(9); // refreshes every approx. 1/100 of a second
			} 
			catch(Exception e) {

			}
		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// Description: overriden from MouseMotionListener to control what happens when user moves mouse
	// Parameters: MouseEvent e is the mouse action that occured
	// Return: none (void)
	public void mouseMoved(MouseEvent e) { 
		// get x and y positions of mouse
		int x = e.getX();
		int y = e.getY();

		// hovered over move button
		if (x >= (int)(scale*890) && x <= (int)(scale*1520) && y >= (int)(scale*30) && y <= (int)(scale*230)) {
			backgroundOption = 2;
		}

		// hovered over new button
		else if (x >= (int)(scale*890) && x <= (int)(scale*1520) && y >= (int)(scale*270) && y <= (int)(scale*470)) {
			backgroundOption = 3;
		}
		
		// hovered over exit button
		else if (x >= (int)(scale*890) && x <= (int)(scale*1520) && y >= (int)(scale*505) && y <= (int)(scale*705)) {
			backgroundOption = 4;
		}
		
		// not hovered over any button
		else {
			backgroundOption = 1;
		}
		
		repaint();
	}

}