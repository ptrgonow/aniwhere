package com.aniwhere.domain.board.notice.service;

import com.aniwhere.domain.board.notice.dto.NoticeDTO;
import com.aniwhere.domain.board.notice.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    private final NoticeMapper noticeMapper;

    public NoticeService(NoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }

    public List<NoticeDTO> getAllNotices() {
        return noticeMapper.getAllNotices();
    }

    public NoticeDTO getNoticeById(Long id) {
        return noticeMapper.getNoticeById(id);
    }

    public void createNotice(NoticeDTO noticeDTO) {
        noticeMapper.insertNotice(noticeDTO);
    }

    public void updateNotice(NoticeDTO noticeDTO) {
        noticeMapper.updateNotice(noticeDTO);
    }
}
