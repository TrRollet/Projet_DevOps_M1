package projet_devops;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class AffichageTest {
    private DataFrame df;
    private Affichage affichage;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        df = new DataFrame();
        affichage = new Affichage();
        
        // Création d'un DataFrame de test
        DataColumn<Integer> col1 = new DataColumn<>("Age", Integer.class);
        col1.add(25);
        col1.add(30);
        col1.add(35);
        col1.add(40);
        col1.add(45);
        col1.add(50);
        
        DataColumn<String> col2 = new DataColumn<>("Nom", String.class);
        col2.add("Alice");
        col2.add("Bob");
        col2.add("Charlie");
        col2.add("David");
        col2.add("Eve");
        col2.add("Frank");
        
        df.addColumn("Age", col1);
        df.addColumn("Nom", col2);
        
        // Capture de la sortie standard
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testAfficherTout() {
        affichage.afficherTout(df);
        String output = outputStream.toString();
        assertTrue(output.contains("Age"));
        assertTrue(output.contains("Nom"));
        assertTrue(output.contains("25"));
        assertTrue(output.contains("Frank"));
        assertEquals(7, output.split("\n").length); // En-tête + 6 lignes
    }

    @Test
    public void testAfficherFirst() {
        affichage.afficherFirst(df);
        String output = outputStream.toString();
        assertTrue(output.contains("Alice"));
        assertTrue(output.contains("Bob"));
        assertEquals(6, output.split("\n").length); // En-tête + 5 premières lignes
    }

    @Test
    public void testAfficherLast() {
        affichage.afficherLast(df);
        String output = outputStream.toString();
        assertTrue(output.contains("Eve"));
        assertTrue(output.contains("Frank"));
        assertEquals(6, output.split("\n").length); // En-tête + 5 dernières lignes
    }

    @Test
    public void testAfficherToutFormat() {
        affichage.afficherTout(df);
        String output = outputStream.toString();
        String[] lines = output.split("\n");
        
        // Vérifie le format de l'en-tête
        assertTrue(lines[0].startsWith("Age"));
        assertTrue(lines[0].contains("Nom"));
        
        // Vérifie que toutes les lignes ont le même format
        for (int i = 1; i < lines.length; i++) {
            assertTrue(lines[i].trim().length() > 0);
            assertTrue(lines[i].contains(" ")); // Vérifie la présence d'espaces entre les colonnes
        }
    }

    @Test
    public void testAfficherFirstSmallDf() { // Df = DataFrame
        // Créer un petit DataFrame avec moins de 5 lignes
        DataFrame smallDf = new DataFrame();
        DataColumn<Integer> smallCol = new DataColumn<>("Test", Integer.class);
        smallCol.add(1);
        smallCol.add(2);
        smallDf.addColumn("Test", smallCol);
        
        affichage.afficherFirst(smallDf);
        String output = outputStream.toString();
        assertEquals(3, output.split("\n").length); // En-tête + 2 lignes
    }

    @Test
    public void testAfficherLastSmallDf() {
        // Créer un petit DataFrame avec moins de 5 lignes
        DataFrame smallDf = new DataFrame();
        DataColumn<Integer> smallCol = new DataColumn<>("Test", Integer.class);
        smallCol.add(1);
        smallCol.add(2);
        smallDf.addColumn("Test", smallCol);
        
        affichage.afficherLast(smallDf);
        String output = outputStream.toString();
        assertEquals(3, output.split("\n").length); // En-tête + 2 lignes
        assertTrue(output.contains("1"));
        assertTrue(output.contains("2"));
    }

    @Test
    public void testAfficherToutEmptyDf() {
        DataFrame emptyDf = new DataFrame();
        DataColumn<Integer> emptyCol = new DataColumn<>("Empty", Integer.class);
        emptyDf.addColumn("Empty", emptyCol);
        
        affichage.afficherTout(emptyDf);
        String output = outputStream.toString();
        assertEquals(1, output.split("\n").length); // Seulement l'en-tête
    }

}