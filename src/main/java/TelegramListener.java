import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class TelegramListener extends TelegramLongPollingBot {
    public String getBotUsername() {
        // Return bot username
        return "BioHelp";
    }

    @Override
    public String getBotToken() {
        // Return bot token
        return "";
    }

	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		if (update.hasMessage() && update.getMessage().hasText()) {
			{
				//NOTE (Roger): Aqui empieza cuando llega un mensaje y se deberia empezar un nuevo thread
				String message = "Hola Mon";
				try {
            		//execute(message); // Sending our message object to user
            	} 
            	catch (TelegramApiException e) 
            	{
            		e.printStackTrace();
            	}
			}
		}
	}

}
