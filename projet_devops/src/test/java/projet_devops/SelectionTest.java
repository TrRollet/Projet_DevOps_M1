package projet_devops;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

/**
 * Classe de test pour la classe Selection
 */
public class SelectionTest {
    private DataFrame df;
    private Selection selection;
    private DataFrame dfEmpty;

    /**
     * Initialise les données de test avant chaque test
     */
    @Before
    public void setUp() {
        df = new DataFrame();
        dfEmpty = new DataFrame();
        selection = new Selection();
        
        // Création d'un DataFrame de test
        DataColumn<Integer> col1 = new DataColumn<>("Age", Integer.class);
        col1.add(25);
        col1.add(30);
        col1.add(35);
        col1.add(40);
        
        DataColumn<String> col2 = new DataColumn<>("Nom", String.class);
        col2.add("Alice");
        col2.add("Bob");
        col2.add("Charlie");
        col2.add("David");
        
        df.addColumn(col1);
        df.addColumn(col2);
    }

    /**
     * Teste la sélection d'un sous-ensemble de lignes
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSelectLines() {
        DataFrame result = selection.selectLines(df, 1, 2);
        assertEquals(2, result.getRowCount());
        assertEquals("Bob", result.getColumn("Nom").get(0));
        assertEquals("Charlie", result.getColumn("Nom").get(1));
        assertEquals(30, ((DataColumn<Integer>)result.getColumn("Age")).get(0).intValue());
        assertEquals(35, ((DataColumn<Integer>)result.getColumn("Age")).get(1).intValue());
    }

    /**
     * Teste la sélection d'une seule colonne
     */
    @Test
    public void testSelectColumns() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Nom");
        
