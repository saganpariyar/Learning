package org.p7h.storm.sentimentanalysis.cameljms;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author robbreuk
 */
public final class StreamingRoute extends RouteBuilder {

	@Override
	public final void configure() throws Exception {
		from("activemq:my-queue").delay(50000)
				.to("websocket://storm?sendToAll=true");
	}
}
