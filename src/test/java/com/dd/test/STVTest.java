package com.dd.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.referendum.tools.Tools;
import com.referendum.voting.ballot.RankedBallot;
import com.referendum.voting.candidate.RankedCandidate;
import com.referendum.voting.election.ElectionRoundItem;
import com.referendum.voting.election.STVElection;
import com.referendum.voting.election.STVElection.Quota;


// test from 
// https://en.wikipedia.org/wiki/Single_transferable_vote#Counting_the_votes
public class STVTest extends TestCase {
	
	static final Logger log = LoggerFactory.getLogger(STVTest.class);

	Integer orange = 1, pear = 2, chocolate = 3, strawberry = 4, mixed = 5;
	
	private List<RankedBallot> setupBallots() {
		
		List<RankedBallot> ballots = new ArrayList<>();
		
		for (int i = 0; i < 4; i++) {
			RankedCandidate rc = new RankedCandidate(orange, 1);
			RankedBallot rb = new RankedBallot(Arrays.asList(rc));
			ballots.add(rb);
		}

		for (int i = 0; i < 2; i++) {
			RankedCandidate rc = new RankedCandidate(pear, 1);
			RankedCandidate rc2 = new RankedCandidate(orange, 2);
			List<RankedCandidate> candidates = new ArrayList<>();

			candidates.add(rc);
			candidates.add(rc2);
			RankedBallot rb = new RankedBallot(candidates);
			ballots.add(rb);
		}

		// Weird thing with the test, do 4 with second choice strawberries,
		// then 4 with second choice sweets, then another 4 with second choice strawberries
		for (int i = 0; i < 4; i++) {
			RankedCandidate rc = new RankedCandidate(chocolate, 1);
			RankedCandidate rc2 = new RankedCandidate(strawberry, 2);
			List<RankedCandidate> candidates = new ArrayList<>();

			candidates.add(rc);
			candidates.add(rc2);
			RankedBallot rb = new RankedBallot(candidates);
			ballots.add(rb);
		}

		for (int i = 0; i < 4; i++) {
			RankedCandidate rc = new RankedCandidate(chocolate, 1);
			RankedCandidate rc2 = new RankedCandidate(mixed, 2);
			List<RankedCandidate> candidates = new ArrayList<>();

			candidates.add(rc);
			candidates.add(rc2);
			RankedBallot rb = new RankedBallot(candidates);
			ballots.add(rb);
		}
		
		for (int i = 0; i < 4; i++) {
			RankedCandidate rc = new RankedCandidate(chocolate, 1);
			RankedCandidate rc2 = new RankedCandidate(strawberry, 2);
			List<RankedCandidate> candidates = new ArrayList<>();

			candidates.add(rc);
			candidates.add(rc2);
			RankedBallot rb = new RankedBallot(candidates);
			ballots.add(rb);
		}

		for (int i = 0; i < 1; i++) {
			RankedCandidate rc = new RankedCandidate(strawberry, 1);
			RankedBallot rb = new RankedBallot(Arrays.asList(rc));
			ballots.add(rb);
		}

		for (int i = 0; i < 1; i++) {
			RankedCandidate rc = new RankedCandidate(mixed, 1);
			RankedBallot rb = new RankedBallot(Arrays.asList(rc));
			ballots.add(rb);
		}

		
		return ballots;
	}

	public void testSTVElection() {
		
		Integer seats = 3;
		
		List<RankedBallot> ballots = setupBallots();
		
		STVElection stv = new STVElection(Quota.DROOP, ballots, seats);
		
		log.info(Tools.GSON2.toJson(stv.getRounds()));
		
		Integer rn = 0, c;
		
		// first round
		c = 0;
		assertEquals(Integer.valueOf(4), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(2), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(12), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(1), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(1), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());

		assertEquals(ElectionRoundItem.Status.ELECTED, stv.getRounds().get(rn++).getRoundItems().get(2).getStatus());
		
		
		// second round
		c = 0;
		assertEquals(Integer.valueOf(4), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(2), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(6), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(5), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(3), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());

		assertEquals(ElectionRoundItem.Status.ELECTED_PREVIOUSLY, stv.getRounds().get(rn).getRoundItems().get(2).getStatus());
		assertEquals(ElectionRoundItem.Status.DEFEATED, stv.getRounds().get(rn++).getRoundItems().get(1).getStatus());

		// third round
		c = 0;
		assertEquals(Integer.valueOf(6), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(6), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(5), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(3), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());

		assertEquals(ElectionRoundItem.Status.ELECTED_PREVIOUSLY, stv.getRounds().get(rn).getRoundItems().get(1).getStatus());
		assertEquals(ElectionRoundItem.Status.ELECTED, stv.getRounds().get(rn++).getRoundItems().get(0).getStatus());

		// fourth round
		c = 0;
		assertEquals(Integer.valueOf(6), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(6), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(5), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(3), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());

		assertEquals(ElectionRoundItem.Status.ELECTED_PREVIOUSLY, stv.getRounds().get(rn).getRoundItems().get(0).getStatus());
		assertEquals(ElectionRoundItem.Status.ELECTED_PREVIOUSLY, stv.getRounds().get(rn).getRoundItems().get(1).getStatus());
		assertEquals(ElectionRoundItem.Status.DEFEATED, stv.getRounds().get(rn++).getRoundItems().get(3).getStatus());

		// fifth round
		c = 0;
		assertEquals(Integer.valueOf(6), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(6), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());
		assertEquals(Integer.valueOf(5), stv.getRounds().get(rn).getRoundItems().get(c++).getVotes());

		assertEquals(ElectionRoundItem.Status.ELECTED_PREVIOUSLY, stv.getRounds().get(rn).getRoundItems().get(0).getStatus());
		assertEquals(ElectionRoundItem.Status.ELECTED_PREVIOUSLY, stv.getRounds().get(rn).getRoundItems().get(1).getStatus());
		assertEquals(ElectionRoundItem.Status.ELECTED, stv.getRounds().get(rn++).getRoundItems().get(2).getStatus());
		
	}

}
