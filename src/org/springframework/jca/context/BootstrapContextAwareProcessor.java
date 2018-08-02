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

package org.springframework.jca.context;

import javax.resource.spi.BootstrapContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * {@link org.springframework.beans.factory.config.BeanPostProcessor}
 * implementation that passes the BootstrapContext to beans that implement
 * the {@link BootstrapContextAware} interface.
 *
 * <p>JCA application contexts will automatically register this with their
 * underlying bean factory. Applications do not use this directly.
 *
 * @author Juergen Hoeller
 * @since 2.1
 * @see BootstrapContextAware
 */
public class BootstrapContextAwareProcessor implements BeanPostProcessor {

	private final BootstrapContext bootstrapContext;


	/**
	 * Create a new BootstrapContextAwareProcessor for the given context.
	 */
	public BootstrapContextAwareProcessor(BootstrapContext bootstrapContext) {
		this.bootstrapContext = bootstrapContext;
	}


	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (this.bootstrapContext != null && bean instanceof BootstrapContextAware) {
			((BootstrapContextAware) bean).setBootstrapContext(this.bootstrapContext);
		}
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
