package org.example.sbe.quotation.publisher;

import com.google.inject.Inject;
import org.agrona.DirectBuffer;
import org.example.sbe.quotation.model.MarketData;
import org.example.sbe.quotation.model.stub.Currency;
import org.example.sbe.quotation.model.stub.Market;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.Random;

public class MarketDataGenerator {

    private final Double defaultValue = 33.89;
    private final QuotationCache quotationCache;
//    private final ByteBuffer buffer = ByteBuffer.allocate(128);

    @Inject
    public MarketDataGenerator(QuotationCache quotationCache) {
        this.quotationCache = quotationCache;
    }

    public MarketData getMarketData() {
        var quotation = getRandomWithExclusion(32.25, 34.78);

        MarketData mk = MarketData
                .builder()
                .market(Market.NASDAQ)
                .amount(1000)
                .currency(Currency.USD)
                .price(quotation)
                .symbol("PETR4")
                .build();

        return mk;
    }

    private double getRandomWithExclusion(Double min, Double max) {
        Double[] exclude = quotationCache.getGeneratedNumbersAsArray();

        Random rnd = new Random();
        OptionalDouble random = rnd.doubles(min, max + 1)
                .filter(num -> Arrays.stream(exclude).noneMatch(ex -> num == ex)).findFirst();
        Double quotation = random.orElse(defaultValue);

        if (quotation.doubleValue() != defaultValue.doubleValue()) {
            quotationCache.setGeneratedNumbers(quotation);
        }
        return quotation;
    }

    public DirectBuffer encodeMarketData(MarketData mk) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(256);
        DirectBuffer directBufferResult = MarketDataUtil.encodeAndWrite(byteBuffer, mk);
        return directBufferResult;
    }
}
