package com.michael.entity.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "message")
@NoArgsConstructor
public class Message extends BaseEntity{

    @NotNull
    @Column(name = "message_timestamp", nullable = false)
    @JsonProperty("message_timestamp")
    @Schema(name = "message_timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime messageTimestamp;

    @NotNull
    @Column(name = "message_text", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    @JsonProperty("message_text")
    @Schema(name = "message_text")
    private String messageText;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_sender", nullable = false)
    @JsonProperty("message_sender")
    @Schema(name = "message_sender")
    private User messageSender;

    @NotNull
    @Column(name = "message_status", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    @JsonProperty("message_status")
    @Schema(name = "message_status")
    private String messageStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_chat", nullable = false)
    @JsonProperty("message_chat")
    @Schema(name = "message_chat")
    private Chat messageChat;

    public Message(
            String id, LocalDateTime messageTimestamp, String messageText,
            User messageSender, Chat messageChat, String messageStatus
    ) {
        super(id);
        this.messageTimestamp = messageTimestamp;
        this.messageText = messageText;
        this.messageSender = messageSender;
        this.messageChat = messageChat;
        this.messageStatus = messageStatus;
    }
}