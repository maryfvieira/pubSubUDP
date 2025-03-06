package org.example.sbe.quotation.subscriber;

import io.aeron.Aeron;
import io.aeron.FragmentAssembler;
import io.aeron.Subscription;
import io.aeron.driver.MediaDriver;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.ShutdownSignalBarrier;
import org.agrona.concurrent.SleepingIdleStrategy;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public class Subscriber {

    private static final AtomicBoolean running = new AtomicBoolean(true);

    public Subscriber() {

    }

    public static void stop() {
        running.set(false);
    }

    public void start() {
        MediaDriver driver = MediaDriver.launchEmbedded();

        var channel = ConfigLoader.get(Constants.UDP_MULTICAST_ADDRESS) + ":" + ConfigLoader.get(Constants.UDP_MULTICAST_PORT);
        var streamId = Integer.parseInt(ConfigLoader.get(Constants.STREAM_ID));

        try (Aeron aeron = Aeron.connect(new Aeron.Context().aeronDirectoryName(driver.aeronDirectoryName()));
             Subscription subscription = aeron.addSubscription(channel, streamId)) {

            ShutdownSignalBarrier barrier = new ShutdownSignalBarrier();

            // FragmentHandler que converte a mensagem recebida para ByteBuffer
            FragmentHandler handler = new ByteBufferFragmentHandler();

            System.out.println("Subscriber iniciado. Aguardando mensagens...");

            IdleStrategy idleStrategy = new SleepingIdleStrategy(100);

            while (running.get()) {
                // Faz o polling das mensagens recebidas
                int fragmentsRead = subscription.poll(new FragmentAssembler(handler), 10);
                if (fragmentsRead == 0) {
                    Thread.yield(); // Evita alto uso de CPU
                }
            }

//            while (true) {
//                subscription.poll((buffer, offset, length, header) -> {
//                    byte[] data = new byte[length];
//                    buffer.getBytes(offset, data);
//                    String message = new String(data);
//                    System.out.println("Recebido: " + message);
//                }, 10);
//
//                idleStrategy.idle();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }
    }

    // Implementação do FragmentHandler que retorna ByteBuffer
    private static class ByteBufferFragmentHandler implements FragmentHandler {
        @Override
        public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
            // Copia os dados para um ByteBuffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(length);
            buffer.getBytes(offset, byteBuffer, 0, length);

            // Exemplo de processamento do ByteBuffer recebido
            System.out.println("Mensagem recebida: " + new String(byteBuffer.array()));
        }
    }

}

