package models;
/**
 * 
 * @author Brayan Cardenas
 *
 */
public abstract class MyThread implements Runnable{

    private Thread thread;
    private boolean isRunning;
    private boolean pause;
    private long speed;

    public MyThread(long speed){
        this.speed = speed;
        thread = new Thread(this);
    }

    public void start(){
        isRunning = true;
        thread.start();
    }

    public synchronized void stop(){
        isRunning = false;
        notify();
    }

    public synchronized void pause(){
        pause = true;
        notify();
    }

    public synchronized void resume(){
        pause = false;
        notify();
    }
    abstract void executeTask();

    @Override
    public void run() {
        while(isRunning){
            executeTask();
            synchronized (this){
                while(pause) {
                    if(!isRunning){
                        break;
                    }
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}