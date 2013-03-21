/**
 * a program meant to create Atari's breakout game
 * @author Abdirahman Wehelie
 */
import java.awt.Color;
import java.awt.event.KeyEvent;

import acm.graphics.*;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;


@SuppressWarnings("serial")
public class BreakoutFinal extends GraphicsProgram {
	//Applet height/width
	private static int APP_WIDTH=400;
	private static int APP_HEIGHT=600;
	//Specs for the brick
	private static int NUM_COl=10;
	private static int NUM_ROW=10;
	private static int SEP_BRICK=4;
	private static int Br_HEIGHT=8;
	private static int Br_WIDTH= APP_WIDTH/NUM_COl-SEP_BRICK;
	private static int BRICK_Y_OFFSET=70;
	//Specs for the ball
	private static int BALL_X=APP_WIDTH/2;
	private static int BALL_Y=APP_HEIGHT/2;
	private int BALL_RADIUS=10;
	//Specs for the paddle
	private static int PADDLE_Y_OFFSET=30;
	private static int PADDLE_WIDTH=60;
	private static int PADDLE_HEIGHT=10;
	//public class objects
	private static GRect paddle;
	private GObject collider;
	private static GOval ball;
	
	//number of bricks and game turns
	public int Num_BRICKS=NUM_ROW*NUM_COl;
	public int Num_Turns=4;
	public GLabel Youwin,ballDropped,loser,Turns;
	//boolean for moveball loop
	boolean stillMove=true;
	//Velocity variables
	private double vx,vy;
	boolean nextBoolean = true;
	//random generator
	private RandomGenerator rgen = new RandomGenerator();

	public void run()
	{
	//Method create the game
		createGame();
	//Method starts the game	
		playGame();
	}

	public void createGame(){
		
		//set the applet size
		setSize(APP_WIDTH,APP_HEIGHT);
		//call the first method which builds the bricks
		brickMaking();
		//call the second method which builds the ball
		paddleMaking();
		//call the third method which builds the paddle
		ballMaking();
		//Key listeners for the left/right direction		
		addKeyListeners();
							
	}

	public void brickMaking() 
	{
		// I mainly copied the below code.
		//I do not fully understand the math behind the loops
		
		
		for (int row=0; row<NUM_ROW; row++)
				{
					int ycoord = BRICK_Y_OFFSET+row*(Br_HEIGHT+SEP_BRICK);
					
			for (int col=0; col<NUM_COl; col++)
					{
						
						int xcoord= getWidth() -NUM_ROW/2*(Br_WIDTH+SEP_BRICK)+col*(Br_WIDTH+SEP_BRICK);
						GRect rect = new GRect (xcoord,ycoord,Br_WIDTH,Br_HEIGHT);
						Color[] colors = {Color.RED, Color.orange, Color.yellow, Color.GREEN, Color.BLUE,};
						xcoord+=SEP_BRICK+Br_WIDTH;
						rect.setFilled(true);
						rect.setColor(colors[row/2]);
						add(rect);
					}
				}

	}

	public void ballMaking() {
		
		// created an oval and placed it on the center of the applet 
		ball = new GOval(BALL_X,BALL_Y , BALL_RADIUS*1.6, BALL_RADIUS*1.6);
		ball.setFilled(true);
		ball.setColor(Color.BLACK);
		add(ball);
		
	}
	
