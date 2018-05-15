
import java.util.Scanner;
import java.util.Vector;

import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;

import Holders.BioData;

import Readers.JsonReader;
import Readers.BioHelpSQLConnector;

import Telegram.TelegramListener;
import Telegram.TelegramThread;

public class Main
{
	public static void main(String[] args)
	{
		PropertyConfigurator.configure("log4j.properties");
		Logger log = Logger.getLogger("BioHelp");
		log.info("https://github.com/RogerMinemu/BioHelp");
		log.info("======================================");
		
		log.info("Trying to load config in: " + args[0]);
		JsonReader jr = new JsonReader(args[0]);

		BioHelpSQLConnector bioDBConnector = new BioHelpSQLConnector(jr.getField("dbhostname"), jr.getField("dbusername"), jr.getField("dbpassword"), jr.getField("database"));
		Vector<BioData> bioData = bioDBConnector.getAllData();

		// Telegram Init
		ApiContextInitializer.init();
		TelegramBotsApi botsApi = new TelegramBotsApi();

		try
		{
			botsApi.registerBot(new TelegramListener(jr.getField("telegramBotAPI"), bioData));
		}
		catch (TelegramApiException e)
		{
			e.printStackTrace();
		}

		Scanner sc = new Scanner(System.in);
		String input;
		
		

		do
		{
			log.info("Some testing input: ");
			input = sc.nextLine();
			int simpos = 0;
			double simint = 0;
			for(int i = 0; i < bioData.size(); i++)
			{
				if(simint < Similarity.compare(input, bioData.get(i).question))
				{
					simpos = i;
					simint = Similarity.compare(input, bioData.get(i).question);
					log.info("New veracity record: " + simint);
				}
			}

			log.info(bioData.get(simpos).answer);
		}
		while (input != "salir");
	}

}
