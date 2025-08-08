package com.thxgraduate.post.controller.dto;

import lombok.Getter;

@Getter
public enum CharacterType {
    BUSINESS("정장 캐릭터"),
    CASUAL("일상복 캐릭터"),
    STUDENT("공부하는 캐릭터"),
    ATHLETE("농구선수 캐릭터"),
    LOVELY("졸업 캐릭터"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8");

    private final String description;

    CharacterType(String description) {
        this.description = description;
    }
}
