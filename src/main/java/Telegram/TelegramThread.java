package Telegram;
import org.telegram.telegrambots.api.objects.Update;

public class TelegramThread extends Thread
{
	private Update update;

	public TelegramThread(Update update)
	{
		this.update = update;
	}
	
	public void run()
	{		
		String userMessage = this.update.getMessage().getText();
	}
}
