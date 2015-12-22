package com.hoangong;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by hoangong on 21/12/2015.
 */
public class StockSymbol {
    private final String symbol;
    private final StockType type;
    private BigDecimal lastDividend;
    private final Optional<BigDecimal> fixedDividend;
    private final BigDecimal parValue;
    private final List<Price> prices;

    public StockSymbol(String symbol, StockType type, Optional<BigDecimal> fixedDividend, BigDecimal parValue, BigDecimal lastDividend) {
        this.symbol = symbol;
        this.type = type;
        this.fixedDividend = fixedDividend;
        this.parValue = parValue;
        this.lastDividend = lastDividend;
        this.prices = new ArrayList<>();
    }

    public String getSymbol() {
        return symbol;
    }

    public StockType getType() {
        return type;
    }

    public Optional<BigDecimal> getFixedDividend() {
        return fixedDividend;
    }

    public BigDecimal getParValue() {
        return parValue;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public Optional<Price> getLastPrice() {
        if (prices.size() >= 1) {
            return Optional.of(prices.get(prices.size() - 1));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Set price
     * @param price price
     * @param quantity quality
     */
    public void addPrices(BigDecimal price, Integer quantity) {
        prices.add(new Price(price, quantity));
    }

    public BigDecimal getLastDividend() {
        return lastDividend;
    }

    /**
     * Calculate all variable and print to screen. It also updates last diviend if calculation successful
     */
    public void printCalculations() {
        getLastPrice().ifPresent(lastPrice -> {
            this.calculateDividendYield().ifPresent(diviend -> {
                if (diviend != null) {
                    BigDecimal peRatio = this.calculatePERatio(lastPrice.getPrice(), diviend);
                    BigDecimal weight = this.calculateVolumeWeight(15 * 60 * 1000);
                    this.lastDividend = diviend;
                    System.out.println("Dividend Yield: " + diviend);
                    System.out.println("P/E Ratio: " + peRatio);
                    System.out.println("Volume Weighted Stock Price: " + weight);
                }
            });
        });
    }

    Optional<BigDecimal> calculateDividendYield() {
        return getLastPrice().map(lastPrice -> {
            if (getType().equals(StockType.PREFERRED) && this.getFixedDividend().isPresent()) {
                return this.getFixedDividend().get().multiply(this.getParValue().divide(lastPrice.getPrice(),2, RoundingMode.HALF_UP));
            } else {
                return this.getLastDividend().divide(lastPrice.getPrice(),2, RoundingMode.HALF_UP);
            }
        });
    }

    BigDecimal calculatePERatio(BigDecimal price, BigDecimal diviend) {
        return price.divide(diviend,2, RoundingMode.HALF_UP);
    }

    BigDecimal calculateVolumeWeight(Integer time) {
        final Timestamp last15Mins = new Timestamp(System.currentTimeMillis() - time);
        final List<Price> pricesAfter15Mins = prices.stream()
                .filter(i -> i.getTime().after(last15Mins)).collect(Collectors.toList());
        final BigDecimal t1 = pricesAfter15Mins.stream()
                .map(i -> i.getPrice().multiply(new BigDecimal(i.getQuantity())))
                .reduce(BigDecimal.ZERO, (i, j) -> i.add(j));
        final Integer t2 = pricesAfter15Mins.stream()
                .map(i -> i.getQuantity())
                .reduce(0, (i, j) -> i + j);
        return t1.divide(new BigDecimal(t2),2, RoundingMode.HALF_UP);
    }

    /**
     * calculate geometric mean
     * @param stocks list of stocks
     * @return mean
     */
    public static Double calculateGeometricMean(List<StockSymbol> stocks) {
        final List<BigDecimal> allPrices = stocks.stream().flatMap(i -> i.getPrices().stream().map(j -> j.getPrice())).collect(Collectors.toList());
        final BigDecimal t1 = allPrices.stream().reduce(BigDecimal.ONE, (i, j) -> i.multiply(j));
        final Integer t2 = allPrices.size();
        if(t2 == 0)
            return 0D;
        return Math.pow(t1.doubleValue(), 1.0 / t2);
    }
}

class Price {
    final private BigDecimal price;
    final private Integer quantity;
    final private Timestamp time;

    public Price(BigDecimal price, Integer quantity) {
        this.price = price;
        this.quantity = quantity;
        this.time = new Timestamp(System.currentTimeMillis());
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Timestamp getTime() {
        return time;
    }

    public Integer getQuantity() {
        return quantity;
    }
}