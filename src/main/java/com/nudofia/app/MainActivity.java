/**Visualizer Code based on Android Visualizer Master by Felix Palmer
 * "Copyright 2011, Felix Palmer
 *
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/"
 *
 *v2
 */
package com.nudofia.app;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.media.audiofx.Visualizer;
import android.widget.TextView;
import android.widget.Toast;

import com.nudofia.utils.TunnelPlayerWorkaround;
import com.nudofia.visualizer.R;
import com.nudofia.visualizer.VisualizerView;
import com.nudofia.visualizer.renderer.BarGraphRenderer;

//Visualizer app developed for Nvidia Shield K1

public class MainActivity extends Activity {
  private MediaPlayer mPlayer;
  private MediaPlayer mSilentPlayer;  /* to avoid tunnel player issue */
  private VisualizerView mVisualizerView;
  private VisualizerView game;
  private boolean ai;
  private int state=0;
  private String url="nope";
  private boolean goBlack=true, musicNotPlaying=true;
  private int REQ_CODE_PICK_SOUNDFILE = 1;
  private Uri audioFileUri;


  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mainmenu);
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    initTunnelPlayerWorkaround();
    init();
  }

  @Override
  protected void onPause()
  {
    cleanUp();
    super.onPause();
  }

  @Override
  protected void onDestroy()
  {
    cleanUp();
    super.onDestroy();
  }

  private void init()
  {
    //mPlayer = MediaPlayer.create(this, null);
    //mPlayer.setLooping(true);
    //mPlayer.start();

    // We need to link the visualizer view to the media player so that
    // it displays something

    /*mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);
    mVisualizerView.link(mPlayer);
    // Start with just bar renderer
    addBarGraphRenderers();
    addCircleBarRenderer();*/
  }

  private void cleanUp()
  {
    /*if (mPlayer != null)
    {
      mVisualizerView.release();
      mPlayer.release();
      mPlayer = null;
    }

    if (mSilentPlayer != null)
    {
      mSilentPlayer.release();
      mSilentPlayer = null;
    }*/
  }
//left over from source project
  // Workaround (for Galaxy S4)
  //
  // "Visualization does not work on the new Galaxy devices"
  //    https://github.com/felixpalmer/android-visualizer/issues/5
  //
  // NOTE:
  //   This code is not required for visualizing default "test.mp3" file,
  //   because tunnel player is used when duration is longer than 1 minute.
  //   (default "test.mp3" file: 8 seconds)
  //
  private void initTunnelPlayerWorkaround() {
    // Read "tunnel.decode" system property to determine
    // the workaround is needed
    if (TunnelPlayerWorkaround.isTunnelDecodeEnabled(this)) {
      mSilentPlayer = TunnelPlayerWorkaround.createSilentMediaPlayer(this);
    }
  }

  // Method that was used for adding renderers to visualizer
  /*private void addBarGraphRenderers()
  {
    Paint paint = new Paint();
    paint.setStrokeWidth(50f);
    paint.setAntiAlias(true);
    paint.setColor(Color.argb(200, 56, 138, 252));
    BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(16, paint, false);
    mVisualizerView.addRenderer(barGraphRendererBottom);
    up=barGraphRendererBottom.getHi();

    Paint paint2 = new Paint();
    paint2.setStrokeWidth(50f);
    paint2.setAntiAlias(true);
    paint2.setColor(Color.argb(200, 181, 111, 233));
    BarGraphRenderer barGraphRendererTop = new BarGraphRenderer(16, paint2, true);
    mVisualizerView.addRenderer(barGraphRendererTop);
    low=barGraphRendererBottom.getHi();
  }*/



  // Actions for buttons defined in xml
  public void startPressed(View view) throws IllegalStateException, IOException
  {
    setContentView(R.layout.main);

    ai=false;

    if (url.equals("nope")||url!=null||url!=""){
      System.out.println("URL IS THERE:"+url);
      goBlack=false;
    }

    else{
      System.out.println("URL IS THERE");
      new DownloadImageTask((ImageView) findViewById(R.id.imageView))
              .execute(url);
    }
    mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);

    if (musicNotPlaying){
      System.out.println("IT'S THE BOOLEAN");
    mPlayer = MediaPlayer.create(this, R.raw.mansion);
    mPlayer.setLooping(true);}
    mPlayer.start();
    mVisualizerView.link(mPlayer);
    mVisualizerView.play(ai, goBlack, 0);

    // Start with just bar renderer
    mVisualizerView.addRenderer();
    System.out.println("Game is about to run...");
    System.out.println("Game is running...");

  }

