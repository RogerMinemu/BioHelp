package Telegram;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.objects.Update;

import Holders.BioData;

import TextUtils.Similarity;

public class TelegramThread extends Thread {
	private Update update;
	private Vector<BioData> bioData;
	Logger log = Logger.getLogger("Telegram Thread");

	public TelegramThread(Update update, Vector<BioData> bioData) {
		this.update = update;
		this.bioData = bioData;
	}

	public void run() {
		String input = this.update.getMessage().getText();

		int simpos = 0;
		double simint = 0;
		
		for (int i = 0; i < bioData.size(); i++) {
			if (simint < Similarity.compare(input, bioData.get(i).question)) {
				simpos = i;
				simint = Similarity.compare(input, bioData.get(i).question);
				log.info("New veracity record: " + simint);
			}
		}
		//Aqui falta responder al usuario con el asnwer marcado abajo
		log.info(bioData.get(simpos).answer);

	}
}
