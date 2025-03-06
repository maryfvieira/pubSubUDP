package org.example.sbe.quotation.publisher;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class BasicModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MarketDataGenerator.class);
        bind(Publisher.class);
    }

    @Provides
    QuotationCache getDatabase() {
        return QuotationCache.getInstance();
    }
}
