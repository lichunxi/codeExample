/**
 * 
 */
package com.lichunxi.netty.client;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http消息分发
 * 
 * @author lichunxi
 * 
 */
public class HttpClient {

	/**
	 * 日志
	 */
	public static final Logger LOGGER = LoggerFactory
			.getLogger(HttpClient.class);

	private ClientBootstrap bootstrap;

	public HttpClient(){
		init();
	}
	
	/**
	 * 向指定的地址发送消息
	 * 
	 * @param uri
	 *            目标uri
	 * @return void
	 */
	public void dispatch(final URI uri) {

		String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
		final String host = uri.getHost() == null ? "localhost" : uri.getHost();
		int port = uri.getPort();
		if (port == -1) {
			if ("http".equalsIgnoreCase(scheme)) {
				port = 80;
			} else if ("https".equalsIgnoreCase(scheme)) {
				port = 443;
			}
		}

		if (!"http".equalsIgnoreCase(scheme)
				&& !"https".equalsIgnoreCase(scheme)) {
			System.err.println("Only HTTP(S) is supported.");
			return;
		}

		// Start the connection attempt.
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
				port));

		// Wait until the connection attempt succeeds or fails.
		future.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future) {
				if (!future.isSuccess()) {
					//LOGGER.error("get channel fail.", future.getCause());
					//LOGGER.error("get channel fail.");
					return;
				}
				Channel channel = future.getChannel();

				HttpRequest request = new DefaultHttpRequest(
	                    HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
				request.headers().set(HttpHeaders.Names.HOST, host);
	            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);

				// Send the HTTP request.
				channel.write(request);

				// Wait for the server to close the connection.
				channel.getCloseFuture().addListener(CLOSE);
			}
		});

	}

	public void init() {
		// Configure the client.
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));

		bootstrap.setOption("connectTimeoutMillis", 10000);
		// Set up the event pipeline factory.
		bootstrap.setPipelineFactory(new HttpClientPipelineFactory());
		LOGGER.info("init client successfully.");
	}

	public void destroy() {
		// Shut down executor threads to exit.
		if (null != bootstrap) {
			bootstrap.releaseExternalResources();
		}
		LOGGER.info("release client successfully.");
	}

}
