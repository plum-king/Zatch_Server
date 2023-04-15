package com.zatch.zatchserver.controller;

import com.zatch.zatchserver.DefaultRes;
import com.zatch.zatchserver.ResponseMessage;
import com.zatch.zatchserver.StatusCode;
import com.zatch.zatchserver.dto.PostChatReqDto;
import com.zatch.zatchserver.dto.PostUserTownReqDto;
import com.zatch.zatchserver.service.ChatService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    //(거래 후) 별점, 후기 등록하기
    @PostMapping("/after_deal")
    @ApiOperation(value = "별점&후기 등록", notes = "별점&후기 등록 API")
    public ResponseEntity postStarReview(HttpServletRequest req) {
        try {
            Long send_user_id = Long.valueOf(req.getParameter("send_user_id"));
            Long receive_user_id = Long.valueOf(req.getParameter("receive_user_id"));
            String review_context = req.getParameter("review_context");
            int star_rating = Integer.parseInt(req.getParameter("star_rating"));
            chatService.after_deal(send_user_id, receive_user_id, review_context, star_rating);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.AFTER_CHAT_SUCCESS, new PostChatReqDto(send_user_id, receive_user_id, review_context, star_rating)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR, "Error After Chat(Review&Star)"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
