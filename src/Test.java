import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        //test de la conversion du json en liste de taches
        testParseTask();

        //test de la conversion de liste de taches en json
        testParseJson();

    }

    public static void testParseTask(){
        String json = "[\n" +
                "{\n" +
                "\"id\":1,\n" +
                "\"description\": \"Acheter la litière.\",\n" +
                "\"status\": \"todo\",\n" +
                "\"createdAt\": \"2025-04-27T12:24:31.444722100Z\",\n" +
                "\"updatedAt\": \"2025-04-27T12:24:31.444722100Z\",\n" +
                "}\n" +
                "]";
        if(!JsonUtils.parseTasks(json).isEmpty() ) {
            System.out.println("Conversion en liste de taches fonctionnelle.");
        }else {
            System.out.println("Conversion en liste de taches Non fonctionnel.");
        }
    }

    public static void testParseJson(){
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(2, "test"));
        String json = JsonUtils.parseJson(tasks);
        if(!json.isEmpty()) {
            System.out.println("Conversion en json fonctionnelle.");
        }else {
            System.out.println("Conversion en json Non fonctionnel.");
        }
    }
}
