package org.example.sbe.quotation.model;

import lombok.Builder;
import lombok.Getter;
import org.example.sbe.quotation.model.stub.Currency;
import org.example.sbe.quotation.model.stub.Market;

import java.util.StringJoiner;

@Getter
@Builder
public class MarketData {
    private final int amount;
    private final double price;
    private final Market market;
    private final Currency currency;
    private final String symbol;

    @Override
    public String toString() {
        return new StringJoiner(", ", MarketData.class.getSimpleName() + "[", "]").add("amount=" + amount)
                .add("price=" + price)
                .add("market=" + market)
                .add("currency=" + currency)
                .add("symbol='" + symbol + "'")
                .toString();
    }
}
