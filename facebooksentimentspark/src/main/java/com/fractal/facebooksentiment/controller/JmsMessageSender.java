package com.fractal.facebooksentiment.controller;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class JmsMessageSender {

	private JmsTemplate jmsTemplate;

	private static JmsMessageSender jmsMessageSender = null;

	public static JmsMessageSender get() {
		if (jmsMessageSender == null) {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					"applicationContext.xml");
			jmsMessageSender = (JmsMessageSender) applicationContext
					.getBean("jmsMessageSender");
		}
		return jmsMessageSender;
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	/**
	 * send text to default destination
	 * 
	 * @param text
	 */
	public static void send(final String text) {

		get().jmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message = session.createTextMessage(text);
				// set ReplyTo header of Message, pretty much like the concept
				// of email.
				// message.setJMSReplyTo(new ActiveMQQueue("Recv2Send"));
				return message;
			}
		});
	}

	/**
	 * Simplify the send by using convertAndSend
	 * 
	 * @param text
	 */
	public void sendText(final String text) {
		this.jmsTemplate.convertAndSend(text);
	}

	/**
	 * Send text message to a specified destination
	 * 
	 * @param text
	 */
	public void send(final Destination dest, final String text) {

		this.jmsTemplate.send(dest, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message = session.createTextMessage(text);
				return message;
			}
		});
	}
}
