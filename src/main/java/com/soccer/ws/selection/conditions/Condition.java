package com.soccer.ws.selection.conditions;

import com.soccer.ws.model.Account;
import com.soccer.ws.model.Doodle;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface Condition {
    Mono<Map<Account, Float>> calculate(Doodle doodle);
}
