package fong.refactor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ref: https://stleary.github.io/JSON-java/
 */
public class OrgJSONTest {

    private final String jsonData = """
               [ { "id": "0", "num": 0 }, { "id": "1", "num": 1 }, { "id": "2", "num": 2 } ]
            """;

    @Test
    public void test_parse_json_array() {

        JSONArray jsonArray = new JSONArray(jsonData);

        assertEquals(jsonArray.getJSONObject(0).getString("id"), "0");
        assertEquals(jsonArray.getJSONObject(0).getInt("num"), 0);
        assertEquals(jsonArray.getJSONObject(1).getString("id"), "1");
        assertEquals(jsonArray.getJSONObject(1).getInt("num"), 1);
        assertEquals(jsonArray.getJSONObject(2).getString("id"), "2");
        assertEquals(jsonArray.getJSONObject(2).getInt("num"), 2);
    }


    @Test
    public void test_parse_json_array_with_for_loop() {

        JSONArray jsonArray = new JSONArray(jsonData);

        assertEquals(jsonArray.length(), 3);
        Integer counter = 0;
        for (Object object : jsonArray.toList()) {
            JSONObject jsonObject = new JSONObject((Map) object);
            assertEquals(jsonObject.getString("id"), (counter).toString());
            assertEquals(jsonObject.getInt("num"), counter);
            counter++;
        }
    }

    @Test
    public void test_parse_json_array_to_list_by_lambda() {

        JSONArray jsonArray = new JSONArray(jsonData);
        List<JSONObject> jsonList =
                jsonArray.toList().stream().map(obj -> new JSONObject((Map) obj)).collect(Collectors.toList());

        assertEquals(jsonList.size(), 3);
        assertEquals(jsonList.get(0).getString("id"), "0");
        assertEquals(jsonList.get(0).getInt("num"), 0);
        assertEquals(jsonList.get(1).getString("id"), "1");
        assertEquals(jsonList.get(1).getInt("num"), 1);
        assertEquals(jsonList.get(2).getString("id"), "2");
        assertEquals(jsonList.get(2).getInt("num"), 2);
    }
}
