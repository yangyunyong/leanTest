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

import javax.jms.Session;

import org.w3c.dom.Element;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.DefaultMessageListenerContainer102;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.SimpleMessageListenerContainer102;
import org.springframework.util.StringUtils;

/**
 * Parser for the JMS <code>&lt;listener-container&gt;</code> element.
 * 
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @since 2.1
 */
public class JmsListenerContainerParser extends AbstractListenerContainerParser {

	private static final String CONTAINER_TYPE_ATTRIBUTE = "container-type";

	private static final String CONNECTION_FACTORY_ATTRIBUTE = "connection-factory";

	private static final String DEFAULT_CONNECTION_FACTORY_BEAN_NAME = "connectionFactory";

	private static final String TASK_EXECUTOR_ATTRIBUTE = "task-executor";

	private static final String DESTINATION_RESOLVER_ATTRIBUTE = "destination-resolver";


	protected BeanDefinition parseContainer(Element listenerEle, Element containerEle, ParserContext parserContext) {
		RootBeanDefinition containerDef = new RootBeanDefinition();
		parseListenerConfiguration(listenerEle, parserContext, containerDef);
		parseContainerConfiguration(containerEle, parserContext, containerDef);

		String containerType = containerEle.getAttribute(CONTAINER_TYPE_ATTRIBUTE);
		if ("default".equals(containerType)) {
			containerDef.setBeanClass(DefaultMessageListenerContainer.class);
		}
		else if ("default102".equals(containerType)) {
			containerDef.setBeanClass(DefaultMessageListenerContainer102.class);
		}
		else if ("simple".equals(containerType)) {
			containerDef.setBeanClass(SimpleMessageListenerContainer.class);
		}
		else if ("simple102".equals(containerType)) {
			containerDef.setBeanClass(SimpleMessageListenerContainer102.class);
		}
		else {
			parserContext.getReaderContext().error(
					"Invalid 'container-type' attribute: only \"default\" and \"simple\" supported.", containerEle);
		}

		String connectionFactoryBeanName = DEFAULT_CONNECTION_FACTORY_BEAN_NAME;
		if (containerEle.hasAttribute(CONNECTION_FACTORY_ATTRIBUTE)) {
			connectionFactoryBeanName = containerEle.getAttribute(CONNECTION_FACTORY_ATTRIBUTE);
			if (!StringUtils.hasText(connectionFactoryBeanName)) {
				parserContext.getReaderContext().error(
						"Listener container 'connection-factory' attribute contains empty value.", containerEle);
			}
		}
		containerDef.getPropertyValues().addPropertyValue("connectionFactory",
				new RuntimeBeanReference(connectionFactoryBeanName));

		String taskExecutorBeanName = containerEle.getAttribute(TASK_EXECUTOR_ATTRIBUTE);
		if (StringUtils.hasText(taskExecutorBeanName)) {
			containerDef.getPropertyValues().addPropertyValue("taskExecutor",
					new RuntimeBeanReference(taskExecutorBeanName));
		}

		String destinationResolverBeanName = containerEle.getAttribute(DESTINATION_RESOLVER_ATTRIBUTE);
		if (StringUtils.hasText(destinationResolverBeanName)) {
			containerDef.getPropertyValues().addPropertyValue("destinationResolver",
					new RuntimeBeanReference(destinationResolverBeanName));
		}

		Integer acknowledgeMode = parseAcknowledgeMode(containerEle, parserContext);
		if (acknowledgeMode != null) {
			if (acknowledgeMode.intValue() == Session.SESSION_TRANSACTED) {
				containerDef.getPropertyValues().addPropertyValue("sessionTransacted", Boolean.TRUE);
			}
			else {
				containerDef.getPropertyValues().addPropertyValue("sessionAcknowledgeMode", acknowledgeMode);
			}
		}

		String transactionManagerBeanName = containerEle.getAttribute(TRANSACTION_MANAGER_ATTRIBUTE);
		if (StringUtils.hasText(transactionManagerBeanName)) {
			if (containerType.startsWith("simple")) {
				parserContext.getReaderContext().error(
						"'transaction-manager' attribute not supported for listener container of type \"simple\".", containerEle);
			}
			containerDef.getPropertyValues().addPropertyValue("transactionManager",
					new RuntimeBeanReference(transactionManagerBeanName));
		}

		String concurrency = containerEle.getAttribute(CONCURRENCY_ATTRIBUTE);
		if (StringUtils.hasText(concurrency)) {
			if (containerType.startsWith("default")) {
				containerDef.getPropertyValues().addPropertyValue("maxConcurrentConsumers", new Integer(concurrency));
			}
			else {
				containerDef.getPropertyValues().addPropertyValue("concurrentConsumers", new Integer(concurrency));
			}
		}

		String prefetch = containerEle.getAttribute(PREFETCH_ATTRIBUTE);
		if (StringUtils.hasText(prefetch)) {
			if (containerType.startsWith("default")) {
				containerDef.getPropertyValues().addPropertyValue("maxMessagesPerTask", new Integer(prefetch));
			}
		}

		return containerDef;
	}

}
