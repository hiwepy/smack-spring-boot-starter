package org.igniterealtime.smack.spring.boot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link SmackProperties}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("SmackProperties Tests")
class SmackPropertiesTest {

    @Test
    @DisplayName("Default values are set correctly")
    void testDefaultValues() {
        SmackProperties props = new SmackProperties();
        assertThat(props.getPrefix()).isEqualTo("");
        assertThat(props.getSuffix()).isEqualTo(".httl");
        assertThat(props.getTemplateLoaderPath()).containsExactly("classpath:/templates/");
        assertThat(props.isPreferFileSystemAccess()).isTrue();
        assertThat(props.isAutoCheck()).isFalse();
        assertThat(props.getSettings()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Prefix and suffix can be set")
    void testPrefixAndSuffix() {
        SmackProperties props = new SmackProperties();
        props.setPrefix("/templates/");
        props.setSuffix(".html");
        assertThat(props.getPrefix()).isEqualTo("/templates/");
        assertThat(props.getSuffix()).isEqualTo(".html");
    }

    @Test
    @DisplayName("Settings can be set")
    void testSettings() {
        SmackProperties props = new SmackProperties();
        Properties settings = new Properties();
        settings.setProperty("key", "value");
        props.setSettings(settings);
        assertThat(props.getSettings()).containsEntry("key", "value");
    }

    @Test
    @DisplayName("Template loader path can be set")
    void testTemplateLoaderPath() {
        SmackProperties props = new SmackProperties();
        props.setTemplateLoaderPath("/path1/", "/path2/");
        assertThat(props.getTemplateLoaderPath()).containsExactly("/path1/", "/path2/");
    }

    @Test
    @DisplayName("PreferFileSystemAccess can be set")
    void testPreferFileSystemAccess() {
        SmackProperties props = new SmackProperties();
        props.setPreferFileSystemAccess(false);
        assertThat(props.isPreferFileSystemAccess()).isFalse();
    }

    @Test
    @DisplayName("AutoCheck can be set")
    void testAutoCheck() {
        SmackProperties props = new SmackProperties();
        props.setAutoCheck(true);
        assertThat(props.isAutoCheck()).isTrue();
    }

    @Test
    @DisplayName("Constants have expected values")
    void testConstants() {
        assertThat(SmackProperties.SMACK_PREFIX).isEqualTo("smack");
        assertThat(SmackProperties.DEFAULT_TEMPLATE_LOADER_PATH).isEqualTo("classpath:/templates/");
        assertThat(SmackProperties.DEFAULT_PREFIX).isEqualTo("");
        assertThat(SmackProperties.DEFAULT_SUFFIX).isEqualTo(".httl");
    }
}
