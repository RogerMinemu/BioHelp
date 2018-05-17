public class DiscordThread extends Thread
{
    private int ThreadNum;
    private Thread t;

    public DiscordThread(int ThreadNum)
    {
        this.ThreadNum = ThreadNum;
    }

    public void start()
    {
        this.t = new Thread();
    }

    public void run()
    {
        System.out.println("Process running: " + ThreadNum);
    }

}
