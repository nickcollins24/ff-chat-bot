package ncollins.schedulers;

import ncollins.chat.groupme.EspnGroupMeBot;
import ncollins.chat.groupme.GroupMeBot;
import ncollins.espn.Espn;
import ncollins.model.chat.Emojis;
import ncollins.model.chat.PollPayload;
import ncollins.model.espn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled that checks for ESPN Transactions every 60 seconds
 */
@Component
@ConditionalOnProperty(value = "TRANSACTION_SCHEDULER_ENABLED",
                       havingValue = "true")
public class EspnTransactionScheduler implements Scheduler {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private EspnGroupMeBot bot;
    private Espn espn;
    private static final String TRADE_POLL_TITLE = "Do you approve of this trade?" + Emojis.FINGER_UP +
            "Majority veto nullifies this agreed upon transaction.";

    public EspnTransactionScheduler(EspnGroupMeBot bot, Espn espn){
        this.bot = bot;
        this.espn = espn;
        start();
    }

    public void start(){
        // schedule task every 30 seconds
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> checkForTransaction(), 0, 60, TimeUnit.SECONDS);

        logger.info("EspnTransactionScheduler enabled.");
    }

    private void checkForTransaction(){
        // get transactions over the last 30 seconds (30000 ms)
        List<Transaction> transactions = espn.getTransactions(System.currentTimeMillis()-60000, System.currentTimeMillis(),
                List.of(TransactionType.TRADE_ACCEPTED.getValue()));

        if(transactions == null){
            return;
        }

        for(Transaction t : transactions){
//            if(isTrade(t)){
                processTrade(t);
//            } else if(isWaiverAdd(t)){
//                processWaiverAdd(t);
//            }
        }
    }

//    private void processWaiverAdd(Transaction t){
//        Player playerAdded = null;
//        Player playerDropped = null;
//        for(int i=0; i < t.getMessages().length; i++){
//            Transaction.Message m = t.getMessages()[i];
//            if(m.getMessageTypeId().equals(TransactionType.ADD_WAIVER.getValue())){
//                playerAdded = espn.getPlayer(m.getTargetId());
//            } else if(m.getMessageTypeId().equals(TransactionType.DROP_WAIVER.getValue())){
//                playerDropped = espn.getPlayer(m.getTargetId());
//            }
//        }
//
//        StringBuilder sb = new StringBuilder();
//        if(playerAdded != null){
//            sb.append("Added: " + playerAdded.getFullName() + " (" + espn.getPositionById(playerAdded.getDefaultPositionId()) + ")\\n");
//        }
//        if(playerDropped != null){
//            sb.append("Dropped: " + playerDropped.getFullName() + " (" + espn.getPositionById(playerDropped.getDefaultPositionId()) + ")");
//        }
//
//        bot.sendMessage(sb.toString());
//    }

    private void processTrade(Transaction t){
        Map<Integer, List<Player>> teamPlayersMap = new HashMap();

        for(int i=0; i < t.getMessages().length; i++){
            Integer toTeamId = t.getMessages()[i].getTo();
            Integer playerId = t.getMessages()[i].getTargetId();

            if(toTeamId > 0){
                if(teamPlayersMap.containsKey(toTeamId)){
                    teamPlayersMap.get(toTeamId).add(espn.getPlayer(playerId));
                } else {
                    List<Player> players = new ArrayList();
                    players.add(espn.getPlayer(playerId));
                    teamPlayersMap.put(toTeamId, players);
                }
            }
        }

        List<Integer> teamIds = new ArrayList(teamPlayersMap.keySet());
        Team team0 = espn.getTeamById(teamIds.get(0));
        Team team1 = espn.getTeamById(teamIds.get(1));
        List<Player> playersToTeam0 = teamPlayersMap.get(teamIds.get(0));
        List<Player> playersToTeam1 = teamPlayersMap.get(teamIds.get(1));

        sendTradePoll(team0, team1, playersToTeam0, playersToTeam1);
    }

//    private Boolean isTrade(Transaction t){
//        return t != null
//                && t.getMessages().length > 0
//                && t.getMessages()[0].getMessageTypeId().equals(TransactionType.TRADE_ACCEPTED.getValue());
//    }
//
//    private Boolean isWaiverAdd(Transaction t){
//        return t != null
//                && t.getMessages().length > 0
//                && (t.getMessages()[0].getMessageTypeId().equals(TransactionType.ADD_WAIVER.getValue())
//                    || t.getMessages()[0].getMessageTypeId().equals(TransactionType.DROP_WAIVER.getValue()));
//    }

    private void sendTradePoll(Team team0, Team team1, List<Player> playersToTeam0, List<Player> playersToTeam1){
        StringBuilder sb0 = new StringBuilder();
        for(Player p : playersToTeam0){
            sb0.append(p.getFullName() + "\\n");
        }

        StringBuilder sb1 = new StringBuilder();
        for(Player p : playersToTeam1){
            sb1.append(p.getFullName() + "\\n");
        }

        //send breaking news message
        bot.sendMessage(Emojis.RED_SIREN + " BREAKING NEWS " + Emojis.RED_SIREN + " \\n\\n" +
                team0.getLocation() + " " + team0.getNickname() + " (" + team0.getAbbrev() + ") and " +
                team1.getLocation() + " " + team1.getNickname() + " (" + team1.getAbbrev() + ") " +
                "have agreed on a trade, multiple sauces tell " + bot.getBotName() + ". Here are the details...\\n\\n" +
                team0.getAbbrev() + " receives:\\n" + sb0.toString() + "\\n" +
                team1.getAbbrev() + " receives:\\n" + sb1.toString());

        //send trade approval poll message that expires after 1 hour
        long expiration = (System.currentTimeMillis() + 60 * 60000)/1000; // measured in seconds
        PollPayload payload = new PollPayload(TRADE_POLL_TITLE, new String[]{"Approve", "Veto"}, expiration);
        bot.sendPollMessage(payload);
    }
}
