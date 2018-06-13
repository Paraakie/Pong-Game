import java.awt.event.KeyEvent;

/*
 * Sven Gerhards, 15031719
 * 159.103, Assignment 1
 */

/*
 * Structure from Course 159103, lab 4 where used for a majority of functions 
 */

/*
 * Basic Rules
 * Player 1 moves with W, S key
 * Player 2 moves with Up, Down key
 * Make the ball touch the opponent's side of the field to gain a point
 * Unlimited play
 */

public class Pong extends GameEngine{
	//Main Function - Creating a new instance of Pong
	public static void main(String args[]) {
		createGame(new Pong());
	}
	
	
	//Values for height, width of window; 
	//Used for the init function as height() and width() function don;t work in init
 	int real_width;
	int real_height;	
		
	
	//used for both players' paddles
	double paddle_width;
	double paddle_height;
	double paddle_vel; //velocity

	
	/* 
	 * Player1's Paddle 
	 */
	double p1_posX; //constant
	double p1_posY; 
	boolean p1_up, p1_down;
	
	
	
	public void updatePlayer1(double dt) {
		//Move up if W is pressed
		if(p1_up == true) {
			p1_posY -= paddle_vel * dt;
		}
		//Move down if S is pressed
		if(p1_down == true) {
			p1_posY += paddle_vel * dt;
		}
		//when it reaches top of the screen, go back to bottom
		if(p1_posY < 0) {
			p1_posY = 0;
		}
		//when it reaches bottom of the scrren, go to top
		if(p1_posY > height()-paddle_height) {
			p1_posY = height()-paddle_height;
		}
	}
	
	//Draw The Player1's Paddle	
	public void drawPlayer1() { 
		drawSolidRectangle(p1_posX, p1_posY, paddle_width, paddle_height);
	}

	/* 
	 * Player2's Paddle 
	 */
	
	double p2_posX; //constant
	double p2_posY; 
	boolean p2_up, p2_down;
	
	public void updatePlayer2(double dt) {
		//When Up is pressed; move Up
		if(p2_up == true) {
			p2_posY -= paddle_vel * dt;
		}
		//when Down is pressed; move down
		if(p2_down == true) {
			p2_posY += paddle_vel * dt;
		}
		//when it reaches top of the screen, go back to bottom
		if(p2_posY < 0) {
			p2_posY = 0;
		}
		//when it reaches bottom of the screen, go to top
		if(p2_posY > height()-paddle_height) {
			p2_posY = height()-paddle_height;
		}
	}
	
	public void drawPlayer2() {
		//Draw The Player 2 Paddle
		drawSolidRectangle(p2_posX, p2_posY, paddle_width, paddle_height);	
	}

	/*
	 * The Pong Ball
	 */
	
	double ball_posX;
	double ball_posY;
	
	double ball_angle; //constant, only velocity will be changed
	double ball_velX; //velocity for X
	double ball_velY; // velocity for Y
	double ball_rad; //constant
	
	//Seperate Function because this is used in other function, to avoid bugs
	public void resetBall() {
		if(ball_posX < 0 || ball_posX > width()) {
			ball_posX = width()/2;
			ball_posY = height()/2;
			
			ball_velY = -cos(ball_angle) * 250;
		}
	}
	
	public void updateBall(double dt) {
		//General Movement
		ball_posX += ball_velX *dt;
		ball_posY += ball_velY *dt;

		//Reset Ball position & velocity
		resetBall();
			
		//ball bounces of roof or floor
		if(ball_posY < ball_rad) {
				ball_velY *= -1;
		} else if(ball_posY > height()-ball_rad) {
				ball_velY *= -1;
		}
	}
	
	public void drawBall(){
		//Draw Pong Ball
		drawSolidCircle(ball_posX, ball_posY, ball_rad);
	}
	
	/*
	 * Score
	 */
	
	int scoreP1;
	int scoreP2;
	
	public void updateScore(double dt) {
		if(ball_posX < ball_rad/2) {
			scoreP2++;
		} 
		else if(ball_posX > width()-ball_rad/2) {
			scoreP1++;
		}
	}
	
	public void drawScore() {
		drawText(50, 50, ""+scoreP1, "Arial", 60);
		drawText(720, 50, ""+scoreP2, "Arial", 60);
	}

