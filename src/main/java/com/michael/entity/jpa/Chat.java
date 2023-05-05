package com.michael.entity.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.michael.entity.jsonviews.JsonViewsCollector;
import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Introspected
@NoArgsConstructor
@JsonView(JsonViewsCollector.Entity.Default.class)
@Table(name = "chat")
public class Chat extends BaseEntity{

    @NotNull
    @Column(name = "chat_creation_date", nullable = false)
    @JsonProperty("chat_creation_date")
    @Schema(name = "chat_creation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate chatCreationDate;

    @Column(name = "chat_last_activity")
    @JsonProperty("chat_last_activity")
    @Schema(name = "chat_last_activity")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime chatLastActivity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_left_user", nullable = false)
    @JsonProperty("chat_left_user")
    @Schema(name = "chat_left_user")
    private User chatLeftUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_right_user", nullable = false)
    @JsonProperty("chat_right_user")
    @Schema(name = "chat_right_user")
    private User chatRightUser;

    @NotNull
    @Column(name = "chat_name", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    @JsonProperty("chat_name")
    @Schema(name = "chat_name")
    private String chatName;

    public Chat(
            String id, LocalDate chatCreationDate, LocalDateTime chatLastActivity,
            User chatLeftUser, User chatRightUser, String chatName
    ) {
        super(id);
        this.chatCreationDate = chatCreationDate;
        this.chatLastActivity = chatLastActivity;
        this.chatLeftUser = chatLeftUser;
        this.chatRightUser = chatRightUser;
        this.chatName = chatName;
    }
}