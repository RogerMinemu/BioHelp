package Telegram;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class TelegramListener extends TelegramLongPollingBot
{
    public String getBotUsername()
    {
        return "BioHelp";
    }

    public String getBotToken()
    {
        return "";
    }

	public void onUpdateReceived(Update update)
	{
		if (update.hasMessage() && update.getMessage().hasText())
		{
			(new TelegramThread(update)).start();
		}
	}

}
