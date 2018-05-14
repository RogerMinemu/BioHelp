import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("https://github.com/RogerMinemu/BioHelp");
		
		//Telegram Init
	      ApiContextInitializer.init();

	      TelegramBotsApi botsApi = new TelegramBotsApi();

	      try {
	          botsApi.registerBot(new TelegramListener());
	      } catch (TelegramApiException e) {
	          e.printStackTrace();
	      }
		

	}

}
