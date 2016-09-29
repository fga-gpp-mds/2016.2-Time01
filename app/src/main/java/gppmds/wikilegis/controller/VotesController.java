package gppmds.wikilegis.controller;

import android.content.Context;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import gppmds.wikilegis.dao.JSONHelper;
import gppmds.wikilegis.dao.VotesDAO;
import gppmds.wikilegis.exception.SegmentException;
import gppmds.wikilegis.exception.VotesException;
import gppmds.wikilegis.model.Votes;

public class VotesController {

    private static List<Votes> votesList = new ArrayList<Votes>();
    private static VotesDAO votesDAO;
    private static Context context;
    private static VotesController instance = null;

    private VotesController(final Context context) {
        this.context = context;
    }

    public static VotesController getInstance(final Context context) {
        if (instance == null) {
            instance = new VotesController(context);
        }
        return  instance;
    }

    public static List<Votes> getVotesByIdOfSegment(final Integer idOfSegment) throws VotesException {
        votesDAO = votesDAO.getInstance(context);
        return votesDAO.getVotesByIdOfSegment(idOfSegment);
    }

    public static List<Votes> getAllVotes() throws VotesException {
        return votesList;
    }

    public static Integer getLikesOfSegment(final Integer idOfSegment) throws VotesException {
        List<Votes> votesListAux = getVotesByIdOfSegment(idOfSegment);

        int countLikes = 0;

        for (int i = 0; i < votesListAux.size(); i++) {
            if (votesListAux.get(i).isVote()) {
                countLikes++;
            }
        }
        return countLikes;
    }

    public static Integer getDislikesOfSegment(final Integer idOfSegment) throws VotesException {
        List<Votes> votesListAux = getVotesByIdOfSegment(idOfSegment);

        int countDislikes = 0;

        for (int i = 0; i < votesListAux.size(); i++) {
            if (!votesListAux.get(i).isVote()) {
                countDislikes++;
            }
        }
        return countDislikes;
    }

    public void initControllerVotes() throws SegmentException, JSONException, VotesException {

        votesDAO = votesDAO.getInstance(context);

        if (votesDAO.isDatabaseEmpty()) {
            votesList = JSONHelper.votesListFromJSON();
            votesDAO.insertAllVotes(votesList);
        } else {
            votesList = votesDAO.getAllVotes();
        }
    }
}