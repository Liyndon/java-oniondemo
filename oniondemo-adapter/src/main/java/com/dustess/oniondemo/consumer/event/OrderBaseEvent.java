package com.dustess.oniondemo.consumer.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderBaseEvent {
    private Long id;
}
