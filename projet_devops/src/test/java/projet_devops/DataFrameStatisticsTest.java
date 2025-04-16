package projet_devops;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DataFrameStatisticsTest {
    private DataFrameStatistics stats;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @Before
    public void setUp() {
        stats = new DataFrameStatistics();
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testSummarizeNumericColumn() {
        DataColumn<Double> column = new DataColumn<>("test_numeric", Double.class);
        column.add(1.0);
        column.add(2.0);
        column.add(3.0);

        stats.summarize(column);
        String output = outContent.toString();

        assertTrue("Doit contenir le nom de la colonne", 
            output.contains("Statistiques pour la colonne : test_numeric"));
        assertTrue("Doit contenir la moyenne", 
            output.contains("Moyenne: 2.00"));
    }

    @Test
    public void testSummarizeTextColumn() {
        DataColumn<String> column = new DataColumn<>("test_text", String.class);
        column.add("A");
        column.add("B");
        column.add("C");

        stats.summarize(column);
        String output = outContent.toString();

        assertTrue("Doit contenir le nom de la colonne", 
            output.contains("Statistiques pour la colonne : test_text"));
        assertTrue("Doit contenir le type", 
            output.contains("Type: String"));
        assertTrue("Doit contenir le nombre de valeurs", 
            output.contains("Nombre de valeurs: 3"));
    }

    @Test
    public void testSummarizeNullValues() {
        DataColumn<Double> column = new DataColumn<>("test_null", Double.class);
        column.add(1.0);
        column.add(null);
        column.add(3.0);

        stats.summarize(column);
        String output = outContent.toString();

        assertTrue("Doit contenir le nom de la colonne", 
            output.contains("Statistiques pour la colonne : test_null"));
        assertTrue("Doit contenir la moyenne", 
            output.contains("Moyenne: 2.00"));
    }

    @Test
    public void testFormatValue() {
        assertEquals("N/A", stats.formatValue(null));
        assertEquals("1.00", stats.formatValue(1.0));
        assertEquals("1.23", stats.formatValue(1.234));
        assertEquals("0.00", stats.formatValue(0.0));
    }

    @Test
    public void testMeanForNonNumericColumn() {
        DataColumn<String> column = new DataColumn<>("test_string", String.class);
        column.add("A");
        column.add("B");
        column.add("C");

        assertNull("Mean should be null for non-numeric column", stats.mean(column));
    }

    @Test
    public void testMedianForNonNumericColumn() {
        DataColumn<String> column = new DataColumn<>("test_string", String.class);
        column.add("X");
        column.add("Y");
        column.add("Z");
        
        assertNull("Median should be null for non-numeric column", stats.median(column));
    }

    @Test
    public void testMedianForEmptyNumericColumn() {
        DataColumn<Double> column = new DataColumn<>("test_empty", Double.class);
        column.add(null);
        column.add(null);
        column.add(null);
        
        assertNull("Median should be null for a column containing only null values", stats.median(column));
    }

    @Test
    public void testMinForNonNumericColumn() {
        DataColumn<String> column = new DataColumn<>("test_string", String.class);
        column.add("A");
        column.add("B");
        column.add("C");
        
        assertNull("Min should be null for non-numeric column", stats.min(column));
    }

    @Test
    public void testMaxForNonNumericColumn() {
        DataColumn<String> column = new DataColumn<>("test_string", String.class);
        column.add("A");
        column.add("B");
        column.add("C");
        
        assertNull("Max should be null for non-numeric column", stats.max(column));
    }

    @Test
    public void testStandardDeviationForNonNumericColumn() {
        DataColumn<String> column = new DataColumn<>("test_string", String.class);
        column.add("A");
        column.add("B");
        column.add("C");
        
        assertNull("Standard deviation should be null for non-numeric column", stats.standardDeviation(column));
    }
}