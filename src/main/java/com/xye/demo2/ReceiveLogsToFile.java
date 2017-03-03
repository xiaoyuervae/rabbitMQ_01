
package com.xye.demo2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class ReceiveLogsToFile {

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
		/**
		 * ����ת���������� ���õ�ת��������Direct Topic Headers Fanout Direct Exchange �C
		 * ����·�ɼ�����Ҫ��һ�����а󶨵��������ϣ�Ҫ�����Ϣ��һ���ض���·�ɼ���ȫƥ�䡣 Fanout Exchange �C
		 * ������·�ɼ�����ֻ��Ҫ�򵥵Ľ����а󶨵��������ϡ�һ�����͵�����������Ϣ���ᱻת������ý������󶨵����ж����ϡ�
		 * ���������㲥��ÿ̨�����ڵ������������һ�ݸ��Ƶ���Ϣ��Fanout������ת����Ϣ�����ġ� Topic Exchange �C
		 * ��·�ɼ���ĳģʽ����ƥ�䡣��ʱ������Ҫ��Ҫһ��ģʽ�ϡ����š�#��ƥ��һ�������ʣ����š�*��ƥ�䲻�಻��һ���ʡ�
		 * ��ˡ�audit.#���ܹ�ƥ�䵽��audit.irs.corporate�������ǡ�audit.*�� ֻ��ƥ�䵽��audit.irs����
		 */
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

			print2File(message);
		}
	}

	private static void print2File(String msg) {
		try {
			String dir = ReceiveLogsToFile.class.getClassLoader().getResource("").getPath();
			String logFileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			File file = new File(dir, logFileName + ".txt");
			FileOutputStream fos = new FileOutputStream(file, true);
			fos.write((msg + "\r\n").getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}