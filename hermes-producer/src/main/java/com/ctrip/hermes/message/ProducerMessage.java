package com.ctrip.hermes.message;

import java.util.HashMap;
import java.util.Map;

public class ProducerMessage<T> {
	private String m_topic;

	private T m_body;

	private String m_key;

	private boolean m_priority = false;

	private String m_partition;

	private int m_partitionNo;

	private int m_msgSeqNo;

	private long m_bornTime;

	private Map<String, Object> m_properties = new HashMap<String, Object>();

	public ProducerMessage() {

	}

	public ProducerMessage(String m_topic, T m_body) {
		this.m_topic = m_topic;
		this.m_body = m_body;
	}

	public ProducerMessage(ProducerMessage<T> other) {
		setTopic(other.getTopic());
		setBody(other.getBody());
		setKey(other.getKey());
		setPriority(other.isPriority());
		setPartition(other.getPartition());
		setProperties(other.getProperties());
		setBornTime(other.getBornTime());
	}

	public int getMsgSeqNo() {
		return m_msgSeqNo;
	}

	public void setMsgSeqNo(int msgSeqNo) {
		m_msgSeqNo = msgSeqNo;
	}

	public int getPartitionNo() {
		return m_partitionNo;
	}

	public void setPartitionNo(int partitionNo) {
		m_partitionNo = partitionNo;
	}

	public T getBody() {
		return m_body;
	}

	public String getKey() {
		return m_key;
	}

	public String getTopic() {
		return m_topic;
	}

	@SuppressWarnings("unchecked")
	public void setBody(Object object) {
		m_body = (T) object;
	}

	public void setKey(String key) {
		m_key = key;
	}

	public boolean isPriority() {
		return m_priority;
	}

	public void setPriority(boolean priority) {
		m_priority = priority;
	}

	public String getPartition() {
		return m_partition;
	}

	public void setPartition(String partition) {
		m_partition = partition;
	}

	public void setTopic(String topic) {
		m_topic = topic;
	}

	public long getBornTime() {
		return m_bornTime;
	}

	public void setBornTime(long bornTime) {
		m_bornTime = bornTime;
	}

	public Map<String, Object> getProperties() {
		return m_properties;
	}

	public void setProperties(Map<String, Object> properties) {
		m_properties = properties;
	}

	public void addProperty(String name, Object value) {
		m_properties.put(name, value);
	}

	@SuppressWarnings("unchecked")
	public <V> V getProperty(String name) {
		return (V) m_properties.get(name);
	}

}
