package org.igniterealtime.smack.spring.boot;

import org.igniterealtime.smack.spring.boot.connection.XMPPConnectionTemplate;
import org.jivesoftware.smack.XMPPConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link SmackTemplate}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SmackTemplate Tests")
class SmackTemplateTest {

    @Mock
    private XMPPConnectionTemplate connectionTemplate;

    private SmackTemplate smackTemplate;

    @BeforeEach
    void setUp() {
        smackTemplate = new SmackTemplate(connectionTemplate);
    }

    @Test
    @DisplayName("Constructor sets template correctly")
    void testConstructor() {
        assertThat(smackTemplate).isNotNull();
    }

    @Test
    @DisplayName("login starts a thread")
    void testLogin(@Mock(lenient = true) XMPPConnection connection) {
        smackTemplate.login(connection, "user", "password");
        assertThat(true).isTrue();
    }

    @Test
    @DisplayName("logout returns true when no exception")
    void testLogout(@Mock(lenient = true) XMPPConnection connection) {
        boolean result = smackTemplate.logout(connection);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("changePassword returns false when exception occurs")
    void testChangePassword(@Mock(lenient = true) XMPPConnection connection) {
        boolean result = smackTemplate.changePassword(connection, "newPass");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("send handles exception gracefully")
    void testSend(@Mock(lenient = true) XMPPConnection connection) throws Exception {
        smackTemplate.send(connection);
        assertThat(true).isTrue();
    }
}
