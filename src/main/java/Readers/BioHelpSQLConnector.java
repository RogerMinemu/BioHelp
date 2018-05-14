package Readers;

import java.sql.DriverManager;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Vector;

import Holders.BioData;

public class BioHelpSQLConnector
{
	private Connection connect = null;
	private Statement statement = null;

	public BioHelpSQLConnector(String host, String user, String pass, String dbname)
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");

			this.connect = DriverManager
					.getConnection("jdbc:mysql://" + host + "/" + dbname + "?user=" + user + "&password=" + pass
							+ "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
			this.statement = this.connect.createStatement();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
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

		return retData;
	}
}
