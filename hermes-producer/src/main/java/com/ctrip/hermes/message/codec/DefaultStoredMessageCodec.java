package com.ctrip.hermes.message.codec;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.ctrip.hermes.message.StoredMessage;
import com.ctrip.hermes.storage.storage.Offset;

public class DefaultStoredMessageCodec implements StoredMessageCodec {

	@Override
	public ByteBuffer encode(List<StoredMessage<byte[]>> msgs) {
		ByteBuffer buf = ByteBuffer.allocateDirect(sizeOf(msgs));
		HermesPrimitiveCodec codec = new HermesPrimitiveCodec(buf);

		codec.writeInt(msgs.size());

		for (StoredMessage<byte[]> msg : msgs) {
			writeMsg(codec, msg);
		}

		return buf;
	}

	private void writeMsg(HermesPrimitiveCodec codec, StoredMessage<byte[]> msg) {
		codec.writeObject(msg.getTopic());
		codec.writeObject(msg.getKey());
		codec.writeObject(msg.getPartition());
		codec.writeObject(msg.isPriority());
		codec.writeObject(msg.getBornTime());

		codec.writeObject(msg.getProperties());

		codec.writeObject(msg.getBody());

		writeOffset(codec, msg.getAckOffset());
		writeOffset(codec, msg.getOffset());
		codec.writeBoolean(msg.isSuccess());
	}

	@Override
	public List<StoredMessage<byte[]>> decode(ByteBuffer buf) {
		HermesPrimitiveCodec codec = new HermesPrimitiveCodec(buf);

		int listSize = codec.readInt();
		List<StoredMessage<byte[]>> result = new ArrayList<StoredMessage<byte[]>>(listSize);

		for (int i = 0; i < listSize; i++) {
			result.add(readMsg(codec));
		}

		return result;
	}

	private StoredMessage<byte[]> readMsg(HermesPrimitiveCodec codec) {
		StoredMessage<byte[]> msg = new StoredMessage<byte[]>();

		msg.setTopic(codec.readString());
		msg.setKey(codec.readString());
		msg.setPartition(codec.readString());
		msg.setPriority(codec.readBoolean());
		msg.setBornTime(codec.readLong());

		msg.setProperties(codec.readMap());

		msg.setBody(codec.readBytes());

		msg.setAckOffset(readOffset(codec));
		msg.setOffset(readOffset(codec));
		msg.setSuccess(codec.readBoolean());

		return msg;
	}

	private void writeOffset(HermesPrimitiveCodec codec, Offset offset) {
		if (offset == null) {
			codec.writeNull();
		} else {
			codec.writeObject(offset.getId());
			codec.writeObject(offset.getOffset());
		}
	}

	private Offset readOffset(HermesPrimitiveCodec codec) {
		Offset offset = null;

		if (!codec.isNextNull()) {
			offset = new Offset(codec.readString(), codec.readLong());
		}

		return offset;
	}

	private int sizeOf(List<StoredMessage<byte[]>> msgs) {
		// TODO estemate msg size
		int size = 0;
		for (StoredMessage<byte[]> msg : msgs) {
			size += msg.getBody().length;
		}

		return size + 1024 * 4;
	}

}
