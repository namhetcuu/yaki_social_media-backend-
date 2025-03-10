package com.zosh.zosh_social_youtube.repository;

import com.zosh.zosh_social_youtube.entity.Comment;
import com.zosh.zosh_social_youtube.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {

    public List<Story> findByUserId(String userId);

}
