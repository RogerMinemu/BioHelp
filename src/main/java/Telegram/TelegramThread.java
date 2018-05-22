package Telegram;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import Holders.BioData;
import Readers.BioHelpSQLConnector;
import Readers.JsonReader;
import TextUtils.Filter;
import TextUtils.Similarity;

public class TelegramThread extends Thread
{
    private Update update;
    private Vector<BioData> bioData;
    private BioHelpSQLConnector bioDBConnector;

    private TelegramListener telegramListener;
    private Logger log = Logger.getLogger("Telegram Thread");

    public TelegramThread(Update update, Vector<BioData> bioData, TelegramListener telegramListener, BioHelpSQLConnector bioDBConnector)
    {
        this.update = update;
        this.bioData = bioData;

        this.bioDBConnector = bioDBConnector;
        this.telegramListener = telegramListener;
    }

    private void saveVeracity(String userRequestString, SendMessage userSendMessage)
    {
        long chatID = Long.parseLong(userSendMessage.getChatId());
        String userMessage = "Veracidad cambiada correctamente.";

        Filter fl = new Filter(userRequestString);
        userMessage = this.bioDBConnector.updateVeracity(chatID+"", fl.getNumber());

        userSendMessage.setText(userMessage);
        this.telegramListener.sendResponse(userSendMessage);
    }

    private void processUserMessage(String userRequestString, SendMessage userSendMessage)
    {
    	long chatID = Long.parseLong(userSendMessage.getChatId());
    	double personalVeracity = this.bioDBConnector.getVeracity(chatID+"");
    	double simint = 0;
        int simpos = 0;
        
    	if(personalVeracity == -1)
    	{
    		personalVeracity = 0.6;
    	}
    	else
    	{
    		personalVeracity = personalVeracity / 100;
    	}
        
        for (int i = 0; i < this.bioData.size(); i++)
        {
            if (simint < Similarity.compare(userRequestString, this.bioData.get(i).question))
            {
                simpos = i;
                simint = Similarity.compare(userRequestString, this.bioData.get(i).question);
            }
        }

        if (simint < personalVeracity) // if veracity is under personal value
        {
            userSendMessage.setText("BioHelp no sabe que estás preguntando, prueba a formular de diferente manera la pregunta.");
            log.warn("Veracity: " + simint + " - BioHelp doesn't know what is the user asking");
        }
        else
        {
            userSendMessage.setText(this.bioData.get(simpos).answer);
            log.info("Veracity: " + simint + " - " + this.bioData.get(simpos).answer);
        }

        this.telegramListener.sendResponse(userSendMessage);
    }

    public void run()
    {
        String userRequestString = this.update.getMessage().getText();

        SendMessage userSendMessage = new SendMessage();
        userSendMessage.setChatId(this.update.getMessage().getChatId());

        // NOTE (Roger): Falta hacer que coja el numero pasado por pregunta y lo use
        // como 2do parametro en setVeracity
        if (Similarity.compare(userRequestString, "Cambiar veracidad a") > 0.63)
        {
            saveVeracity(userRequestString, userSendMessage);
        }
        else
        {
            processUserMessage(userRequestString, userSendMessage);
        }
    }
}
