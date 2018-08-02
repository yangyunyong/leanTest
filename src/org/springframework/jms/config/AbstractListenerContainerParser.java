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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.util.StringUtils;

/**
 * Abstract parser for JMS listener container elements, providing support for
 * common properties that are identical for all listener container variants.
 *
 * @author Juergen Hoeller
 * @since 2.1
 */
public abstract class AbstractListenerContainerParser implements BeanDefinitionParser {

	protected static final String LISTENER_ELEMENT = "listener";

	protected static final String ID_ATTRIBUTE = "id";

	protected static final String DESTINATION_ATTRIBUTE = "destination";

	protected static final String SUBSCRIPTION_ATTRIBUTE = "subscription";

	protected static final String SELECTOR_ATTRIBUTE = "selector";

	protected static final String HANDLER_BEAN_ATTRIBUTE = "ref";

	protected static final String HANDLER_METHOD_ATTRIBUTE = "method";

	protected static final String DESTINATION_TYPE_ATTRIBUTE = "destination-type";

	protected static final String DESTINATION_TYPE_QUEUE = "queue";

	protected static final String DESTINATION_TYPE_TOPIC = "topic";

	protected static final String DESTINATION_TYPE_DURABLE_TOPIC = "durableTopic";

	protected static final String CLIENT_ID_ATTRIBUTE = "client-id";

	protected static final String ACKNOWLEDGE_ATTRIBUTE = "acknowledge";

	protected static final String ACKNOWLEDGE_AUTO = "auto";

	protected static final String ACKNOWLEDGE_CLIENT = "client";

	protected static final String ACKNOWLEDGE_DUPS_OK = "dups-ok";

	protected static final String ACKNOWLEDGE_TRANSACTED = "transacted";

	protected static final String TRANSACTION_MANAGER_ATTRIBUTE = "transaction-manager";

	protected static final String CONCURRENCY_ATTRIBUTE = "concurrency";

	protected static final String PREFETCH_ATTRIBUTE = "prefetch";


