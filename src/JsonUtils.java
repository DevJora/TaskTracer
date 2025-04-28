import java.sql.CallableStatement;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Convertit une liste de task en  chaine json.
     * @param tasks
     * @return
     */
    public static String parseJson(List<Task> tasks) {

        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for(int  i = 0; i < tasks.size(); i++) {
            sb.append("{\n");
            sb.append("\"id\":").append(tasks.get(i).getId()).append(",\n");
            sb.append("\"description\": \"").append(escape(tasks.get(i).getDescription())).append("\",\n");
            sb.append("\"status\": \"").append(escape(tasks.get(i).getStatus())).append("\",\n");
            sb.append("\"createdAt\": \"").append((tasks.get(i).getCreatedAt())).append("\",\n");
            sb.append("\"updatedAt\": \"").append((tasks.get(i).getUpdatedAt())).append("\",\n");
            sb.append("}");
            if(i < tasks.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Parse une chaine json en liste de tâches.
     * @param json
     * @return
     */
    public static List<Task> parseTasks(String json) {
        List<Task> tasks = new ArrayList<>();
        json = json.trim();
        if(!json.startsWith("[") || !json.endsWith("]")) return tasks;

        //supprime les crochets.
        json = json.substring(1, json.length() - 1).trim();

        // Sépare les objets json.
        String[] items = json.split("\\},\\s*\\{");

        for(String item: items){
            item = item.trim();
            if (!item.startsWith("{")) item = "{" + item;
            if (!item.endsWith("}")) item = item + "}";

            Map<String, String> map = parseJsonObject(item);
            try {
                int id = Integer.parseInt(map.get("id"));
                String description = map.get("description");
                String status = map.get("status");
                Instant createdAt = Instant.parse(map.get("createdAt").replace("\"", "").replace(",", "").trim());
                Instant updatedAt = Instant.parse(map.get("updatedAt").replace("\"", "").replace(",", "").trim());

                Task task = new Task(id, description);
                task.setStatus(status);
                task.setCreatedAt(createdAt);
                task.setUpdatedAt(updatedAt);

                tasks.add(task);
            }catch (Exception e){
                System.out.println("Erreur: parsing de tâche. ("+e.getMessage()+").");
            }
        }

        return tasks;
    }

    /**
     * Parse un objet Json en map clé-valeur
     * @param json
     * @return
     */
    private static Map<String, String> parseJsonObject(String json) {
        Map<String, String> map = new HashMap<>();
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);

        String[] pairs = json.split(",\\s*\"");

        for (String pair : pairs) {
            pair = pair.trim();
            if (pair.startsWith("\"")) pair = pair.substring(1);
            String[] kv = pair.split("\":\\s*", 2);
            if (kv.length == 2) {
                String key = kv[0];
                String value = kv[1];
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                map.put(key, value);
            }
        }

        return map;
    }

    /**
     * Échappe les guillemets dans les chaînes
     * @param s
     * @return
     */
    public static String escape(String s) {
        return s.replace("\"", "\\\"");
    }
}



