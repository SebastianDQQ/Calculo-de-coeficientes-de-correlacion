import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Correlacion {
    public static void main(String[] args) {
        String archivo = "startup-profit.csv";
        String linea;
        String separadoPor = ",";

        ArrayList<Double> profits = new ArrayList<>();
        ArrayList<Double> RdSpend = new ArrayList<>();
        ArrayList<Double> MarketingSpend = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String header = br.readLine();
            if (header != null) {
                String[] columnas = header.split(separadoPor);

                int profitIndex = -1;
                int RdSpendIndex = -1;
                int MarketingSpendIndex = -1;

                for (int i = 0; i < columnas.length; i++) {
                    switch (columnas[i].trim()) {
                        case "Profit":
                            profitIndex = i;
                            break;
                        case "R&D Spend":
                            RdSpendIndex = i;
                            break;
                        case "Marketing Spend":
                            MarketingSpendIndex = i;
                            break;
                    }
                }

                if (profitIndex == -1 || RdSpendIndex == -1 || MarketingSpendIndex == -1) {
                    throw new IllegalArgumentException("No se encontraron todas las columnas en el archivo");
                }

                while ((linea = br.readLine()) != null) {
                    String[] data = linea.split(separadoPor);

                    try {
                        double profit = Double.parseDouble(data[profitIndex].trim());
                        double rd = Double.parseDouble(data[RdSpendIndex].trim());
                        double marketing = Double.parseDouble(data[MarketingSpendIndex].trim());

                        profits.add(profit);
                        RdSpend.add(rd);
                        MarketingSpend.add(marketing);
                    } catch (NumberFormatException e) {
                        System.out.println("Error de análisis: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double rdCorrelacion = calcularCorrelacion(profits, RdSpend);
        double marketingCorrelacion = calcularCorrelacion(profits, MarketingSpend);

        System.out.println("Correlacion entre Profit y R&D Spend: " + rdCorrelacion);
        System.out.println("Correlacion entre Profit y Marketing Spend: " + marketingCorrelacion);
    }

    public static double calcularCorrelacion(ArrayList<Double> x, ArrayList<Double> y) {
        double X = calcular(x);
        double Y = calcular(y);

        double numerador = 0;
        double sumaX = 0;
        double sumaY = 0;

        for (int i = 0; i < x.size(); i++) {
            double deltaX = x.get(i) - X;
            double deltaY = y.get(i) - Y;
            numerador += deltaX * deltaY;
            sumaX += deltaX * deltaX;
            sumaY += deltaY * deltaY;
        }

        double denominador = Math.sqrt(sumaX * sumaY);
        return numerador / denominador;
    }

    public static double calcular(ArrayList<Double> valores) {
        double suma = 0;
        for (double valor : valores) {
            suma += valor;
        }
        return suma / valores.size();
    }
}