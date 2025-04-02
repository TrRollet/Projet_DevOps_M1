package projet_devops;

import java.io.IOException;

/**
 * Classe principale pour exécuter le programme
 */
public class Main {

    /**
     * Constructeur par défaut de la classe Main.
     * Cette classe utilitaire ne nécessite pas d'instanciation.
     */
    private Main() {
        throw new IllegalStateException("Classe utilitaire");
    }

    /**
     * Méthode principale pour exécuter le programme
     * 
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        try {
            // MAIN TEMPORAIRE
            if (args.length < 1) {
                System.err.println("Erreur: Veuillez spécifier le chemin du fichier CSV");
                System.exit(1);
            }

            DataFrame df = DataFrame.fromCSV(args[0]);

            System.out.println("DataFrame chargé avec succès !");
            System.out.println("Nombre de lignes : " + df.getRowCount());
            System.out.println("Nombre de colonnes : " + df.getColumnCount());
            System.out.println("\nNom et type de chaque colonne :");

            for (int i = 0; i < df.getColumnCount(); i++) {
                DataColumn<?> column = df.getColumn(i);
                System.out.println("Colonne " + (i + 1) + ": " + column.getColumnName() + " (" + column.getDataType().getSimpleName() + ")");
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            System.exit(1);
        }
    }
}