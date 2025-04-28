public class TaskTracker {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        if(args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0];
        switch (command) {
            case "add":
                if(args.length < 2) {
                    System.out.println("Veuillez fournir une description pour la tâche.");
                }else {
                    String description = args[1];
                    manager.addTask(description);
                }
                break;
            case "delete":
                int id = Integer.parseInt(args[1]);
                manager.removeTask(id);
                break;
            case "update":
                if(args.length < 3) System.out.println("Veuillez fournir l'ID et la description de la tâche à modifier.");
                else {
                    try {
                        int i = Integer.parseInt(args[1]);
                        String newDescription = args[2];
                        manager.updateTask(i, newDescription);
                    }catch (NumberFormatException e) {
                        System.out.println("ID invalide.");
                    }
                }
                break;
            case "progress":
                if(args.length < 2) System.out.println("Veuillez fournir l'ID de la tâche à marquer comme en cours.");
                else {
                    try {
                        int idprogress = Integer.parseInt(args[1]);
                        manager.markInProgress(idprogress);
                    }catch (NumberFormatException e) {
                        System.out.println("ID invalide.");
                    }
                }
                break;
            case "done":
                if(args.length < 2) System.out.println("Veuillez fournir l'ID de la tâche à marquer terminé.");
                else {
                    try {
                        int idDone = Integer.parseInt(args[1]);
                        manager.markDone(idDone);
                    }catch (NumberFormatException e) {
                        System.out.println("ID invalide.");
                    }
                }
                break;
            case "list":
                if(args.length == 1) manager.listTasks("all");
                else {
                    String statuts = args[1];
                    manager.listTasks(statuts);
                }
                break;
            default:
                System.out.println("Commande inconnu : " + command + ".");
                printHelp();
                break;
        }
    }

    /**
     * Affiche l'aide
     */
    public static void printHelp(){
        System.out.println("Utilisation :");
        System.out.println("\t add \"description\"              - Ajouter une nouvelle tâche.");
        System.out.println("update ID \"description\"           - Mettre à jour la description d'une tâche.");
        System.out.println("delete ID                           - Supprimer une tâche.");
        System.out.println("progress ID                          - Supprimer une tâche.");
        System.out.println("done ID                         - Marquer une tâche comme 'en cours'.");
        System.out.println("list [todo|progress|all]         - Lister les tâches selon le statut.");
    }
}