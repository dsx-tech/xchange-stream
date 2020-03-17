package info.bitrich.xchangestream.bequant.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.bequant.BequantStreamingService;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class BequantStreamingServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BequantStreamingService streamingService = new BequantStreamingService("testUrl");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getChannelNameFromMessageTest() throws IOException, InvocationTargetException, IllegalAccessException {


        Method method = MethodUtils.getMatchingMethod(BequantStreamingService.class, "getChannelNameFromMessage", JsonNode.class);
        method.setAccessible(true);

        String json = "{\"method\":\"aaa\"}";
        Assert.assertEquals("aaa", method.invoke(streamingService, objectMapper.readTree(json)));

        json = "{ \"method\": \"updateOrderbook\", \"params\": { \"symbol\": \"ETHBTC\" } }";
        Assert.assertEquals("orderbook-ETHBTC", method.invoke(streamingService, objectMapper.readTree(json)));

        json = "{ \"method\": \"snapshotOrderbook\", \"params\": { \"symbol\": \"ETHBTC\" } }";
        Assert.assertEquals("orderbook-ETHBTC", method.invoke(streamingService, objectMapper.readTree(json)));

        json = "{ \"method\": \"test\", \"params\": { \"symbol\": \"ETHBTC\" } }";
        Assert.assertEquals("test-ETHBTC", method.invoke(streamingService, objectMapper.readTree(json)));

        json = "{ \"noMethod\": \"updateOrderbook\" } }";

        thrown.expect(InvocationTargetException.class);
        method.invoke(streamingService, objectMapper.readTree(json));
    }

}
