/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.jms.config;

import org.w3c.dom.Element;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.jms.listener.endpoint.JmsActivationSpecConfig;
import org.springframework.jms.listener.endpoint.JmsMessageEndpointManager;
import org.springframework.util.StringUtils;

/**
 * Parser for the JMS <code>&lt;jca-listener-container&gt;</code> element.
 *
 * @author Juergen Hoeller
 * @since 2.1
 */
public class JcaListenerContainerParser extends AbstractListenerContainerParser {

	private static final String RESOURCE_ADAPTER_ATTRIBUTE = "resource-adapter";

	private static final String DEFAULT_RESOURCE_ADAPTER_BEAN_NAME = "resourceAdapter";

	private static final String ACTIVATION_SPEC_FACTORY_ATTRIBUTE = "activation-spec-factory";


	protected BeanDefinition parseContainer(Element listenerEle, Element containerEle, ParserContext parserContext) {
		RootBeanDefinition containerDef = new RootBeanDefinition(JmsMessageEndpointManager.class);

		String resourceAdapterBeanName = DEFAULT_RESOURCE_ADAPTER_BEAN_NAME;
		if (containerEle.hasAttribute(RESOURCE_ADAPTER_ATTRIBUTE)) {
			resourceAdapterBeanName = containerEle.getAttribute(RESOURCE_ADAPTER_ATTRIBUTE);
			if (!StringUtils.hasText(resourceAdapterBeanName)) {
				parserContext.getReaderContext().error(
						"Listener container 'resource-adapter' attribute contains empty value.", containerEle);
			}
		}
		containerDef.getPropertyValues().addPropertyValue("resourceAdapter",
				new RuntimeBeanReference(resourceAdapterBeanName));

		String activationSpecFactoryBeanName = containerEle.getAttribute(ACTIVATION_SPEC_FACTORY_ATTRIBUTE);
		if (StringUtils.hasText(activationSpecFactoryBeanName)) {
			containerDef.getPropertyValues().addPropertyValue("activationSpecFactory",
					new RuntimeBeanReference(activationSpecFactoryBeanName));
		}

		RootBeanDefinition configDef = new RootBeanDefinition(JmsActivationSpecConfig.class);
		parseListenerConfiguration(listenerEle, parserContext, configDef);
		parseContainerConfiguration(containerEle, parserContext, configDef);

		Integer acknowledgeMode = parseAcknowledgeMode(containerEle, parserContext);
		if (acknowledgeMode != null) {
			configDef.getPropertyValues().addPropertyValue("acknowledgeMode", acknowledgeMode);
		}

		String transactionManagerBeanName = containerEle.getAttribute(TRANSACTION_MANAGER_ATTRIBUTE);
		if (StringUtils.hasText(transactionManagerBeanName)) {
			containerDef.getPropertyValues().addPropertyValue("transactionManager",
					new RuntimeBeanReference(transactionManagerBeanName));
		}

		String concurrency = containerEle.getAttribute(CONCURRENCY_ATTRIBUTE);
		if (StringUtils.hasText(concurrency)) {
			configDef.getPropertyValues().addPropertyValue("maxConcurrency", new Integer(concurrency));
		}

		String prefetch = containerEle.getAttribute(PREFETCH_ATTRIBUTE);
		if (StringUtils.hasText(prefetch)) {
			configDef.getPropertyValues().addPropertyValue("prefetchSize", new Integer(prefetch));
		}

		containerDef.getPropertyValues().addPropertyValue("activationSpecConfig", configDef);

		return containerDef;
	}

}
