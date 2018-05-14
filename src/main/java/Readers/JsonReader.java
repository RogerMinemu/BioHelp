package Readers;
import com.google.gson.*;

public class JsonReader
{
	private JsonObject json;
	
	public JsonReader(String ruta)
	{
		json = new JsonParser().parse(ruta + "'\'config.json").getAsJsonObject();
	}
	
	public String getField(String field)
	{
		return json.get(field).getAsString();
	}
}
