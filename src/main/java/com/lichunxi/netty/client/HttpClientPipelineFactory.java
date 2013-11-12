/**
 * 
 */
package com.lichunxi.netty.client;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpClientCodec;

/**
 * @author lichunxi
 * 
 */
public class HttpClientPipelineFactory implements ChannelPipelineFactory {

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("codec", new HttpClientCodec());
		pipeline.addLast("handler", new HttpClientHandler());
		// pipeline.addLast("codec", codec);
		// pipeline.addLast("handler", handler);
		return pipeline;
	}

}