        DataFrame result = selection.selectColumns(df, columns);
        assertEquals(1, result.getColumnCount());
        assertEquals(4, result.getRowCount());
        assertTrue(result.getColumnNames().contains("Nom"));
        assertFalse(result.getColumnNames().contains("Age"));
    }

    /**
     * Teste la sélection de plusieurs colonnes
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSelectMultipleColumns() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Age");
        columns.add("Nom");
        
        DataFrame result = selection.selectColumns(df, columns);
        assertEquals(2, result.getColumnCount());
        assertEquals(4, result.getRowCount());
        assertEquals("Alice", result.getColumn("Nom").get(0));
        assertEquals(25, ((DataColumn<Integer>)result.getColumn("Age")).get(0).intValue());
    }

    /**
     * Teste la sélection d'une seule ligne
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSelectSingleLine() {
        DataFrame result = selection.selectLines(df, 1, 1);
        assertEquals(1, result.getRowCount());
        assertEquals("Bob", result.getColumn("Nom").get(0));
        assertEquals(30, ((DataColumn<Integer>)result.getColumn("Age")).get(0).intValue());
    }

    /**
     * Teste la sélection de toutes les lignes
     */
    @Test
    public void testSelectAllLines() {
        DataFrame result = selection.selectLines(df, 0, df.getRowCount() - 1);
        assertEquals(df.getRowCount(), result.getRowCount());
        assertEquals("Alice", result.getColumn("Nom").get(0));
        assertEquals("David", result.getColumn("Nom").get(3));
    }

    /**
     * Teste l'exception lors de la sélection de lignes hors limites
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSelectLinesOutOfBounds() {
        selection.selectLines(df, 5, 6); // Index hors limites
    }

    /**
     * Teste l'exception lors d'une plage de sélection invalide
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSelectLinesInvalidRange() {
        selection.selectLines(df, 2, 1); // début > fin
    }

    /**
     * Teste l'exception lors de la sélection de colonnes avec une liste vide
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSelectColumnsEmpty() {
        ArrayList<String> columns = new ArrayList<>();
        selection.selectColumns(dfEmpty, columns);
    }

    /**
     * Teste l'exception lors de la sélection d'une colonne inexistante
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSelectColumnsInvalidName() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("InvalidColumn");
        selection.selectColumns(df, columns);
    }

    /**
     * Teste la préservation de l'ordre des colonnes lors de la sélection
     */
    @Test
    public void testSelectColumnsPreservesOrder() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Nom");
        columns.add("Age");
        
        DataFrame result = selection.selectColumns(df, columns);
        assertEquals("Nom", result.getColumnNames().get(0));
        assertEquals("Age", result.getColumnNames().get(1));
    }

    /**
     * Teste l'intégrité des données lors de la sélection de colonnes
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSelectColumnsDataIntegrity() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Age");
        
        DataFrame result = selection.selectColumns(df, columns);
        DataColumn<Integer> ageColumn = (DataColumn<Integer>) result.getColumn("Age");
        
        for (int i = 0; i < df.getRowCount(); i++) {
            assertEquals(
                ((DataColumn<Integer>)df.getColumn("Age")).get(i),
                ageColumn.get(i)
            );
        }
    }

    /**
     * Teste l'évaluation avec une constante
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEvalWithConstant() {
        DataColumn<?> result = selection.eval(df, "Age * 2");
        assertEquals(4, result.size());
        assertEquals(50.0, ((DataColumn<Double>)result).get(0), 0.001);
        assertEquals(60.0, ((DataColumn<Double>)result).get(1), 0.001);
        assertEquals(70.0, ((DataColumn<Double>)result).get(2), 0.001);
        assertEquals(80.0, ((DataColumn<Double>)result).get(3), 0.001);
    }

    /**
     * Teste l'évaluation avec deux colonnes
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEvalWithTwoColumns() {
        DataColumn<Integer> salaryColumn = new DataColumn<>("Salary", Integer.class);
        salaryColumn.add(1000);
        salaryColumn.add(2000);
        salaryColumn.add(3000);
        salaryColumn.add(4000);
        df.addColumn(salaryColumn);

        DataColumn<?> result = selection.eval(df, "Age + Salary");
        assertEquals(4, result.size());
        assertEquals(1025.0, ((DataColumn<Double>)result).get(0), 0.001);
        assertEquals(2030.0, ((DataColumn<Double>)result).get(1), 0.001);
        assertEquals(3035.0, ((DataColumn<Double>)result).get(2), 0.001);
        assertEquals(4040.0, ((DataColumn<Double>)result).get(3), 0.001);
    }

    /**
     * Teste l'évaluation de la division
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEvalDivision() {
        DataColumn<?> result = selection.eval(df, "Age / 5");
        assertEquals(4, result.size());
        assertEquals(5.0, ((DataColumn<Double>)result).get(0), 0.001);
        assertEquals(6.0, ((DataColumn<Double>)result).get(1), 0.001);
        assertEquals(7.0, ((DataColumn<Double>)result).get(2), 0.001);
        assertEquals(8.0, ((DataColumn<Double>)result).get(3), 0.001);
    }

    /**
     * Teste l'exception lors d'une expression invalide
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvalInvalidExpression() {
        selection.eval(df, "Age $ 2");
    }

    /**
     * Teste l'exception lors de l'évaluation d'une colonne non numérique
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvalNonNumericColumn() {
        selection.eval(df, "Nom + 2");
    }

    /**
     * Teste l'exception lors de l'évaluation d'une colonne inexistante
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvalInvalidColumnName() {
        selection.eval(df, "InvalidColumn + 2");
    }

    /**
     * Teste l'évaluation avec des valeurs null
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEvalWithNull() {
        // Ajouter une colonne avec des valeurs null
        DataColumn<Integer> testColumn = new DataColumn<>("Test", Integer.class);
        testColumn.add(10);
        testColumn.add(null);
        testColumn.add(30);
        testColumn.add(40);
        df.addColumn(testColumn);

        DataColumn<?> result = selection.eval(df, "Test * 2");
        assertEquals(4, result.size());
        assertEquals(20.0, ((DataColumn<Double>)result).get(0), 0.001);
        assertNull(((DataColumn<Double>)result).get(1));
        assertEquals(60.0, ((DataColumn<Double>)result).get(2), 0.001);
        assertEquals(80.0, ((DataColumn<Double>)result).get(3), 0.001);
    }

    /**
     * Teste l'évaluation d'une division par zéro
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEvalDivisionByZero() {
        DataColumn<?> result = selection.eval(df, "Age / 0");
        assertEquals(4, result.size());
        assertNull(((DataColumn<Double>)result).get(0));
        assertNull(((DataColumn<Double>)result).get(1));
        assertNull(((DataColumn<Double>)result).get(2));
        assertNull(((DataColumn<Double>)result).get(3));
    }

    /**
     * Teste la création d'un masque booléen
     */
    @Test
    public void testCreateBooleanMask() {
        ArrayList<Boolean> mask = selection.createBooleanMask(df, "Age", ">", 30);
        assertEquals(4, mask.size());
        assertEquals(false, mask.get(0)); // 25 > 30
        assertEquals(false, mask.get(1)); // 30 > 30
        assertEquals(true, mask.get(2));  // 35 > 30
        assertEquals(true, mask.get(3));  // 40 > 30
    }

    /**
     * Teste le filtrage par masque booléen
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFilterByMask() {
        ArrayList<Boolean> mask = selection.createBooleanMask(df, "Age", ">", 30);
        DataFrame filtered = selection.filterByMask(df, mask);
        
        assertEquals(2, filtered.getRowCount());
        assertEquals(2, filtered.getColumnCount());
        assertEquals(35, ((DataColumn<Integer>)filtered.getColumn("Age")).get(0).intValue());
        assertEquals(40, ((DataColumn<Integer>)filtered.getColumn("Age")).get(1).intValue());
        assertEquals("Charlie", filtered.getColumn("Nom").get(0));
        assertEquals("David", filtered.getColumn("Nom").get(1));
    }

    /**
     * Teste l'exception lors de l'utilisation d'un opérateur invalide dans le masque booléen
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateBooleanMaskInvalidOperator() {
        selection.createBooleanMask(df, "Age", "??", 30);
    }

    /**
     * Teste l'exception lors de l'utilisation d'un masque de taille invalide
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFilterByMaskInvalidSize() {
        ArrayList<Boolean> mask = new ArrayList<>();
        mask.add(true);
        selection.filterByMask(df, mask);
    }

    /**
     * Teste l'exception lors de la création d'un masque avec une colonne invalide
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateBooleanMaskInvalidColumn() {
        selection.createBooleanMask(df, "InvalidColumn", ">", 30);
    }

    /**
     * Teste la création d'un masque booléen avec des valeurs null
     */
    @Test
    public void testCreateBooleanMaskWithNull() {
        DataColumn<Integer> testColumn = new DataColumn<>("Test", Integer.class);
        testColumn.add(10);
        testColumn.add(null);
        testColumn.add(30);
        testColumn.add(40);
        df.addColumn(testColumn);

        ArrayList<Boolean> mask = selection.createBooleanMask(df, "Test", ">", 20);
        assertEquals(4, mask.size());
        assertEquals(false, mask.get(0)); // 10 > 20
        assertEquals(false, mask.get(1)); // null > 20
        assertEquals(true, mask.get(2));  // 30 > 20
        assertEquals(true, mask.get(3));  // 40 > 20
    }

    /**
     * Teste la création d'un masque booléen avec tous les opérateurs disponibles
     */
    @Test
    public void testCreateBooleanMaskAllOperators() {
        // Test tous les opérateurs
        assertTrue(selection.createBooleanMask(df, "Age", ">", 30).get(3));  // 40 > 30
        assertTrue(selection.createBooleanMask(df, "Age", "<", 30).get(0));  // 25 < 30
        assertTrue(selection.createBooleanMask(df, "Age", ">=", 40).get(3)); // 40 >= 40
        assertTrue(selection.createBooleanMask(df, "Age", "<=", 25).get(0)); // 25 <= 25
        assertTrue(selection.createBooleanMask(df, "Age", "==", 35).get(2)); // 35 == 35
        assertTrue(selection.createBooleanMask(df, "Age", "!=", 30).get(0)); // 25 != 30
    }

    /**
     * Teste l'évaluation avec différents opérateurs arithmétiques
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEvalWithMultipleOperators() {
        DataColumn<?> resultAdd = selection.eval(df, "Age + 5");
        DataColumn<?> resultSub = selection.eval(df, "Age - 5");
        DataColumn<?> resultMul = selection.eval(df, "Age * 2");
        DataColumn<?> resultDiv = selection.eval(df, "Age / 5");

        assertEquals(30.0, ((DataColumn<Double>)resultAdd).get(0), 0.001); // 25 + 5
        assertEquals(20.0, ((DataColumn<Double>)resultSub).get(0), 0.001); // 25 - 5
        assertEquals(50.0, ((DataColumn<Double>)resultMul).get(0), 0.001); // 25 * 2
        assertEquals(5.0, ((DataColumn<Double>)resultDiv).get(0), 0.001);  // 25 / 5
    }

    /**
     * Teste l'exception lors de l'utilisation d'un opérateur invalide dans l'évaluation
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvalWithInvalidOperator() {
        selection.eval(df, "Age % 2"); // Opérateur non supporté
    }
}