package com.sonic.control.services;

import com.sonic.control.models.ResultDetail;

import java.util.List;

public interface ResultDetailService {
    List<ResultDetail> findByResultIdAndType(int resultId, String type);

    void deleteByResultId(int resultId);
}
