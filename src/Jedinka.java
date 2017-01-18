import java.text.DecimalFormat;
import java.util.Random;

public class Jedinka implements Comparable<Jedinka> {
    double kazna;
    double[] geni;

    public Jedinka(double[] geni) {
        this.geni = geni;
    }

    public static Jedinka slucajnoGenerirajJedinku(Random random, int broj_gena) {
        double[] geni = new double[broj_gena];

        for (int i = 0; i < geni.length; i++) {
            geni[i] = 1 - random.nextDouble() * 10;
             //geni[i] = random.nextDouble();
        }

        return new Jedinka(geni);
    }

    @Override
    public int compareTo(Jedinka o) {
        // bolja jedinka ima manju kaznu
        return Double.valueOf(kazna).compareTo(o.kazna);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.##");
        builder.append("[");
        for (int i = 0; i < geni.length - 1; i++) {
            builder.append(df.format(geni[i]) + ", ");
        }
        builder.append(df.format(geni[geni.length - 1]));
        builder.append("]");
        builder.append("\nerror: " + kazna);
        return builder.toString();
    }
}
