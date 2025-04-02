package projet_devops;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class SelectionTest {
    private DataFrame df;
    private Selection selection;
    private DataFrame dfEmpty;

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
        
        df.addColumn("Age", col1);
        df.addColumn("Nom", col2);
    }

    @Test
    public void testSelectLines() {
        DataFrame result = selection.selectLines(df, 1, 2);
        assertEquals(2, result.getRowCount());
        assertEquals("Bob", result.getColumn("Nom").get(0));
        assertEquals("Charlie", result.getColumn("Nom").get(1));
        assertEquals(30, ((DataColumn<Integer>)result.getColumn("Age")).get(0).intValue());
        assertEquals(35, ((DataColumn<Integer>)result.getColumn("Age")).get(1).intValue());
    }

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

    @Test
    public void testSelectSingleLine() {
        DataFrame result = selection.selectLines(df, 1, 1);
        assertEquals(1, result.getRowCount());
        assertEquals("Bob", result.getColumn("Nom").get(0));
        assertEquals(30, ((DataColumn<Integer>)result.getColumn("Age")).get(0).intValue());
    }

    @Test
    public void testSelectAllLines() {
        DataFrame result = selection.selectLines(df, 0, df.getRowCount() - 1);
        assertEquals(df.getRowCount(), result.getRowCount());
        assertEquals("Alice", result.getColumn("Nom").get(0));
        assertEquals("David", result.getColumn("Nom").get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)

    public void testSelectLinesOutOfBounds() {
        selection.selectLines(df, 5, 6); // Index hors limites
    }

    @Test(expected = IllegalArgumentException.class)

    public void testSelectLinesInvalidRange() {
        selection.selectLines(df, 2, 1); // début > fin
    }

    @Test(expected = IllegalArgumentException.class)

    public void testSelectColumnsEmpty() {
        ArrayList<String> columns = new ArrayList<>();
        selection.selectColumns(dfEmpty, columns);
    }

    @Test(expected = IllegalArgumentException.class)

    public void testSelectColumnsInvalidName() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("InvalidColumn");
        selection.selectColumns(df, columns);
    }

    @Test
    public void testSelectColumnsPreservesOrder() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Nom");
        columns.add("Age");
        
        DataFrame result = selection.selectColumns(df, columns);
        assertEquals("Nom", result.getColumnNames().get(0));
        assertEquals("Age", result.getColumnNames().get(1));
    }

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
}