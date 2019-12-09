package com.szydd.software.service.Interface;

import com.szydd.software.domain.DutiyChange;

import java.util.List;

public interface DutiyChangeService {
    DutiyChange addDutiyChange(DutiyChange dutiyChange);
    void deleteByDutiyChangeId(Long dutiyChangeId);
    public DutiyChange findByDutiyChangeId(Long dutiyChangeId);

    Long findLargestDutiyChangeId();
    public List<DutiyChange> findAllByPages(int page,int row);
    public List<DutiyChange> findAllByPages(String status, int page, int row);
}
