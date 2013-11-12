/**
 * 
 */
package com.lichunxi.netty.server;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.base64.Base64;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.util.CharsetUtil;

/**
 * @author lichunxi
 * 
 */
public class HttpServerHandler extends SimpleChannelUpstreamHandler {

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		HttpRequest request = (HttpRequest) e.getMessage();
		StringBuilder buf = new StringBuilder();
		buf.append("===================================\r\n");

		buf.append("VERSION: " + request.getProtocolVersion() + "\r\n");
		buf.append("HOSTNAME: " + HttpHeaders.getHost(request, "unknown")
				+ "\r\n");
		buf.append("REQUEST_URI: " + request.getUri() + "\r\n\r\n");

		for (Map.Entry<String, String> h : request.headers()) {
			buf.append("HEADER: " + h.getKey() + " = " + h.getValue() + "\r\n");
		}
		buf.append("\r\n");

		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(
				request.getUri());
		Map<String, List<String>> params = queryStringDecoder.getParameters();
		if (!params.isEmpty()) {
			for (Entry<String, List<String>> p : params.entrySet()) {
				String key = p.getKey();
				List<String> vals = p.getValue();
				for (String val : vals) {
					buf.append("PARAM: " + key + " = " + val + "\r\n");
				}
			}
			buf.append("\r\n");
		}

		ChannelBuffer content = request.getContent();
		if (content.readable()) {
			buf.append("CONTENT: " + content.toString(CharsetUtil.UTF_8)
					+ "\r\n");
		}
		System.out.print(buf);
		writeResponse(e);
	}

	private void writeResponse(MessageEvent e) {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK);
		response.headers().add(HttpHeaders.Names.CONTENT_TYPE,
				"text/plain; charset=UTF-8");

		StringBuilder buf = new StringBuilder();
		buf.append("my time is:");
		buf.append(System.currentTimeMillis());
		response.setContent(ChannelBuffers.copiedBuffer(buf.toString(),
				CharsetUtil.UTF_8));
		response.headers().add(HttpHeaders.Names.CONTENT_LENGTH,
				response.getContent().readableBytes());

		System.out.println(buf);
		System.out.println("base64 encode:"
				+ Base64.encode(ChannelBuffers.copiedBuffer(buf.toString(),
						CharsetUtil.UTF_8)));
		ChannelFuture future = e.getChannel().write(response);
		future.addListener(ChannelFutureListener.CLOSE);
	}
}