package com.calma.DocManagerServer.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor // Constructor vacío para Spring
@Builder
public class EmailRequest {
    private String email;

}
