package com.ctrip.hermes.message.codec;

import java.nio.ByteBuffer;

import org.unidal.lookup.annotation.Inject;

import com.ctrip.hermes.message.Message;

public class DefaultMessageCodec implements MessageCodec {

	@Inject
	private CodecManager m_codecManager;

	@Override
	public ByteBuffer encode(Message<?> msg) {
		Codec bodyCodec = m_codecManager.getCodec(msg.getTopic());
		byte[] msgBody = bodyCodec.encode(msg.getBody());

		ByteBuffer buf = ByteBuffer.allocateDirect(sizeOf(msgBody, msg));
		HermesPrimitiveCodec codec = new HermesPrimitiveCodec(buf);

		write(msg, msgBody, codec);

		return buf;
	}

	@Override
	public Message<byte[]> decode(ByteBuffer buf) {
		HermesPrimitiveCodec codec = new HermesPrimitiveCodec(buf);
		Message<byte[]> msg = read(codec);

		return msg;
	}

	public void write(Message<?> msg, byte[] msgBody, HermesPrimitiveCodec codec) {
		codec.writeString(msg.getTopic());
		codec.writeString(msg.getKey());
		codec.writeString(msg.getPartition());
		codec.writeBoolean(msg.isPriority());
		codec.writeLong(msg.getBornTime());

		codec.writeMap(msg.getProperties());

		codec.writeBytes(msgBody);
	}

	public Message<byte[]> read(HermesPrimitiveCodec codec) {
		Message<byte[]> msg = new Message<>();

		msg.setTopic(codec.readString());
		msg.setKey(codec.readString());
		msg.setPartition(codec.readString());
		msg.setPriority(codec.readBoolean());
		msg.setBornTime(codec.readLong());

		msg.setProperties(codec.readMap());

		msg.setBody(codec.readBytes());
		return msg;
	}

	public int sizeOf(byte[] body, Message<?> msg) {
		// todo: calculate right size.
		return HermesPrimitiveCodec.calLength(msg.getTopic()) +
				  HermesPrimitiveCodec.calLength(msg.getKey()) +
				  HermesPrimitiveCodec.calLength(msg.getPartition()) +
				  HermesPrimitiveCodec.calLength(msg.isPriority()) +
				  HermesPrimitiveCodec.calLength(msg.getBornTime()) +
				  HermesPrimitiveCodec.calLength(msg.getProperties()) +
				  HermesPrimitiveCodec.calLength(body) + 100;
	}
}
