import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    private static final Scanner scanner = new Scanner(System.in);

    // Free, no-API-key-required exchange rate API
    private static final String API_URL = "https://open.er-api.com/v6/latest/";

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("          CURRENCY CONVERTER");
        System.out.println("========================================");

        System.out.print("\nEnter the base currency code (e.g., USD): ");
        String baseCurrency = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter the target currency code (e.g., INR): ");
        String targetCurrency = scanner.nextLine().trim().toUpperCase();

        double amount = getDoubleInput("Enter the amount to convert: ");

        try {
            double rate = fetchExchangeRate(baseCurrency, targetCurrency);
            double convertedAmount = convertCurrency(amount, rate);
            displayResult(amount, baseCurrency, convertedAmount, targetCurrency, rate);
        } catch (Exception e) {
            System.out.println("\nError fetching exchange rate: " + e.getMessage());
            System.out.println("Please check the currency codes and your internet connection.");
        }

        scanner.close();
    }

    /**
     * Fetches the real-time exchange rate between two currencies from the API.
     */
    private static double fetchExchangeRate(String baseCurrency, String targetCurrency) throws Exception {
        String urlString = API_URL + baseCurrency;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("API request failed with response code " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return parseRateFromJson(response.toString(), targetCurrency);
    }

    /**
     * Extracts the exchange rate for the target currency from the JSON response.
     * Avoids external JSON libraries by doing simple string parsing on the
     * "rates" object returned by the API.
     */
    private static double parseRateFromJson(String json, String targetCurrency) {
        String searchKey = "\"" + targetCurrency + "\":";
        int index = json.indexOf(searchKey);
        if (index == -1) {
            throw new RuntimeException("Currency code '" + targetCurrency + "' not found in API response.");
        }

        int valueStart = index + searchKey.length();
        int valueEnd = valueStart;
        while (valueEnd < json.length()
                && (Character.isDigit(json.charAt(valueEnd))
                || json.charAt(valueEnd) == '.'
                || json.charAt(valueEnd) == '-')) {
            valueEnd++;
        }

        String rateString = json.substring(valueStart, valueEnd);
        return Double.parseDouble(rateString);
    }

    /**
     * Converts the input amount from the base currency to the target currency.
     */
    private static double convertCurrency(double amount, double rate) {
        return amount * rate;
    }

    /**
     * Displays the converted amount and the target currency to the user.
     */
    private static void displayResult(double amount, String baseCurrency,
                                       double convertedAmount, String targetCurrency, double rate) {
        System.out.println("\n========================================");
        System.out.println("              RESULT");
        System.out.println("========================================");
        System.out.printf("%.2f %s = %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);
        System.out.printf("Exchange Rate: 1 %s = %.4f %s%n", baseCurrency, rate, targetCurrency);
    }

    /**
     * Keeps asking until the user enters a valid decimal number.
     */
    private static double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value < 0) {
                    System.out.println("Amount cannot be negative.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
