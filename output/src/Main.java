import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {

    public static void main(String[] args) {
        String batteryFile = "bateria/dados_bateria.csv";
        String weatherFile = "weather/weather.csv";
        String outputFile = "output/src/output.csv";

        try {
            // Lê os dados dos arquivos CSV
            List<DataEntry> batteryData = readBatteryData(batteryFile);
            List<DataEntry> weatherData = readWeatherData(weatherFile);

            // Calcula a média da temperatura e umidade para cada minuto
            Map<String, DataEntry> averageData = calculateAverages(weatherData);

            if (!averageData.isEmpty()) {
                // Escreve os resultados no arquivo de saída
                writeOutput(outputFile, batteryData, averageData);
                System.out.println("Dados processados com sucesso e escritos em " + outputFile);
            } else {
                System.out.println("Não foram encontradas médias para escrever no arquivo de saída.");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // Classe para representar uma entrada de dados (temperatura, umidade, etc.)
    static class DataEntry {
        double temperature;
        int humidity;
        Date timestamp;

        public DataEntry(double temperature, int humidity, Date timestamp) {
            this.temperature = temperature;
            this.humidity = humidity;
            this.timestamp = timestamp;
        }
    }

    // Lê os dados do arquivo CSV de bateria
    private static List<DataEntry> readBatteryData(String filename) throws IOException, ParseException {
        List<DataEntry> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            int batteryLevel = Integer.parseInt(parts[0]);
            Date timestamp = dateFormat.parse(parts[1]);
            data.add(new DataEntry(batteryLevel, -1, timestamp)); // Usamos -1 para indicar que não há informação de temperatura/umidade
        }

        br.close();
        return data;
    }

    // Lê os dados do arquivo CSV de temperatura e umidade
    private static List<DataEntry> readWeatherData(String filename) throws IOException, ParseException {
        List<DataEntry> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            double temperature = Double.parseDouble(parts[0]);
            int humidity = Integer.parseInt(parts[1]);
            Date timestamp = dateFormat.parse(parts[2]);
            data.add(new DataEntry(temperature, humidity, timestamp));
        }

        br.close();
        return data;
    }

    // Calcula a média da temperatura e umidade para cada minuto
    private static Map<String, DataEntry> calculateAverages(List<DataEntry> weatherData) {
        Map<String, List<DataEntry>> minuteData = new HashMap<>();
        Map<String, DataEntry> averages = new HashMap<>();

        for (DataEntry entry : weatherData) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(entry.timestamp);
            cal.set(Calendar.SECOND, 0); // Zera os segundos para agrupar por minuto
            cal.set(Calendar.MILLISECOND, 0); // Zera os milissegundos para evitar problemas de precisão
            String key = cal.getTime().toString();

            if (!minuteData.containsKey(key)) {
                minuteData.put(key, new ArrayList<>());
            }
            minuteData.get(key).add(entry);
        }

        for (Map.Entry<String, List<DataEntry>> entry : minuteData.entrySet()) {
            String timestamp = entry.getKey();
            List<DataEntry> entries = entry.getValue();
            double totalTemperature = 0;
            int totalHumidity = 0;

            for (DataEntry e : entries) {
                totalTemperature += e.temperature;
                totalHumidity += e.humidity;
            }

            double avgTemperature = totalTemperature / entries.size();
            int avgHumidity = totalHumidity / entries.size();

            averages.put(timestamp, new DataEntry(avgTemperature, avgHumidity, entries.get(0).timestamp)); // Usamos a marca de tempo do primeiro elemento como referência
        }

        return averages;
    }


    // Escreve os resultados no arquivo de saída
    private static void writeOutput(String filename, List<DataEntry> batteryData, Map<String, DataEntry> averageData) throws IOException {
        FileWriter writer = new FileWriter(filename);

        // Criar uma lista de entradas do mapa de médias para ordenação
        List<Map.Entry<String, DataEntry>> sortedEntries = new ArrayList<>(averageData.entrySet());

        // Ordenar as entradas com base na marca de tempo
        sortedEntries.sort(Map.Entry.comparingByKey());

        // Escrever os resultados ordenados no arquivo de saída
        for (Map.Entry<String, DataEntry> entry : sortedEntries) {
            DataEntry avgEntry = entry.getValue();
            Date timestamp = avgEntry.timestamp;

            // Encontrar o registro de bateria mais próximo no tempo
            DataEntry closestBatteryEntry = findClosestBatteryEntry(batteryData, timestamp);

            if (closestBatteryEntry != null) {
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
                String formattedTemperature = String.format(Locale.ENGLISH, "%.2f", avgEntry.temperature); // Formata a temperatura com duas casas decimais
                writer.write(closestBatteryEntry.temperature + "," + formattedTemperature + "," + avgEntry.humidity + "," + formattedDate + "\n");
            }
        }

        writer.close();
    }

    // Método para encontrar o registro de bateria mais próximo no tempo
    private static DataEntry findClosestBatteryEntry(List<DataEntry> batteryData, Date targetTimestamp) {
        long targetTime = targetTimestamp.getTime();
        DataEntry closestBatteryEntry = null;
        long minDifference = Long.MAX_VALUE;

        for (DataEntry batteryEntry : batteryData) {
            long difference = Math.abs(batteryEntry.timestamp.getTime() - targetTime);
            if (difference < minDifference) {
                minDifference = difference;
                closestBatteryEntry = batteryEntry;
            }
        }

        return closestBatteryEntry;
    }


}