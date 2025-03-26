package projet_devops;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe représantant un DataFrame
 */
public class DataFrame {
    private Map<String, DataColumn<?>> columns;
    private int rowCount;

	/**
	 * Constructeur de la classe DataFrame initialisant les colonnes et le nombre de lignes
	 */
    public DataFrame() {
        this.columns = new HashMap<>();
        this.rowCount = 0;
    }

	/**
	 * Ajoute une colonne au DataFrame
	 * 
	 * @param name   Le nom de la colonne
	 * @param column La colonne à ajouter
	 * @throws IllegalArgumentException Si le nombre de lignes de la nouvelle colonne ne correspond pas au nombre de lignes existant
	 */
    public void addColumn(String name, DataColumn<?> column) {
        if (columns.isEmpty()) {
            rowCount = column.size();
        } else if (column.size() != rowCount) {
            throw new IllegalArgumentException("La nouvelle colonne doit avoir le même nombre de lignes");
        }
        columns.put(name, column);
    }

	/**
	 * Retourne la colonne correspondant au nom spécifié
	 * 
	 * @param name Le nom de la colonne
	 * @return La colonne correspondante
	 */
    public DataColumn<?> getColumn(String name) {
        return columns.get(name);
    }


    /**
     * Retourne le noms de toutes les colonnes
	 * 
	 * @return Liste de noms des colonnes
	 */
    public List<String> getColumnNames() {
        return new ArrayList<>(columns.keySet());
    }

	/**
	 * Retourne le nombre de lignes du DataFrame
	 * 
	 * @return Le nombre de lignes
	 */
    public int getRowCount() {
        return rowCount;
    }

	/**
	 * Retourne le nombre de colonnes du DataFrame
	 * 
	 * @return Le nombre de colonnes
	 */
    public int getColumnCount() {
        return columns.size();
    }

	/**
	 * Lis un fichier CSV et crée un DataFrame à partir de celui-ci
	 *
	 * @param filename Le nom du fichier CSV
	 * @return Un DataFrame contenant les données du fichier CSV
	 * @throws IOException Si une erreur d'entrée/sortie se produit
	 */
    public static DataFrame fromCSV(String filename) throws IOException {
        DataFrame df = new DataFrame();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String[] headers = br.readLine().split(",");
            Map<String, DataColumn<String>> tempColumns = new HashMap<>();
            
            // Initialiser les colonnes
            for (String header : headers) {
                tempColumns.put(header, new DataColumn<>(header, String.class));
            }

            // Lire les données
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for (int i = 0; i < values.length && i < headers.length; i++) {
                    tempColumns.get(headers[i]).add(values[i]);
                }
            }

            // Ajouter les colonnes au DataFrame
            for (Map.Entry<String, DataColumn<String>> entry : tempColumns.entrySet()) {
                df.addColumn(entry.getKey(), entry.getValue());
            }
        }
        return df;
    }
}