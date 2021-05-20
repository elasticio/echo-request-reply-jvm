package io.elastic.reply.triggers;

import io.elastic.api.ExecutionParameters;
import io.elastic.api.Function;
import io.elastic.api.Message;
import io.elastic.api.HttpReply;
import io.elastic.api.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;

import java.io.ByteArrayInputStream;

import java.lang.Thread;


public class Reply implements Function {
    private static final Logger logger = LoggerFactory.getLogger(Reply.class);

    /**
     * @param parameters execution parameters
     */
    @Override
    public void execute(final ExecutionParameters parameters) {
	Thread.sleep(40);

        final JsonObject body = Json.createObjectBuilder()
                .add("echo", parameters.getMessage().getBody())
                .build();

        final Message data
                = new Message.Builder().body(body).build();

        final HttpReply httpReply = new HttpReply.Builder()
                .header("Content-type", "application/json")
                .status(HttpReply.Status.ACCEPTED)
                .content(new ByteArrayInputStream(JSON.stringify(body).getBytes()))
                .build();

        logger.info("Emitting data");

        // emitting the message to the platform
        parameters.getEventEmitter().emitData(data);


        logger.info("Emitting reply");
        parameters.getEventEmitter().emitHttpReply(httpReply);
    }
}
