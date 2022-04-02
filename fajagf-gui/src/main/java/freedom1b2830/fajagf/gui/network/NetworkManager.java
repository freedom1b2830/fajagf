package freedom1b2830.fajagf.gui.network;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freedom1b2830.fajagf.Packet;
import freedom1b2830.fajagf.libs.Config;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NetworkManager {
	private static final Logger LOGGER = LoggerFactory.getLogger("NetworkManager");

	static Bootstrap b;
	private static ChannelFuture future;
	private static NioEventLoopGroup gr;

	public static boolean connect(String host, Integer port) throws Throwable {
		b = new Bootstrap();
		b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 20000);
		// b.option(ChannelOption.SO_TIMEOUT, 30000);

		gr = new NioEventLoopGroup();

		b.group(gr).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new ObjectDecoder(Config.MAXOBJECTSIZE,
						ClassResolvers.softCachingConcurrentResolver(Packet.class.getClassLoader())));
				pipeline.addLast(new ObjectEncoder());

				pipeline.addLast(new GuiHandler());
			}

		});
		future = b.connect(host, port);
		boolean time = future.awaitUninterruptibly(2, TimeUnit.MINUTES);

		LOGGER.info("time:{} future:{}", time, future);
		Throwable throwable = future.cause();
		if (throwable != null) {
			throw throwable;
		}
		return future.isSuccess();
	}

	public static void disconnect() throws InterruptedException {
		future.sync();
		gr.shutdownGracefully();
	}

}
