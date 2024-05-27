package com.aniwhere.domain.board.faq.mapper;

import com.aniwhere.domain.board.faq.dto.FaqDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FaqMapper {

    List<FaqDTO> selectFaq();
    FaqDTO selectFaqById(Long id);
    void insertFaq(FaqDTO faqDTO);
    void updateFaq();
    void deleteFaq();

}
