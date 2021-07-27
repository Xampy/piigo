package com.xampy.piigo.models.timer;

import android.util.Log;

public class SetIntervalTimer implements  Runnable{

    private Thread mThread;
    private long mInterval;
    private boolean mAlive;
    private OnSetIntervalTimerListener mListener;

    public interface OnSetIntervalTimerListener {
        void onTimeElapsed();
    }


    public SetIntervalTimer(final long interval, final OnSetIntervalTimerListener listener){
        mAlive = true;

        mListener = listener;
        mInterval = interval;
        mThread = new Thread(this);
        mThread.start();

    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (mAlive) {

            try {
                Thread.sleep(mInterval);
                if (mListener != null) {
                    mListener.onTimeElapsed();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){

        mAlive = false;
        mThread = null;
    }
}
