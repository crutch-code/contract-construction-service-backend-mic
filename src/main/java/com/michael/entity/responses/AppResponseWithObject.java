package com.michael.entity.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public final class AppResponseWithObject extends DefaultAppResponse{

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object source;

    public AppResponseWithObject() {
        source = null;
    }

    public AppResponseWithObject(Object source) {
        this.source = source;
    }

    public AppResponseWithObject(int internal_code, String message, String endpoint, Object source) {
        super(internal_code, message, endpoint);
        this.source = source;
    }


}
