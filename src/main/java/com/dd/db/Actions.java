package com.dd.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dd.tools.Tools;
import com.dd.voting.ballot.RankedBallot;
import com.dd.voting.candidate.RankedCandidate;
import com.dd.voting.election.STVElection;
import com.dd.voting.election.STVElection.Quota;

import static com.dd.db.Tables.*;

// http://ondras.zarovi.cz/sql/demo/?keyword=dd_tyhou

public class Actions {

	static final Logger log = LoggerFactory.getLogger(Actions.class);

	
	public static String createPoll(String userId, String subject, String text) {
		
		// First create a discussion
		Discussion d = DISCUSSION.createIt("subject", subject,
				"text", text);
		
		POLL.createIt("poll_type_id", 1,
				"discussion_id", d.getId().toString());
		
		return "Poll created";
		
		
	}
	
	public static String createCandidate(String userId, String pollId, String subject, String text) {
		
		// First create a discussion
		Discussion d = DISCUSSION.createIt("subject", subject,
				"text", text);
		
		CANDIDATE.createIt("poll_id", pollId,
				"discussion_id", d.getId().toString());
		
		return "Candidate created";
		
	}

	public static String createBallot(String userId, String pollId, 
			Map<String, String> vars) {
		
		// TODO always just create a new ballot for now
		// In the future just get the current one if it doesn't exist
		Ballot b = BALLOT.createIt("poll_id", pollId,
				"user_id", userId);
		
		
		for (Entry<String, String> e: vars.entrySet()) {
			BALLOT_ITEM.createIt("ballot_id", b.getId(),
					"candidate_id", e.getKey(),
					"rank", e.getValue());
		}
		
		return "Ballot created";
		
		
	}

	public static String runSTVElection(String pollId) {
		
		
		// Get all the ballots for the poll
		List<BallotItemView> dbBallots = BALLOT_ITEM_VIEW.find("id = ?", pollId);
		
		
		// Group them up by ballot id to ballot items
		Map<Integer, List<RankedCandidate>> ballotMap = new HashMap<>();
		for (BallotItemView dbBallot : dbBallots) {
			Integer ballotId = Integer.valueOf(dbBallot.getId().toString());
			
			RankedCandidate rc = new RankedCandidate(dbBallot.getInteger("candidate_id"), 
					dbBallot.getInteger("rank"));
			
			ballotMap.get(ballotId);
			
			// The list needs to be created
			if (ballotMap.get(ballotId) == null) {
				List<RankedCandidate> rcs = new ArrayList<>();
				rcs.add(rc);
				ballotMap.put(ballotId, rcs);
			} else {
				List<RankedCandidate> rcs = ballotMap.get(ballotId);
				rcs.add(rc);
			}
			
		}
		
		// Add them to a list of ballots
		List<RankedBallot> ballots = new ArrayList<>();
		for (Entry<Integer, List<RankedCandidate>> e : ballotMap.entrySet()) {
			RankedBallot rb = new RankedBallot(e.getValue());
			ballots.add(rb);
		}
		
		
		STVElection election = new STVElection(Quota.DROOP, ballots, 1);
		
		String json = Tools.GSON.toJson(election.getRounds());
		
		return json;
		
	}
	
	

}
