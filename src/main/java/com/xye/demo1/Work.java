
package com.xye.demo1;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Work {
	// ��������
	private final static String QUEUE_NAME = "workqueue-durable";

	public static void main(String[] args) throws Exception {
		// ���ֲ�ͬ�������̵����
		int hashCode = Work.class.hashCode();
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

		QueueingConsumer consumer = new QueueingConsumer(channel);

		/**
		 * ack= true: Round-robin ת�� �����߱�ɱ������Ϣ�ᶪʧ ack=false:��ϢӦ��
		 * ��Ϊ�˱�֤��Ϣ��Զ���ᶪʧ��RabbitMQ֧����ϢӦ��message acknowledgments����
		 * �����߷���Ӧ���RabbitMQ����������Ϣ�Ѿ������պʹ���Ȼ��RabbitMQ�������ɵĽ�����Ϣɾ����
		 * ��������߱�ɱ����û�з���Ӧ��RabbitMQ����Ϊ����Ϣû�б���ȫ�Ĵ���Ȼ�󽫻�����ת������������ߡ�
		 * ͨ�����ַ�ʽ�������ȷ����Ϣ���ᱻ��ʧ����ʹ����ż����ɱ���� ��������Ҫ�ķ��ر��ر𳤵�ʱ��������ġ�
		 * 
		 */

		boolean ack = false; // ��Ӧ�����
		// ָ�����Ѷ���
		channel.basicConsume(QUEUE_NAME, ack, consumer);

		// ��ƽת�� ����������ת����Ϣ���� ֻ���������߿��е�ʱ��ᷢ����һ����Ϣ��
		int prefetchCount = 1;
		channel.basicQos(prefetchCount);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());

			System.out.println(hashCode + " Received Message��'" + message + "'");
			doWork(message);
			System.out.println(hashCode + " Received Done");
			// ����Ӧ��
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

		}
	}

	/**
	 * ÿ�����ʱ1s
	 * 
	 * @param task
	 * @throws InterruptedException
	 */
	private static void doWork(String task) throws InterruptedException {
		for (char ch : task.toCharArray()) {
			if (ch == '.')
				Thread.sleep(1000);
		}
	}

}