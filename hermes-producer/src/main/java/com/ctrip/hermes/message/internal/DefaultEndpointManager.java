package com.ctrip.hermes.message.internal;

import org.unidal.lookup.annotation.Inject;

import com.ctrip.hermes.meta.MetaService;
import com.ctrip.hermes.meta.entity.Endpoint;

/**
 * @author Leo Liang(jhliang@ctrip.com)
 *
 */
public class DefaultEndpointManager implements EndpointManager {

	@Inject
	private MetaService m_metaService;

	@Override
	public Endpoint getEndpoint(String topic, int partitionNo) {
		return m_metaService.findEndpoint(m_metaService.getPartitions(topic).get(partitionNo).getEndpoint());
	}
}
