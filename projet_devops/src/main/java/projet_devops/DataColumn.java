package projet_devops;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une colonne dans un DataFrame
 */
public class DataColumn<T> {
    private String columnName;
    private List<T> values;
    private Class<T> dataType;
    
    /**
     * Constructeur de la classe DataColumn
     * 
     * @param columnName Le nom de la colonne
     * @param dataType   Le type de données de la colonne
     */
    public DataColumn(String columnName, Class<T> dataType) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.values = new ArrayList<>();
    }

    /**
     * Ajoute une valeur à la colonne
     * 
     * @param value La valeur à ajouter
     */
    public void add(T value) {
        values.add(value);
    }

    /**
     * Retourne la valeur à l'index spécifié
     * 
     * @param index L'index de la valeur à retourner
     * @return La valeur à l'index spécifié
     * @throws IndexOutOfBoundsException Si l'index est en dehors des limites de la liste
     */
    public T get(int index) {
        return values.get(index);
    }

    /**
     * Retourne la liste des valeurs
     * 
     * @return La liste des valeurs de la colonne
     */
    public List<T> getValues() {
        return new ArrayList<>(values);
    }

    /**
     * Retourne le nombre de valeurs dans la colonne
     * 
     * @return Le nombre de valeurs dans la colonne
     */
    public int size() {
        return values.size();
    }

    /**
     * Retourne le type de données de la colonne
     * 
     * @return Le type de données de la colonne
     */
    public Class<T> getDataType() {
        return dataType;
    }

    /**
     * Retourne le nom de la colonne
     * 
     * @return Le nom de la colonne
     */
    public String getColumnName() {
        return columnName;
    }
}