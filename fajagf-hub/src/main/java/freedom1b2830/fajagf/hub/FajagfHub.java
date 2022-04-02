package freedom1b2830.fajagf.hub;

import java.net.InetSocketAddress;

import freedom1b2830.fajagf.Packet;
import freedom1b2830.fajagf.libs.Config;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class FajagfHub {
	static FajagfHub hub;

	public static void main(String[] args) {
		hub = new FajagfHub();
		hub.init();
		hub.bind();
	}

	private ChannelFuture bindChannelFuture;

	private ServerBootstrap serverBootstrap = new ServerBootstrap();
	private EventLoopGroup serverWorkgroup = new NioEventLoopGroup();

	private void bind() {
		// network
		bindChannelFuture = serverBootstrap.bind().awaitUninterruptibly();
	}

	private void init() {
		// network
		serverBootstrap.group(serverWorkgroup).channel(NioServerSocketChannel.class)
				.localAddress(new InetSocketAddress(Config.SERVERPORT));
		serverBootstrap.option(ChannelOption.SO_REUSEADDR, true);

		serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();

				p.addLast(new ObjectDecoder(Config.MAXOBJECTSIZE,
						ClassResolvers.softCachingConcurrentResolver(Packet.class.getClassLoader())));
				p.addLast(new ObjectEncoder());

				NetServerHandler handler = new NetServerHandler();
				p.addLast(handler);
			}
		});
	}
}
