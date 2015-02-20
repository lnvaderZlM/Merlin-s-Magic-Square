package com.example.merlinsmagicsquare;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

/**
 * drawing class for Merlin's Magic Square
 * 
 * @author Micah Alconcel
 * @version 17 September 2013
 */
public class MySurfaceView extends SurfaceView {

	public static boolean[] initDisplay;
	//ON = true, OFF = false
	private static final boolean ON = true;
	private static final boolean OFF = false;

	public static int vicCount = 0;

	static int countPresses;

	static Paint onSquare = new Paint();
	//	onSquare.setColor(Color.onSquare);
	static Paint darkOnSquare = new Paint();
	//	darkonSquare.setColor(0xff8b0000);

	static Paint offSquare = new Paint();

	static Paint darkOffSquare = new Paint();

	//the color values for the seek bars.
	public static int redVal = 255;
	public static int greenVal = 0;
	public static int blueVal = 0;

	//checks whether or not these shapes have been formed
	//so that if you make the same shape twice, the score doesn't go up
	public static boolean did0 = false;
	public static boolean did4 = false;
	public static boolean did7 = false;
	public static boolean did1 = false;

	public MySurfaceView(Context context) {
		super(context);
		init();
		// perform common initialization
	}// ctor


	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// perform common initialization
	}// ctor

	private void init() {
		initDisplay = new boolean[9];
		//sets each element of the initDisplay array randomly to either true or false (ON or OFF)
		onSquare.setARGB(255,redVal,greenVal,blueVal);
		darkOnSquare.setColor(0xff8b0000);			
		darkOffSquare.setColor(0xff00008B);
		offSquare.setColor(Color.BLUE);
		for(int i = 0;i<initDisplay.length;i++)
		{
			int onOff = (int)(Math.random()*2);
			if (onOff == 0)
			{
				initDisplay[i]=ON;
			}
			else
			{
				initDisplay[i]=OFF;
			}
		}
	}


	/*
	 * draws pretty much everything
	 * @param g Canvas - the canvas on which everything gets drawn
	 */
	@Override
	public void onDraw(Canvas g)
	{
		onSquare.setARGB(200,redVal,greenVal,blueVal);
		Paint black = new Paint();
		black.setColor(Color.BLACK);
		g.drawRect(0, 0,1280,1280,black);
		checkForWin();
		//draws either a onSquare rectangle or offSquare rectangle, depending on whether or not the element in the array
		//is either on or off.
		//0 = top left, 1 = middle left, 2 = bottom left, 3 = top middle, etc...
		for(int i = 0;i<initDisplay.length;i++)
		{
			if (initDisplay[i]==ON)
			{
				rectOns(g,i);
			}
			else
			{
				rectOffs(g,i);
			}
		}
		invalidate();
	}

	/*
	 * @param
	 * @g Canvas the drawing method
	 * @which int the square in question
	 */
	public void rectOns(Canvas g, int which)
	{

		switch(which)
		{
		case 0:
			g.drawRect(0,0,180,180,darkOnSquare);
			g.drawRect(5, 5,175,175,onSquare);
			break;
		case 1:
			g.drawRect(0, 180,180,360,darkOnSquare);
			g.drawRect(5, 185, 175, 355, onSquare);
			break;
		case 2:
			g.drawRect(0,360,180,540,darkOnSquare);
			g.drawRect(5, 365, 175, 535,onSquare);
			break;
		case 3:
			g.drawRect(180,0,360,180,darkOnSquare);
			g.drawRect(185, 5, 355,175,onSquare);
			break;
		case 4:
			g.drawRect(180,180,360,360,darkOnSquare);
			g.drawRect(185, 185, 355, 355, onSquare);
			break;
		case 5:
			g.drawRect(180,360,360,540,darkOnSquare);
			g.drawRect(185, 365, 355, 535, onSquare);
			break;
		case 6:
			g.drawRect(360,0,540,180,darkOnSquare);
			g.drawRect(365, 5, 535, 175,onSquare);
			break;
		case 7:
			g.drawRect(360,180, 540,360,darkOnSquare);
			g.drawRect(365, 185, 535, 355,onSquare);
			break;
		case 8:
			g.drawRect(360, 360,540, 540,darkOnSquare);
			g.drawRect(365, 365, 535, 535,onSquare);
			break;
		default:		
			break;
		}
	}

	//go up one method
	public void rectOffs(Canvas g, int which)
	{
		switch(which)
		{
		case 0:
			g.drawRect(0,0,180,180,darkOffSquare);
			g.drawRect(5, 5,175,175,offSquare);
			break;
		case 1:
			g.drawRect(0, 180,180,360,darkOffSquare);
			g.drawRect(5, 185, 175, 355, offSquare);
			break;
		case 2:
			g.drawRect(0,360,180,540,darkOffSquare);
			g.drawRect(5, 365, 175, 535,offSquare);
			break;
		case 3:
			g.drawRect(180,0,360,180,darkOffSquare);
			g.drawRect(185, 5, 355,175,offSquare);
			break;
		case 4:
			g.drawRect(180,180,360,360,darkOffSquare);
			g.drawRect(185, 185, 355, 355, offSquare);
			break;
		case 5:
			g.drawRect(180,360,360,540,darkOffSquare);
			g.drawRect(185, 365, 355, 535, offSquare);
			break;
		case 6:
			g.drawRect(360,0,540,180,darkOffSquare);
			g.drawRect(365, 5, 535, 175,offSquare);
			break;
		case 7:
			g.drawRect(360,180, 540,360,darkOffSquare);
			g.drawRect(365, 185, 535, 355,offSquare);
			break;
		case 8:
			g.drawRect(360, 360,540, 540,darkOffSquare);
			g.drawRect(365, 365, 535, 535,offSquare);
			break;
		default:
			break;
		}
	}

	/*
	 * switches one square from on to off
	 * @param idx int - which element of the array was touched, which decides which squares will be switched
	 */
	public static void switchOnOff (int idx)
	{
		//since the touched square will always be switched
		switchOne(idx);

		//the sides:
		//if user hits one of the side squares, the squares above and below it are switched.
		if(idx==1|idx==7)
		{
			switchOne(idx+1);
			switchOne(idx-1);
		}

		//the corners:
		//if user hits one of the corners, each adjacent square gets switched, including diagonal.
		if(idx==0|idx==6)
		{
			switchOne(idx+1);
			switchOne(3);
			switchOne(4);
		}		
		if(idx==8|idx==2)
		{
			switchOne(5);
			switchOne(idx-1);
			switchOne(4);
		}

		//the center square:
		//squares in a cross formation get switched
		if(idx==4)
		{
			switchOne(1);
			switchOne(3);
			switchOne(5);
			switchOne(7);
		}


		//the top middle and top bottom:
		//when user hits one of these, the one to the left and right get switched
		if(idx==5|idx==3)
		{
			switchOne(idx+3);
			switchOne(idx-3);
		}
		countPresses++;
		
		//if idx = 10, that means you pressed the auto win button to solve it for you! (also known as CHEATING!)
		if(idx==10)
		{
			countPresses = countPresses + 100;
		}
		checkForWin();
	}

	/*
	 * The method that actually switches the square on or off.
	 * "idx" is what the thingy is.
	 * If it's 10, that means the user has pressed the autowin button.
	 */
	public static void switchOne (int idx)
	{
		if(idx==10)
		{
			
		}
		
		else if(initDisplay[idx]==ON)
		{
			initDisplay[idx]=OFF;
		}
		else
		{
			initDisplay[idx]=ON;
		}	
	}
	
	/*
	 * Checks if the display meets any of the victory conditions.
	 * A point is gained when a 4, 7, 0, or 1 is formed.
	 * No points are gained for repeatedly making these shapes, though.
	 * @return boolean whether or not the user has completed a configuration
	 */

	public static boolean checkForWin()
	{
		boolean outerRing = ON;
		//checking for the first case: a 0.
		for(int i = 0;i<initDisplay.length;i++)
		{
			if((i!=4)&(initDisplay[i]==OFF))
			{
				outerRing = OFF;
			}
		}

		if((initDisplay[4]==OFF)&&(outerRing==ON))
		{
			if(did0==false)
			{
				did0 = true;
				vicCount++;
				return true;
			}
		}

		//checking for the second case: a 7
		if((initDisplay[0]==ON) && (initDisplay[1]==OFF) && (initDisplay [2] == OFF) && (initDisplay[3] == ON) && (initDisplay [4] == OFF) &&
				(initDisplay [5] == OFF) && (initDisplay[6] == ON) && (initDisplay [7]==ON) && (initDisplay[8]==ON))
		{
			if(did7 == false)
			{
				did7 = true;
				vicCount++;
				return true;
			}
		}

		//check for the third case: a 4
		if((initDisplay[0]==ON) && (initDisplay[1]==ON) && (initDisplay [2] == OFF) && (initDisplay[3] == OFF) && (initDisplay [4] == ON) &&
				(initDisplay [5] == OFF) && (initDisplay[6] == ON) && (initDisplay [7]==ON) && (initDisplay[8]==ON))
		{
			if(did4 == false)
			{
				did4 = true;
				vicCount++;
				return true;
			}
		}

		//checking for the third case: a 1
		if((initDisplay[0]==ON) && (initDisplay[1]==OFF) && (initDisplay [2] == ON) && (initDisplay[3] == ON) && (initDisplay [4] == ON) &&
				(initDisplay [5] == ON) && (initDisplay[6] == OFF) && (initDisplay [7]==OFF) && (initDisplay[8]==ON))
		{
			if(did1 == false)
			{
				did1 = true;
				vicCount++;
				return true;
			}
		}
		return false;
	}

	/*
	 * Called when the "reset" button is hit.
	 * Basically resets everything to factory default settings and stuff.
	 */
	public static void restart()
	{
		vicCount = 0;
		did0 = false;
		did4 = false;
		did7 = false;
		did1 = false;
		countPresses = 0;
		initDisplay = new boolean[9];
		//sets each element of the initDisplay array randomly to either true or false (ON or OFF)
		for(int i = 0;i<initDisplay.length;i++)
		{
			int onOff = (int)(Math.random()*2);
			if (onOff == 0)
			{
				initDisplay[i]=ON;
			}
			else
			{
				initDisplay[i]=OFF;
			}
		}
	}



}