//ai mode
  public void aiPressed(View view) throws IllegalStateException, IOException
  {
    setContentView(R.layout.select);


  }
  public void easyPressed(View view)throws IllegalStateException, IOException
  {
    setContentView(R.layout.main);
    ai=true;
    if (url.equals("nope")||url!=null||url!=""){
      System.out.println("URL IS THERE:"+url);
      goBlack=false;
    }

    else{
      System.out.println("URL ISNT THERE");
    }

    mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);

    if (musicNotPlaying){
      System.out.println("IT'S THE BOOLEAN");
      mPlayer = MediaPlayer.create(this, R.raw.mansion);
      mPlayer.setLooping(true);}
    mPlayer.start();
    mVisualizerView.link(mPlayer);
    mVisualizerView.play(ai, goBlack, 5);

    // Start with just bar renderer
    mVisualizerView.addRenderer();
    System.out.println("Game is about to run...");
    System.out.println("Game is running...");
  }

  public void mediumPressed(View view)throws IllegalStateException, IOException
  {
    setContentView(R.layout.main);
    ai=true;
    if (url.equals("nope")||url!=null||url!=""){
      System.out.println("URL IS THERE:"+url);
      goBlack=false;
    }

    else{
      System.out.println("URL ISNT THERE");
    }

    mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);

    if (musicNotPlaying){
      System.out.println("IT'S THE BOOLEAN");
      mPlayer = MediaPlayer.create(this, R.raw.mansion);
      mPlayer.setLooping(true);}
    mPlayer.start();
    mVisualizerView.link(mPlayer);
    mVisualizerView.play(ai, goBlack, 10);

    // Start with just bar renderer
    mVisualizerView.addRenderer();
    System.out.println("Game is about to run...");
    System.out.println("Game is running...");
  }

  public void hardPressed(View view)throws IllegalStateException, IOException
  {
    setContentView(R.layout.main);
    ai=true;
    if (url.equals("nope")||url!=null||url!=""){
      System.out.println("URL IS THERE:"+url);
      goBlack=false;
    }

    else{
      System.out.println("URL ISNT THERE");
    }

    mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);

    if (musicNotPlaying){
      System.out.println("IT'S THE BOOLEAN");
      mPlayer = MediaPlayer.create(this, R.raw.mansion);
      mPlayer.setLooping(true);}
    mPlayer.start();
    mVisualizerView.link(mPlayer);
    mVisualizerView.play(ai, goBlack, 15);

    // Start with just bar renderer
    mVisualizerView.addRenderer();
    System.out.println("Game is about to run...");
    System.out.println("Game is running...");
  }

  //get background
  public void changePressed(View view) throws IllegalStateException, IOException{

    EditText txtDescription = (EditText) findViewById(R.id.txt);
    url = txtDescription.getText().toString();

  }


  //pause game button
  public void stopPressed(View view) throws IllegalStateException, IOException
  {

    if (state==0){
      state++;
      mVisualizerView.mPause(state);
      mPlayer.pause();
    }
    else{
      state--;
      mPlayer.start();
      mVisualizerView.mPause(state);

    }
    System.out.println("STATE: "+state);
  }

//return to main screen
  public void returnPressed (View view) throws IllegalStateException, IOException{

    mVisualizerView.clearRenderers();
    setContentView(R.layout.mainmenu);
    mVisualizerView.upstat();
    musicNotPlaying=true;
    goBlack=true;
  }

  //select/change music
  public void musicPressed (View view)throws IllegalStateException, IOException{

    Intent intent;
    intent = new Intent();
    intent.setType("audio/mpeg");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Open Audio file"), REQ_CODE_PICK_SOUNDFILE);
  }
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQ_CODE_PICK_SOUNDFILE && resultCode == Activity.RESULT_OK){

        audioFileUri = data.getData();

        musicNotPlaying=false;
        mPlayer= MediaPlayer.create(this, audioFileUri);

        mPlayer.start();
    }
  }

  //end app
  public void exitPressed (View view)throws IllegalStateException, IOException{

    android.os.Process.killProcess(android.os.Process.myPid());
    System.exit(1);

  }



// wip function
  public void clearPressed(View view)
  {
    mVisualizerView.clearRenderers();
  }


}