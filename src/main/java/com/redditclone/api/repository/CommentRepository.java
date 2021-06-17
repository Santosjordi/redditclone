package com.redditclone.api.repository;

import com.redditclone.api.model.Comment;
import com.redditclone.api.model.Post;
import com.redditclone.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
