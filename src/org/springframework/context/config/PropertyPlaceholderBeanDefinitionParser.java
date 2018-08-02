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

package org.springframework.context.config;

import org.w3c.dom.Element;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;

/**
 * Parser for the &lt;context:property-placeholder/&gt; element.
 *
 * @author Juergen Hoeller
 * @since 2.1
 */
class PropertyPlaceholderBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	protected Class getBeanClass(Element element) {
		return PropertyPlaceholderConfigurer.class;
	}

	protected boolean shouldGenerateId() {
		return true;
	}

	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		String location = element.getAttribute("location");
		String[] locations = StringUtils.commaDelimitedListToStringArray(location);
		builder.addPropertyValue("locations", locations);
		builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
	}

}
