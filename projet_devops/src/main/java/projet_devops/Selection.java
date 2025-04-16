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

            newDf.addColumn(indexColumn);
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
                
                newDf.addColumn(originalColumn);
        }

        return newDf;
    }

    /**
     * Évalue une expression mathématique sur les colonnes numériques
     * @param df Le DataFrame source
     * @param expression L'expression à évaluer (ex: "age + salary", "age * 2")
     * @return Une nouvelle colonne contenant le résultat
     */
    public DataColumn<?> eval(DataFrame df, String expression) {
        // Diviser l'expression en tokens
        String[] tokens = expression.split("\\s+");
        if (tokens.length != 3) {
            throw new IllegalArgumentException("L'expression doit être de la forme 'colonne opérateur valeur' ou 'colonne opérateur colonne'");
        }

        String col1Name = tokens[0];
        String operator = tokens[1];
        String col2OrValue = tokens[2];

        DataColumn<?> col1 = df.getColumn(col1Name);
        if (col1 == null) {
            throw new IllegalArgumentException("Colonne non trouvée: " + col1Name);
        }

        // Vérifier que la première colonne est numérique
        if (col1.getDataType() != Integer.class && col1.getDataType() != Double.class) {
            throw new IllegalArgumentException("La colonne doit être numérique");
        }

        DataColumn<Double> result = new DataColumn<>("result", Double.class);

        // Essayer de parser col2OrValue comme un nombre
        try {
            double value = Double.parseDouble(col2OrValue);
            // Opération avec une valeur constante
            for (Object val : col1.getValues()) {
                if (val == null) {
                    result.add(null);
                    continue;
                }
                double num = val instanceof Integer ? ((Integer) val).doubleValue() : (Double) val;
                result.add(performOperation(num, value, operator));
            }
        } catch (NumberFormatException e) {
            // Opération entre deux colonnes
            DataColumn<?> col2 = df.getColumn(col2OrValue);
            if (col2 == null) {
                throw new IllegalArgumentException("Colonne non trouvée: " + col2OrValue);
            }
            if (col2.getDataType() != Integer.class && col2.getDataType() != Double.class) {
                throw new IllegalArgumentException("La deuxième colonne doit être numérique");
            }

            for (int i = 0; i < col1.size(); i++) {
                Object val1 = col1.getValues().get(i);
                Object val2 = col2.getValues().get(i);
                if (val1 == null || val2 == null) {
                    result.add(null);
                    continue;
                }
                double num1 = val1 instanceof Integer ? ((Integer) val1).doubleValue() : (Double) val1;
                double num2 = val2 instanceof Integer ? ((Integer) val2).doubleValue() : (Double) val2;
                result.add(performOperation(num1, num2, operator));
            }
        }

        return result;
    }

    private Double performOperation(double a, double b, String operator) {
        switch (operator) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return b == 0 ? null : a / b;
            default:
                throw new IllegalArgumentException("Opérateur non supporté: " + operator);
        }
    }

    /**
     * Crée un masque booléen basé sur une condition
     * @param df Le DataFrame source
     * @param columnName Le nom de la colonne à vérifier
     * @param operator L'opérateur de comparaison (">", "<", ">=", "<=", "==", "!=")
     * @param value La valeur de comparaison
     * @return Une liste de booléens représentant le masque
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Boolean> createBooleanMask(DataFrame df, String columnName, String operator, Object value) {
        DataColumn<?> column = df.getColumn(columnName);
        if (column == null) {
            throw new IllegalArgumentException("Colonne non trouvée: " + columnName);
        }

        ArrayList<Boolean> mask = new ArrayList<>();
        for (Object val : column.getValues()) {
            if (val == null) {
                mask.add(false);
                continue;
            }

            Comparable<Object> comparableVal = (Comparable<Object>) val;
            Comparable<Object> comparableValue = (Comparable<Object>) value;

            boolean result;
            switch (operator) {
                case ">":
                    result = comparableVal.compareTo(comparableValue) > 0;
                    break;
                case "<":
                    result = comparableVal.compareTo(comparableValue) < 0;
                    break;
                case ">=":
                    result = comparableVal.compareTo(comparableValue) >= 0;
                    break;
                case "<=":
                    result = comparableVal.compareTo(comparableValue) <= 0;
                    break;
                case "==":
                    result = comparableVal.compareTo(comparableValue) == 0;
                    break;
                case "!=":
                    result = comparableVal.compareTo(comparableValue) != 0;
                    break;
                default:
                    throw new IllegalArgumentException("Opérateur non supporté: " + operator);
            }
            mask.add(result);
        }
        return mask;
    }

    /**
     * Filtre le DataFrame en utilisant un masque booléen
     * @param df Le DataFrame source
     * @param mask Le masque booléen à appliquer
     * @return Un nouveau DataFrame contenant uniquement les lignes où le masque est true
     */
    @SuppressWarnings("unchecked")
    public <T> DataFrame filterByMask(DataFrame df, ArrayList<Boolean> mask) {
        if (mask.size() != df.getRowCount()) {
            throw new IllegalArgumentException("La taille du masque doit correspondre au nombre de lignes");
        }

        DataFrame newDf = new DataFrame();
        for (String columnName : df.getColumnNames()) {
            DataColumn<T> originalColumn = (DataColumn<T>) df.getColumn(columnName);
            DataColumn<T> filteredColumn = new DataColumn<>(columnName, originalColumn.getDataType());
            
            for (int i = 0; i < mask.size(); i++) {
                if (mask.get(i)) {
                    filteredColumn.add(originalColumn.get(i));
                }
            }
            
            newDf.addColumn(filteredColumn);
        }
        return newDf;
    }
}
