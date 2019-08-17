package com.soccer.ws.selection.conditions;

import com.soccer.ws.model.Account;
import com.soccer.ws.model.Doodle;
import reactor.core.publisher.Mono;

import java.util.Map;

public abstract class AbstractCondition implements Condition {
    protected final float weight;

    protected AbstractCondition(float weight) {
        this.weight = weight;
    }

    @Override
    public abstract Mono<Map<Account, Float>> calculate(Doodle doodle);
}