	@Override
	public void init() {
		//Booleans
		p1_up = false;
		p1_down = false;
		p2_up = false;
		p2_down = false;

		//Integers
		real_width = 800;
		real_height = 500;
		
		scoreP1 = 0;
		scoreP2 = 0;
		
		//Doubles
		paddle_width = 12;
		paddle_height = 80;
		paddle_vel = 300; //constant
		
		p1_posX = 0; //constant
		p1_posY = (real_height/2)-(paddle_height/2);
		p2_posX = real_width-12;
		p2_posY = (real_height/2)-(paddle_height/2);
		
		ball_posX = real_width/2; 
		ball_posY = real_height/2;
		
		ball_angle = 315;
		ball_velX = sin(ball_angle) * 250;
		ball_velY = -cos(ball_angle) * 250;
		
		ball_rad = 10; //constant
	}
	
	
	@Override 
	public void update(double dt) { //Auto-generated method stub by eclipse
		//Update Player1's Paddle;  based on Player Input (W, S)
		updatePlayer1(dt);
		
		//Update Player2's Paddle;  based on Player Input (Up, Down)
		updatePlayer2(dt);
		
		//Temporary values, that are used to make the if statements more readable
		double p1Hitbox_X = paddle_width + ball_rad;
		double p2Hitbox_X = width() - paddle_width - ball_rad;
		double p1Hitbox_Ybot = p1_posY + paddle_height;
		double p2Hitbox_Ybot = p2_posY + paddle_height;
		
		//Detect Collision of Ball & Player1's Paddle
		if(ball_posY > p1_posY && ball_posY < p1Hitbox_Ybot) { //Checks Y-Coordinate
			if(ball_posX < p1Hitbox_X) { //Checks X-Coordinate 
				ball_velX *= -1.05; //revert velocity and increase speed at the same time
				ball_posX = p1Hitbox_X; //Fixes a bug where the ball would get stuck in the paddle
			}	
		}
		//Detect Collision of Ball & Player2's Paddle
		if(ball_posY > p2_posY && ball_posY < p2Hitbox_Ybot) { //Checks Y-Coordinate
			if(ball_posX > p2Hitbox_X) { //Checks X-Coordinate
				ball_velX *= -1.05; //revert velocity and increase speed at the same time
				ball_posX = p2Hitbox_X; //Fixes a bug where the ball would get stuck in the paddle
			}
		}
		//Update Ball Movements
		updateBall(dt);
		
		//Update the Score
		updateScore(dt);
	}

	@Override 
	public void paintComponent() { //Auto-generated method stub by eclipse
		//Resize window, for more space for the ball to bounce
		setWindowSize(real_width, real_height);
		
		// Change the background to black
		changeBackgroundColor(black);
		clearBackground(width(), height());
		
		//Middle Line, to assist gameplay
		changeColor(red);
		drawLine(width()/2, 0, width()/2, height());
		
		/*
		 * Game Objects
		 */
		
		//Change White for all other visual opponents
		changeColor(white);
		
		//Draw  Pong Bars
		drawPlayer1();
		drawPlayer2();

		//Draw Pong Ball
		drawBall();
		
		//Draw Score
		drawScore();
		
	}
	
	// Called whenever a key is pressed
	public void keyPressed(KeyEvent e) {
		// Player1 pressed up arrow
		if(e.getKeyCode() == KeyEvent.VK_W) {
			// Move Player1 object UP
			p1_up = true;
		}
		// Player1 pressed down arrow
		if(e.getKeyCode() == KeyEvent.VK_S) {
			// Move Player1 object DOWN
			p1_down = true;
		}
		
		// Player2 pressed up arrow
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			// Move Player2 object UP
			p2_up = true;
		}
		// Player2 pressed down arrow
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			// Move Player2 object DOWN
			p2_down = true;
		}
	}

	// Called whenever a key is released
	public void keyReleased(KeyEvent e) {
		// Player1 released up arrow
		if(e.getKeyCode() == KeyEvent.VK_W) {
			// Record it
			p1_up = false;
		}
		// Player1 released up arrow
		if(e.getKeyCode() == KeyEvent.VK_S) {
			// Record it
			p1_down = false;
		}
		
		// Player2 released up arrow
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			// Record it
			p2_up = false;
		}
		// Player2 released up arrow
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			// Record it
			p2_down = false;
		}
	}
}