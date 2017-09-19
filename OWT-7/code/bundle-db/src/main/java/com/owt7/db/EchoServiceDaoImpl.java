package com.owt7.db;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import com.owt7.api.EchoServiceDao;
import com.owt7.api.dto.MessageDTO;
import com.owt7.model.MessageChat;

@Singleton
@Transactional

@OsgiServiceProvider(classes = { EchoServiceDao.class })
public class EchoServiceDaoImpl implements EchoServiceDao {
	@PersistenceContext(unitName = "owt7PU")
	private EntityManager em;

	@Override
	public Integer create(MessageDTO messageDTO) {

		MessageChat messageChat = new MessageChat();
		messageChat.setDate(new Date());
		messageChat.setName("USER");
		messageChat.setMessage(messageDTO.getMessage());

		try {
			em.persist(messageChat);

		} catch (Exception e) {
			System.err.println("ERROR: " + e.getMessage());
			e.printStackTrace();
		}

		return messageChat.getId();
	}

	@Override
	public MessageDTO get(Integer id) {
		MessageChat messageChat = em.find(MessageChat.class, id);
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setMessage(messageChat.getMessage());
		return messageDTO;
	}

	@Override
	public void update(Integer id) {
		try {
			MessageChat messageChat = em.find(MessageChat.class, id);

			messageChat.setMessage(messageChat.getMessage() +" - Received at: "
			+(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(messageChat.getDate())));
			
			messageChat.setResponded(true);
			em.persist(messageChat);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
