package ncollins.schedulers;

import ncollins.chat.bots.Bot;
import ncollins.espn.Espn;
import ncollins.helpers.BotFactory;
import ncollins.model.chat.BotType;
import ncollins.model.chat.Emojis;
import ncollins.model.espn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

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
    private static final String ESPN_TRANSACTION_CHAT_ID = System.getenv("SLACK_ESPN_TRANSACTION_CHAT_ID");

    private Bot bot;
    private Espn espn;

    public EspnTransactionScheduler(Espn espn){
        this.bot = BotFactory.buildBot(BotType.ESPN, ESPN_TRANSACTION_CHAT_ID);
        this.espn = espn;
        start();
    }

    public void start(){
        // schedule task every 60 seconds
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> checkForTransaction(), 0, 60, TimeUnit.SECONDS);

        logger.info("EspnTransactionScheduler enabled.");
    }

    private void checkForTransaction(){
        // get transactions over the last 60 seconds (60000 ms)
        List<Transaction> transactions = espn.getTransactions(
                espn.getCurrentSeasonId(),
                System.currentTimeMillis()-60000, System.currentTimeMillis(),
                List.of(TransactionType.TRADE_ACCEPTED.getValue(), TransactionType.TRADE_BLOCK_ADDED.getValue()));

        if(transactions == null){
            return;
        }

        for(Transaction t : transactions){
            if(isTrade(t)){
                processTrade(t);
            } else if(isTradeBlockAdd(t)){
                processTradeBlockAdd(t);
            }
        }
    }

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

        sendTradeMessage(team0, team1, playersToTeam0, playersToTeam1);
    }

    private void processTradeBlockAdd(Transaction t){
        Integer playerId = t.getMessages()[0].getForId();
        Integer teamId = t.getMessages()[0].getTargetId();
        String memberId = t.getMessages()[0].getAuthor();

        Player player = espn.getPlayer(playerId);
        String playerPosition = espn.getPositionById(player.getDefaultPositionId());
        Team team = espn.getTeamById(teamId);
        Member member = espn.getMemberByOwnerId(memberId);

        //send breaking news message
        bot.sendMessage("TRADE BLOCK ALERT " + Emojis.EYES_LEFT + " \\n\\n" +
                "Disgruntled " + playerPosition + " " + player.getFullName() + " has requested a trade from " +
                team.getLocation() + " " + team.getNickname() + ", citing growing frustration with team owner " + member.getFirtName() + " " + member.getLastName() + ".\\n\\n" +
                "The two sides will work together to find a new home for " + player.getLastName() + " over the coming days.");
    }

    private Boolean isTrade(Transaction t){
        return isTransactionType(t, TransactionType.TRADE_ACCEPTED.getValue());
    }

    private Boolean isTradeBlockAdd(Transaction t){
        return isTransactionType(t, TransactionType.TRADE_BLOCK_ADDED.getValue());
    }

    private Boolean isTransactionType(Transaction t, int typeId){
        return t != null
                && t.getMessages().length > 0
                && t.getMessages()[0].getMessageTypeId().equals(typeId);
    }

    private void sendTradeMessage(Team team0, Team team1, List<Player> playersToTeam0, List<Player> playersToTeam1){
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
    }
}
