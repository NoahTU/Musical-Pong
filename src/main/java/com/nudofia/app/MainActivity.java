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


public class MainActivity extends Activity {
  private MediaPlayer mPlayer;
  private MediaPlayer mSilentPlayer;  /* to avoid tunnel player issue */
  private VisualizerView mVisualizerView;
  //private GameThread gamethr;
  private VisualizerView game;
  private boolean ai;
  private int one=0, two=0, state=0;
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

  // Methods for adding renderers to visualizer
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

    if (url.equals("nope")){
      System.out.println("URL ISNT THERE");
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
    mVisualizerView.play(ai, goBlack);

    // Start with just bar renderer
    mVisualizerView.addRenderer();
   // addCircleBarRenderer();
    System.out.println("Game is about to run...");
    //mVisualizerView.play();
    System.out.println("Game is running...");
    //while (!mVisualizerView.getWinStatus()){


    //}
   /* if(mPlayer.isPlaying())
    {
      return;
    }
    mPlayer.prepare();
    mPlayer.start();*/


   //game= new VisualizerView(this);

  }


  public void aiPressed(View view) throws IllegalStateException, IOException
  {
    setContentView(R.layout.main);

    ai=true;
    if (url.equals("nope")){
      System.out.println("URL IS THERE");
      goBlack=false;
    }

    else{
      System.out.println("URL ISNT THERE");
    }

    mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);

    if (musicNotPlaying){
      mPlayer = MediaPlayer.create(this, R.raw.mansion);
      mPlayer.setLooping(true);}

    mVisualizerView.link(mPlayer);
    mVisualizerView.play(ai, goBlack);

    // Start with just bar renderer
    mVisualizerView.addRenderer();
    // addCircleBarRenderer();
    System.out.println("Game is about to run...");
    //mVisualizerView.play();
    System.out.println("Game is running...");


   /* if(mPlayer.isPlaying())
    {
      return;
    }
    mPlayer.prepare();
    mPlayer.start();*/


    //game= new VisualizerView(this);

  }
  public void changePressed(View view) throws IllegalStateException, IOException{



    EditText txtDescription =
            (EditText) findViewById(R.id.txt);
    url = txtDescription.getText().toString();



  }


  public void stopPressed(View view) throws IllegalStateException, IOException
  {
    //setContentView(R.layout.mainmenu);
    if (state==0){
      state++;
      mVisualizerView.mPause(state);
      //mPlayer.stop();
    }
    else{
      state--;
      mVisualizerView.mPause(state);
    }
    //mPlayer.stop();
  }


  public void returnPressed (View view) throws IllegalStateException, IOException{
    //onDestroy();
    //cleanUp();
    mVisualizerView.clearRenderers();
    //init();
    //super.onPause();
    //super.onDestroy();
    //super.onResume();
    setContentView(R.layout.mainmenu);
    mVisualizerView.upstat();
    musicNotPlaying=true;
    goBlack=true;
    //mPlayer = MediaPlayer.create(this, R.raw.mansion);
   // mPlayer.setLooping(true);
    //mPlayer.start();
    //onDestroy();
    //init();
    //cleanUp();

  }

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
      //if ((data != null) && (data.getData() != null)){
        audioFileUri = data.getData();

        musicNotPlaying=false;

       /* try {
          mPlayer.setDataSource(getApplicationContext(), audioFileUri);
          mPlayer= MediaPlayer.create(this, audioFileUri);
        } catch (IllegalArgumentException e) {
          Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
          Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
          Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
          e.printStackTrace();
        }
      try {
        mPlayer.prepare();
      } catch (IllegalStateException e) {
        Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
      } catch (IOException e) {
        Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
      }*/
      //mPlayer.setLooping(true);
      mPlayer= MediaPlayer.create(this, audioFileUri);
      mPlayer.start();


        // Now you can use that Uri to get the file path, or upload it, ...
      //}
    }
  }

  public void exitPressed (View view)throws IllegalStateException, IOException{

    android.os.Process.killProcess(android.os.Process.myPid());
    System.exit(1);
    /*Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_HOME);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);*/
    //finish();
  }




  public void clearPressed(View view)
  {
    mVisualizerView.clearRenderers();
  }


}