
package com.xye.demo1;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Task {

	// ��������
	private final static String QUEUE_NAME = "workqueue-durable";

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
		boolean durable = true; // ������Ϣ�־û�
								// RabbitMQ������ʹ�ò�ͬ�Ĳ������¶���һ�����У������Ѿ����ڵĶ��У������޷��޸������ԡ�
		// ��������
		channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

		// ����10����Ϣ����������Ϣ���渽��1-10����
		for (int i = 5; i > 0; i--) {
			String dots = "";
			for (int j = 0; j <= i; j++) {
				dots += ".";
			}
			String message = "helloworld" + dots + dots.length();
			// MessageProperties.PERSISTENT_TEXT_PLAIN ��ʶ���ǵ���ϢΪ�־û���
			channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			System.out.println("Sent Message��'" + message + "'");
		}
		// �ر�Ƶ������Դ
		channel.close();
		connection.close();
	}

}