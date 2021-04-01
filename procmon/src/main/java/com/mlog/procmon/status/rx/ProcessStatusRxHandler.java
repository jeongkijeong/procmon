package com.mlog.procmon.status.rx;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.medialog.meerkat.handler.MeerKat;
import com.mlog.procmon.cli.CliManager;
import com.mlog.procmon.common.Utils;
import com.mlog.procmon.context.TimeHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

public class ProcessStatusRxHandler extends TimeHandler implements MeerKat {
	private Map<ChannelId, ChannelHandlerContext> connectdChannel = null;

	public ProcessStatusRxHandler() {
		super();
		connectdChannel = new HashMap<ChannelId, ChannelHandlerContext>();
	}

	@Override
	public void handler(Object object) {
		if (object != null) {
			String message = Utils.objectToJsonStr(object);
			sendMessage(message);
		}
	}

	@Override
	public void channelActived(ChannelHandlerContext ctx) {
		connectdChannel.put(ctx.channel().id(), ctx);
	}

	@Override
	public void channelRemoved(ChannelHandlerContext ctx) {
		connectdChannel.remove(ctx.channel().id());
	}

	@Override
	public void recvMessage(String msg) {
		if (msg == null || msg.length() == 0) {
			return;
		}

		CliManager.getInstance().address(msg);
	}

	@Override
	public void recvMessage(ChannelHandlerContext ctx, Object msg) {
		recvMessage(msg.toString());
	}

	@Override
	public void sendMessage(String msg) {
		for (Entry<ChannelId, ChannelHandlerContext> entry : connectdChannel.entrySet()) {
			ChannelHandlerContext context = entry.getValue();

			if (context != null) {
				context.writeAndFlush(msg);
			}
		}
	}

}
