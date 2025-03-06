package org.example.sbe.quotation.publisher;

import com.google.inject.Inject;
import io.aeron.Aeron;
import io.aeron.Publication;
import io.aeron.driver.MediaDriver;
import org.example.sbe.quotation.model.MarketData;

public class Publisher {

    private final MarketDataGenerator marketDataGenerator;

    @Inject
    public Publisher(MarketDataGenerator marketDataGenerator) {
        this.marketDataGenerator = marketDataGenerator;
    }

    /// /    private static final Logger log = LoggerFactory.getLogger(UdpSbeWriter.class);


    public void start() {
        MediaDriver driver = MediaDriver.launchEmbedded();

        var channel = ConfigLoader.get(Constants.UDP_MULTICAST_ADDRESS) + ":" + ConfigLoader.get(Constants.UDP_MULTICAST_PORT);
        var streamId = Integer.parseInt(ConfigLoader.get(Constants.STREAM_ID));

        try (Aeron aeron = Aeron.connect(new Aeron.Context().aeronDirectoryName(driver.aeronDirectoryName()));
             Publication publication = aeron.addPublication(channel, streamId)) {

            while (true) {
                MarketData mk = marketDataGenerator.getMarketData();
                var ret = marketDataGenerator.encodeMarketData(mk);

                publication.offer(ret);

                System.out.printf("Quotation sent " +
                                " Symbol:%s," +
                                " market:%s," +
                                " currency:%s," +
                                " amount:%s," +
                                " price:%s %n",
                        mk.getSymbol(),
                        mk.getMarket().toString(),
                        mk.getCurrency().toString(),
                        mk.getAmount(),
                        mk.getPrice());

                Thread.sleep(1500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }
    }
}

