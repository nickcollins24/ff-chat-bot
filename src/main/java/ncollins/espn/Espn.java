package ncollins.espn;

import ncollins.model.espn.League;

public class Espn {
    private League league;

    public Espn() {
        this.league = new EspnDataLoader().loadLeague();
    }

    public League getLeague(){
        return this.league;
    }


//# gets the provided weeks matchups during provided year
//    def get_scoreboard(week, year)

//# get most/least PointsFor through the provided week, in league history
//    def get_pf_through(total, week, descending)

//# show longest regular season streak where team scores >= a specified pf total
//    def get_streaks_pf(total, pf)

//# show biggest blowouts of all time
//    def get_blowouts(total)

//# show closest of all time
//    def get_heartbreaks(total)

//# show longest win streaks during a single season
//    def get_streaks_wins(total)

//# show longest loss streaks during a single season
//    def get_streaks_losses(total)

//# gets all-time matchup stats for two provided teams
//    def get_matchup(t0, t1)

//# get all-time standings
//    def get_standings_ever()

//# gets scores this year in order of points
//    def get_scores(total, descending)

//# a juju describes a week in which a fantasy football team:
//#   1) has a bottom-five score for the week
//#   2) is below the average score for that week
//#   3) wins
//    def get_jujus()

//# a salty describes a week in which a fantasy football team:
//#   1) has a top-five score for the week
//#   2) is above the average score for the week
//#   3) loses
//    def get_salties()

//# get all matchups ever
//    def get_all_matchups_ever()

//# get top scores of all time
//    def get_all_scores_ever(total, descending)

//# gets top/bottom scoring rostered players ever
//    def get_players_ever(total, position, descending)

//# gets top/bottom scoring players in the provided week of the provided year
//    def get_players_week(total, position, week, year, descending)

//# converts positionId to String
//    def get_position(position_id)

//# gets name of the team with the provided team id
//    def get_team(team_id)

//# get boxscore data in json form for the provided week and year
//    def get_boxscore_data(week, year, team_id)

//# get scoreboard data in json form for the provided week and year
//    def get_scoreboard_data(week, year)

//# get standings data in json form for the provided year
//    def get_standings_data(year)

//# get team data in json form for the provided year
//    def get_team_data(year)

//# gets the most recent transaction with transactionLogItemtypeId equal to tran_id
//# tranType = 1: Moved
//# tranType = 2: Added
//# tranType = 3: Dropped
//# tranType = 4: Traded
//# tranType = 5: Drafted
//# tranType = 11: Trade Accepted?
//    def get_latest_trade_time()

//# get the current week
//    def get_current_week()

}
