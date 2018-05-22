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

	public int getDataVersion()
	{
		try
		{
			ResultSet resultSet = this.statement.executeQuery("SELECT `value` FROM `dbconfig` WHERE `field` = 'last_data_version';");

			while (resultSet.next())
			{
				return resultSet.getInt("value");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	public Vector<BioData> getAllData()
	{
		Vector<BioData> retData = new Vector<BioData>();

		try
		{
			log.info("Getting BioData from Database");
			ResultSet resultSet = this.statement.executeQuery("SELECT * FROM `questions`;");

			while (resultSet.next())
			{
				retData.add(new BioData(resultSet.getString("question"), resultSet.getString("answer")));
			}
		}
		catch (SQLException e)
		{
			log.error("Could not connect to Database");
			e.printStackTrace();
		}

		log.warn("Saved BioData in local");
		return retData;
	}
	
	public int getVeracity(String chatID)
	{
		try
		{
			ResultSet resultSet = this.statement.executeQuery("SELECT veracity_percent FROM `chatVeracity` WHERE chatID ='" + chatID + "';");
			
			while (resultSet.next())
			{
				return resultSet.getInt("veracity_percent");
			}
			
			return -1;
			
			/*
			if(resultSet.next() == false)
			{
				return -1;
			}
			else
			{
				log.info("Else - getVeracity");
				//resultSet.beforeFirst();
				return resultSet.getInt("veracity_percent");
			}
			*/
		}
		catch(SQLException e)
		{
			log.error("Could not connect to Database");
			e.printStackTrace();
		}
		return -1;
	}
	
	public String updateVeracity(String chatID, String veracity)
	{
		log.info("VERACITY: TRACE1" + getVeracity(chatID));
		if(getVeracity(chatID) == -1)
		{
			log.info("VERACITY: REGISTER VERACITY");
			setVeracity(chatID, veracity);
			return "Veracidad personal registrada";
		}
		else
		{
			log.info("VERACITY: UPDATING VERACITY");
			try
			{
				this.statement.executeUpdate("UPDATE chatveracity SET veracity_percent = " + veracity + " WHERE chatID = '" + chatID + "';");
				return "Veracidad personal actualizada";
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return "No es posible cambiar la veracidad en este momento";
	}
	
	public String setVeracity(String chatID, String string)
	{
		try
		{
			this.statement.executeUpdate("INSERT INTO chatVeracity (chatID, veracity_percent) VALUES('" + chatID + "', " + string + ");");
			return "Veracidad cambiada correctamente";
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return "No es posible cambiar la veracidad en este momento";
	}

}