	public BeanDefinition parse(Element element, ParserContext parserContext) {
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = child.getLocalName();
				if (LISTENER_ELEMENT.equals(localName)) {
					parseListener((Element) child, element, parserContext);
				}
			}
		}
		return null;
	}

	private void parseListener(Element listenerEle, Element containerEle, ParserContext parserContext) {
		RootBeanDefinition listenerDef = new RootBeanDefinition(MessageListenerAdapter.class);

		String handlerBean = listenerEle.getAttribute(HANDLER_BEAN_ATTRIBUTE);
		if (!StringUtils.hasText(handlerBean)) {
			parserContext.getReaderContext().error(
					"Listener 'ref' attribute contains empty value.", listenerEle);
		}
		listenerDef.getPropertyValues().addPropertyValue("delegate", new RuntimeBeanReference(handlerBean));

		String handlerMethod = null;
		if (listenerEle.hasAttribute(HANDLER_METHOD_ATTRIBUTE)) {
			handlerMethod = listenerEle.getAttribute(HANDLER_METHOD_ATTRIBUTE);
			if (!StringUtils.hasText(handlerMethod)) {
				parserContext.getReaderContext().error(
						"Listener 'method' attribute contains empty value.", listenerEle);
			}
		}
		listenerDef.getPropertyValues().addPropertyValue("defaultListenerMethod", handlerMethod);

		BeanDefinition containerDef = parseContainer(listenerEle, containerEle, parserContext);
		containerDef.getPropertyValues().addPropertyValue("messageListener", listenerDef);

		String containerBeanName = listenerEle.getAttribute(ID_ATTRIBUTE);
		if (StringUtils.hasText(containerBeanName)) {
			parserContext.getRegistry().registerBeanDefinition(containerBeanName, containerDef);
		}
		else {
			parserContext.getReaderContext().registerWithGeneratedName(containerDef);
		}
	}

	protected abstract BeanDefinition parseContainer(
			Element listenerEle, Element containerEle, ParserContext parserContext);


	protected void parseListenerConfiguration(Element ele, ParserContext parserContext, BeanDefinition configDef) {
		String destination = ele.getAttribute(DESTINATION_ATTRIBUTE);
		if (!StringUtils.hasText(destination)) {
			parserContext.getReaderContext().error(
					"Listener 'destination' attribute contains empty value.", ele);
		}
		configDef.getPropertyValues().addPropertyValue("destinationName", destination);

		String subscription = null;
		if (ele.hasAttribute(SUBSCRIPTION_ATTRIBUTE)) {
			subscription = ele.getAttribute(SUBSCRIPTION_ATTRIBUTE);
			if (!StringUtils.hasText(subscription)) {
				parserContext.getReaderContext().error(
						"Listener 'subscription' attribute contains empty value.", ele);
			}
			configDef.getPropertyValues().addPropertyValue("durableSubscriptionName", subscription);
		}

		String selector = null;
		if (ele.hasAttribute(SELECTOR_ATTRIBUTE)) {
			selector = ele.getAttribute(SELECTOR_ATTRIBUTE);
			if (!StringUtils.hasText(selector)) {
				parserContext.getReaderContext().error(
						"Listener 'selector' attribute contains empty value.", ele);
			}
			configDef.getPropertyValues().addPropertyValue("messageSelector", selector);
		}

	}

	protected void parseContainerConfiguration(Element ele, ParserContext parserContext, BeanDefinition configDef) {
		String destinationType = ele.getAttribute(DESTINATION_TYPE_ATTRIBUTE);
		if (StringUtils.hasText(destinationType)) {
			boolean pubSubDomain = false;
			boolean subscriptionDurable = false;
			if (DESTINATION_TYPE_DURABLE_TOPIC.equals(destinationType)) {
				pubSubDomain = true;
				subscriptionDurable = true;
			}
			else if (DESTINATION_TYPE_TOPIC.equals(destinationType)) {
				pubSubDomain = true;
			}
			else if (!DESTINATION_TYPE_QUEUE.equals(destinationType)) {
				parserContext.getReaderContext().error("Invalid listener container 'destination-type': " +
						"only \"queue\", \"topic\" and \"durableTopic\" supported.", ele);
			}
			configDef.getPropertyValues().addPropertyValue("pubSubDomain", Boolean.valueOf(pubSubDomain));
			configDef.getPropertyValues().addPropertyValue("subscriptionDurable", Boolean.valueOf(subscriptionDurable));
		}

		if (ele.hasAttribute(CLIENT_ID_ATTRIBUTE)) {
			String clientId = ele.getAttribute(CLIENT_ID_ATTRIBUTE);
			if (!StringUtils.hasText(clientId)) {
				parserContext.getReaderContext().error(
						"Listener 'client-id' attribute contains empty value.", ele);
			}
			configDef.getPropertyValues().addPropertyValue("clientId", clientId);
		}
	}

	protected Integer parseAcknowledgeMode(Element ele, ParserContext parserContext) {
		String acknowledge = ele.getAttribute(ACKNOWLEDGE_ATTRIBUTE);
		if (StringUtils.hasText(acknowledge)) {
			int acknowledgeMode = Session.AUTO_ACKNOWLEDGE;
			if (ACKNOWLEDGE_TRANSACTED.equals(acknowledge)) {
				acknowledgeMode = Session.SESSION_TRANSACTED;
			}
			else if (ACKNOWLEDGE_DUPS_OK.equals(acknowledge)) {
				acknowledgeMode = Session.DUPS_OK_ACKNOWLEDGE;
			}
			else if (ACKNOWLEDGE_CLIENT.equals(acknowledge)) {
				acknowledgeMode = Session.CLIENT_ACKNOWLEDGE;
			}
			else if (!ACKNOWLEDGE_AUTO.equals(acknowledge)) {
				parserContext.getReaderContext().error("Invalid listener container 'acknowledge' setting: " +
						"only \"auto\", \"client\", \"dups-ok\" and \"transacted\" supported.", ele);
			}
			return new Integer(acknowledgeMode);
		}
		else {
			return null;
		}
	}

}
