package com.thxgraduate.post.controller.dto;

import lombok.Getter;

@Getter
public enum CharacterType {
    BACKPACK("책가방 캐릭터"),
    REPORT_CARD("성적표 캐릭터"),
    STUDY("공부하는 캐릭터"),
    CARROT("당근 캐릭터"),
    CHEERLEADER("치어리더 캐릭터");


    private final String description;

    CharacterType(String description) {
        this.description = description;
    }
}
