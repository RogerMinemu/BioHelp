package Telegram;

import java.util.Vector;

import org.telegram.telegrambots.api.objects.Update;

import Holders.BioData;

public class TelegramThread extends Thread
{
	private Update update;
	private Vector<BioData> bioData;

	public TelegramThread(Update update, Vector<BioData> bioData)
	{
		this.update = update;
		this.bioData = bioData;
	}

	public void run()
	{
		String userMessage = this.update.getMessage().getText();
		
		
		
	}
}
