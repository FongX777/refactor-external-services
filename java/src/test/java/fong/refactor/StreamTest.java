package fong.refactor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ref: https://dzone.com/articles/how-to-use-map-filter-collect-of-stream-in-java-8
 * ref: https://blog.csdn.net/dalinsi/article/details/78136031
 */
public class StreamTest {

    ArrayList<Person> people = new ArrayList<>(Arrays.asList(
            new Person("Sam", "20"),
            new Person("David", "30"),
            new Person("Yammy", "12")
    ));

    @Test
    public void test_filter_even_numbers() {
        List<String> numbers = Arrays.asList("1", "2", "3", "4", "5", "6");

        System.out.println("original list: " + numbers);

        List<Integer> even = numbers.stream()
                .map(s -> Integer.valueOf(s))
                .filter(number -> number % 2 == 0)
                .collect(Collectors.toList());

        assertEquals(even.size(), 3);
        assertEquals(even.get(0), 2);
        assertEquals(even.get(1), 4);
        assertEquals(even.get(2), 6);
    }

    @Test
    public void test_map_object_to_string_type() {

        List<String> introductions = people.stream()
                .map(s -> s.getSelfIntroduction())
                .collect(Collectors.toList());

        assertEquals(introductions.size(), 3);
        assertEquals(introductions.get(1), "Hi I'm David, living 30 years.");

    }

    @Test
    public void find_the_element() {
        Optional<Person> samOptional = people.stream()
                .filter(p -> p.name.equals("Sam"))
                .findFirst();

        assertEquals(samOptional.isPresent(), true);
        assertEquals(samOptional.isEmpty(), false);

        Person sam = samOptional.get();
        assertTrue(sam.name.equals("Sam"));
    }

    @Test
    public void read_file_parse_json_stream_to_list() {
        List<JSONObject> list =
                (new JSONArray(FileIOTest.readSystemResource("test.json"))).toList().stream().map(object -> new JSONObject((Map) object)).collect(Collectors.toList());

        assertEquals(list.size(), 3);
        assertEquals(list.get(0).getString("id"), "0");
        assertEquals(list.get(0).getInt("num"), 0);
        assertEquals(list.get(1).getString("id"), "1");
        assertEquals(list.get(1).getInt("num"), 1);
        assertEquals(list.get(2).getString("id"), "2");
        assertEquals(list.get(2).getInt("num"), 2);


    }

    private class Person {
        public String name;
        public String age;

        public Person(String name, String age) {
            this.name = name;
            this.age = age;
        }

        public String getSelfIntroduction() {
            return String.format("Hi I'm %s, living %s years.", this.name, this.age);
        }

    }
}


