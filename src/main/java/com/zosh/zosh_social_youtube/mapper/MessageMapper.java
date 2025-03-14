package com.zosh.zosh_social_youtube.mapper;

import com.zosh.zosh_social_youtube.dto.request.MessageRequest;
import com.zosh.zosh_social_youtube.dto.response.MessageResponse;
import com.zosh.zosh_social_youtube.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    //Tạo một thể hiện (instance) của MessageMapper mà không cần tự triển khai.
    //Giúp bạn có thể sử dụng MessageMapper ngay mà không cần inject vào Spring.
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    // Chuyển từ MessageRequest -> Message (Entity)
    @Mapping(target = "id", ignore = true) // ID tự sinh, không cần ánh xạ
    @Mapping(target = "createdAt", ignore = true) // Hibernate tự set
    @Mapping(target = "updatedAt", ignore = true) // Hibernate tự set
    Message toMessage(MessageRequest request);

    // Chuyển từ Message (Entity) -> MessageResponse (DTO)
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "chat.id", target = "chatId")
    MessageResponse toMessageResponse(Message message);
}
