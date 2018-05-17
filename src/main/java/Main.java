
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

import TextUtils.Similarity;

import Telegram.TelegramListener;

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

        TelegramListener telegramListener = new TelegramListener(jr.getField("telegramBotAPI"), bioData, bioDBConnector);
        (new DBListener(telegramListener, bioDBConnector)).start();

        try
        {
            botsApi.registerBot(telegramListener);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }

        Scanner consoleInput = new Scanner(System.in);
        log.info("Successfully Loaded. Now you can test questions as a command");

        while (true)
        {
            String input = consoleInput.nextLine();
            log.info("Question: " + input);

            if (input.equals("exit"))
            {
                break;
            }

            double simint = 0;
            int simpos = 0;

            for (int i = 0; i < bioData.size(); i++)
            {
                if (simint < Similarity.compare(input, bioData.get(i).question))
                {
                    simpos = i;
                    simint = Similarity.compare(input, bioData.get(i).question);
                    log.info("New veracity record: " + simint);
                }
            }

            log.info(bioData.get(simpos).answer);
        }

        log.info("Exit the application");
        consoleInput.close();

        System.exit(0);
    }
}
