package projet_devops;

import java.util.ArrayList;

public class Selection {
    
    @SuppressWarnings("unchecked")

    public <T> DataFrame selectIndex(DataFrame df, int start, int end) {
        DataFrame newDf = new DataFrame();

        for (String columnName : df.getColumnNames()) {

            DataColumn<T> originalColumn = (DataColumn<T>) df.getColumn(columnName);
            DataColumn<T> indexColumn = new DataColumn<>(columnName, originalColumn.getDataType());
            
            for (int i = start; i <= end; i++) {
                T value = originalColumn.get(i);
                indexColumn.add(value);
            }

            newDf.addColumn(columnName, indexColumn);
        }

        return newDf;
    }  


    @SuppressWarnings("unchecked")

    public <T> DataFrame selectColumn(DataFrame df, ArrayList<String> columns) {
        DataFrame newDf = new DataFrame();

        for (int i = 0; i < columns.size(); i++) {
                DataColumn<T> originalColumn = (DataColumn<T>) df.getColumn(columns.get(i));
                
                newDf.addColumn(columns.get(i), originalColumn);
        }

        return newDf;
    }  
}
