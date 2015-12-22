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

    private List<StockSymbol> stockMarket;
    final static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Main app = new Main();
        app.preloadData();
        app.run();
    }

    public void run() {
        logger.info("Start application");
        if(this.getStockMarket().size() == 0){
            logger.error("No data");
            return;
        }
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
                List<StockSymbol> matchedSymbols = getStockMarket().stream().filter(i -> i.getSymbol().equals(symbol)).collect(Collectors.toList());
                if (matchedSymbols.size() == 0) {
                    System.out.println("Stock symbol does not exist");
                } else {
                    StockSymbol selected = matchedSymbols.get(0);
                    selected.addPrices(price, quantity);
                    selected.printCalculations();
                    System.out.println("Geometric Mean: " + StockSymbol.calculateGeometricMean(getStockMarket()));
                }
            }
        }
        doneApplication();
    }

    void doneApplication() {
        logger.info("Done program");
    }

    void preloadData() {
        logger.debug("populate sample data");
        setStockMarket(new ArrayList<>());
        getStockMarket().add(new StockSymbol("TEA", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(100), BigDecimal.valueOf(0)));
        getStockMarket().add(new StockSymbol("POP", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(100), BigDecimal.valueOf(8)));
        getStockMarket().add(new StockSymbol("ALE", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(60), BigDecimal.valueOf(23)));
        getStockMarket().add(new StockSymbol("GIN", StockType.PREFERRED, Optional.of(BigDecimal.valueOf(2)), BigDecimal.valueOf(100), BigDecimal.valueOf(8)));
        getStockMarket().add(new StockSymbol("JOE", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(250), BigDecimal.valueOf(13)));
    }

    public List<StockSymbol> getStockMarket() {
        return stockMarket;
    }

    public void setStockMarket(List<StockSymbol> stockMarket) {
        this.stockMarket = stockMarket;
    }
}
