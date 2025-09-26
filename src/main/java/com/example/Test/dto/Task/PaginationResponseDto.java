package com.example.Test.dto.Task;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginationResponseDto<T> {
    private int statusCode;
    private DataWrapper<T> data;

    @Data
    @Builder
    public static class DataWrapper<T> {
        private List<T> items;
        private Meta meta;
    }

    @Data
    @Builder
    public static class Meta {
        private int limit;
        private int offset;
        private long total;
        private Integer totalPages;
    }
}
