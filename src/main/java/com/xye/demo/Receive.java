
package com.xye.demo;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Receive {

	// ��Ϣ��������
	private final static String QUEUE_NAME = "helloword";

	public static void main(String[] args) throws Exception {
		// �����Ӻʹ���Ƶ�����뷢�Ͷ�һ��
		ConnectionFactory factory = new ConnectionFactory();
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
		// �������У���ҪΪ�˷�ֹ��Ϣ�����������д˳��򣬶��л�������ʱ�������С�
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// ��������������
		QueueingConsumer consumer = new QueueingConsumer(channel);
		// ָ�����Ѷ���
		channel.basicConsume(QUEUE_NAME, true, consumer);
		while (true) {
			// nextDelivery��һ�������������ڲ�ʵ����ʵ���������е�take������
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println("Received Message��'" + message + "'");
		}
	}

}
