package ncollins.schedulers;

import ncollins.chat.groupme.GroupMeBot;
import ncollins.espn.Espn;
import ncollins.model.chat.PollPayload;
import ncollins.model.espn.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EspnTransactionScheduler implements Scheduler {
    private GroupMeBot bot;
    private Espn espn;
    private static final String TRADE_POLL_TITLE = "Do you approve of this trade?\uD83D\uDC46Majority veto nullifies this agreed upon transaction.";

    public EspnTransactionScheduler(GroupMeBot bot, Espn espn){
        this.bot = bot;
        this.espn = espn;
    }

    public void start(){
        // schedule task every 30 seconds
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> checkForEspnTrade(), 0, 60, TimeUnit.SECONDS);
    }

    private void checkForEspnTrade(){
        // get transactions over the last 30 seconds (30000 ms)
        List<Transaction> transactions = espn.getTransactions(System.currentTimeMillis()-30000, System.currentTimeMillis(),
                List.of(TransactionType.TRADE_ACCEPTED.getValue()));

        if(transactions == null){
            return;
        }

        for(Transaction t : transactions){
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
    }

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
        bot.sendMessage("\uD83D\uDEA8 BREAKING NEWS \uD83D\uDEA8 \\n\\n" +
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
