package com.zosh.zosh_social_youtube.repository;

import com.zosh.zosh_social_youtube.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    public List<Message> findByChatId(String sender);

}
