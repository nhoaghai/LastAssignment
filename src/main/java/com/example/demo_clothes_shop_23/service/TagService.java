package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.Tag;
import com.example.demo_clothes_shop_23.exception.ResourceNotFoundException;
import com.example.demo_clothes_shop_23.repository.TagRepository;
import com.example.demo_clothes_shop_23.request.UpsertTagRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag getTagById(Integer id) {
        return tagRepository.findById(id).orElse(null);
    }

    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    public List<Tag> getAllByIds (List<Integer> ids) {
        return tagRepository.findAllById(ids);
    }

    public Tag createTag(UpsertTagRequest upsertTagRequest) {
        Tag tag = Tag.builder()
            .name(upsertTagRequest.getName())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        return tagRepository.save(tag);
    }

    public Tag updateTag(UpsertTagRequest upsertTagRequest, Integer tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(
            () -> new ResourceNotFoundException("Không tìm thấy tag này")
        );
        tag.setName(upsertTagRequest.getName());
        tag.setUpdatedAt(LocalDateTime.now());
        return tagRepository.save(tag);
    }

    public Tag deleteTag(Integer tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(
            () -> new ResourceNotFoundException("Không tìm thấy tag này")
        );
        tagRepository.delete(tag);
        return tag;
    }
}
