import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class TelegramThread extends Thread {

	private int ThreadNum;
	private Thread t;
	private Update update;

	public TelegramThread(int ThreadNum, Update update)
	{
		this.ThreadNum = ThreadNum;
		this.update = update;
	}
	
	public void run()
	{
		ApiContextInitializer.init();
		TelegramBotsApi botsApi = new TelegramBotsApi();
		System.out.println("Process running: " + ThreadNum);
		
	}

}
