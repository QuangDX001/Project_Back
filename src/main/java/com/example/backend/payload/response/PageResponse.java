package com.example.backend.payload.response;

import com.example.backend.payload.dto.PagingDTO;
import lombok.Data;

import java.util.List;

@Data
public class PageResponse {
    private List<PagingDTO> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
