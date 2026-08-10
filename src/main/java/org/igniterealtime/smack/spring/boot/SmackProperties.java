/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Smack XMPP template engine.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = SmackProperties.SMACK_PREFIX)
public class SmackProperties {

	public static final String SMACK_PREFIX = "smack";

	public static final String DEFAULT_TEMPLATE_LOADER_PATH = "classpath:/templates/";

	public static final String DEFAULT_PREFIX = "";

	public static final String DEFAULT_SUFFIX = ".httl";

	/**
	 * Template prefix.
	 */
	private String prefix = DEFAULT_PREFIX;

	/**
	 * Template suffix.
	 */
	private String suffix = DEFAULT_SUFFIX;

	/**
	 * Well-known Beetl keys which will be passed to Beetl's  Configuration.
	 */
	private Properties settings = new Properties();

	/**
	 * Comma-separated list of template paths.
	 */
	private String[] templateLoaderPath = new String[] { DEFAULT_TEMPLATE_LOADER_PATH };

	/**
	 * Prefer file system access for template loading. File system access enables
	 * hot detection of template changes.
	 */
	private boolean preferFileSystemAccess = true;

	/**
	 * 是否自动检查文件是否变动
	 */
	private boolean autoCheck = false;

	public SmackProperties() {
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Properties getSettings() {
		return this.settings;
	}

	public void setSettings(Properties settings) {
		this.settings = settings;
	}

	public String[] getTemplateLoaderPath() {
		return this.templateLoaderPath;
	}

	public boolean isPreferFileSystemAccess() {
		return this.preferFileSystemAccess;
	}

	public void setPreferFileSystemAccess(boolean preferFileSystemAccess) {
		this.preferFileSystemAccess = preferFileSystemAccess;
	}

	public void setTemplateLoaderPath(String... templateLoaderPaths) {
		this.templateLoaderPath = templateLoaderPaths;
	}

	public boolean isAutoCheck() {
		return autoCheck;
	}

	public void setAutoCheck(boolean autoCheck) {
		this.autoCheck = autoCheck;
	}

}
