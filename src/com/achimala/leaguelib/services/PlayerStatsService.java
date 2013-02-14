/*
 *  This file is part of LeagueLib.
 *  LeagueLib is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  LeagueLib is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with LeagueLib.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.achimala.leaguelib.services;

import com.achimala.leaguelib.connection.*;
import com.achimala.leaguelib.models.*;
import com.achimala.leaguelib.errors.*;
import com.achimala.util.Callback;
import com.gvaneyck.rtmp.TypedObject;

public class PlayerStatsService extends LeagueAbstractService {
    private final String SUMMONERS_RIFT = "CLASSIC";
    
    public PlayerStatsService(LeagueConnection connection) {
        super(connection);
    }
    
    public String getServiceName() {
        return "playerStatsService";
    }
    
    public void fillRankedStats(LeagueSummoner summoner) throws LeagueException {
        TypedObject obj = call("getAggregatedStats", new Object[] { summoner.getAccountId(), SUMMONERS_RIFT, LeagueCompetitiveSeason.CURRENT.toString() });
        summoner.setRankedStats(new LeagueSummonerRankedStats(obj.getTO("body")));
    }
    
    public void fillRankedStats(final LeagueSummoner summoner, final Callback<LeagueSummoner> callback) {
        callAsynchronously("getAggregatedStats", new Object[] { summoner.getAccountId(), SUMMONERS_RIFT, LeagueCompetitiveSeason.CURRENT.toString() }, new Callback<TypedObject>() {
            public void onCompletion(TypedObject obj) {
                try {
                    summoner.setRankedStats(new LeagueSummonerRankedStats(obj.getTO("body")));
                    callback.onCompletion(summoner);
                } catch(Exception ex) {
                    callback.onError(ex);
                }
            }
            
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }
    
    // getRecentGames -> match history
}