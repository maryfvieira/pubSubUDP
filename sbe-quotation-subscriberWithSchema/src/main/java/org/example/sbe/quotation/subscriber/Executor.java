package org.example.sbe.quotation.subscriber;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Executor {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BasicModule());
        Subscriber sub = injector.getInstance(Subscriber.class);
        sub.start();
    }
}
