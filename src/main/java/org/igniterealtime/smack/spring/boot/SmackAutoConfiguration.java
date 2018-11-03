package org.igniterealtime.smack.spring.boot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.debugger.SmackDebuggerFactory;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.debugger.slf4j.SLF4JDebuggerFactory;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChat.MucCreateConfigFormHandle;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ XMPPTCPConnectionConfiguration.class })
@EnableConfigurationProperties(SmackProperties.class)
public class SmackAutoConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(SmackAutoConfiguration.class);

	@Bean
	@ConditionalOnMissingBean
	public SmackDebuggerFactory debuggerFactory() {
		return SLF4JDebuggerFactory.INSTANCE;
	}

	@Bean
	@ConditionalOnMissingBean
	public XMPPTCPConnectionConfiguration XMPPTCPConnectionConfiguration(SmackDebuggerFactory debuggerFactory)
			throws XmppStringprepException {
		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
				// 服务器IP地址
				.setHost("119.29.193.12")
				// 服务器端口
				.setPort(5222)
				// 服务器名称(管理界面的 主机名)
				.setXmppDomain("izqhrnmkjn55syz")
				// 是否开启安全模式
				.setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)
				.setUsernameAndPassword("user", "password")

				// 设置登录状态
				.setSendPresence(false)

				// 是否开启压缩
				.setCompressionEnabled(false)
				// 开启调试模式
				.setDebuggerFactory(debuggerFactory).build();

		return config;
	}

	

	
}
