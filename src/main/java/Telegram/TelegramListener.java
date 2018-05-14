package Telegram;

import java.util.Vector;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import Holders.BioData;

public class TelegramListener extends TelegramLongPollingBot
{
	private String telegramBotAPI;
	private Vector<BioData> bioData;
	
	public TelegramListener(String telegramBotAPI, Vector<BioData> bioData)
	{
		this.telegramBotAPI = telegramBotAPI;
		this.bioData = bioData;
	}
	
	public String getBotUsername()
	{
		return "BioHelp";
	}

	public String getBotToken()
	{
		return this.telegramBotAPI;
	}

	public void onUpdateReceived(Update update)
	{
		if (update.hasMessage() && update.getMessage().hasText())
		{
			(new TelegramThread(update, bioData)).start();
		}
	}

}
