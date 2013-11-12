/**
 * 
 */
package com.lichunxi.netty.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpServerCodec;

/**
 * @author lichunxi
 * 
 */
public class HttpServerPipelineFactory implements ChannelPipelineFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();

		// pipeline.addLast("decoder", new HttpRequestDecoder());
		// pipeline.addLast("encoder", new HttpResponseEncoder());
		// pipeline.addLast("handler", new HttpServerHandler());
		pipeline.addLast("codec", new HttpServerCodec());
		pipeline.addLast("handler", new HttpServerHandler());
		return pipeline;
	}

}
