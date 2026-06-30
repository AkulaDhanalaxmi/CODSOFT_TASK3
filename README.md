# Currency Converter

A console-based Java program that converts an amount from one currency to another using real-time exchange rates fetched from a free online API.

## Features

- Lets the user choose a base currency and a target currency
- Fetches real-time exchange rates from the [open.er-api.com](https://www.exchangerate-api.com/docs/free) API
- Takes the amount the user wants to convert
- Converts the amount from the base currency to the target currency using the fetched rate
- Displays the converted amount along with the target currency code

## How It Works

1. Enter the base currency code (e.g., USD).
2. Enter the target currency code (e.g., INR).
3. Enter the amount you want to convert.
4. The program fetches the current exchange rate and calculates the converted amount.
5. The result is displayed, along with the exchange rate used.

## Running the Program

Requires an internet connection to fetch live exchange rates.

```
javac CurrencyConverter.java
java CurrencyConverter
```

## Example

```
Enter the base currency code (e.g., USD): USD
Enter the target currency code (e.g., INR): INR
Enter the amount to convert: 100

========================================
              RESULT
========================================
100.00 USD = 8345.20 INR
Exchange Rate: 1 USD = 83.4520 INR
```

## Notes

- Currency codes must follow the ISO 4217 standard (e.g., USD, EUR, INR, GBP, JPY).
- The API used (open.er-api.com) is free and does not require an API key.
- No external libraries are used; JSON parsing is done manually with simple string operations.
