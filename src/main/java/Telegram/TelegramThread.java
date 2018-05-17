package Telegram;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import Holders.BioData;
import Readers.BioHelpSQLConnector;
import Readers.JsonReader;
import TextUtils.Similarity;

public class TelegramThread extends Thread {
	private Update update;
	private Vector<BioData> bioData;
	private BioHelpSQLConnector bioDBConnector;

	private TelegramListener telegramListener;
	private Logger log = Logger.getLogger("Telegram Thread");

	public TelegramThread(Update update, Vector<BioData> bioData, TelegramListener telegramListener,
			BioHelpSQLConnector bioDBConnector) {
		this.update = update;
		this.bioData = bioData;
		this.telegramListener = telegramListener;
	}

	public void run() {
		String input = this.update.getMessage().getText();
		String answer = null;

		//NOTE (Roger): Falta hacer que coja el numero pasado por pregunta y lo use como 2do parametro en setVeracity
		if (Similarity.compare(input, "Cambiar veracidad a") > 0.63) {
			log.warn(update.getMessage().getChatId());
			bioDBConnector.setVeracity(update.getMessage().getChatId(), 50);
		} 
		else 
		{
			double simint = 0;
			int simpos = 0;

			for (int i = 0; i < this.bioData.size(); i++) 
			{
				if (simint < Similarity.compare(input, this.bioData.get(i).question)) 
				{
					simpos = i;
					simint = Similarity.compare(input, this.bioData.get(i).question);
				}
			}

			if (simint < 0.5) // if veracity is under %
			{
				answer = "BioHelp no sabe que estás preguntando, prueba a formular de diferente manera la pregunta.";
				log.warn("Veracity: " + simint + " - BioHelp doesn't know what is the user asking 😂");
			} else {
				answer = this.bioData.get(simpos).answer;
				log.info("Veracity: " + simint + " - " + answer);
			}
		}

		SendMessage userMessage = new SendMessage();
		userMessage.setChatId(this.update.getMessage().getChatId());
		userMessage.setText(answer);

		this.telegramListener.sendResponse(userMessage);
	}
}
