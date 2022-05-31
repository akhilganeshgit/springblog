package com.dev.springblog.service;

import static java.util.stream.Collectors.toList;
import java.time.Instant;
import java.util.List;
import com.dev.springblog.model.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.dev.springblog.dto.PostDto;
import com.dev.springblog.exception.PostNotFoundException;
import com.dev.springblog.model.Post;
import com.dev.springblog.repository.PostRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class PostService {

    private final AuthService authService;
    private final PostRepository postRepository;

    public void createPost(PostDto postDto) {
        Post post = mapFromDtoToPost(postDto);
        postRepository.save(post);
    }

    public List<PostDto> showAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::mapFromPostToDto).collect(toList());
    }

    public PostDto readSinglePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));
        return mapFromPostToDto(post);
    }

    private Post mapFromDtoToPost(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        User loggedInUser = authService.getCurrentUser();
        post.setCreatedOn(Instant.now());
        post.setUsername(loggedInUser.getUsername());
        post.setUpdatedOn(Instant.now());
        return post;
    }

    private PostDto mapFromPostToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setUsername(post.getUsername());
        return postDto;
    }
}
