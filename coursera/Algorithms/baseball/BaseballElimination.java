import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class BaseballElimination {
    private final int teamCount;
    private final int totalV;
    private HashMap<String, Integer> teamToIndex;
    private String[] teams;
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g;

    // Create a baseball division from given filename in format specified below.
    public BaseballElimination(String filename) {
        In fileIn = new In(filename);
        this.teamCount = Integer.parseInt(fileIn.readLine().strip());
        this.teams = new String[this.teamCount];
        this.totalV = 1 + this.teamCount * this.teamCount + this.teamCount + 1;
        this.w = new int[this.teamCount];
        this.l = new int[this.teamCount];
        this.r = new int[this.teamCount];
        this.g = new int[this.teamCount][this.teamCount];
        this.teamToIndex = new HashMap<>();
        for (int i = 0; i < this.teamCount; i++) {
            String line = fileIn.readLine();
            String[] tokens = line.strip().split("\\s+");
            this.teams[i] = tokens[0];
            this.teamToIndex.put(tokens[0], i);
            this.w[i] = Integer.parseInt(tokens[1]);
            this.l[i] = Integer.parseInt(tokens[2]);
            this.r[i] = Integer.parseInt(tokens[3]);
            for (int j = 0; j < this.teamCount; j++) {
                this.g[i][j] = Integer.parseInt(tokens[j + 4]);
            }
        }
    }

    // Number of teams.
    public int numberOfTeams() {
        return teamCount;
    }

    // All teams.
    public Iterable<String> teams() {
        return teamToIndex.keySet();
    }

    // Number of wins for given team.
    public int wins(String team) {
        checkTeam(team);
        return w[teamToIndex.get(team)];
    }

    // Number of losses for given team.
    public int losses(String team) {
        checkTeam(team);
        return l[teamToIndex.get(team)];
    }

    // Number of remaining games for given team.
    public int remaining(String team) {
        checkTeam(team);
        return r[teamToIndex.get(team)];
    }

    // Number of remaining games between team1 and team2.
    public int against(String team1, String team2) {
        checkTeam(team1);
        checkTeam(team2);
        return g[teamToIndex.get(team1)][teamToIndex.get(team2)];
    }

    // Is given team eliminated?
    public boolean isEliminated(String team) {
        checkTeam(team);
        int teamIndex = teamToIndex.get(team);

        if (isTrivialEliminated(teamIndex) != -1) {
            return true;
        }

        FlowNetwork flowNetwork = getFlowNetwork(teamIndex);

        new FordFulkerson(flowNetwork, 0, totalV - 1);

        for (FlowEdge flowEdge : flowNetwork.adj(0)) {
            if (flowEdge.flow() < flowEdge.capacity()) {
                return true;
            }
        }
        return false;
    }

    // Subset R of teams that eliminates given team; null if not eliminated.
    public Iterable<String> certificateOfElimination(String team) {
        checkTeam(team);
        int teamIndex = teamToIndex.get(team);

        if (isTrivialEliminated(teamIndex) != -1) {
            return Collections.singletonList(teams[isTrivialEliminated(teamIndex)]);
        }

        HashSet<String> subset = new HashSet<>();

        FlowNetwork flowNetwork = getFlowNetwork(teamIndex);

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, totalV - 1);

        for (int i = 0; i < teamCount; i++) {
            if (i == teamIndex) {
                continue;
            }

            int teamVidx = getTeamVidx(i);
            if (fordFulkerson.inCut(teamVidx)) {
                subset.add(teams[i]);
            }
        }

        if (subset.isEmpty()) {
            return null;
        }
        return subset;
    }

    private int isTrivialEliminated(int teamIndex) {
        for (int i = 0; i < teamCount; i++) {
            if (w[teamIndex] + r[teamIndex] - w[i] < 0) {
                return i;
            }
        }
        return -1;
    }

    private FlowNetwork getFlowNetwork(int teamIndex) {
        FlowNetwork flowNetwork = new FlowNetwork(totalV);

        // Add edges.
        for (int i = 0; i < teamCount; i++) {
            for (int j = i + 1; j < teamCount; j++) {
                if (i == teamIndex || j == teamIndex) {
                    // Skip adding flow edge for current team or remaining match is 0.
                    continue;
                }
                // Add edges from (s = 0) to g[i][j].
                flowNetwork.addEdge(new FlowEdge(0, getGameVidx(i, j), g[i][j]));
                // Add edges from g[i][j] to team i and team j.
                flowNetwork.addEdge(
                        new FlowEdge(getGameVidx(i, j), getTeamVidx(i), Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(
                        new FlowEdge(getGameVidx(i, j), getTeamVidx(j), Double.POSITIVE_INFINITY));
            }
        }
        // Add edges from team i to (t = totalV - 1).
        for (int i = 0; i < teamCount; i++) {
            if (i == teamIndex) {
                continue;
            }
            flowNetwork.addEdge(
                    new FlowEdge(getTeamVidx(i), totalV - 1, w[teamIndex] + r[teamIndex] - w[i]));
        }

        return flowNetwork;
    }

    private int getGameVidx(int teamI, int teamJ) {
        return teamI * teamCount + teamJ + 1;
    }

    private int getTeamVidx(int team) {
        return team + teamCount * teamCount + 1;
    }

    private void checkTeam(String team) {
        if (!teamToIndex.containsKey(team)) {
            throw new IllegalArgumentException("Team not found for " + team);
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("teams36.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
