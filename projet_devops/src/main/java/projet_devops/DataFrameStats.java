package projet_devops;

public class DataFrameStats {
    
    /**
     * Calcule la moyenne d'une colonne numérique
     * @param column La colonne à analyser
     * @return La moyenne des valeurs ou null si la colonne n'est pas numérique
     */
    @SuppressWarnings("unchecked")
    public Double mean(DataColumn<?> column) {
        if (column.getDataType() != Integer.class && column.getDataType() != Double.class) {
            return null;
        }

        double sum = 0;
        int count = 0;

        if (column.getDataType() == Integer.class) {
            DataColumn<Integer> intColumn = (DataColumn<Integer>) column;
            for (Integer value : intColumn.getValues()) {
                if (value != null) {
                    sum += value;
                    count++;
                }
            }
        } else {
            DataColumn<Double> doubleColumn = (DataColumn<Double>) column;
            for (Double value : doubleColumn.getValues()) {
                if (value != null) {
                    sum += value;
                    count++;
                }
            }
        }

        return count > 0 ? sum / count : null;
    }

    /**
     * Affiche un résumé statistique du DataFrame
     * @param df Le DataFrame à analyser
     */
    public void summary(DataFrame df) {
        for (String columnName : df.getColumnNames()) {
            DataColumn<?> column = df.getColumn(columnName);
            System.out.println("Colonne: " + columnName);
            
            if (column.getDataType() == Integer.class || column.getDataType() == Double.class) {
                Double meanValue = mean(column);
                System.out.println("  Moyenne: " + (meanValue != null ? String.format("%.2f", meanValue) : "N/A"));
                // Ajouter d'autres statistiques selon vos besoins
            } else {
                System.out.println("  Type: " + column.getDataType().getSimpleName());
                System.out.println("  Nombre de valeurs: " + column.size());
            }
            System.out.println();
        }
    }

    /**
     * Crée un nouveau DataFrame contenant uniquement les colonnes spécifiées
     * @param df Le DataFrame source
     * @param labels Les labels des colonnes à sélectionner
     * @return Un nouveau DataFrame avec les colonnes sélectionnées
     * @throws IllegalArgumentException si un label n'existe pas
     */
    public DataFrame selectColumns(DataFrame df, String... labels) {
        DataFrame result = new DataFrame();
        
        for (String label : labels) {
            DataColumn<?> column = df.getColumn(label);
            if (column == null) {
                throw new IllegalArgumentException("La colonne '" + label + "' n'existe pas");
            }
            result.addColumn(label, column);
        }
        
        return result;
    }

    /**
     * Affiche les statistiques pour un sous-ensemble de colonnes
     * @param df Le DataFrame source
     * @param labels Les labels des colonnes à analyser
     */
    public void summaryForColumns(DataFrame df, String... labels) {
        DataFrame selected = selectColumns(df, labels);
        summary(selected);
    }
}