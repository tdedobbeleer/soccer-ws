package com.soccer.ws.selection;

import com.soccer.ws.model.Doodle;
import com.soccer.ws.selection.conditions.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelectionService {
    private final List<Condition> conditions;

    @Autowired
    public SelectionService(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public void generate(Doodle doodle) {

    }
}
