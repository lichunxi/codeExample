/**
 * 
 */
package com.lichunxi.netty.client;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lichunxi
 * 
 */
public class HttpClientHandler extends SimpleChannelUpstreamHandler {
	public static final Logger LOGGER = LoggerFactory
			.getLogger(HttpClientHandler.class);
	private Boolean isChunked = false;

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (!isChunked) {
			HttpResponse response = (HttpResponse) e.getMessage();
//			LOGGER.debug(response.getContent().toString(
//					Charset.forName("UTF-8")));
			if (response.isChunked()) {
				isChunked = true;
			} else {
				// 此处开始处理响应消息
//				ChannelBuffer buf = response.getContent();
//				if (buf.readable()) {
//					System.out.println(buf.toString(CharsetUtil.UTF_8));
//				}
				System.out.println(response.getStatus().getCode());
				System.out.println(System.currentTimeMillis());
			}
		} else {
			HttpChunk chunk = (HttpChunk) e.getMessage();
			if (chunk.isLast()) {
				isChunked = false;
			} else {
//				ChannelBuffer buf = chunk.getContent();
//				if (buf.readable()) {
//					System.out.println(buf.toString(CharsetUtil.UTF_8));
//				}
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		//LOGGER.error("exception in channel.", e.getCause());
		//LOGGER.error("exception in channel.");
		e.getChannel().close();
	}
}
