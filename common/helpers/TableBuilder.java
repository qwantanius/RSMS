package RSMS.common.helpers;

import java.util.List;

public class TableBuilder {

    public static String getTable(List<List<String>> rawData) {
        int[] maxLengths = TableBuilder.getMaxLengths(rawData);
        StringBuilder stringBuilder = new StringBuilder();
        for (List<String> row : rawData) {
            for (int i = 0; i < row.size(); i++) {
                String cell = row.get(i) != null ? row.get(i) : "NULL";
                stringBuilder.append(cell);
                stringBuilder.append(" ".repeat(Math.max(0, maxLengths[i] - cell.length() + 1)));
                stringBuilder.append("| ");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private static int[] getMaxLengths(List<List<String>> rawData) {
        int[] maxLengths = new int[rawData.get(0).size()];
        for (List<String> row : rawData) {
            for (int i = 0; i < row.size(); i++) {
                String cell = row.get(i) != null ? row.get(i) : "NULL";
                if (cell.length() > maxLengths[i]) {
                    maxLengths[i] = cell.length();
                }
            }
        }
        return maxLengths;
    }
}
