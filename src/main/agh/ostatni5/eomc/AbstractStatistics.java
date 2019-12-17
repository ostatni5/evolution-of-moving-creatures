package agh.ostatni5.eomc;

abstract public class AbstractStatistics {
    @Override
    public abstract String toString();

    public String toHtml() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html>");
        stringBuilder.append("<table>");
        for (String s : toString().split("\n")) {
            String[] ss = s.split(":");
            stringBuilder.append("<tr>");
            for (String s1 : ss) {
                stringBuilder.append("<td>");
                stringBuilder.append(s1);
                stringBuilder.append("</td>");
            }
            stringBuilder.append("</tr>");
        }
        stringBuilder.append("</table>");
        stringBuilder.append("</html>");
        return stringBuilder.toString();
    }
}