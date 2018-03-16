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

import org.jivesoftware.smack.debugger.SmackDebuggerFactory;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.debugger.slf4j.SLF4JDebuggerFactory;
import org.junit.Test;
import org.jxmpp.stringprep.XmppStringprepException;

public class XMPPTCPConnectionTest {

	@Test
	public void testCon() throws XmppStringprepException {
		
		XMPPTCPConnection connection = newXMPPTCPConnection(XMPPTCPConnectionConfiguration());
		
		
	}
	
	public SmackDebuggerFactory debuggerFactory() {
		return SLF4JDebuggerFactory.INSTANCE; 
	}
	
	public XMPPTCPConnectionConfiguration XMPPTCPConnectionConfiguration() throws XmppStringprepException {
		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
				// 服务器IP地址
				.setHost("10.71.33.167")
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
				.setDebuggerFactory(debuggerFactory())
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
			XMPPTCPConnection connection = new XMPPTCPConnection(config);
			connection.connect();
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
