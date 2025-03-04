package com.zosh.zosh_social_youtube.entity;

import com.zosh.zosh_social_youtube.enums.EnumRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "roles")
public class Role {

    @Id
    String name;

    String description;

    @ManyToMany
    Set<Permission> permissions;

    @ManyToMany(mappedBy = "roles") // Liên kết ngược về User để tránh tạo bảng trung gian thừa
    Set<User> users = new HashSet<>();

}

