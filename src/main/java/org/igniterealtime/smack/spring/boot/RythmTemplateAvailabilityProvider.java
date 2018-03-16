/*
 * Copyright (c) 2010-2020, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.igniterealtime.smack.spring.boot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.autoconfigure.template.PathBasedTemplateAvailabilityProvider;

public class RythmTemplateAvailabilityProvider extends PathBasedTemplateAvailabilityProvider {

	public RythmTemplateAvailabilityProvider() {
		super("org.beetl.core.Configuration", RythmTemplateAvailabilityProperties.class, "spring.rythm");
	}

	static final class RythmTemplateAvailabilityProperties extends TemplateAvailabilityProperties {

		private List<String> templateLoaderPath = new ArrayList<String>(Arrays.asList(RythmProperties.DEFAULT_TEMPLATE_LOADER_PATH));

		RythmTemplateAvailabilityProperties() {
			super(RythmProperties.DEFAULT_PREFIX, RythmProperties.DEFAULT_SUFFIX);
		}

		@Override
		protected List<String> getLoaderPath() {
			return this.templateLoaderPath;
		}

		public List<String> getTemplateLoaderPath() {
			return this.templateLoaderPath;
		}

		public void setTemplateLoaderPath(List<String> templateLoaderPath) {
			this.templateLoaderPath = templateLoaderPath;
		}

	}

}
