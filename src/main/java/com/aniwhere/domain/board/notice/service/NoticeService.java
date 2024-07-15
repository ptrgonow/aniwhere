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
        noticeMapper.incrementHit(id); // 조회수 증가
        return noticeMapper.getNoticeById(id);
    }


    public void createNotice(NoticeDTO noticeDTO) {
        noticeMapper.insertNotice(noticeDTO);
    }

    public void updateNotice(NoticeDTO noticeDTO) {
        noticeMapper.updateNotice(noticeDTO);
    }

    // 공지사항 삭제
    public void deleteNotice(int id) {
        noticeMapper.deleteNotice(id);
    }

    // 검색

    public List<NoticeDTO> searchNotices(String query) {
        return noticeMapper.searchNotices(query);
    }


}