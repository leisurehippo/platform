package com.bisaibang.monojwt.domain.vm;

/**
 * Created by arslan on 3/18/2017.
 */
public class BlizzardVM {

    private String id;
    private String battleTag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBattleTag() {
        return battleTag;
    }

    public void setBattleTag(String battleTag) {
        this.battleTag = battleTag;
    }

    @Override
    public String toString() {
        return "BlizzardVM{" +
            "id='" + id + '\'' +
            ", battleTag='" + battleTag + '\'' +
            '}';
    }
}
