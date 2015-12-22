package com.hoangong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private static List<StockSymbol> stockMarket;
    final static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Start application");
        preloadData();
        System.out.println("Available stocks: TEA POP ALE GIN JOE");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Stock Symbol. Enter \"q\" to exist: ");
            String symbol = scanner.next().toUpperCase();
            logger.debug("input symbol {}", symbol);
            if (symbol.equals("Q")) {
                break;
            } else {
                BigDecimal price;
                Integer quantity;
                try {
                    System.out.print("Price: ");
                    String t = scanner.next();
                    logger.debug("Input price: {}", t);
                    price = new BigDecimal(t);
                    System.out.print("Quantity: ");
                    t = scanner.next();
                    logger.debug("Input price: {}", t);
                    quantity = Integer.parseInt(t);
                } catch (Exception ex) {
                    logger.error("error price input",ex);
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
        logger.info("Done program");
    }

    private static void preloadData() {
        logger.debug("populate sample data");
        stockMarket = new ArrayList<>();
        stockMarket.add(new StockSymbol("TEA", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(100), BigDecimal.valueOf(0)));
        stockMarket.add(new StockSymbol("POP", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(100), BigDecimal.valueOf(8)));
        stockMarket.add(new StockSymbol("ALE", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(60), BigDecimal.valueOf(23)));
        stockMarket.add(new StockSymbol("GIN", StockType.PREFERRED, Optional.of(BigDecimal.valueOf(2)), BigDecimal.valueOf(100), BigDecimal.valueOf(8)));
        stockMarket.add(new StockSymbol("JOE", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(250), BigDecimal.valueOf(13)));
    }

}
