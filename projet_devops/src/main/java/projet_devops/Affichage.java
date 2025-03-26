package projet_devops;

public class Affichage {

    private static final int COLUMN_WIDTH = 12;

    /**
     * Affiche l'intégralité du DataFrame sous forme de tableau
     * 
     * @param df Le DataFrame à afficher
     */
    public void afficherTout(DataFrame df) {
        for (String columnName : df.getColumnNames()) {
            System.out.printf("%-" + COLUMN_WIDTH + "s", columnName);
            System.out.print(" ");
        }
        System.out.println();

        for (int i = 0; i < df.getRowCount(); i++) {
            for (String columnName : df.getColumnNames()) {
                System.out.printf("%-" + COLUMN_WIDTH + "s", df.getColumn(columnName).get(i));
                System.out.print(" ");
            }
            System.out.println();
        }
    }

     /**
     * Affiche les 5 premières lignes du DataFrame
     * 
     * @param df Le DataFrame dont on veut afficher le début
     */
    public void afficherFirst(DataFrame df) {
        for (String columnName : df.getColumnNames()) {
            System.out.printf("%-" + COLUMN_WIDTH + "s", columnName);
            System.out.print(" ");
        }
        System.out.println();

        for (int i = 0; i < 5; i++) {
            for (String columnName : df.getColumnNames()) {
                System.out.printf("%-" + COLUMN_WIDTH + "s", df.getColumn(columnName).get(i));
                System.out.print(" ");
            }
            System.out.println();
        }
    }

     /**
     * Affiche les 5 dernières lignes du DataFrame
     * 
     * @param df Le DataFrame dont on veut afficher la fin
     */
    public void afficherLast(DataFrame df) {
        for (String columnName : df.getColumnNames()) {
            System.out.printf("%-" + COLUMN_WIDTH + "s", columnName);
            System.out.print(" ");
        }
        System.out.println();

        for (int i = df.getRowCount() - 5; i < df.getRowCount(); i++) {
            for (String columnName : df.getColumnNames()) {
                System.out.printf("%-" + COLUMN_WIDTH + "s", df.getColumn(columnName).get(i));
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
