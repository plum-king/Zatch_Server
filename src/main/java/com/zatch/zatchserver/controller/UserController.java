package com.zatch.zatchserver.controller;

import com.zatch.zatchserver.domain.User;
import com.zatch.zatchserver.dto.*;
import com.zatch.zatchserver.service.AuthService;
import com.zatch.zatchserver.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/new")
    @ApiOperation(value = "회원가입", notes = "회원가입 API")
    public PostUserResDto postUser(@RequestBody PostUserReqDto postUserReqDto) {
        List<String> adjectives = Arrays.asList("귀여운", "당황한", "어리둥절", "깜찍한", "동글동글", "초롱초롱", "배고픈", "의아한", "놀라운", "어여쁜", "차분한", "한가한", "화려한", "깨끗한",
                "정직한", "활발한", "긍정적인", "낙천적인", "다정한", "단호한", "겸손한", "매력적인", "발랄한", "민첩한", "상냥한", "솔직한", "신중한", "용감한", "수줍은", "소중한");
        List<String>  animals = Arrays.asList("강아지", "거북이", "고래", "고양이", "공작", "기린", "까치", "낙타", "너구리", "늑대", "다람쥐", "부엉이", "사슴", "사자", "새우", "수달", "순록",
                "악어", "여우", "오리", "올빼미", "청설모", "치타", "코끼리", "토끼", "팬더", "펭귄", "표범", "햄스터", "호랑이");
        Collections.shuffle(adjectives);
        Collections.shuffle(animals);

        User newUser = new User(
                postUserReqDto.getName(),
                postUserReqDto.getEmail(),
                adjectives.get(0) + " " + animals.get(0)
        );

        userService.join(newUser);

        return new PostUserResDto(newUser.getName(), newUser.getEmail(), adjectives.get(0) + " " + animals.get(0));
    }

    @GetMapping("/{email}/login")
    @ApiOperation(value = "로그인", notes = "로그인 API")
    public GetUserReqDto getLogin(@PathVariable("email") String email) {
        userService.getUser(email);
        return new GetUserReqDto(email);
    }


    @PatchMapping("/{userId}/nickname")
    @ApiOperation(value = "회원 닉네임 수정", notes = "회원 닉네임 수정 API")
    public PatchUserNicknameResDto patchNickname(@PathVariable("userId") Long userId
            , @RequestBody PatchUserNicknameReqDto pathUserNicknameReqDto) {
        String newNickname = pathUserNicknameReqDto.getNewNickname();
        Long idOfModifiedUser = userId;

        userService.modifyNickname(idOfModifiedUser, newNickname);
        return new PatchUserNicknameResDto(newNickname);
    }

    @GetMapping("/{userId}/profile")
    @ApiOperation(value = "회원 프로필", notes = "회원 프로필 API")
    public GetProfileResDto getProfile(@PathVariable("userId") Long userId) {
        String userNickname = userService.profile(userId);
        return new GetProfileResDto(userNickname);
    }

    @PostMapping("/{userId}/town")
    @ApiOperation(value = "회원 동네", notes = "회원 동네 API")
    public PostUserTownReqDto postTown(@PathVariable("userId") Long userId, @RequestBody PostUserTownReqDto postUserTownReqDTO) {
        String town = postUserTownReqDTO.getTown();
        String userTown = userService.town(userId, town);
        return new PostUserTownReqDto(userTown);
    }
}
