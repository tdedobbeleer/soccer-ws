package com.soccer.ws.model;

import com.soccer.ws.migration.model.NewAccount;
import com.soccer.ws.migration.model.NewBaseClass;
import com.soccer.ws.migration.model.NewMatch;
import com.soccer.ws.migration.model.NewPoll;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.dozer.loader.api.FieldsMappingOptions.copyByReference;
import static org.junit.Assert.assertEquals;

public class DozerMapperTest {
    @Test
    public void test() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(BaseClass.class, NewBaseClass.class,
                        TypeMappingOptions.oneWay()
                )
                        .fields("created", "created",
                                copyByReference()
                        )
                        .fields("modified", "modified",
                                copyByReference()
                        );
                mapping(Account.class, NewAccount.class,
                        TypeMappingOptions.oneWay()
                )
                        .fields("passwordLastSet", "passwordLastSet",
                                copyByReference()
                        );
                mapping(Match.class, NewMatch.class,
                        TypeMappingOptions.oneWay()
                )
                        .fields("date", "date",
                                copyByReference()
                        );
                mapping(Poll.class, NewPoll.class,
                        TypeMappingOptions.oneWay()
                )
                        .fields("startDate", "startDate",
                                copyByReference()
                        )
                        .fields("endDate", "endDate",
                                copyByReference()
                        );
            }
        };

        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(builder);
        Account account = new Account();
        account.setCreated(DateTime.now().minusDays(1));
        account.setPasswordLastSet(DateTime.now().minusDays(1));
        account.setModified(DateTime.now().minusDays(1));

        NewAccount newAccount = new NewAccount();

        mapper.map(account, newAccount);

        assertEquals(account.getPasswordLastSet(), newAccount.getPasswordLastSet());
        assertEquals(account.getModified(), newAccount.getModified());
        assertEquals(account.getCreated(), newAccount.getCreated());
    }
}
