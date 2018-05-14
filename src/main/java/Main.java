import java.util.ArrayList;
import java.util.Scanner;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("https://github.com/RogerMinemu/BioHelp");
		
		int cores = Runtime.getRuntime().availableProcessors();
		
		ArrayList<DiscordThread>ThD = new ArrayList<DiscordThread>(cores);
		ArrayList<TelegramThread>ThT = new ArrayList<TelegramThread>(cores);
		
		//Telegram Init
	      ApiContextInitializer.init();

	      TelegramBotsApi botsApi = new TelegramBotsApi();

	      try {
	          botsApi.registerBot(new TelegramListener());
	      } catch (TelegramApiException e) {
	          e.printStackTrace();
	      }
		
		
		Scanner sc = new Scanner(System.in);
		String input;
		
		do {
			System.out.println("Some testing input: ");
			input = sc.nextLine();
			
			System.out.println(Similarity.compare("bases de datos de metabolomica", input));
		}while(input != "salir");

	}

}
