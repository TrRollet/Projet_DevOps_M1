package projet_devops;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.IOException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Classe de test pour la classe DataFrame
 */
public class DataFrameTest {
    /**
     * DataFrame à tester
     */
    private DataFrame df;
    /**
     * Fichier temporaire pour les tests
     */
    private Path tempFile;

    /**
     * Méthode exécutée avant chaque test pour initialiser le DataFrame et le fichier temporaire
     */
    @Before
    public void setUp() throws IOException {
        df = new DataFrame();
        tempFile = Files.createTempFile("test", ".csv");
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("nom,prenom,age,ville,code_postal, salaire\n");
            writer.write("NomA,PrenomA,25,Paris,75001, 3000.55\n");
            writer.write("NomB,PrenomB,30,Lyon,69001, 4000.75\n");
            writer.write("NomC,PrenomC,35,Marseille,13001, 2525\n");
        }
    }

    /**
     * Test de la création d'un DataFrame
     */
    @Test
    public void testDataFrameCreation() {
        assertNotNull("Un nouveau DataFrame ne devrait pas être null", df);
        assertEquals("Un nouveau DataFrame devrait avoir 0 colonnes", 0, df.getColumnCount());
    }

    /**
     * Test du nombre de lignes dans le DataFrame
     */
    @Test
    public void testCSVRowCount() throws IOException {
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        assertEquals("Le DataFrame devrait contenir 3 lignes", 3, dfFromCsv.getRowCount());
    }

    /**
     * Test du nombre de colonnes dans le DataFrame
     */
    @Test
    public void testCSVColumnCount() throws IOException {
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        assertEquals("Le DataFrame devrait contenir 6 colonnes", 6, dfFromCsv.getColumnCount());
    }

    /**
     * Test du type de la colonne String
     */
    @Test
    public void testStringColumnType() throws IOException {
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        assertEquals("La colonne 'nom' devrait être de type String", String.class, dfFromCsv.getColumn("nom").getDataType());
    }

    /**
     * Test du type de la colonne numérique
     */
    @Test
    public void testNumericColumnType() throws IOException {
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        assertEquals("La colonne 'age' devrait être de type Integer", Integer.class, dfFromCsv.getColumn("age").getDataType());
    }

    /**
     * Test du type 
     */
    @Test
    public void testStringColumnValue() throws IOException {
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        assertEquals("La première ville devrait être 'Paris'", "Paris", dfFromCsv.getColumn("ville").get(0));
    }

    /**
     * Test de la valeur d'une colonne numérique
     */
    @Test
    public void testNumericColumnValue() throws IOException {
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        assertEquals("Le deuxième âge devrait être 30", Integer.valueOf(30), dfFromCsv.getColumn("age").get(1));
    }

    /**
     * Récupération d'une colonne par son nom
     */
    @Test
    public void testGetColumnByName() throws IOException {
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        assertNotNull("La colonne 'prenom' devrait exister", dfFromCsv.getColumn("prenom"));
    }

    /**
     * Récupération d'une colonne par son index
     */
    @Test
    public void testGetColumnByIndex() throws IOException {
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        assertNotNull("La première colonne ne devrait pas être null", dfFromCsv.getColumn(0));
    }

    /**
     * Test de la récupération d'une colonne par un index négatif
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetColumnByInvalidIndex() throws IOException {
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        dfFromCsv.getColumn(-1); // Devrait lever une exception
    }

    /**
     * Test de la récupération d'une colonne par un index trop grand
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetColumnByTooLargeIndex() throws IOException {
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        dfFromCsv.getColumn(10); // Devrait lever une exception
    }

    /**
     * Test de la récupération des noms des colonnes
     */
    @Test
    public void testGetColumnNames() throws IOException {
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        assertTrue("La liste des colonnes devrait contenir 'nom'", dfFromCsv.getColumnNames().contains("nom"));
    }

    /**
     * Test de l'ajout d'une colonne de taille différente
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddColumnWithDifferentSize() {
        DataColumn<String> col1 = new DataColumn<>("test1", String.class);
        DataColumn<String> col2 = new DataColumn<>("test2", String.class);
        col1.add("value1");
        col2.add("value2");
        col2.add("value3");
        
        df.addColumn(col1);
        df.addColumn(col2); // Devrait lever une exception car tailles différentes
    }

    /**
     * Test de l'ajout d'une colonne avec des valeurs nulles
     */
    @Test
    public void testEmptyValueInCSV() throws IOException {
        // CSV avec des valeurs vides
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("nom,age\n");
            writer.write("NomA,25\n");
            writer.write(",30\n");
        }
        
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        assertNull("Une valeur vide devrait être lue comme null", dfFromCsv.getColumn("nom").get(1));
    }

    /**
     * Test de l'ajout d'une colonne avec des données incomplètes
     */
    @Test
    public void testIncompleteRowInCSV() throws IOException {
        // CSV avec une ligne incomplète (moins de colonnes)
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("nom,prenom,age\n");
            writer.write("NomA,PrenomA\n");          // Ligne incomplète
            writer.write("NomB,PrenomB,30\n");
        }
        
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        assertNull("Une colonne manquante devrait être lue comme null", 
            dfFromCsv.getColumn("age").get(0));
    }

    /**
     * Test de l'ajout d'une colonne avec des valeurs vides au milieu
     */
    @Test
    public void testEmptyFieldInCSV() throws IOException {
        // CSV avec des champs vides au milieu
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("nom,prenom,age\n");
            writer.write("NomA,,25\n");              // Champ vide au milieu
            writer.write("NomB,PrenomB,30\n");
        }
        
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        assertNull("Un champ vide devrait être lu comme null", 
            dfFromCsv.getColumn("prenom").get(0));
    }

    /**
     * Test de l'ajout d'une colonne avec des valeurs mixtes (nombres et texte)
     */
    @Test
    public void testMixedValuesInNumericColumn() throws IOException {
        // CSV avec des valeurs mixtes (nombres et texte)
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("valeur\n");
            writer.write("10.20\n");
            writer.write("vingt\n");    // texte dans une colonne numérique
        }
        
        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        assertEquals("Une colonne avec texte et nombres devrait être de type String",String.class, dfFromCsv.getColumn("valeur").getDataType());
    }
    
    /**
     * Test de l'ajout d'une colonne avec des valeurs nulles dans une colonne numérique
     */
    @Test
    public void testAddNullDoubleValue() throws IOException {
        // CSV avec des valeurs doubles et des lignes vides
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("nom,valeur\n");
            writer.write("NomA,10.20\n");
            writer.write("NomB,\n");          // Ligne vide
            writer.write("NomC,20.30\n");
        }

        DataFrame dfFromCsv = DataFrame.fromCSV(tempFile.toString());
        assertEquals("La première valeur devrait être 10.20", Double.valueOf(10.20), dfFromCsv.getColumn("valeur").get(0));
        assertNull("La deuxième valeur devrait être null", dfFromCsv.getColumn("valeur").get(1));
        assertEquals("La troisième valeur devrait être 20.30", Double.valueOf(20.30), dfFromCsv.getColumn("valeur").get(2));
    }
}