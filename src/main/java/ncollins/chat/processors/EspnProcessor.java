package ncollins.chat.processors;

import ncollins.chat.bots.groupme.EspnGroupMeBot;
import ncollins.espn.EspnMessageBuilder;
import ncollins.model.Order;
import ncollins.model.chat.ProcessResult;
import ncollins.model.chat.ProcessResultType;
import ncollins.model.espn.Outcome;
import ncollins.model.espn.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EspnProcessor {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private EspnMessageBuilder espnMessageBuilder;

    @Autowired
    public EspnProcessor(EspnMessageBuilder espnMessageBuilder){
        this.espnMessageBuilder = espnMessageBuilder;
    }

    //TODO: add in cases for "all-time", "current year", and "a given year"
    public ProcessResult processResponse(String text){
        // {top|bottom} [TOTAL] scores {ever|YEAR|}
        if(text.matches("(top|bottom) \\d* ?scores(\\sever|\\s\\d+|)$")) {
            Order order = text.startsWith("top") ? Order.DESC : Order.ASC;

            String[] textSplit = text.split("scores");
            String totalStr = textSplit[0].replaceAll("\\D+", "");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);

            String yearStr = textSplit.length == 1 ? "" :
                    textSplit[1].replaceAll("\\D+", "");

            // history
            if(text.contains("ever")){
                return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildScoresMessage(order, total, false));
                // specified season
            } else if(!yearStr.isEmpty()){
                Integer seasonId = Integer.parseInt(yearStr);
                return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildScoresMessage(order, total, null, seasonId,false));
                // current season
            } else {
                return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildScoresMessageCurrentYear(order, total,false));
            }
            // {top|bottom} [TOTAL] records {ever|YEAR|}
        } else if(text.matches("(top|bottom) \\d* ?records(\\sever|\\s\\d+|)$")){
            Order order = text.startsWith("top") ? Order.DESC : Order.ASC;

            String[] textSplit = text.split("records");
            String totalStr = textSplit[0].replaceAll("\\D+", "");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);

            String yearStr = textSplit.length == 1 ? "" :
                    textSplit[1].replaceAll("\\D+", "");

            // history
            if(text.contains("ever")){
                return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildRecordsMessage(order, total));
                // specified season
            } else if(!yearStr.isEmpty()){
                Integer seasonId = Integer.parseInt(yearStr);
                return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildRecordsMessage(order, total, seasonId));
                // current season
            } else {
                return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildRecordsMessageCurrentYear(order, total));
            }
            // standings {ever|YEAR|}
        } else if(text.matches("standings(\\sever|\\s\\d+|)$")){
            String yearStr = text.replaceAll("\\D+", "");

            // history
            if(text.contains("ever")){
                return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildStandingsMessage());
                // specified season
            } else if(!yearStr.isEmpty()){
                Integer seasonId = Integer.parseInt(yearStr);
                return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildStandingsMessage(seasonId));
                // current season
            } else {
                return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildStandingsMessageCurrentYear());
            }
            // trades {ever|YEAR|}
        } else if(text.matches("trades(\\s\\d+|)$")){
            String yearStr = text.replaceAll("\\D+", "");

            if(!yearStr.isEmpty()){
                Integer seasonId = Integer.parseInt(yearStr);
                return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildTradesMessage(seasonId));
                // current season
            } else {
                return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildTradesMessageCurrentYear());
            }
            // trade count {ever|YEAR|}
        } else if(text.matches("trade count(\\sever|\\s\\d+|)$")){
            String yearStr = text.replaceAll("\\D+", "");

            // history
            if(text.contains("ever")){
                return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildTradeCountMessage());
                // specified season
            } else if(!yearStr.isEmpty()){
                Integer seasonId = Integer.parseInt(yearStr);
                return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildTradeCountMessage(seasonId));
                // current season
            } else {
                return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildTradeCountMessageCurrentYear());
            }
//        // trade baes {ever|}
//        } else if(text.matches("trade baes(\\sever|)$")){
//            getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildTradeBaesMessage());
//        // playoff standings {ever|}
        } else if(text.matches("playoff standings(\\sever|)$")){
            return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildPlayoffStandingsMessage());
            // {top|bottom} [TOTAL] [POSITION|players] {WEEK|}
        } else if(text.matches("(top|bottom) \\d* ?([a-zA-Z]+|players)$")) {
            Order order = text.startsWith("top") ? Order.DESC : Order.ASC;
            String totalStr = text.replaceAll("\\D+","");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);

            Position position;
            try {
                String positionStr = text.replaceAll("(top|bottom)", "").
                        replaceAll("\\d+", "").trim();
                position = positionStr.equals("players") ? null : Position.valueOf(positionStr.toUpperCase());
            } catch(Exception e){
                return new ProcessResult(ProcessResultType.TEXT, "enter a valid position [qb,rb,wr,te,k,d,flex]");
            }

            return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildPlayersMessageByCurrentWeek(order, total, position));
            // {top|bottom} [TOTAL] {pf|wins|losses} through [WEEK]
//        } else if(text.matches("(top|bottom)(\\s\\d)* (pf|wins|losses) through \\d+$")) {
//            Order order = text.startsWith("top") ? Order.DESC : Order.ASC;
//            Outcome outcome = text.contains(" wins ") ?
//                    Outcome.WIN :
//                    text.contains(" losses ") ? Outcome.LOSS : null;
//
//            String[] textSplit = text.split("through");
//            String totalStr = textSplit[0].replaceAll("\\D+", "");
//            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
//            String weekStr = textSplit[1].replaceAll("\\D+", "");
//            int week = Integer.parseInt(weekStr);
//
//            if(outcome == null){
//                return getEspnBot().getMessageBuilder().buildPointsThroughMessage(order, total, week);
//            } else {
//                return getEspnBot().getMessageBuilder().buildOutcomeThroughMessage(order, outcome, total, week);
//            }

            // top [TOTAL] pf streaks
        } else if(text.matches("top(\\s\\d)* pf streaks$")) {
            String totalStr = text.replaceAll("\\D+", "");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildPointsStreakMessage(total));
            // [TOTAL] {win|loss} streaks
        } else if(text.matches("\\d* ?(win|loss) streaks$")) {
            Outcome outcome = text.contains(" win ") ? Outcome.WIN : Outcome.LOSS;
            String totalStr = text.replaceAll("\\D+", "");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildOutcomeStreakMessage(outcome, total));
            // [TOTAL] blowouts
        } else if(text.matches("(^|\\d+\\s)blowouts$")){
            String totalStr = text.replaceAll("\\D+","");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildBlowoutsMessage(total));
            // [TOTAL] heartbreaks
        } else if(text.matches("(^|\\d+\\s)heartbreaks$")){
            String totalStr = text.replaceAll("\\D+","");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildHeartbreaksMessage(total));
            // matchups [TEAM1] [TEAM2]
        } else if(text.matches("matchups \\S+ \\S+$")) {
            String[] teams = text.split("\\s");
            return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildMatchupsMessage(teams[1], teams[2]));
            // sackos ever
        } else if(text.equals("sackos")){
            return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildSackosMessage());
            // champs ever
        } else if(text.equals("champs")){
            return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildChampsMessage());

            // pf winners
        } else if(text.equals("pf winners")){
            return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildWeeklyPfWinners());
            // jujus
        } else if(text.equals("jujus")) {
            return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildJujusMessage());
            // salties
        } else if(text.equals("salties")) {
            return new ProcessResult(ProcessResultType.TEXT, espnMessageBuilder.buildSaltiesMessage());
        } else return new ProcessResult(ProcessResultType.TEXT, "please enter a valid request.");
    }
}
