package com.hoangong;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private static List<StockSymbol> stockMarket;

    public static void main(String[] args) {
        preloadData();
        System.out.println("Available stocks: TEA POP ALE GIN JOE");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Stock Symbol. Enter \"q\" to exist: ");
            String symbol = scanner.next().toUpperCase();
            if (symbol.equals("Q")) {
                break;
            } else {
                BigDecimal price;
                Integer quantity;
                try {
                    System.out.print("Price: ");
                    price = new BigDecimal(scanner.next());
                    System.out.print("Quantity: ");
                    quantity = Integer.parseInt(scanner.next());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Price or quality should be in number format");
                    continue;
                }
                List<StockSymbol> matchedSymbols = stockMarket.stream().filter(i -> i.getSymbol().equals(symbol)).collect(Collectors.toList());
                if (matchedSymbols.size() == 0) {
                    System.out.println("Stock symbol does not exist");
                } else {
                    StockSymbol selected = matchedSymbols.get(0);
                    selected.addPrices(price, quantity);
                    selected.printCalculations();
                    System.out.println("Geometric Mean: " + StockSymbol.calculateGeometricMean(stockMarket));
                }
            }
        }
    }

    private static void preloadData() {
        stockMarket = new ArrayList<>();
        stockMarket.add(new StockSymbol("TEA", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(100), BigDecimal.valueOf(0)));
        stockMarket.add(new StockSymbol("POP", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(100), BigDecimal.valueOf(8)));
        stockMarket.add(new StockSymbol("ALE", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(60), BigDecimal.valueOf(23)));
        stockMarket.add(new StockSymbol("GIN", StockType.PREFERRED, Optional.of(BigDecimal.valueOf(2)), BigDecimal.valueOf(100), BigDecimal.valueOf(8)));
        stockMarket.add(new StockSymbol("JOE", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(250), BigDecimal.valueOf(13)));
    }

}
