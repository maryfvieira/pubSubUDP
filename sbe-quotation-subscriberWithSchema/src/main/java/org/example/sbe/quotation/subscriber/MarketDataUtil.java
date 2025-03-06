package org.example.sbe.quotation.subscriber;

import org.agrona.concurrent.UnsafeBuffer;
import org.example.sbe.quotation.model.MarketData;
import org.example.sbe.quotation.model.stub.MessageHeaderDecoder;
import org.example.sbe.quotation.model.stub.TradeDataDecoder;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

public class MarketDataUtil {

    public static MarketData readAndDecode(ByteBuffer buffer) {

        final int pos = buffer.position();

        final UnsafeBuffer directBuffer = new UnsafeBuffer(buffer);
        final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        final TradeDataDecoder dataDecoder = new TradeDataDecoder();

        dataDecoder.wrapAndApplyHeader(directBuffer, pos, headerDecoder);

        // set position
        final int encodedLength = headerDecoder.encodedLength() + dataDecoder.encodedLength();
        buffer.position(pos + encodedLength);

        final double price = BigDecimal.valueOf(dataDecoder.quote()
                        .price()
                        .mantissa())
                .scaleByPowerOfTen(dataDecoder.quote()
                        .price()
                        .exponent())
                .doubleValue();

        return MarketData.builder()
                .amount(dataDecoder.amount())
                .symbol(dataDecoder.quote()
                        .symbol())
                .market(dataDecoder.quote()
                        .market())
                .currency(dataDecoder.quote().currency())
                .price(price)
                .build();
    }
}
