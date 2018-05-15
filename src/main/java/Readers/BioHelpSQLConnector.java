package Readers;

import java.sql.DriverManager;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Vector;

import org.apache.log4j.Logger;

import Holders.BioData;

public class BioHelpSQLConnector
{
	private Connection connect = null;
	private Statement statement = null;
	Logger log = Logger.getLogger("BioHelpSQLConnector");

	public BioHelpSQLConnector(String host, String user, String pass, String dbname)
	{
		try
		{
			log.info("Connecting to SQL Server: " + host);
			Class.forName("com.mysql.cj.jdbc.Driver");

			this.connect = DriverManager
					.getConnection("jdbc:mysql://" + host + "/" + dbname + "?user=" + user + "&password=" + pass
							+ "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
			this.statement = this.connect.createStatement();
			log.info("Connected with SQL Server");
		}
		catch (SQLException e)
		{
			log.warn("Cannot connect to SQL Server. Printing info");
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			log.warn("Cannot connect to SQL Server. Printing info");
			e.printStackTrace();
		}
	}

	public void close()
	{
		try
		{
			this.statement.close();
			this.connect.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public Vector<BioData> getAllData()
	{
		Vector<BioData> retData = new Vector<BioData>();

		try
		{
			log.warn("Getting BioData from Database");
			ResultSet resultSet = this.statement.executeQuery("SELECT * FROM `questions`;");

			while (resultSet.next())
			{
				retData.add(new BioData(resultSet.getString("question"), resultSet.getString("answer")));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		log.warn("Saved BioData in local");
		return retData;
	}
}
