package com.dns.resttestbuilder.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.entity.Result;
import com.dns.resttestbuilder.entity.TestResult;
import com.dns.resttestbuilder.entity.embeddedresult.Dates;
import com.dns.resttestbuilder.entity.embeddedresult.Differences;
import com.dns.resttestbuilder.entity.embeddedstep.mainRequest.ExpectedPerformaceResults;
import com.dns.resttestbuilder.entity.embeddedstep.mainRequest.TimeSuccessKind;
import com.dns.resttestbuilder.model.steps.Times;
import com.dns.resttestbuilder.repository.ResultRepository;
import com.dns.resttestbuilder.validation.DefaultData;

@RestController
@RequestMapping(path = "/users/{userID}")
public class ResultController {

	@Autowired
	ResultRepository resultRepository;

	@Autowired
	DefaultData defaultData;

	@GetMapping("/results/{resultID}")
	Result getResult(@PathVariable Long userID, @PathVariable Long resultID) {
		Result result = resultRepository.findById(resultID).map((r) -> {
			defaultData.handleNotValidUserID(TestResult.class, resultID, r.getUserID(), userID);
			return r;
		}).orElseThrow(defaultData.getNotFoundSupplier(TestResult.class, resultID));
		return result;
	}

	public Result saveFull(Result result) {
		return resultRepository.save(result);
	}

	public void updateTimes(ExpectedPerformaceResults exRS, Result result, Times tt, Times pt) {
		setTimes(result, pt);
		setTimes(result, tt);
		updateResultTimes(exRS, result, tt, pt);
	}

	private void updateResultTimes(ExpectedPerformaceResults exRS, Result result, Times tt, Times pt) {

		List<TimeSuccessKind> timeSuccessKinds = exRS.getTimeSuccessKind();
		Dates dates = result.getDates();

		boolean needTestEvaluated = needEval(timeSuccessKinds, TimeSuccessKind.TOTAL_TIME);
		boolean needParallEvaluated = needEval(timeSuccessKinds, TimeSuccessKind.PARALLEL_TIME);
		boolean needSingleEvaluated = needEval(timeSuccessKinds, TimeSuccessKind.SINGLE_TIME);

		Long expectedSingleTime = exRS.getExpectedSingleTime();
		Long expectedParallelTime = exRS.getExpectedPararellTime();
		Long expectedTotalTime = exRS.getExpectedTotalTime();

		boolean totalPassed = !needTestEvaluated|| timePassed(expectedTotalTime, tt);
		boolean parallPassed = !needParallEvaluated|| timePassed(expectedParallelTime, pt);

		long singleDiff = timeDif(dates.getRequestDate(), dates.getResponseDate());
		boolean singlePassed = !needSingleEvaluated || testTimePassed(singleDiff, expectedSingleTime);
		
		boolean passed = totalPassed && parallPassed && singlePassed;

		if (!needTestEvaluated && !needParallEvaluated) {
			result.getEvaluation().setTimePassed(passed);
		}
		result.getDifferences().setRequestResponseDiff(singleDiff);

		updateDiffereces(needTestEvaluated, passed, pt, this::parallDiffSetter);
		updateDiffereces(false, passed, tt, this::totallDiffSetter);

	}

	private void updateDiffereces(Boolean needMoreEval, Boolean passed, Times t, DiffSetter diffSetter) {
		if (checkEval(t)) {
			long timeDiff = timeDif(t.getMinRequestDate(), t.getMaxResponseDate());
			ArrayList<Result> rs = t.getTotalResult();
			rs.forEach((r) -> {
				Differences diff = r.getDifferences();
				Dates rd = r.getDates();
				Long requestParallDiff = timeDif(rd.getRequestDate(), t.getMaxResponseDate());
				diffSetter.setDiff(rd, t.getMaxResponseDate(), diff, timeDiff, requestParallDiff);
				if(!needMoreEval) {
					r.getEvaluation().setTimePassed(passed);
				}
			});
			resultRepository.saveAll(rs);
		}
	}

	private void parallDiffSetter(Dates dts, Date date, Differences diff, long parallDiff, long requestParallDiff) {
		diff.setParallDiff(parallDiff);
		diff.setRequestParallDiff(requestParallDiff);
		dts.setParallTestEndDate(date);
	}

	private void totallDiffSetter(Dates dts, Date date, Differences diff, long totalDiff, long requestTotalDiff) {
		diff.setTotalDiff(totalDiff);
		diff.setRequestTotalDiff(requestTotalDiff);
		dts.setTestEndDate(date);
	}

	private boolean needEval(List<TimeSuccessKind> timeSuccessKinds, TimeSuccessKind timeSuccesKind) {
		return timeSuccessKinds != null && !timeSuccessKinds.contains(timeSuccesKind);
	}

	private boolean checkEval(Times times) {
		return times.getExtectedResponses().equals((long) times.getTotalResult().size());
	}

	private void setTimes(Result result, Times t) {
		t.getTotalResult().add(result);
		Date requestDate = result.getDates().getRequestDate();
		Date responseDate = result.getDates().getResponseDate();
		Date maxResponseDate = t.getMaxResponseDate();
		Date minRequestDate = t.getMinRequestDate();
		t.setMinRequestDate(min(requestDate, minRequestDate));
		t.setMaxResponseDate(max(responseDate, maxResponseDate));

	}

	public <T extends Comparable<T>> T min(T a, T b) {
		return a == null ? b : (b == null ? a : (a.compareTo(b) > 0 ? b : a));
	}

	public <T extends Comparable<T>> T max(T a, T b) {
		return a == null ? b : (b == null ? a : (a.compareTo(b) > 0 ? a : b));
	}

	private boolean timePassed(Long expected, Times t) {
		return t.getExtectedResponses().equals((long) t.getTotalResult().size()) && t.getMinRequestDate() == null
				? false
				: testTimePassed(timeDif(t.getMinRequestDate(), t.getMaxResponseDate()), expected);
	}

	private Long timeDif(Date init, Date end) {
		return init == null || end == null ? null : Math.abs(end.getTime() - init.getTime());
	}

	private boolean testTimePassed(Long timeDiff, double expectedTestTime) {
		return timeDiff == null ? false : timeDiff < expectedTestTime;
	}

	@FunctionalInterface
	public interface DiffSetter {
		void setDiff(Dates rd, Date date, Differences differences, long diff, long requestDiff);
	}

}
