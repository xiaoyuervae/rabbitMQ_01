
package com.xye.demo2;

import java.util.Date;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendLog {
	// ת����
	private final static String EXCHANGE_NAME = "ex_log";

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		// �������Ӻ�Ƶ��
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.41.71");
		// ָ���û� ����
		factory.setUsername("xiaoyuervae");
		factory.setPassword("standby123@");
		// ָ���˿�
		factory.setPort(AMQP.PROTOCOL.PORT);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		/**
		 * ����ת���������� ���õ�ת��������Direct Topic Headers Fanout
		 * �ο���http://melin.iteye.com/blog/691265 Direct Exchange �C
		 * ����·�ɼ�����Ҫ��һ�����а󶨵��������ϣ�Ҫ�����Ϣ��һ���ض���·�ɼ���ȫƥ�䡣 Fanout Exchange �C
		 * ������·�ɼ�����ֻ��Ҫ�򵥵Ľ����а󶨵��������ϡ�һ�����͵�����������Ϣ���ᱻת������ý������󶨵����ж����ϡ�
		 * ���������㲥��ÿ̨�����ڵ������������һ�ݸ��Ƶ���Ϣ��Fanout������ת����Ϣ�����ġ� Topic Exchange �C
		 * ��·�ɼ���ĳģʽ����ƥ�䡣��ʱ������Ҫ��Ҫһ��ģʽ�ϡ����š�#��ƥ��һ�������ʣ����š�*��ƥ�䲻�಻��һ���ʡ�
		 * ��ˡ�audit.#���ܹ�ƥ�䵽��audit.irs.corporate�������ǡ�audit.*�� ֻ��ƥ�䵽��audit.irs����
		 */
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

		String message = new Date().toLocaleString() + " : log something";
		// ָ����Ϣ���͵���ת����
		channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());

		System.out.println(" [x] Sent '" + message + "'");

		channel.close();
		connection.close();
	}

}