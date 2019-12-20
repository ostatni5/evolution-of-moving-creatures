package agh.ostatni5.eomc;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadOptionsJSON {
    private Options parsedOptions= null;

    public ReadOptionsJSON()
    {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("options.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            parsedOptions= parseOptions((JSONObject) obj);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private Options parseOptions(JSONObject optionsJSON)
    {
       Options options = new Options();
        for (int i = 0; i < Options.names.length; i++) {
            options.values[i] = ((Long)optionsJSON.get(Options.names[i])).intValue();
        }
        return options;
    }

    public Options getParsedOptions() {
        return parsedOptions;
    }
}
