package Telegram;

import java.util.Vector;
import java.util.concurrent.locks.StampedLock;
import java.util.concurrent.locks.ReadWriteLock;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import org.apache.log4j.Logger;
import Holders.BioData;
import Readers.BioHelpSQLConnector;

public class TelegramListener extends TelegramLongPollingBot
{
	private String telegramBotAPI;
	private Vector<BioData> bioData;
	private BioHelpSQLConnector bioDBConnector;

	// NOTE(Andrei): use read-write lock as there are several readers
	//               and only one writter.
	ReadWriteLock readWriteLock = new StampedLock().asReadWriteLock();
	Logger log = Logger.getLogger("Telegram Listener");

	public TelegramListener(String telegramBotAPI, Vector<BioData> bioData, BioHelpSQLConnector bioDBConnector)
	{
		this.telegramBotAPI = telegramBotAPI;
		this.bioData = bioData;
		this.bioDBConnector = bioDBConnector;
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
			log.info("Creating Thread for user @" + update.getMessage().getFrom().getUserName() + ": " + update.getMessage().getText());

			// NOTE(Andrei): Allow multiple readers as long as no writer is waiting,
			//               the unlock is done in sendResponse functions because is
			//               when each one of the threads has stopped using the data.
			this.readWriteLock.readLock().lock();
			(new TelegramThread(update, bioData, this, bioDBConnector)).start();
		}
	}

	public void updateDate(Vector<BioData> bioData)
	{
		this.readWriteLock.writeLock().lock();
			this.bioData.clear();
			this.bioData = bioData;
		this.readWriteLock.writeLock().unlock();
	}

	public void sendResponse(SendMessage userMessage)
	{
	    // NOTE(Andrei): Unlock the writer as is done using the data.
	    this.readWriteLock.readLock().unlock();

		try
		{
			execute(userMessage);
		}
		catch (TelegramApiException e)
		{
			e.printStackTrace();
		}
	}
}
