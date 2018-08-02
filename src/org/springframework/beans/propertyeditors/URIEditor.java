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

package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

/**
 * Editor for <code>java.net.URI</code>, to directly populate a URI property
 * instead of using a String property as bridge.
 *
 * <p>Supports Spring-style URI notation: any fully qualified standard URI
 * ("file:", "http:", etc) and Spring's special "classpath:" pseudo-URL,
 * which will be resolved to a corresponding URI.
 *
 * <p>Note: A URI is more relaxed than a URL in that it does not require
 * a valid protocol to be specified. Any scheme within a valid URI syntax
 * is allowed, even without a matching protocol handler being registered.
 *
 * @author Juergen Hoeller
 * @since 2.0.2
 * @see java.net.URI
 * @see URLEditor
 */
public class URIEditor extends PropertyEditorSupport {

	private final ClassLoader classLoader;


	/**
	 * Create a new URIEditor,
	 * using the default ClassLoader for "classpath:" resources.
	 */
	public URIEditor() {
		this.classLoader = ClassUtils.getDefaultClassLoader();
	}

	/**
	 * Create a new URIEditor,
	 * using the given ClassLoader for "classpath:" resources.
	 * @param classLoader the ClassLoader to use
	 */
	public URIEditor(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}


	public void setAsText(String text) throws IllegalArgumentException {
		if (text == null) {
			setValue(null);
		}
		else if (text.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
			ClassPathResource resource =
					new ClassPathResource(text.substring(ResourceUtils.CLASSPATH_URL_PREFIX.length()), this.classLoader);
			try {
				setValue(new URI(resource.getURL().toString()));
			}
			catch (IOException ex) {
				throw new IllegalArgumentException("Could not retrieve URI for " + resource + ": " + ex.getMessage());
			}
			catch (URISyntaxException ex) {
				throw new IllegalArgumentException("Invalid URI syntax: " + ex);
			}
		}
		else {
			try {
				setValue(new URI(text));
			}
			catch (URISyntaxException ex) {
				throw new IllegalArgumentException("Invalid URI syntax: " + ex);
			}
		}
	}

	public String getAsText() {
		URI value = (URI) getValue();
		return (value != null ? value.toString() : "");
	}

}
