package org.example.sbe.quotation.publisher;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Executor {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BasicModule());

        Publisher sbeWriter = injector.getInstance(Publisher.class);
        sbeWriter.start();

    }
}


