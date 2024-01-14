package com.example.dto.mapper;

import com.example.dto.MessageDto;
import com.example.entity.Message;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    private final ModelMapper modelMapper;

    public MessageMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Message toEntity(MessageDto messageDto) {
        return modelMapper.map(messageDto, Message.class);
    }

    public MessageDto toDto(Message message) {
        return modelMapper.map(message, MessageDto.class);
    }
}
