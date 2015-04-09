package com.ctrip.hermes.broker.queue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.unidal.lookup.annotation.Named;

import com.ctrip.hermes.core.bo.Tpg;
import com.ctrip.hermes.core.transport.endpoint.EndpointChannel;

/**
 * @author Leo Liang(jhliang@ctrip.com)
 *
 */
@Named(type = MessageQueuePullerManager.class)
public class DefaultMessageQueuePullerManager implements MessageQueuePullerManager {

	private ConcurrentMap<Tpg, MessageQueuePuller> m_pullers = new ConcurrentHashMap<>();

	@Override
	public void startPuller(Tpg tpg, long correlationId, EndpointChannel channel) {
		m_pullers.putIfAbsent(tpg, new DefaultMessageQueuePuller(tpg));
		m_pullers.get(tpg).addEndpoint(correlationId, channel);
		m_pullers.get(tpg).start();
	}

}