package projet_devops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataFrameStatistics {
    
    /**
     * Calcule la moyenne d'une colonne numérique
     * @param column La colonne à analyser
     * @return La moyenne ou null si la colonne n'est pas numérique
     */
    public Double mean(DataColumn<?> column) {
        if (!isNumericColumn(column)) {
            return null;
        }

        double sum = 0;
        int count = 0;

        for (Object value : column.getValues()) {
            if (value != null) {
                sum += ((Number) value).doubleValue();
                count++;
            }
        }

        return count > 0 ? sum / count : null;
    }

    /**
     * Calcule la médiane d'une colonne numérique
     * @param column La colonne à analyser
     * @return La médiane ou null si la colonne n'est pas numérique
     */
    public Double median(DataColumn<?> column) {
        if (!isNumericColumn(column)) {
            return null;
        }

        List<Double> values = new ArrayList<>();
        for (Object value : column.getValues()) {
            if (value != null) {
                values.add(((Number) value).doubleValue());
            }
        }

        if (values.isEmpty()) {
            return null;
        }

        Collections.sort(values);
        int size = values.size();
        if (size % 2 == 0) {
            return (values.get(size / 2 - 1) + values.get(size / 2)) / 2;
        } else {
            return values.get(size / 2);
        }
    }

    /**
     * Trouve la valeur minimale d'une colonne numérique
     * @param column La colonne à analyser
     * @return Le minimum ou null si la colonne n'est pas numérique
     */
    public Double min(DataColumn<?> column) {
        if (!isNumericColumn(column)) {
            return null;
        }

        Double min = null;
        for (Object value : column.getValues()) {
            if (value != null) {
                double current = ((Number) value).doubleValue();
                if (min == null || current < min) {
                    min = current;
                }
            }
        }
        return min;
    }

    /**
     * Trouve la valeur maximale d'une colonne numérique
     * @param column La colonne à analyser
     * @return Le maximum ou null si la colonne n'est pas numérique
     */
    public Double max(DataColumn<?> column) {
        if (!isNumericColumn(column)) {
            return null;
        }

        Double max = null;
        for (Object value : column.getValues()) {
            if (value != null) {
                double current = ((Number) value).doubleValue();
                if (max == null || current > max) {
                    max = current;
                }
            }
        }
        return max;
    }

    /**
     * Calcule l'écart-type d'une colonne numérique
     * @param column La colonne à analyser
     * @return L'écart-type ou null si la colonne n'est pas numérique
     */
    public Double standardDeviation(DataColumn<?> column) {
        Double mean = mean(column);
        if (mean == null) {
            return null;
        }

        double sumSquaredDiff = 0;
        int count = 0;

        for (Object value : column.getValues()) {
            if (value != null) {
                double val = ((Number) value).doubleValue();
                sumSquaredDiff += Math.pow(val - mean, 2);
                count++;
            }
        }

        return count > 0 ? Math.sqrt(sumSquaredDiff / count) : null;
    }

    /**
     * Vérifie si une colonne est numérique
     * @param column La colonne à vérifier
     * @return true si la colonne est numérique
     */
    private boolean isNumericColumn(DataColumn<?> column) {
        return column.getDataType() == Integer.class || 
               column.getDataType() == Double.class;
    }

    /**
     * Affiche un résumé statistique complet d'une colonne
     * @param column La colonne à analyser
     */
    public void summarize(DataColumn<?> column) {
        System.out.println("Statistiques pour la colonne : " + column.getColumnName());
        if (isNumericColumn(column)) {
            System.out.println("Moyenne: " + formatValue(mean(column)));
            System.out.println("Médiane: " + formatValue(median(column)));
            System.out.println("Minimum: " + formatValue(min(column)));
            System.out.println("Maximum: " + formatValue(max(column)));
            System.out.println("Écart-type: " + formatValue(standardDeviation(column)));
        } else {
            System.out.println("Type: " + column.getDataType().getSimpleName());
            System.out.println("Nombre de valeurs: " + column.size());
        }
        System.out.println();
    }

    String formatValue(Double value) {
        return value != null ? String.format("%.2f", value) : "N/A";
    }
}