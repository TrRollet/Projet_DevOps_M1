package projet_devops;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.List;

public class DataColumnTest {
    private DataColumn<String> stringColumn;
    private DataColumn<Integer> intColumn;

    @Before
    public void setUp() {
        stringColumn = new DataColumn<>("test", String.class);
        intColumn = new DataColumn<>("nombres", Integer.class);
    }

    @Test
    public void testEmptyColumn() {
        assertEquals("Une nouvelle colonne devrait être vide", 0, stringColumn.size());
    }

    @Test
    public void testNullValue() {
        stringColumn.add(null);
        assertNull("La valeur null devrait être stockée comme null", stringColumn.get(0));
    }

    @Test
    public void testMultipleNullValues() {
        intColumn.add(null);
        intColumn.add(42);
        intColumn.add(null);
        
        assertNull("La première valeur devrait être null", intColumn.get(0));
        assertEquals("La deuxième valeur devrait être 42", Integer.valueOf(42), intColumn.get(1));
        assertNull("La troisième valeur devrait être null", intColumn.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        stringColumn.add("test");
        stringColumn.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetTooLargeIndex() {
        stringColumn.add("test");
        stringColumn.get(1);
    }

    @Test
    public void testGetValues() {
        stringColumn.add("A");
        stringColumn.add("B");
        List<String> values = stringColumn.getValues();
        
        assertEquals("La liste devrait contenir 2 éléments", 2, values.size());
        assertEquals("La taille de la colonne ne devrait pas changer après modification de la copie", 2, stringColumn.size());
    }

    @Test
    public void testGetDataType() {
        assertEquals("Le type de la colonne string devrait être String", String.class, stringColumn.getDataType());
        assertEquals("Le type de la colonne int devrait être Integer", Integer.class, intColumn.getDataType());
    }

    @Test
    public void testGetColumnName() {
        assertEquals("Le nom de la colonne string devrait être 'test'", "test", stringColumn.getColumnName());
        assertEquals("Le nom de la colonne int devrait être 'nombres'", "nombres", intColumn.getColumnName());
    }

    @Test
    public void testAddAndGetValue() {
        stringColumn.add("test");
        assertEquals("La valeur ajoutée devrait être récupérable", "test", stringColumn.get(0));
        assertEquals("La taille devrait être 1 après ajout", 1, stringColumn.size());
    }
}