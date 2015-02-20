package com.example.merlinsmagicsquare;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Micah Alconcel
 * Enrichments:
 * Plays sound on touchy stuff
 * Different layout for portrait and landscape
 *
 */
public class MainActivity extends Activity {
	String name = "yar";
	boolean loaded = false;

	//creating the background music thingies
	BackgroundSounds mBackgroundSound;
	BackgroundSound2 mBackgroundSound2;
	BackgroundSound3 mBackgroundSound3;
	BackgroundSound4 mBackgroundSound4;
	BackgroundSound5 mBackgroundSound5;
	MediaPlayer player1;
	MediaPlayer levelplayer;
	int counter = 0;

	/*
	 * for the sound effect that plays when the player advances a level
	 * only plays once
	 */
	boolean hyper=false;
	boolean mega=false;
	boolean superr = false;
	boolean ultra = false;
	boolean extra = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			
		setContentView(R.layout.activity_main);

		//the SurfaceView that contains the 3x3 grid of squares
		final SurfaceView stuff = (SurfaceView) findViewById(R.id.mySurfaceView1);

		//the reset button
		Button reset = (Button) findViewById(R.id.resetButton);

		//seekbars: used to change the color of the ON squares.
		SeekBar redBar = (SeekBar) findViewById(R.id.redBar);
		SeekBar greenBar = (SeekBar) findViewById(R.id.greenBar);
		SeekBar blueBar = (SeekBar) findViewById(R.id.blueBar);
		redBar.setMax(255);
		redBar.setProgress(255);
		greenBar.setMax(255);
		greenBar.setProgress(0);
		blueBar.setMax(255);
		blueBar.setProgress(0);

		//the diagrams that show what shapes you're supposed to make
		final ImageView four = (ImageView) findViewById(R.id.four);
		final ImageView seven = (ImageView) findViewById(R.id.seven);
		final ImageView zero = (ImageView) findViewById(R.id.zero);
		final ImageView one = (ImageView) findViewById(R.id.one);



		redBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar skb, int prog, boolean fromUser) {
				// TODO Auto-generated method stub
				MySurfaceView.redVal = prog;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// do nothing, as everything I need it to do is in the progressChanged method

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//do nothing, as everything I need it to do is in the progressChanged method

			}

		});

		greenBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar skb, int prog, boolean fromUser) {
				MySurfaceView.greenVal = prog;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// do nothing, as everything I need it to do is in the progressChanged method

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// do nothing, as everything I need it to do is in the progressChanged method

			}

		});

		blueBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar skb, int prog, boolean fromUser) {
				MySurfaceView.blueVal = prog;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				//do nothing, as everything I need it to do is in the progressChanged method

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// do nothing, as everything I need it to do is in the progressChanged method

			}

		});

		//the autowin button - in case things get too hard.
		//I dunno, it takes me upwards of 300 presses to finish my own game lol.
		//one of my friends memorized how to make most of the shapes from a blank slate, so he does it in like, 30.
		Button autoWin = (Button) findViewById(R.id.autoWin);

		//the textviews that hold the count of how many shapes have been completed (wins) and the victory message
		final TextView wins = (TextView) findViewById(R.id.wins);
		final TextView victory = (TextView) findViewById(R.id.victory);

		//creating the actual background music thingies and setting their values (done in the class)
		player1 = MediaPlayer.create(MainActivity.this, R.raw.hypers);
		mBackgroundSound = new BackgroundSounds(player1);
		mBackgroundSound2 = new BackgroundSound2();
		mBackgroundSound3 = new BackgroundSound3();
		mBackgroundSound4 = new BackgroundSound4();
		mBackgroundSound5 = new BackgroundSound5();

		//plays a song depending on how many shapes the player has completed.
		//songs get more lively and active the farther the user is in the game
		switch(MySurfaceView.vicCount)
		{
		case 0:
			mBackgroundSound.execute();
			break;

		case 1:
			mBackgroundSound2.execute();
			break;
		case 2:
			mBackgroundSound3.execute();
			break;
		case 3:
			mBackgroundSound4.execute();
			break;
		}
		final TextView buttonPresses = (TextView) (findViewById(R.id.buttonPresses));

		//at the beginning, plays this sound.
		if(hyper==false)
		{
			hyper = true;
			levelplayer = MediaPlayer.create(MainActivity.this, R.raw.levelhyper);
			levelplayer.setLooping(false);
			levelplayer.start();
		}


		/*
		 * AutoWin automatically sets up the board in a configuration that hasn't been completed yet
		 * In order of priority: 0, 7, 1, 4 (tried to order them based on how difficult they are to make)
		 */
		autoWin.setOnClickListener(new View.OnClickListener(){
			public void onClick (View v)
			{
				if(MySurfaceView.did0==false)
				{
					MySurfaceView.initDisplay[0]=true;
					MySurfaceView.initDisplay[1]=true;
					MySurfaceView.initDisplay[2]=true;
					MySurfaceView.initDisplay[3]=true;
					MySurfaceView.initDisplay[4]=false;
					MySurfaceView.initDisplay[5]=true;
					MySurfaceView.initDisplay[6]=true;
					MySurfaceView.initDisplay[7]=true;
					MySurfaceView.initDisplay[8]=true;
					MySurfaceView.switchOnOff(10);
					zero.setVisibility(ImageView.INVISIBLE);
				}

				else if (MySurfaceView.did7==false)
				{
					MySurfaceView.initDisplay[0]=true;
					MySurfaceView.initDisplay[1]=false;
					MySurfaceView.initDisplay[2]=false;
					MySurfaceView.initDisplay[3]=true;
					MySurfaceView.initDisplay[4]=false;
					MySurfaceView.initDisplay[5]=false;
					MySurfaceView.initDisplay[6]=true;
					MySurfaceView.initDisplay[7]=true;
					MySurfaceView.initDisplay[8]=true;
					MySurfaceView.switchOnOff(10);
					seven.setVisibility(ImageView.INVISIBLE);
				}

				else if(MySurfaceView.did4==false)
				{
					MySurfaceView.initDisplay[0]=true;
					MySurfaceView.initDisplay[1]=true;
					MySurfaceView.initDisplay[2]=false;
					MySurfaceView.initDisplay[3]=false;
					MySurfaceView.initDisplay[4]=true;
					MySurfaceView.initDisplay[5]=false;
					MySurfaceView.initDisplay[6]=true;
					MySurfaceView.initDisplay[7]=true;
					MySurfaceView.initDisplay[8]=true;
					MySurfaceView.switchOnOff(10);
					four.setVisibility(ImageView.INVISIBLE);
				}
				else if(MySurfaceView.did1==false)
				{
					MySurfaceView.initDisplay[0]=true;
					MySurfaceView.initDisplay[1]=false;
					MySurfaceView.initDisplay[2]=true;
					MySurfaceView.initDisplay[3]=true;
					MySurfaceView.initDisplay[4]=true;
					MySurfaceView.initDisplay[5]=true;
					MySurfaceView.initDisplay[6]=false;
					MySurfaceView.initDisplay[7]=false;
					MySurfaceView.initDisplay[8]=true;
					MySurfaceView.switchOnOff(10);
					one.setVisibility(ImageView.INVISIBLE);

				}


				//to auto update the board and stuff.
				MySurfaceView.checkForWin();
				if(MySurfaceView.vicCount>3)
				{
					victory.setText("YOU'RE WINNER");
				}
				playSounds();
				wins.setText("Completed: " + MySurfaceView.vicCount);
				buttonPresses.setText("Buttons pressed: " + MySurfaceView.countPresses);
			}
		});

		/*
		 * Resets pretty much everythaaaang.
		 * stops all the music and sets them back to their default positions (0).
		 */
		reset.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				MySurfaceView.restart();
				buttonPresses.setText("Button Presses: "+ MySurfaceView.countPresses);
				wins.setText("Completed: "+ MySurfaceView.vicCount);
				victory.setText("");
				//on reset, the diagrams should all become visible again
				zero.setVisibility(ImageView.VISIBLE);
				four.setVisibility(ImageView.VISIBLE);
				seven.setVisibility(ImageView.VISIBLE);
				one.setVisibility(ImageView.VISIBLE);
				//stops all the music
				mBackgroundSound.foo();
				mBackgroundSound2.foo();
				mBackgroundSound3.foo();
				mBackgroundSound4.foo();
				mBackgroundSound5.foo();

				//resets all the "gates" to false
				hyper = false;
				mega = false;
				superr = false;
				ultra = false;
				extra = false;

				//tries to release the MediaPlayer
				//in case the sound was never started, catch the exception
				try
				{
					levelplayer.release();
				}
				catch(NullPointerException mpnotstarted)
				{

				}

				if(hyper==false)
				{
					hyper = true;
					levelplayer = MediaPlayer.create(MainActivity.this, R.raw.levelhyper);
					levelplayer.setLooping(false);
					levelplayer.start();
				}

				//recreate the background musics, then start the first track
				player1 = MediaPlayer.create(MainActivity.this, R.raw.hypers);
				mBackgroundSound = new BackgroundSounds(player1);
				mBackgroundSound2 = new BackgroundSound2();
				mBackgroundSound3 = new BackgroundSound3();
				mBackgroundSound4 = new BackgroundSound4();
				mBackgroundSound5 = new BackgroundSound5();
				mBackgroundSound.execute();

			}
		});
		stuff.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent e) {
				int spot = checkWhere(e.getX(),e.getY());
				switch(e.getAction())
				{
				case MotionEvent.ACTION_UP:
					/*
					 * plays a random sound effect when the button gets released.
						sound effect chosen depends on how far you are into the game.
						hyper means you're in the beginning;
						mega means you're in the middle;
						super means you're in the end
						in the actual game I'm basing this off of, it goes
						hyper, mega, super, ultra, extra
						but that'd be a lot more code lol.
						also, there's four different beats per progress level.
					 */


					int y = (int)(Math.random()*4);
					final MediaPlayer player;
					switch(y)
					{
					case 0:
						counter++;
						switch(MySurfaceView.vicCount)
						{
						case 0:
							player = MediaPlayer.create(MainActivity.this, R.raw.megabeata);
							break;
						case 1: case 2:
							player = MediaPlayer.create(MainActivity.this, R.raw.hyperbeata);
							break;
						default:
							player = MediaPlayer.create(MainActivity.this, R.raw.superbeata);
							break;
						}

						break;

					case 1:
						counter++;
						switch(MySurfaceView.vicCount)
						{
						case 0:
							player = MediaPlayer.create(MainActivity.this, R.raw.megabeatb);
							break;
						case 1:
							player = MediaPlayer.create(MainActivity.this, R.raw.hyperbeatb);
							break;
						default:
							player = MediaPlayer.create(MainActivity.this, R.raw.superbeatb);
							break;
						}
						break;
					case 2:
						counter++;
						switch(MySurfaceView.vicCount)
						{
						case 0:
							player = MediaPlayer.create(MainActivity.this, R.raw.megabeatc);
							break;
						case 1: case 2:
							player = MediaPlayer.create(MainActivity.this, R.raw.hyperbeatc);
							break;
						default:
							player = MediaPlayer.create(MainActivity.this, R.raw.superbeatc);
							break;
						}
						break;
					default:
						counter = 0;
						switch(MySurfaceView.vicCount)
						{
						case 0:
							player = MediaPlayer.create(MainActivity.this, R.raw.megabeatd);
							break;
						case 1: case 2:
							player = MediaPlayer.create(MainActivity.this, R.raw.hyperbeatd);
							break;
						default:
							player = MediaPlayer.create(MainActivity.this, R.raw.superbeatd);
							break;
						}
						break;

					}

					player.setVolume(0.25f,0.25f);
					player.setLooping(false);
					player.setOnCompletionListener(new OnCompletionListener() {

						//when the sound completes, release it so the next one can play (I think)
						@Override
						public void onCompletion(MediaPlayer mp) {
							player.release();
						}

					});   
					player.start();
					if(spot!=9)
					{
						if(MySurfaceView.vicCount>3)
						{
							victory.setText("YOU'RE WINNER");
						}
						else
						{
							victory.setText("");
						}
						MySurfaceView.checkForWin();
						MySurfaceView.switchOnOff(spot);
						buttonPresses.setText("Button Presses: " + MySurfaceView.countPresses);
						wins.setText("Completed: "+ MySurfaceView.vicCount);
						if(MySurfaceView.vicCount>3)
						{
							victory.setText("YOU'RE WINNER");
							wins.setText("Completed: " + MySurfaceView.vicCount);
						}
						else
						{
							victory.setText("");
						}
					}
					if(MySurfaceView.did0 == true)
					{
						zero.setVisibility(ImageView.INVISIBLE);
					}

					if(MySurfaceView.did1 == true)
					{
						one.setVisibility(ImageView.INVISIBLE);
					}

					if(MySurfaceView.did7 == true)
					{
						seven.setVisibility(ImageView.INVISIBLE);
					}

					if(MySurfaceView.did4 == true)
					{
						four.setVisibility(ImageView.INVISIBLE);
					}


					//chooses a song to play based on how far you are into solving merlin's square.
					//it generally gets louder and more active the farther you are
					//placed in a try-catch because if it's already executing, an exception gets thrown
					playSounds();
					return true;
				}
				return true;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//stops the current music
		switch(MySurfaceView.vicCount)
		{
		case 0:
			mBackgroundSound.foo();
			break;
		case 1:
			mBackgroundSound2.foo();
			break;
		case 2:
			mBackgroundSound3.foo();
			break;
		case 3:
			mBackgroundSound4.foo();
			break;
		}
		//store the array that was used to determine which squares are on or off
		//so that the layout doesn't re-randomize on orientation change
		outState.putBooleanArray("name", MySurfaceView.initDisplay);
		super.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		//restarts the music, depending on where the user is in the game
		switch(MySurfaceView.vicCount)
		{
		case 0:
			mBackgroundSound.playS();
			break;
		case 1:
			mBackgroundSound2.playS();
			break;
		case 2:
			mBackgroundSound3.playS();
			break;
		case 3:
			mBackgroundSound4.playS();
			break;
		}
		MySurfaceView.initDisplay = savedInstanceState.getBooleanArray("name");
		super.onRestoreInstanceState(savedInstanceState);
	}

	/*
	 * the method that plays the sounds.
	 * a different song is played depending on how far you are in the game.
	 * generally each level gets louder and more active.
	 */
	protected void playSounds()
	{
		int x;
		try
		{
			switch(MySurfaceView.vicCount)
			{
			case 0:
				mBackgroundSound.execute();
				mBackgroundSound5.foo();
				break;
			case 1:
				if(mega==false)
				{
					mega=true;
					levelplayer = MediaPlayer.create(MainActivity.this, R.raw.levemega);
					levelplayer.setLooping(false);
					levelplayer.start();
				}
				x = mBackgroundSound.getTime();
				mBackgroundSound.foo();
				mBackgroundSound2.execute();
				mBackgroundSound2.fastF(x);
				break;
			case 2:
				if(superr==false)
				{
					superr=true;
					levelplayer = MediaPlayer.create(MainActivity.this, R.raw.levelsuper);
					levelplayer.setLooping(false);
					levelplayer.start();
				}
				x = mBackgroundSound2.getTime();
				mBackgroundSound2.foo();
				mBackgroundSound3.execute();
				mBackgroundSound3.fastF(x);
				break;
			case 3:
				if(ultra==false)
				{
					ultra=true;
					levelplayer = MediaPlayer.create(MainActivity.this, R.raw.levelultra);
					levelplayer.setLooping(false);
					levelplayer.start();
				}
				x = mBackgroundSound3.getTime();
				mBackgroundSound3.foo();
				mBackgroundSound4.execute();
				mBackgroundSound4.fastF(x);
				break;
			case 4:
				if(extra==false)
				{
					extra=true;
					levelplayer = MediaPlayer.create(MainActivity.this, R.raw.levelextra);
					levelplayer.setLooping(false);
					levelplayer.start();
				}
				x = mBackgroundSound4.getTime();
				mBackgroundSound4.foo();
				mBackgroundSound5.execute();
				mBackgroundSound5.fastF(x);
				break;
			}
		}
		catch (IllegalStateException ise)
		{

		}
	}

	/*
	 * checks where the press was.
	 * @return - the element of the array that should be switched (0-8).
	 * a value of 9 means that an area outside the screen was pressed.
	 * in such a case, nothing happens.
	 */
	protected int checkWhere(float x, float y)
	{
		//check if it's in the first column
		if((x>0)&&(x<180))
		{
			//if it's in the first column, check which row it's in
			if(y<180)
			{
				return 0;
			}
			if(y<360)
			{
				return 1;
			}
			if(y<540)
			{
				return 2;
			}
		}

		//check if it's in the second column
		if((x>180)&&(x<360))
		{
			//if it is, check which row it's in
			if(y<180)
			{
				return 3;
			}
			if(y<360)
			{
				return 4;
			}
			if(y<540)
			{
				return 5;
			}
		}

		//if it's not in the first or second column, it's in the third column
		if(x>360)
		{
			//check which row it's in
			if(y<180)
			{
				return 6;
			}
			if(y<360)
			{
				return 7;
			}
			if(y<540)
			{
				return 8;
			}
		}
		//just in case it's not in anything, then return 9.
		//this will cause the thingy to not change
		return 9;
	}

	/*
	 * These classes are where the media files get stored.
	 */
	public class BackgroundSound extends AsyncTask<Void, Void, Void> {

		MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.hypers); 
		@Override	
		protected Void doInBackground(Void... params) {	
			player.setLooping(true); // Set looping 
			player.setVolume(1.0f,1.0f); 
			player.start(); 
			return null;
		}

		//stops the player so that it can be restarted later
		protected void foo()
		{
			player.stop();
		}

		/*
		 * @return int the position of the player.
		 * used to start the other players
		 */
		protected int getTime()
		{
			return player.getCurrentPosition();
		}
		/*
		 * restarts the music
		 */
		protected void playS()
		{
			player.start();
		}

	}

	public class BackgroundSound2 extends AsyncTask<Void, Void, Void> {
		MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.hyper2);
		@Override	
		protected Void doInBackground(Void... params) {	

			player.setLooping(true); // Set looping 
			player.setVolume(1.0f,1.0f); 
			player.start(); 
			return null;
		}

		protected void foo()
		{
			player.stop();
		}

		protected int getTime()
		{
			return player.getCurrentPosition();
		}

		protected void fastF(int y)
		{
			player.seekTo(y);
		}
		protected void playS()
		{
			player.start();
		}

	}

	public class BackgroundSound3 extends AsyncTask<Void, Void, Void> {
		MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.mega3); 
		@Override	
		protected Void doInBackground(Void... params) {	

			player.setLooping(true); // Set looping 
			player.setVolume(1.0f,1.0f); 
			player.start(); 
			return null;
		}

		protected void foo()
		{
			player.stop();
		}

		protected int getTime()
		{
			return player.getCurrentPosition();
		}

		protected void fastF(int y)
		{
			player.seekTo(y);
		}
		protected void playS()
		{
			player.start();
		}
	}

	public class BackgroundSound4 extends AsyncTask<Void, Void, Void> {
		MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.super3);

		@Override	
		protected Void doInBackground(Void... params) {	

			player.setLooping(true); // Set looping 
			player.setVolume(1.0f,1.0f); 
			player.start();
			return null;
		}

		protected void foo()
		{
			player.stop();
		}

		protected int getTime()
		{
			return player.getCurrentPosition();
		}

		protected void fastF(int y)
		{
			player.seekTo(y);
		}
		protected void playS()
		{
			player.start();
		}
	}

	public class BackgroundSound5 extends AsyncTask<Void, Void, Void> {
		MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.extra1);

		@Override	
		protected Void doInBackground(Void... params) {	

			player.setLooping(true); // Set looping 
			player.setVolume(200,200); 
			player.start();
			return null;
		}

		protected void foo()
		{
			player.stop();
		}

		protected int getTime()
		{
			return player.getCurrentPosition();
		}

		protected void fastF(int y)
		{
			player.seekTo(y);
		}
		protected void playS()
		{
			player.start();
		}
	}

	public class BackgroundSounds extends AsyncTask<Void, Void, Void>
	{
		MediaPlayer player;
		public BackgroundSounds(MediaPlayer players)
		{
			player = players;
		}
		@Override
		protected Void doInBackground(Void... params) {	

			player.setLooping(true); // Set looping 
			player.setVolume(200,200); 
			player.start();
			return null;
		}

		protected void foo()
		{
			player.stop();
		}

		protected int getTime()
		{
			return player.getCurrentPosition();
		}

		protected void fastF(int y)
		{
			player.seekTo(y);
		}
		protected void playS()
		{
			player.start();

		}
	}



}


