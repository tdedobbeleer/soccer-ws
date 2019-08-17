package com.soccer.ws.selection.conditions;

import com.soccer.common.DataFactory;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Doodle;
import com.soccer.ws.model.Presence;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class FifoConditionTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void calculate() {
        List<Presence> presences = DataFactory.getPresences(10);
        Doodle doodle = new Doodle();
        doodle.setPresences(presences.stream().peek(p -> {
            p.setModified(DateTime.now());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).collect(Collectors.toSet()));
        FifoCondition condition = new FifoCondition(0.2f);

        Map<Account, Float> res = condition.calculate(doodle).block();

        float i = (float) 1.0;
        for (Presence p : presences) {
            float toTest = Objects.requireNonNull(res).get(p.getAccount());
            assertEquals((i * 0.2f), toTest, 0.001);
            i = i - (float) 0.1;
        }
    }
}
