/**
 * 
 */
package com.softisland.bean.utils;

import com.softisland.common.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 邮件发送工具类
 * 
 * @author qinxiaofei
 * 
 *
 */
public class EmailUtils
{
	private static final Logger log = LoggerFactory.getLogger(EmailUtils.class);

	// 发送邮件的超时时间
	private static final int SEND_MAIL_TIMEOUT = 30000;

	// 多收件地址邮件的分隔符
	private static final String MAIL_ADDRESS_SPLIT = ";";

	// 启用鉴权属性
	private static final String KEY_AUTH_PROP = "mail.smtp.auth";

	// 设置超时时间属性
	private static final String KEY_TIMETOUT_PROP = "mail.smtp.timeout";
	
	// 启动SSL属性
	private static final String KEY_SSL_ENABLE_PROP = "mail.smtp.starttls.enable";
	
	private static final String KEY_SSL_REQUIRED_PROP = "mail.smtp.starttls.required";

	// 邮件发送服务器
	@Value("${email.host}")
	private String emailHost;

	// 发送者的鉴权用户名
	@Value("${email.port}")
	private String emailPort;

	// 发送者的鉴权密码
	@Value("${email.sender.password}")
	private String password;

	// 发送者邮件地址
	@Value("${email.sender.address}")
	private String senderMail;

	// 接受者邮件地址
	@Value("${email.reciver.address}")
	private String reciverMail;

	@Value("${email.sys.name}")
	private String emailSysName;

	/**
	 * 邮件发送器
	 */
	@Autowired
	private JavaMailSenderImpl mailSender;

	/**
	 * 按照指定的标题和内容发送邮件 如果有多个邮件接受者，请以;隔开
	 * 
	 * @param title	 邮件标题
	 * @param content 邮件内容
	 * @throws Exception，当出现异常时，抛出
	 * 
	 */
	public void sendEmail(String title, String content) throws Exception
	{
		if (StringUtils.isEmpty(title) || StringUtils.isEmpty(content))
		{
			log.error("parameter title or content is empty.");
			throw new Exception("parameter invalid");
		}

		// 构建纯文本的邮件消息
		SimpleMailMessage mailMessage = new SimpleMailMessage ();
		mailMessage.setFrom(senderMail);
		mailMessage.setTo(reciverMail.split(MAIL_ADDRESS_SPLIT));
		mailMessage.setSubject(title);
		mailMessage.setText(content);

		// 设置属性，比如启动鉴权和启动SSL
		Properties properties = new Properties();
		properties.put(KEY_AUTH_PROP, true);
		properties.put(KEY_TIMETOUT_PROP, SEND_MAIL_TIMEOUT);
        properties.put(KEY_SSL_ENABLE_PROP, true);
        properties.put(KEY_SSL_REQUIRED_PROP, true);


		mailSender.setHost(emailHost);
		if (StringUtils.isNotEmpty(emailPort))
		{
			mailSender.setPort(Integer.parseInt(emailPort));
		}
		mailSender.setUsername(senderMail);
		mailSender.setPassword(password);
		mailSender.setJavaMailProperties(properties);

		try 
		{
			mailSender.send(mailMessage);
			log.debug("send email success, sender is {}, reciver is {}, title is {}.", senderMail, reciverMail, title);
		}
		catch (MailException e) 
		{
			log.error("send email failed, sender is {}, reciver is {}.", senderMail, reciverMail);
			log.error("send email catch faield.", e);
			throw new Exception("send email exception.", e);
		}
	}

	public void sendErrorEmail(String title, String content) {
		int times = 0;
		while (times++ < 50){
			try
			{
				sendEmail("【"+emailSysName+"】"+title, content);
				return;
			}
			catch (Exception e)
			{
				log.error("send email exce",e);
				Utils.threadSleep(TimeUnit.MILLISECONDS,800);
			}
		}
	}
}
