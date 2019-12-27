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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.igniterealtime.smack.spring.boot.connection.XMPPConnectionTemplate;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smackx.admin.ServiceAdministrationManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.iqprivate.PrivateDataManager;
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

/**
 * TODO
 * @author 		： <a href="https://github.com/hiwepy">hiwepy</a>
 */

public class SmackTemplate {

	private final XMPPConnectionTemplate template;
	
	public SmackTemplate(XMPPConnectionTemplate template) {
		this.template = template;
	}
	
	/**
	 * 创建一个新用户
	 *
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param attr
	 *            一些用户资料；attr对应的一些属性值
	 *            name；email；first；last；city；state；zip；phone；url；date；misc；text；remove（可以查看AccountManager中的getAccountAttributes()函数注释）
	 * @throws InterruptedException 
	 * @throws XMPPException 
	 * @throws IOException 
	 * @throws SmackException 
	 * @see AccountManager
	 */
	public boolean registerAccount(String username, String password, Map<String, String> attr) throws SmackException, IOException, XMPPException, InterruptedException {
		
		// 获取连接
		XMPPConnection connection = template.getXMPPTCPConnection(username, password);
		
		ServiceAdministrationManager.getInstanceFor(connection);
		PrivateDataManager.getInstanceFor(connection);
		
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
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 */
	public void login(XMPPConnection connection, final String userName, final String password) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//connection.login(userName, password);
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
	 * @code true 退出成功 @code false 退出失败
	 */
	public boolean logout(XMPPConnection connection) {
		try {
			//connection.instantShutdown();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 修改密码
	 *
	 * @param newPassword
	 *            新密码
	 * @return code
	 * @code true 修改成功 @code false 修改失败
	 */
	public boolean changePassword(XMPPConnection connection, String newPassword) {
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
	public Roster getContact(XMPPConnection connection) {
		Roster roster = Roster.getInstanceFor(connection);
		// 获得所有的联系人组
		Collection<RosterGroup> groups = roster.getGroups();
		for (RosterGroup group : groups) {
			// 获得每个组下面的好友
			List<RosterEntry> entries = group.getEntries();
			for (RosterEntry entry : entries) {
				// 获得好友基本信息
				BareJid jid = entry.getJid();
				jid.toString();
				entry.getName();
				entry.getType();
			}
		}
		return roster;
	}

	/**
	 * 一上线获取离线消息 设置登录状态为在线
	 */
	private void getOfflineMessage(XMPPConnection connection) {
		OfflineMessageManager offlineManager = new OfflineMessageManager(connection);
		try {
			List<Message> list = offlineManager.getMessages();
			// 删除离线消息
			offlineManager.deleteMessages();
			// 将状态设置成在线
			Presence presence = new Presence(Presence.Type.available);
			connection.sendStanza(presence);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(XMPPConnection connection) throws IOException, InterruptedException {
		try {
			ChatManager manager = ChatManager.getInstanceFor(connection);
			EntityBareJid jid = JidCreate.entityBareFrom("azhon@10.104.179.23");
			Chat chat = manager.chatWith(jid);
			chat.send("HelloWord");
		} catch (SmackException.NotConnectedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化聊天消息监听
	 */
	public void initListener(XMPPConnection connection, IncomingChatMessageListener listener, OutgoingChatMessageListener outListener) {
		ChatManager manager = ChatManager.getInstanceFor(connection);
		// 设置信息的监听
		manager.addIncomingListener(listener);
		manager.addOutgoingListener(outListener);
		/*
		 * final ChatMessageListener messageListener = new ChatMessageListener() {
		 * 
		 * @Override public void processMessage(Chat chat, Message message) {
		 * //当消息返回为空的时候，表示用户正在聊天窗口编辑信息并未发出消息 if (!TextUtils.isEmpty(message.getBody()))
		 * { //message为用户所收到的消息 } } }; ChatManagerListener chatManagerListener = new
		 * ChatManagerListener() {
		 * 
		 * @Override public void chatCreated(Chat chat, boolean arg1) {
		 * chat.addMessageListener(messageListener); } };
		 * manager.addChatListener(chatManagerListener);
		 */
	}

	/**
	 * 加入一个群聊聊天室
	 *
	 * @param jid
	 *            聊天室ip 格式为>>群组名称@conference.ip
	 * @param nickName
	 *            用户在聊天室中的昵称
	 * @param password
	 *            聊天室密码 没有密码则传""
	 * @return
	 */
	public MultiUserChat join(XMPPConnection connection, String jid, String nickName, String password) {
		try {
			// 使用XMPPConnection创建一个MultiUserChat窗口
			MultiUserChat muc = MultiUserChatManager.getInstanceFor(connection)
					.getMultiUserChat(JidCreate.entityBareFrom(jid));
			// 聊天室服务将会决定要接受的历史记录数量
			/*
			 * MucEnterConfiguration history = new MucEnterConfiguration(null);
			 * history.setMaxChars(0);
			 */
			// 用户加入聊天室
			muc.join(Resourcepart.from(nickName), password);

			return muc;
		} catch (XMPPException | SmackException e) {
			e.printStackTrace();
			if ("XMPPError: not-authorized - auth".equals(e.getMessage())) {
				// 需要密码加入
			}

		} catch (XmppStringprepException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取服务器上的所有群组
	 */
	private List<HostedRoom> getHostedRoom(XMPPConnection connection) {
		MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
		try {
			// serviceNames->conference.106.14.20.176
			List<DomainBareJid> serviceNames = manager.getMucServiceDomains();
			for (int i = 0; i < serviceNames.size(); i++) {
				// manager.getJoinedRooms(user);
				// manager.getMultiUserChat(jid);

				return manager.getHostedRooms(serviceNames.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param jid
	 *            格式为>>群组名称@conference.ip
	 * @throws XmppStringprepException
	 */
	private void initListener(XMPPConnection connection, String jid) throws XmppStringprepException {
		MultiUserChat multiUserChat = MultiUserChatManager.getInstanceFor(connection)
				.getMultiUserChat(JidCreate.entityBareFrom(jid));
		multiUserChat.addMessageListener(new MessageListener() {
			@Override
			public void processMessage(final Message message) {
				// 当消息返回为空的时候，表示用户正在聊天窗口编辑信息并未发出消息
				if (!TextUtils.isEmpty(message.getBody())) {
					// 收到的消息
				}
			}
		});

		/*
		 * MultiUserChat multiUserChat =
		 * MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(jid);
		 * multiUserChat.sendMessage("Hello World");
		 */

	}

	/**
	 * 创建群聊聊天室
	 *
	 * @param roomName
	 *            聊天室名字
	 * @param nickName
	 *            创建者在聊天室中的昵称
	 * @param password
	 *            聊天室密码
	 * @return
	 * @throws XmppStringprepException
	 * @throws InterruptedException
	 */
	public MultiUserChat createChatRoom(XMPPConnection connection, String roomName, String nickName, String password)
			throws XmppStringprepException, InterruptedException {
		MultiUserChat muc;
		try {

			EntityBareJid jid = JidCreate
					.entityBareFromUnescaped(roomName + "@conference." + connection.getXMPPServiceDomain().toString());
			// 创建一个MultiUserChat
			muc = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(jid);

			// 创建聊天室
			MucCreateConfigFormHandle isCreated = muc.createOrJoin(Resourcepart.from(roomName));
			try {
				isCreated.makeInstant();

				// 获得聊天室的配置表单
				Form form = muc.getConfigurationForm();
				// 根据原始表单创建一个要提交的新表单。
				Form submitForm = form.createAnswerForm();
				// 向要提交的表单添加默认答复
				List<FormField> fields = form.getFields();
				for (int i = 0; fields != null && i < fields.size(); i++) {
					if (FormField.Type.hidden != fields.get(i).getType() && fields.get(i).getVariable() != null) {
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
				// Toast.makeText(this, "创建成功", Toast.LENGTH_SHORT).show();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				// Toast.makeText(this, "创建失败", Toast.LENGTH_SHORT).show();

			}

		} catch (XMPPException | SmackException e) {
			e.printStackTrace();
			// Toast.makeText(this, "创建失败" + e.getMessage(), Toast.LENGTH_LONG).show();
			return null;
		}
		return muc;
	}

	/**
	 * 添加好友请求信息监听
	 * 
	 * @throws InterruptedException
	 * @throws XmppStringprepException
	 * @throws NotConnectedException
	 * @throws XMPPErrorException
	 * @throws NoResponseException
	 * @throws NotLoggedInException
	 */
	public void addFriendListener(XMPPConnection connection) throws NotLoggedInException, NoResponseException, XMPPErrorException,
			NotConnectedException, XmppStringprepException, InterruptedException {

		/**
		 * 添加好友
		 *
		 * @param user帐号
		 * @param nickName
		 *            昵称
		 * @param groupName
		 *            组名
		 */
		Roster.getInstanceFor(connection).createEntry(JidCreate.bareFrom("azhon@10.104.179.23"), "", null);

		// 条件过滤
		StanzaFilter filter = new AndFilter();
		StanzaListener listener = new StanzaListener() {

			@Override
			public void processStanza(Stanza packet)
					throws NotConnectedException, InterruptedException, NotLoggedInException {
				DiscoverInfo p = (DiscoverInfo) packet;
				// p中可以得到对方的信息
				if (p.getType().toString().equals("subscrib")) {
					// 好友申请
				} else if (p.getType().toString().equals("subscribed")) {
					// 通过了好友请求
				} else if (p.getType().toString().equals("unsubscribe")) {
					// 拒绝好友请求
				}

			}
		};
		connection.addAsyncStanzaListener(listener, filter);
	}
	
}
