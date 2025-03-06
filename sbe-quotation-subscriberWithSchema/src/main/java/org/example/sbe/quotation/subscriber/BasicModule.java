package org.example.sbe.quotation.subscriber;

import com.google.inject.AbstractModule;

public class BasicModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Subscriber.class);
    }


}
