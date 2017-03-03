
package com.xye.demo;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {
	// ��Ϣ��������
	private final static String QUEUE_NAME = "helloword";

	public static void main(String[] args) throws Exception {
		/**
		 * �����������ӵ�MabbitMQ
		 */
		ConnectionFactory factory = new ConnectionFactory();
		// ����MabbitMQ��������ip����������
		factory.setHost("192.168.41.71");
		// ָ���û� ����
		factory.setUsername("xiaoyuervae");
		factory.setPassword("standby123@");
		// ָ���˿�
		factory.setPort(AMQP.PROTOCOL.PORT);
		// ����һ������
		Connection connection = factory.newConnection();
		// ����һ��Ƶ��
		Channel channel = connection.createChannel();
		// ָ��һ������
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		// ���͵���Ϣ
		String message = "hello world!";
		// �������з���һ����Ϣ
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println("Sent Message��'" + message + "'");
		// �ر�Ƶ��������
		channel.close();
		connection.close();
	}

}