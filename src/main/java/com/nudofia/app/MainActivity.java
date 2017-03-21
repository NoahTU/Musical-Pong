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
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

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
    mPlayer = MediaPlayer.create(this, R.raw.mansion);
    mPlayer.setLooping(true);
    mPlayer.start();

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
    if (mPlayer != null)
    {
      mVisualizerView.release();
      mPlayer.release();
      mPlayer = null;
    }

    if (mSilentPlayer != null)
    {
      mSilentPlayer.release();
      mSilentPlayer = null;
    }
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
  private void addBarGraphRenderers()
  {
    Paint paint = new Paint();
    paint.setStrokeWidth(50f);
    paint.setAntiAlias(true);
    paint.setColor(Color.argb(200, 56, 138, 252));
    BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(16, paint, false);
    mVisualizerView.addRenderer(barGraphRendererBottom);

    Paint paint2 = new Paint();
    paint2.setStrokeWidth(50f);
    paint2.setAntiAlias(true);
    paint2.setColor(Color.argb(200, 181, 111, 233));
    BarGraphRenderer barGraphRendererTop = new BarGraphRenderer(16, paint2, true);
    mVisualizerView.addRenderer(barGraphRendererTop);
  }



  // Actions for buttons defined in xml
  public void startPressed(View view) throws IllegalStateException, IOException
  {
    setContentView(R.layout.main);
    ai=false;

    mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);
    mVisualizerView.link(mPlayer);
    mVisualizerView.play(ai);

    // Start with just bar renderer
    addBarGraphRenderers();
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


  public void aiPressed(View view) throws IllegalStateException, IOException
  {
    setContentView(R.layout.main);
    ai=true;

    mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);
    mVisualizerView.link(mPlayer);
    mVisualizerView.play(ai);

    // Start with just bar renderer
    addBarGraphRenderers();
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

  public void stopPressed(View view)
  {
    mPlayer.stop();
  }

  public void barPressed(View view)
  {
    addBarGraphRenderers();
  }



  public void clearPressed(View view)
  {
    mVisualizerView.clearRenderers();
  }
}