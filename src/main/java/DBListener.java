import org.apache.log4j.Logger;

import Readers.BioHelpSQLConnector;
import Telegram.TelegramListener;

public class DBListener extends Thread
{
	private TelegramListener telegramListener;
	private BioHelpSQLConnector sqlConnector;

	public DBListener(TelegramListener telegramListener, BioHelpSQLConnector sqlConnector)
	{
		this.telegramListener = telegramListener;
		this.sqlConnector = sqlConnector;
	}

	public void run()
	{
		int lastDataVersion = this.sqlConnector.getDataVersion();
		Logger log = Logger.getLogger("BioHelp");

		while(true)
		{
			int currentDataVersion = this.sqlConnector.getDataVersion();

			if(currentDataVersion != lastDataVersion)
			{
				log.info("Update data");
				lastDataVersion = currentDataVersion;
				this.telegramListener.updateDate(this.sqlConnector.getAllData());
			}

			try
			{
				sleep(1000 * 5);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
