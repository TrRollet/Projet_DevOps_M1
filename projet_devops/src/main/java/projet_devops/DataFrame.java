package projet_devops;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
        this.columns = new LinkedHashMap<>();
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
	 * Retourne la colonne à l'index spécifié
	 * 
	 * @param index L'index de la colonne
	 * @return La colonne correspondante
	 * @throws IndexOutOfBoundsException Si l'index est en dehors des limites
	 */
	public DataColumn<?> getColumn(int index) {
		if (index < 0 || index >= columns.size()) {
			throw new IndexOutOfBoundsException("Index de colonne invalide");
		}

		String columnName = (String) columns.keySet().toArray()[index];
		return columns.get(columnName);
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
			// Première ligne: en-têtes
			String[] headers = br.readLine().split(",");
			Map<String, DataColumn<?>> tempColumns = new LinkedHashMap<>();
			
			//========= Détection du type de chaque colonne =========
			List<String[]> allData = new ArrayList<>();
			String line;
			while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
				allData.add(line.split(","));
			}
		
			for (int col = 0; col < headers.length; col++) {
				String header = headers[col].trim();
				boolean allInteger = true;
				boolean allDouble = true;
				
				// On prend le type le plus global
				// ex: 10 et 10.5 -> Double, trente et 20 -> String, 20.0, trente, 20 -> String
				for (String[] row : allData) {
					if (col >= row.length || row[col].trim().isEmpty()) {
						continue;
					}
					String value = row[col].trim();
					
					if (allInteger && !isInteger(value)) {
						allInteger = false;
					}
					if (allDouble && !isDouble(value)) {
						allDouble = false;
					}
					
					if (!allInteger && !allDouble) {
						break; // si pas un nombre alors String
					}
				}
				
				// Création de la colonne avec le bon type
				DataColumn<?> column;
				if (allInteger) {
					column = new DataColumn<>(header, Integer.class);
				} else if (allDouble) {
					column = new DataColumn<>(header, Double.class);
				} else {
					column = new DataColumn<>(header, String.class);
				}
				tempColumns.put(header, column);
			}
			
			//========== Remplissage des colonnes =========
			for (String[] values : allData) {
				for (int i = 0; i < headers.length; i++) {
					String header = headers[i].trim();
					String value = i < values.length ? values[i].trim() : "";
					addValueToColumn(tempColumns.get(header), value);
				}
			}
			
			//========= Ajout des colonnes au DataFrame =========
			for (String header : headers) {
				df.addColumn(header.trim(), tempColumns.get(header.trim()));
			}
		}
		return df;
	}
		
	/**
	 * Vérifie si une chaîne de caractères peut être convertie en entier
	 * 
	 * @param value La chaîne à vérifier
	 * @return true si la chaîne peut être convertie en entier, false sinon
	 */
	private static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
		
	/**
	 * Vérifie si une chaîne de caractères peut être convertie en double
	 * 
	 * @param value La chaîne à vérifier
	 * @return true si la chaîne peut être convertie en double, false sinon
	 */
	private static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Ajoute une valeur à la colonne spécifiée, en cas d'erreur la valeur null est ajoutée
	 *
	 * @param column La colonne à laquelle ajouter la valeur
	 * @param value  La valeur à ajouter
	 */
	@SuppressWarnings("unchecked")
	private static void addValueToColumn(DataColumn<?> column, String value) {
		if (value.isEmpty()) {
			((DataColumn<Object>)column).add(null);
			return;
		}
		
		try {
			if (column.getDataType() == Integer.class) {
				try {
					((DataColumn<Integer>)column).add(Integer.parseInt(value));
				} catch (NumberFormatException e) {
					((DataColumn<Integer>)column).add(null);
				}
			} else if (column.getDataType() == Double.class) {
				try {
					((DataColumn<Double>)column).add(Double.parseDouble(value));
				} catch (NumberFormatException e) {
					((DataColumn<Double>)column).add(null);
				}
			} else {
				((DataColumn<String>)column).add(value);
			}
		} catch (Exception e) {
			((DataColumn<Object>)column).add(null);
		}
	}
}