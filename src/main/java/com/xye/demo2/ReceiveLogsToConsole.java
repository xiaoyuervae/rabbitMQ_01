
package com.xye.demo2;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class ReceiveLogsToConsole {
	private final static String EXCHANGE_NAME = "ex_log";

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

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		// ����һ���ǳ־õġ�Ψһ�����Զ�ɾ���Ķ����Ҷ��������ɷ������������һ��������������amq.gen-JzTY20BRgKO-HjmUJj0wLg
		// ���ơ�
		String queueName = channel.queueDeclare().getQueue();
		// Ϊת����ָ�����У�����binding
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		// ָ�������ߣ��ڶ�������Ϊ�Զ�Ӧ�������ֶ�Ӧ��
		channel.basicConsume(queueName, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(" [x] Received '" + message + "'");

		}
	}

}
