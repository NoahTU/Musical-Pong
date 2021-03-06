/**
 * Copyright 2011, Felix Palmer
 *
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */
package com.nudofia.visualizer.renderer;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.nudofia.app.GameState;
import com.nudofia.visualizer.AudioData;
import com.nudofia.visualizer.FFTData;

import static java.util.logging.Logger.global;

public class BarGraphRenderer extends Renderer
{
  private int mDivisions;
  private Paint mPaint;
  private boolean mTop;
  private GameState state;
  public int x=0, ii=0;
  public int y=0, iii=0;
  public int[] topArray = new int[17];
  public int[] botArray = new int[17];
 // global int yb;

  /**
   * Renders the FFT data as a series of lines, in histogram form
   * @param divisions - must be a power of 2. Controls how many lines to draw
   * @param paint - Paint to draw lines with
   * @param top - whether to draw the lines at the top of the canvas, or the bottom
   */
  public BarGraphRenderer(int divisions,
                          Paint paint,
                          boolean top)
  {
    super();
    mDivisions = divisions;
    mPaint = paint;
    mTop = top;
    //state = new GameState(false);
  }

  @Override
  public void onRender(Canvas canvas, AudioData data, Rect rect)
  {
    // Do nothing, we only display FFT data
  }

  @Override
  public void onRender(Canvas canvas, FFTData data, Rect rect)
  {
    for (int i = 1; i < mDivisions; i++) {
      mFFTPoints[i * 4] = i * 4 * mDivisions;
      mFFTPoints[i * 4 + 2] = i * 4 * mDivisions;
      byte rfk = data.bytes[mDivisions * i];
      byte ifk = data.bytes[mDivisions * i + 1];
      float magnitude = (rfk * rfk + ifk * ifk);
      int dbValue = (int) (10 * Math.log10(magnitude));
      if(mTop)
      {
        mFFTPoints[i * 4 + 1] = 0;
        topArray[i]=0;

        mFFTPoints[i * 4 + 3] = (dbValue * 2 - 10)*9;//gets array of y axis points for gamestate to interact with
        topArray[i]=(dbValue * 2 - 10)*9;;
        ii=i;
      }
      else
      {
        mFFTPoints[i * 4 + 1] = rect.height()*13;
        botArray[i]=rect.height()*13;

        mFFTPoints[i * 4 + 3] = rect.height() - (dbValue * 2 - 10)*9;
        botArray[i]=rect.height() - (dbValue * 2 - 10)*9;//gets array of y axis points for gamestate to interact with
        iii=i;
      }
      //canvas.drawLines(mFFTPoints, mPaint);
    }


    canvas.drawLines(mFFTPoints, mPaint);
  }
  public static int getScreenWidth() {
    return Resources.getSystem().getDisplayMetrics().widthPixels;
  }

  public static int getScreenHeight() {
    return Resources.getSystem().getDisplayMetrics().heightPixels;
  }
  public int[] getHi() {
    return topArray;
}
  public int[] getLo() {
    return botArray;
  }
  public int getTopi() {
    return ii;
  }
  public int getLowi() {
    return iii;
  }
}
