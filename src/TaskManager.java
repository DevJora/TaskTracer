import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static final String FILE_PATH = "tasks.json";
    private List<Task> tasks;

    public TaskManager() {
        loadTasks();
    }

    /**
     * Chargement des tâches
     */
    public void loadTasks(){
        try {
            Path path = Paths.get(FILE_PATH);
            if(!Files.exists(path)) {
                tasks = new ArrayList<>();
                saveTasks();
            }else {
                String content = new String(Files.readAllBytes(path));
                tasks = JsonUtils.parseTasks(content);
                System.out.println(tasks);
            }
        }catch (IOException e){
            System.out.println("Erreur : chargement des tâches impossible. (" + e.getMessage() + ").");
            tasks = new ArrayList<>();
        }
    }

    /**
     * Mettre à jour la liste de tâches dans le fichier json.
     */
    public void saveTasks(){
        try {
            Files.write(Paths.get(FILE_PATH), JsonUtils.parseJson(tasks).getBytes());
        } catch (IOException e) {
            System.out.println("Erreur : sauvegarde impossible (" + e.getMessage() + ").");
        }
    }

    /**
     * Ajouter une nouvelle tâche.
     * @param description
     */
    public void addTask(String description) {

        int id = tasks.size()+1;
        Task newTask = new Task(id, description);
        tasks.add(newTask);
        saveTasks();
        System.out.println("Tâche ajoutée avec succès (ID: " + id + ").");
    }

    /**
     * Supprimer une tâche.
     * @param id
     */
    public void removeTask(int id) {
        Task task = findTaskById(id);
        if(task != null) {
            tasks.remove(task);
            saveTasks();
            System.out.println("Tâche suprrimée avec succès (ID: " + id + ").");
        }else {
            System.out.println("La tâche sélectionnée n'existe pas. ");
        }
    }

    /**
     * Modifier la description d'une tâche.
     * @param id
     * @param newDescription
     */
    public void updateTask(int id, String newDescription){
        Task task = findTaskById(id);
        if(task != null) {
            task.setDescription(newDescription);
            task.setUpdatedAt(Instant.now());
            saveTasks();
            System.out.println("Tâche mise à jour avec succès. (ID: "+id+").");
        }else {
            System.out.println("Tâche non trouvée (ID: " + id + ")");
        }
    }

    /**
     * Marquer une tâche en progès.
     * @param id
     */
    public void markInProgress(int id){
        Task task = findTaskById(id);

        if(task != null){
            task.setStatus("progress");
            task.setUpdatedAt(Instant.now());
            saveTasks();
            System.out.println("Tâche désormais en cours (ID: " + id + ")");
        }else {
            System.out.println("Tâche non trouvée (ID: " + id + ")");
        }
    }

    /**
     * Marquer une tâche terminée.
     * @param id
     */
    public void markDone(int id){
        Task task = findTaskById(id);

        if(task != null){
            task.setStatus("done");
            task.setUpdatedAt(Instant.now());
            saveTasks();
            System.out.println("Tâche désormais terminé (ID: " + id + ")");
        }else {
            System.out.println("Tâche non trouvée (ID: " + id + ")");
        }
    }

    /**
     * Lister les tâches en fonction des status.
     * @param arg
     */
    public void listTasks(String arg) {
        List<Task> filteredTasks = filterTasksByStatus(arg);
        switch (arg.toLowerCase()){
            case "todo":
            case "progress":
            case "done":
                filteredTasks = filterTasksByStatus(arg.toLowerCase());
                break;
            case "all":
            default:
                filteredTasks = tasks;
                break;
        }

        if(filteredTasks.isEmpty()) System.out.println("Aucune tâche actuellement avec ce status.");
        else
            for(Task task : filteredTasks) System.out.println(formatTask(task));
    }

    /**
     * retourne la tache par id.
     * @param id
     * @return
     */
    private Task findTaskById(int id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    /**
     * Filtrage des tâches en fonction des status.
     * @param status
     * @return
     */
    private List<Task> filterTasksByStatus(String status) {
        List<Task> filtered = new ArrayList<>();
        for(Task task : tasks) {
            if(task.getStatus().equals(status)) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    /**
     * Formatage de tâche.
     * @param task
     * @return
     */
    private String formatTask(Task task) {
        return String.format("ID: %d | Description: %s | Statut: %s | Créée le: %s | Mise à jour le: %s",
                task.getId(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt().toString(),
                task.getUpdatedAt().toString());
    }

}
