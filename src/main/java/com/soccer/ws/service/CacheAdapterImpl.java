/**
 * Copyright (c) 2013 KU Leuven. All Rights Reserved.
 * <p>
 * This software is the confidential and proprietary information of KU Leuven.
 * ("Confidential Information"). It may not be copied or reproduced in any
 * manner without the express written permission of KU Leuven.
 */
package com.soccer.ws.service;

import com.soccer.ws.dto.AccountStatisticDTO;
import com.soccer.ws.dto.MatchDTO;
import com.soccer.ws.model.Account;
import com.soccer.ws.persistence.AccountDao;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.integration.CacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class CacheAdapterImpl implements CacheAdapter {

    private static final Logger log = LoggerFactory.getLogger(CacheAdapterImpl.class);

    private static final String ACTIVE_ACCOUNTS = "activeAccounts";

    private final ConcurrentDataService dataService;

    private final AccountDao accountDao;

    private Cache<UUID, Account> accountCache =
            new Cache2kBuilder<UUID, Account>() {
            }
                    .loader(new CacheLoader<UUID, Account>() {
                        @Override
                        public Account load(UUID l) throws Exception {
                            return accountDao.findById(l).orElse(null);
                        }
                    })
                    .permitNullValues(false)
                    .expireAfterWrite(1, TimeUnit.DAYS)
                    .build();

    private Cache<String, List<Account>> activeAccountCache =
            new Cache2kBuilder<String, List<Account>>() {
            }
                    .loader(new CacheLoader<String, List<Account>>() {
                        @Override
                        public List<Account> load(String s) throws Exception {
                            return accountDao.findAllByActive(true);
                        }
                    })
                    .refreshAhead(true)
                    .expireAfterWrite(1, TimeUnit.DAYS)
                    .build();

    private Cache<Parameters, List<AccountStatisticDTO>> statisticsCache =
            new Cache2kBuilder<Parameters, List<AccountStatisticDTO>>() {
            }
                    .loader(new CacheLoader<Parameters, List<AccountStatisticDTO>>() {
                        @Override
                        public List<AccountStatisticDTO> load(Parameters p) throws Exception {
                            return dataService.getAccountStatisticsForSeason(p.seasonId, p.isLoggedIn);
                        }
                    })
                    .expireAfterWrite(1, TimeUnit.DAYS)
                    .build();

    private Cache<Parameters, List<MatchDTO>> matchesCache =
            new Cache2kBuilder<Parameters, List<MatchDTO>>() {
            }
                    .loader(new CacheLoader<Parameters, List<MatchDTO>>() {

                        @Override
                        public List<MatchDTO> load(Parameters s) throws Exception {
                            return dataService.getMatchForSeason(s.seasonId, s.isLoggedIn);
                        }
                    })
                    .expireAfterWrite(1, TimeUnit.DAYS)
                    .build();

    @Autowired
    public CacheAdapterImpl(ConcurrentDataService dataService, AccountDao accountDao) {
        this.dataService = dataService;
        this.accountDao = accountDao;
    }

    @Override
    public Account getAccount(UUID l) {
        return accountCache.get(l);
    }

    @Override
    public List<Account> getActiveAccounts() {
        return activeAccountCache.get(ACTIVE_ACCOUNTS);
    }

    @Override
    public List<MatchDTO> getMatchesForSeason(final UUID seasonId, final boolean isLoggedIn) {
        log.debug("Getting matchActionWrappers");
        return matchesCache.get(new Parameters(seasonId, isLoggedIn));
    }

    @Override
    public List<AccountStatisticDTO> getStatisticsForSeason(final UUID seasonId, final boolean isLoggedIn) {
        log.debug("Getting account statistics");
        return statisticsCache.get(new Parameters(seasonId, isLoggedIn));
    }

    @Override
    public void resetStatisticsCache() {
        log.debug("resetCache - statistics");
        statisticsCache.removeAll();
    }

    @Override
    public void resetMatchesCache() {
        log.debug("resetCache - matches");
        matchesCache.removeAll();
    }

    @Override
    public void resetAccountsCache() {
        log.debug("resetCache - accounts");
        accountCache.removeAll();
    }

    @Override
    public void reset() {
        resetMatchesCache();
        resetStatisticsCache();
        resetAccountsCache();
    }

    private class Parameters {
        private final UUID seasonId;
        private final boolean isLoggedIn;

        Parameters(UUID seasonId, boolean isLoggedIn) {
            this.seasonId = seasonId;
            this.isLoggedIn = isLoggedIn;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Parameters that = (Parameters) o;

            if (seasonId != that.seasonId) return false;
            return isLoggedIn == that.isLoggedIn;
        }

        @Override
        public int hashCode() {
            int result = seasonId.hashCode();
            result = 31 * result + (isLoggedIn ? 1 : 0);
            return result;
        }
    }
}
