package com.hoangong;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by hoangong on 22/12/2015.
 */
@RunWith(JUnit4.class)
public class StockSymbolTest {
    List<StockSymbol> stockMarket;
    StockSymbol stockTEA;
    StockSymbol stockPOP;
    StockSymbol stockALE;
    StockSymbol stockGIN;
    StockSymbol stockJOE;

    @Before
    public void setUp(){
        stockTEA = new StockSymbol("TEA", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(100), BigDecimal.valueOf(0));
        stockPOP = new StockSymbol("POP", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(100), BigDecimal.valueOf(8));
        stockALE = new StockSymbol("ALE", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(60), BigDecimal.valueOf(23));
        stockGIN = new StockSymbol("GIN", StockType.PREFERRED, Optional.of(BigDecimal.valueOf(0.02)), BigDecimal.valueOf(100), BigDecimal.valueOf(8));
        stockJOE = new StockSymbol("JOE", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(250), BigDecimal.valueOf(13));
        stockMarket = new ArrayList<>();
        stockMarket.add(stockTEA);
        stockMarket.add(stockPOP);
        stockMarket.add(stockALE);
        stockMarket.add(stockGIN);
        stockMarket.add(stockJOE);
    }

    @Test
    public void testGetPrices() throws Exception {
        stockPOP.addPrices(new BigDecimal(200),1000);
        assertTrue(stockTEA.getPrices().isEmpty());
        assertFalse(stockPOP.getPrices().isEmpty());
    }

    @Test
    public void testGetLastPrice() throws Exception {
        stockPOP.addPrices(new BigDecimal(200),1000);
        assertEquals(Optional.empty(),stockTEA.getLastPrice());
        assertEquals(new BigDecimal(200),stockPOP.getLastPrice().get().getPrice());
        assertTrue(1000 == stockPOP.getLastPrice().get().getQuantity());
    }

    @Test
    public void testAddPrices() throws Exception {
        stockPOP.addPrices(new BigDecimal(200),1000);
        assertEquals(1,stockPOP.getPrices().size());
        stockPOP.addPrices(new BigDecimal(200),1000);
        assertEquals(2,stockPOP.getPrices().size());
    }

    @Test
    public void shouldUpdateLastDiviendCalculationSetLastDiviendIfSuccess() throws Exception {
        stockALE.addPrices(new BigDecimal(200),1000);
        stockALE.printCalculations();
        assertNotEquals(new BigDecimal(23),stockALE.getLastDividend());
    }

    @Test
    public void shouldNotUpdateLastDiviendCalculationSetLastDiviendIfNotSuccess() throws Exception {
        stockALE.printCalculations();
        assertEquals(new BigDecimal(23),stockALE.getLastDividend());
    }

    @Test
    public void testCalculateGeometricMean() throws Exception {
        stockPOP.addPrices(new BigDecimal(50),3000);
        stockPOP.addPrices(new BigDecimal(2),1000);
        assertTrue(10D == StockSymbol.calculateGeometricMean(stockMarket));
    }

    @Test
    public void testCalculateGeometricMeanWithEmpty() throws Exception {
        assertTrue(0D == StockSymbol.calculateGeometricMean(stockMarket));
    }

    @Test
    public void testCalculateDividendYield() throws Exception {
        stockPOP.addPrices(new BigDecimal(100),3000);
        stockGIN.addPrices(new BigDecimal(200),1000);

        assertEquals(Optional.of(new BigDecimal("0.08")),stockPOP.calculateDividendYield());
        assertEquals(Optional.of(new BigDecimal("0.0100")),stockGIN.calculateDividendYield());
        assertEquals(Optional.empty(),stockTEA.calculateDividendYield());
    }

    @Test
    public void testCalculatePERatio() throws Exception {
        assertEquals(new BigDecimal("10.00"),stockPOP.calculatePERatio(new BigDecimal(100),new BigDecimal(10)));

    }

    @Test
    public void shouldCalculateVolumeWeightIn15Mins() throws Exception {
        stockPOP.addPrices(new BigDecimal(100),3000);
        stockPOP.addPrices(new BigDecimal(200),1000);
        assertEquals(new BigDecimal("125.00"),stockPOP.calculateVolumeWeight(15 * 60 * 1000));
    }

    @Test
    public void shouldNotCalculateVolumeWeightPast15Mins() throws Exception {
        stockPOP.addPrices(new BigDecimal(100),1000);
        Thread.sleep(1000);
        stockPOP.addPrices(new BigDecimal(100),3000);
        stockPOP.addPrices(new BigDecimal(200),1000);
        assertEquals(new BigDecimal("125.00"),stockPOP.calculateVolumeWeight(400));
    }
}