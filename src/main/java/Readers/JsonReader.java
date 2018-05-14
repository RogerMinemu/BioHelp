package Readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.google.gson.*;

public class JsonReader {
	private JsonObject json;

	private String getFileContent(String path) throws FileNotFoundException
	{
		File file = new File(path);
		StringBuilder fileContents = new StringBuilder((int) file.length());

		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");

		try
		{
			while (scanner.hasNextLine())
			{
				fileContents.append(scanner.nextLine() + lineSeparator);
			}

			return fileContents.toString();
		}
		finally
		{
			scanner.close();
		}
	}

	public JsonReader(String ruta)
	{
		String fileContent = "{}";

		try
		{
			fileContent = getFileContent(ruta);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		json = new JsonParser().parse(fileContent).getAsJsonObject();
	}

	public String getField(String field)
	{
		return json.get(field).getAsString();
	}
}
