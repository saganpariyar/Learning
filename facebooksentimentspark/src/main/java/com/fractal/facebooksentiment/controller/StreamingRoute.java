package com.fractal.facebooksentiment.controller;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author robbreuk
 */
public final class StreamingRoute extends RouteBuilder {

    @Override
    public final void configure() throws Exception {
        from("activemq:fb-queue")
        .to("websocket://fbstorm?sendToAll=true");
    }
}
