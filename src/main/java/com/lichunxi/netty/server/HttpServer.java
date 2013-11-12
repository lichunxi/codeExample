/**
 * 
 */
package com.lichunxi.netty.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lichunxi
 * 
 */
public class HttpServer {
	/**
	 * 日志
	 */
	public static final Logger LOGGER = LoggerFactory
			.getLogger(HttpServer.class);

	/**
	 * 服务器监听端口
	 */
	private int port;
	private ServerBootstrap bootstrap;
	private Channel serverChannel;

	public HttpServer(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void start() {
		// Configure the server.
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));

		// Set up the event pipeline factory.
		bootstrap.setPipelineFactory(new HttpServerPipelineFactory());

		// Bind and start to accept incoming connections.
		serverChannel = bootstrap.bind(new InetSocketAddress(port));

		LOGGER.info("server started at port:" + this.port);
	}

	public void stop() {
		if (null != serverChannel) {
			serverChannel.close().addListener(ChannelFutureListener.CLOSE);
		}

		if (null != bootstrap) {
			bootstrap.releaseExternalResources();
		}
		LOGGER.info("stop server successfully.");
	}

	public static void main(String[] args) {
		HttpServer server = new HttpServer(8080);
		server.start();
	}

}
