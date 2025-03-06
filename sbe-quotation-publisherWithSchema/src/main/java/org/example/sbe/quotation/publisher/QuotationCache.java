package org.example.sbe.quotation.publisher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuotationCache {

    private static volatile QuotationCache instance;
    private final List<Double> generatedNumbers = Collections.synchronizedList(new ArrayList<>());

    private QuotationCache() {

    }

    public static QuotationCache getInstance() {
        QuotationCache result = instance;
        if (result == null) {
            synchronized (QuotationCache.class) {
                result = instance;
                if (result == null) {
                    instance = result = new QuotationCache();
                }
            }
        }
        return result;
    }

    public Double[] getGeneratedNumbersAsArray() {
        return getGeneratedNumbers().stream().toArray(value -> new Double[value]);
    }

    public List<Double> getGeneratedNumbers() {
        return generatedNumbers;
    }

    public void setGeneratedNumbers(Double value) {
        generatedNumbers.add(value);
    }
}
