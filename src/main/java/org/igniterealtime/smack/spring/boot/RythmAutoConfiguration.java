package org.igniterealtime.smack.spring.boot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
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
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ XMPPTCPConnectionConfiguration.class })
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(RythmProperties.class)
public class RythmAutoConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(RythmAutoConfiguration.class);

	private XMPPTCPConnection connection;

	@Bean
	@ConditionalOnMissingBean
	public SmackDebuggerFactory debuggerFactory() {
		return SLF4JDebuggerFactory.INSTANCE; 
	}
	
	@Bean
	@ConditionalOnMissingBean
	public XMPPTCPConnectionConfiguration XMPPTCPConnectionConfiguration(SmackDebuggerFactory debuggerFactory) throws XmppStringprepException {
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
				
				//设置登录状态
                .setSendPresence(false)
                
				// 是否开启压缩
				.setCompressionEnabled(false)
				// 开启调试模式
				.setDebuggerFactory(debuggerFactory)
				.build();
		
		return config;
	}

	 /**
     * 获得与服务器的连接
     *
     * @return
     */
	public XMPPTCPConnection newXMPPTCPConnection(XMPPTCPConnectionConfiguration config) {
		try {
			if (connection == null) {
				connection = new XMPPTCPConnection(config);
				connection.connect();
			}
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;

	}
	
	/**
     * 创建一个新用户
     *
     * @param username 用户名
     * @param password 密码
     * @param attr     一些用户资料；attr对应的一些属性值 name；email；first；last；city；state；zip；phone；url；date；misc；text；remove（可以查看AccountManager中的getAccountAttributes()函数注释）
     * @see AccountManager 
     */
    public boolean registerAccount(String username, String password, Map<String, String> attr) {
        AccountManager manager = AccountManager.getInstance(connection);
        try {
            if (attr == null) {
            	manager.createAccount(Localpart.from(username), password);
            } else {
                manager.createAccount(Localpart.from(username), password, attr);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 密码
     */
    public void login(final String userName, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connection.login(userName, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
    /**
     * 退出登录
     *
     * @return code
     * @code true 退出成功
     * @code false 退出失败
     */
    public boolean logout() {
        try {
            connection.instantShutdown();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 修改密码
     *
     * @param newPassword 　新密码
     * @return code
     * @code true 修改成功
     * @code false 修改失败
     */
    public boolean changePassword(String newPassword) {
        try {
            AccountManager manager = AccountManager.getInstance(connection);
            manager.changePassword(newPassword);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 获得所有联系人
     */
    public Roster getContact() {
        Roster roster = Roster.getInstanceFor(connection);
        //获得所有的联系人组
        Collection<RosterGroup> groups = roster.getGroups();
        for (RosterGroup group : groups) {
            //获得每个组下面的好友
            List<RosterEntry> entries = group.getEntries();
            for (RosterEntry entry : entries) {
                //获得好友基本信息
                entry.getUser();
                entry.getName();
                entry.getType();
                entry.getStatus();
            }
        }
        return roster;
    }
    
    /**
     * 一上线获取离线消息
     * 设置登录状态为在线
     */
    private void getOfflineMessage() {
        OfflineMessageManager offlineManager = new OfflineMessageManager(connection);
        try {
            List<Message> list = offlineManager.getMessages();
            //删除离线消息
            offlineManager.deleteMessages();
            //将状态设置成在线
            Presence presence = new Presence(Presence.Type.available);
            connection.sendStanza(presence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void send() {
    	try {
            ChatManager manager =  ChatManager.getInstanceFor(connection);
            EntityBareJid jid = JidCreate.entityBareFrom("azhon@10.104.179.23");
            Chat chat =   manager.chatWith(jid);
            chat.send("HelloWord");
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
	}
    
    /**
     * 初始化聊天消息监听
     */
    public void initListener(IncomingChatMessageListener listener,
    		OutgoingChatMessageListener outListener) {
        ChatManager manager = ChatManager.getInstanceFor(connection);
        //设置信息的监听
        manager.addIncomingListener(listener);
        manager.addOutgoingListener(outListener);
       /*
        final ChatMessageListener messageListener = new ChatMessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                //当消息返回为空的时候，表示用户正在聊天窗口编辑信息并未发出消息
                if (!TextUtils.isEmpty(message.getBody())) {
                    //message为用户所收到的消息
                }
            }
        };
        ChatManagerListener chatManagerListener = new ChatManagerListener() {

            @Override
            public void chatCreated(Chat chat, boolean arg1) {
                chat.addMessageListener(messageListener);
            }
        };
        manager.addChatListener(chatManagerListener);*/
    }
    
    /**
     * 加入一个群聊聊天室
     *
     * @param jid 聊天室ip 格式为>>群组名称@conference.ip
     * @param nickName 用户在聊天室中的昵称
     * @param password 聊天室密码 没有密码则传""
     * @return
     */
    public MultiUserChat join(String jid, String nickName, String password) {
        try {
            // 使用XMPPConnection创建一个MultiUserChat窗口
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(jid);
            // 聊天室服务将会决定要接受的历史记录数量
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxChars(0);
            // 用户加入聊天室
            muc.join(nickName, password);
            return muc;
        } catch (XMPPException | SmackException e) {
            e.printStackTrace();
            if ("XMPPError: not-authorized - auth".equals(e.getMessage())) {
                //需要密码加入
            }
            return null;
        }
    }
    
    /**
     * 获取服务器上的所有群组
     */
    private List<HostedRoom> getHostedRoom() {
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
        try {
            //serviceNames->conference.106.14.20.176
            List<DomainBareJid> serviceNames = manager.getXMPPServiceDomains();
            for (int i = 0; i < serviceNames.size(); i++) {
            	//manager.getJoinedRooms(user);
            	//manager.getMultiUserChat(jid);
            	
                return manager.getHostedRooms(serviceNames.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * @param jid 格式为>>群组名称@conference.ip
     */
    private void initListener(String jid) {
        MultiUserChat multiUserChat = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(jid);
        multiUserChat.addMessageListener(new MessageListener() {
            @Override
            public void processMessage(final Message message) {
                //当消息返回为空的时候，表示用户正在聊天窗口编辑信息并未发出消息
                if (!TextUtils.isEmpty(message.getBody())) {
                    //收到的消息
                }
            }
        });
        
       /* MultiUserChat multiUserChat = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(jid);
        multiUserChat.sendMessage("Hello World");*/
        
    }
    
    /**
     * 创建群聊聊天室
     *
     * @param roomName 聊天室名字
     * @param nickName 创建者在聊天室中的昵称
     * @param password 聊天室密码
     * @return
     */
    public MultiUserChat createChatRoom(String roomName, String nickName, String password) {
        MultiUserChat muc;
        try {
        	
        	EntityBareJid jid = JidCreate.entityBareFromUnescaped(roomName + "@conference." + connection.getXMPPServiceDomain().toString());
            // 创建一个MultiUserChat
            muc = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(jid);
            // 创建聊天室
            boolean isCreated = muc.createOrJoin(Resourcepart.from(roomName));
            if (isCreated) {
                // 获得聊天室的配置表单
                Form form = muc.getConfigurationForm();
                // 根据原始表单创建一个要提交的新表单。
                Form submitForm = form.createAnswerForm();
                // 向要提交的表单添加默认答复
                List<FormField> fields = form.getFields();
                for (int i = 0; fields != null && i < fields.size(); i++) {
                    if (FormField.Type.hidden != fields.get(i).getType() &&
                            fields.get(i).getVariable() != null) {
                        // 设置默认值作为答复
                        submitForm.setDefaultAnswer(fields.get(i).getVariable());
                    }
                }
                // 设置聊天室的新拥有者
                List owners = new ArrayList();
                owners.add(connection.getUser());// 用户JID
                submitForm.setAnswer("muc#roomconfig_roomowners", owners);
                // 设置聊天室是持久聊天室，即将要被保存下来
                submitForm.setAnswer("muc#roomconfig_persistentroom", true);
                // 房间仅对成员开放
                submitForm.setAnswer("muc#roomconfig_membersonly", false);
                // 允许占有者邀请其他人
                submitForm.setAnswer("muc#roomconfig_allowinvites", true);
                if (password != null && password.length() != 0) {
                    // 进入是否需要密码
                    submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
                    // 设置进入密码
                    submitForm.setAnswer("muc#roomconfig_roomsecret", password);
                }
                // 能够发现占有者真实 JID 的角色
                // submitForm.setAnswer("muc#roomconfig_whois", "anyone");
                // 登录房间对话
                submitForm.setAnswer("muc#roomconfig_enablelogging", true);
                // 仅允许注册的昵称登录
                submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
                // 允许使用者修改昵称
                submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
                // 允许用户注册房间
                submitForm.setAnswer("x-muc#roomconfig_registration", false);
                // 发送已完成的表单（有默认值）到服务器来配置聊天室
                muc.sendConfigurationForm(submitForm);
                Toast.makeText(this, "创建成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "创建失败", Toast.LENGTH_SHORT).show();
            }
        } catch (XMPPException | SmackException e) {
            e.printStackTrace();
            Toast.makeText(this, "创建失败" + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
        return muc;
    }
    
    
    /**
     * 添加好友请求信息监听
     */
    public void addFriendListener() {
    	
    	 /**
         * 添加好友
         *
         * @param user帐号
         * @param nickName  昵称
         * @param groupName 组名
         */
        Roster.getInstanceFor(connection).createEntry("azhon@10.104.179.23", "", null);
        
        //条件过滤
        StanzaFilter filter = new AndFilter();
        StanzaListener listener = new StanzaListener() {
            @Override
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                DiscoverInfo p = (DiscoverInfo) packet;
                //p中可以得到对方的信息
                if (p.getType().toString().equals("subscrib")) {
                    //好友申请
                } else if (p.getType().toString().equals("subscribed")) {
                    //通过了好友请求
                } else if (p.getType().toString().equals("unsubscribe")) {
                    //拒绝好友请求
                }
            }
        };
        connection.addAsyncStanzaListener(listener, filter);
    }
}
