package projet_devops;

import java.util.ArrayList;

public class Selection {
    
    @SuppressWarnings("unchecked")
    /**
     * Sélectionne un sous-ensemble de lignes d'un DataFrame à partir de leurs indices
     * 
     * @param df Le DataFrame source
     * @param start L'index de début de la sélection (inclus)
     * @param end L'index de fin de la sélection (inclus)
     * @return Un nouveau DataFrame contenant uniquement les lignes sélectionnées
     * @throws IllegalArgumentException si l'index de début est supérieur à l'index de fin ou si le DataFrame est vide
     * @throws IndexOutOfBoundsException si les index sont hors limites
     */

    public <T> DataFrame selectLines(DataFrame df, int start, int end) {
        DataFrame newDf = new DataFrame();

        if(start> end){
            throw new IllegalArgumentException("L'index de début doit être inférieur à l'index de fin.");
        }
        if (start < 0 || end >= df.getRowCount()) {
            throw new IndexOutOfBoundsException("Index hors limites");
        }
        if (df.getRowCount() == 0) {
            throw new IllegalArgumentException("Le DataFrame est vide");
        }


        for (String columnName : df.getColumnNames()) {

            DataColumn<T> originalColumn = (DataColumn<T>) df.getColumn(columnName);
            DataColumn<T> indexColumn = new DataColumn<>(columnName, originalColumn.getDataType());
            
            for (int i = start; i <= end; i++) {
                T value = originalColumn.get(i);
                indexColumn.add(value);
            }

            newDf.addColumn(columnName, indexColumn);
        }

        return newDf;
    }  


    @SuppressWarnings("unchecked")
    /**
     * Sélectionne un sous-ensemble de colonnes d'un DataFrame à partir de leurs noms
     * 
     * @param df Le DataFrame source
     * @param columns La liste des noms de colonnes à sélectionner
     * @return Un nouveau DataFrame contenant uniquement les colonnes sélectionnées
     * @throws IllegalArgumentException si la liste des colonnes est vide ou si une colonne n'existe pas
     */

    public <T> DataFrame selectColumns(DataFrame df, ArrayList<String> columns) {
        DataFrame newDf = new DataFrame();

        if (columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException("La liste des colonnes ne peut pas être vide");
        }

        for (String columnName : columns) {
            if (!df.getColumnNames().contains(columnName)) {
                throw new IllegalArgumentException("Colonne invalide : " + columnName);
            }
        }

        for (int i = 0; i < columns.size(); i++) {
                DataColumn<T> originalColumn = (DataColumn<T>) df.getColumn(columns.get(i));
                
                newDf.addColumn(columns.get(i), originalColumn);
        }

        return newDf;
    }  
}
