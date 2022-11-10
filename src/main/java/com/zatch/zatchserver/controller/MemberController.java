package com.zatch.zatchserver.controller;

import com.zatch.zatchserver.domain.Member;
import com.zatch.zatchserver.dto.*;
import com.zatch.zatchserver.repository.MemberRepository;
import com.zatch.zatchserver.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/{memberId}")
    @ApiOperation(value = "회원 조회", notes = "회원 id로 회원 조회 API")
    public GetMemberResDto getMember(@PathVariable("memberId") Long memberId) {
        Member findMember = memberService.findOne(memberId);

        return new GetMemberResDto(findMember.getName(), findMember.getNickname(), findMember.getEmail());
    }

    @GetMapping("")
    @ApiOperation(value = "전체 회원 조회", notes = "전체 회원 조회 API")
    public List<GetMemberResDto> getAllMembers() {
        List<Member> findMembers = memberService.findAll();

        List<GetMemberResDto> getMemberResDtos = new ArrayList<>();
        for (Member findMember : findMembers) {
            getMemberResDtos.add(new GetMemberResDto(findMember.getName(), findMember.getNickname(),
                    findMember.getEmail()));
        }

        return getMemberResDtos;
    }

    @PostMapping("/new")
    @ApiOperation(value = "회원가입", notes = "회원가입 API")
    public PostMemberResDto postMember(@RequestBody PostMemberReqDto postMemberReqDto) {
        Member newMember = Member.createMember(
                postMemberReqDto.getName(),
                postMemberReqDto.getNickname(),
                postMemberReqDto.getEmail(),
                postMemberReqDto.getPassword()
        );

        memberService.save(newMember);

        return new PostMemberResDto(newMember.getName(), newMember.getEmail());
    }

    @PatchMapping("/{memberId}/nickname")
    @ApiOperation(value = "회원 닉네임 수정", notes = "회원 닉네임 수정 API")
    public PatchMemberNicknameResDto patchNickname(@PathVariable("memberId") Long memberId,
                                                   @RequestBody PatchMemberNicknameReqDto pathMemberNicknameReqDto) {
        String newNickname = pathMemberNicknameReqDto.getNewNickname();
        Long idOfModifiedMember = memberService.modifyNickname(memberId, newNickname);

        Member modifiedMember = memberService.findOne(idOfModifiedMember);
        return new PatchMemberNicknameResDto(modifiedMember.getName(), modifiedMember.getNickname(), modifiedMember.getEmail());
    }
}
