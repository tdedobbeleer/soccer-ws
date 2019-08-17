package com.soccer.ws.selection.conditions;

import com.google.common.collect.Maps;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.BaseClass;
import com.soccer.ws.model.Doodle;
import com.soccer.ws.model.Presence;
import edu.emory.mathcs.backport.java.util.Collections;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FifoCondition extends AbstractCondition {

    public FifoCondition(float weight) {
        super(weight);
    }

    @Override
    public Mono<Map<Account, Float>> calculate(Doodle doodle) {
        return Mono.fromSupplier(() -> {
            Map<Account, Float> res = Maps.newConcurrentMap();
            //First order by date (old -> new)
            List<Presence> ordered = doodle.getPresences().stream().filter(Presence::isPresent).sorted(Comparator.comparing(BaseClass::getModified)).collect(Collectors.toList());
            //Reverse the list: oldest should get higher points
            Collections.reverse(ordered);
            for (int i = 0; i < ordered.size(); i++) {
                Presence p = ordered.get(i);
                res.put(p.getAccount(), ((float) (i + 1) / ordered.size()) * weight);
            }
            return res;
        });
    }
}
