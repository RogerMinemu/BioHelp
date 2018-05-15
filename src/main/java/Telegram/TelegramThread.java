package Telegram;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import Holders.BioData;

import TextUtils.Similarity;

public class TelegramThread extends Thread
{
	private Update update;
	private Vector<BioData> bioData;

	private TelegramListener telegramListener;
	private Logger log = Logger.getLogger("Telegram Thread");

	public TelegramThread(Update update, Vector<BioData> bioData, TelegramListener telegramListener)
	{
		this.update = update;
		this.bioData = bioData;
	}

	public void run()
	{
		String input = this.update.getMessage().getText();

		double simint = 0;
		int simpos = 0;

		for (int i = 0; i < this.bioData.size(); i++)
		{
			if (simint < Similarity.compare(input, this.bioData.get(i).question))
			{
				simpos = i;
				simint = Similarity.compare(input, this.bioData.get(i).question);

				log.info("New veracity record: " + simint);
			}
		}

		String answer = this.bioData.get(simpos).answer;
		SendMessage userMessage = new SendMessage();
		log.info(answer);

		userMessage.setChatId(this.update.getChannelPost().getChatId());
		userMessage.setText(answer);

		this.telegramListener.sendResponse(userMessage);
	}
}
