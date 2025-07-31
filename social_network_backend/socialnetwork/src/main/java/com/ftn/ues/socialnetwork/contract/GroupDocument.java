package com.ftn.ues.socialnetwork.contract;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDocument {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private Long adminId;
}