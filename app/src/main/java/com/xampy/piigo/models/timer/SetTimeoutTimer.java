package com.xampy.piigo.models.timer;

public class SetTimeoutTimer implements Runnable {


    private boolean mAlive;


    public interface OnSetTimeoutTimerListener {
        void onTimeReached();
    }

    public SetTimeoutTimer(long interval, OnSetTimeoutTimerListener listener){

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

    }
}