	public void paddleMaking() 
	{
		//created a black paddle using given height/width
		paddle = new GRect(APP_WIDTH / 2 - PADDLE_WIDTH/2, APP_HEIGHT - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setColor(Color.RED);
		add(paddle);
		
	}
	
	private void playGame() {
		// call the setup method to display number of turns
		setTurns();
		waitForClick();
		 //setup velocity
		vx=rgen.nextDouble(2.6, 3.0);
			vy=3;
					
		if (rgen.nextBoolean())
			{	
			vx=-vx;
			}	
	
		//use while loop to move ball and remove bricks	
		while(stillMove){
			//call the moveball method
			moveBall();
			//call the wallimpact method
			wallImpact(); 
			//call the objectimpact method
			comparePaddleImpact();
			pause(20);
		}
		 
	}
	
	public void setTurns() {
		
		 //The counter for the # of turns doesn't work 
		Turns = new GLabel("You have" + Num_Turns+"turns left" +".");
		Turns.setLocation(0,0);
        add(Turns);
        pause(100 );
        remove(Turns);
        
        
		
		 if (Num_Turns==0)
		 {
			loser = new GLabel("You lost.Thank!");
			loser.setLocation(APP_WIDTH/2 - loser.getWidth()/2, APP_HEIGHT/2 - 2 * BALL_RADIUS);
			add(loser);
			pause(1000);
			remove(loser);
			remove(ball);;
			}
	
		}
	
	public void moveBall() 
	{
				
		//move the ball
		ball.move(vx,vy);	
			
		}

	public void wallImpact()
	{
		
		//this method makes sure the ball bounces off the wall. 
		
			if (ball.getX() + (2 * BALL_RADIUS) >= getWidth()) 
			{
				vx = - vx;
				}
	 				 	
				else if (ball.getX() <= 0){
	 			vx =  Math.abs(vx);
				}
			
				else if (ball.getY() >= getHeight())
				{
				vy = - Math.abs(vy);
				}
				else if (ball.getY() <= 0)
				{
				vy =  Math.abs(vy);
				}
			//if it hits the bottom, minus a turn and send a message to user
				else if (ball.getY() + (2 * BALL_RADIUS) > APP_HEIGHT) 
		    	    {		
	    			 Num_Turns--;
	    			 ballDropped = new GLabel("You missed it.");
	    	         ballDropped.setLocation(APP_WIDTH/2 - ballDropped.getWidth()/2, APP_HEIGHT/2 - 2 * BALL_RADIUS);
	    	         add(ballDropped);
	    	         pause(1000);
	    	         remove(ballDropped);
	    	         ball.setLocation(APP_WIDTH/2 - BALL_RADIUS, APP_HEIGHT/2 - BALL_RADIUS);
	    	         playGame();
	    	         println ("number of turns"+Num_Turns);	
		      		}
		
				}		
			
	public void comparePaddleImpact() 
	{
		// This method make sure that if the ball hit the paddle it bounces
		// If it is a brick, then it is removed
		
		
		collider= getCollidingObject();
			
		if (collider != paddle && collider !=null)
		{
			 remove(collider);
			 
             vy = -vy;
             Num_BRICKS--;
             
             if (Num_BRICKS==0)
     			{
             	Youwin = new GLabel ("You Won!!");
     			Youwin.setLocation(APP_WIDTH/2 - Youwin.getWidth()/2, APP_HEIGHT/2 - 2 * BALL_RADIUS);
     			add(Youwin);
     			remove(Youwin);
     			remove(ball);
     			}
             println ("bricks"+Num_BRICKS);
		}
		else if(collider == paddle) 
        {          
        	vy = - vy;
        }
	}	
	
	private GObject getCollidingObject() {
		// This method make sure that the coordinate of the collection it recorder
		//and send to paddleimpact method
		collider = getElementAt(ball.getX(),ball.getY());
				
		 if(getElementAt(ball.getX(),ball.getY()) != null)
		  return getElementAt(ball.getX(),ball.getY());
		 else if(getElementAt(ball.getX() + (2*BALL_RADIUS) , ball.getY())!= null)
		 return (getElementAt(ball.getX() + (2*BALL_RADIUS) , ball.getY()));
		 else if (getElementAt(ball.getX(), ball.getY() + (BALL_RADIUS *2)) != null)
		 return (getElementAt(ball.getX(), ball.getY() + (BALL_RADIUS *2)));
		 else if (getElementAt(ball.getX() + (BALL_RADIUS *2), ball.getY() + BALL_RADIUS *2) != null)
		return (getElementAt(ball.getX() + (BALL_RADIUS *2), ball.getY() + BALL_RADIUS *2));
				 
		return null;
        	
	}
	
	public void keyPressed(KeyEvent e){
		// This method sets the left/right direction for paddle and speed 
		switch(e.getKeyCode()){
		
			case KeyEvent.VK_LEFT:
				if (paddle.getX() - SEP_BRICK+10 >= 0)
					paddle.move(-30,0);
			break;
							
			case KeyEvent.VK_RIGHT:
				if (paddle.getX() + (PADDLE_WIDTH + SEP_BRICK)-10 <= getWidth())
					paddle.move(30, 0);
			break;
			
			default:
			return;
			
		}

	
	}
}

