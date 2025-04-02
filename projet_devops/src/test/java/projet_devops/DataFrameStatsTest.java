package projet_devops;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DataFrameStatsTest {
    private DataFrame df;
    private DataFrameStats stats;

    @Before
    public void setUp() {
        // Création d'un DataFrame de test
        df = new DataFrame();
        
        // Création d'une colonne d'entiers
        DataColumn<Integer> ageColumn = new DataColumn<>("age", Integer.class);
        ageColumn.add(25);
        ageColumn.add(30);
        ageColumn.add(35);
        df.addColumn("age", ageColumn);
        
        // Création d'une colonne de doubles
        DataColumn<Double> salaryColumn = new DataColumn<>("salary", Double.class);
        salaryColumn.add(45000.0);
        salaryColumn.add(50000.0);
        salaryColumn.add(55000.0);
        df.addColumn("salary", salaryColumn);
        
        // Création d'une colonne de strings
        DataColumn<String> nameColumn = new DataColumn<>("name", String.class);
        nameColumn.add("Alice");
        nameColumn.add("Bob");
        nameColumn.add("Charlie");
        df.addColumn("name", nameColumn);

        stats = new DataFrameStats();
    }

    @Test
    public void testMeanInteger() {
        Double meanAge = stats.mean(df.getColumn("age"));
        assertEquals(30.0, meanAge, 0.001);
    }

    @Test
    public void testMeanDouble() {
        Double meanSalary = stats.mean(df.getColumn("salary"));
        assertEquals(50000.0, meanSalary, 0.001);
    }

    @Test
    public void testMeanString() {
        Double meanName = stats.mean(df.getColumn("name"));
        assertNull(meanName);
    }

    @Test
    public void testSummary() {
        // Test that summary runs without throwing exceptions
        stats.summary(df);
    }

    @Test
    public void testSelectColumns() {
        DataFrame subset = stats.selectColumns(df, "age", "name");
        assertEquals(2, subset.getColumnCount());
        assertTrue(subset.getColumnNames().contains("age"));
        assertTrue(subset.getColumnNames().contains("name"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSelectColumnsInvalidLabel() {
        stats.selectColumns(df, "age", "invalidColumn");
    }

    @Test
    public void testSummaryForColumns() {
        // Vérifie que la méthode s'exécute sans erreur
        stats.summaryForColumns(df, "age", "salary");
    }
}