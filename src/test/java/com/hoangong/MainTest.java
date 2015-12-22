package com.hoangong;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by hoangong on 22/12/2015.
 */
@RunWith(JUnit4.class)
public class MainTest {
    Main main;

    @Before
    public void setUp(){
        main = new Main();
    }

    @Test
    public void shouldPreloadData(){
        main.preloadData();
        System.setIn(new ByteArrayInputStream("q".getBytes()));
        main.run();
        assertEquals(5,main.getStockMarket().size());
    }

    @Test
    public void shouldFailIfNodata(){
        Main spy = spy(main);
        when(spy.getStockMarket()).thenReturn(new ArrayList<>());
        spy.run();
        verify(spy,never()).doneApplication();
    }

    @Test
    public void shouldAskUserInput(){
        List<StockSymbol> stockMarket = new ArrayList<>();
        stockMarket.add(new StockSymbol("TEA", StockType.COMMON, Optional.empty(), BigDecimal.valueOf(100), BigDecimal.valueOf(0)));
        Main spy = spy(main);
        when(spy.getStockMarket()).thenReturn(stockMarket);
        System.setIn(new ByteArrayInputStream("q".getBytes()));
        spy.run();
        verify(spy).doneApplication();
    }
}