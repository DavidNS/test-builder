package com.dns.resttestbuilder.model.evaluation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.controller.ResultController;
import com.dns.resttestbuilder.controller.TestExecutorController;
import com.dns.resttestbuilder.entity.Result;
import com.dns.resttestbuilder.entity.TestResult;
import com.dns.resttestbuilder.entity.embeddedresult.Dates;
import com.dns.resttestbuilder.entity.embeddedresult.Differences;
import com.dns.resttestbuilder.entity.embeddedstep.mainRequest.ExpectedPerformaceResults;
import com.dns.resttestbuilder.entity.embeddedstep.mainRequest.TimeSuccessKind;
import com.dns.resttestbuilder.model.execution.steps.Times;

@Component
public class TimeEvaluation {

	@Autowired
	ResultController resultController;

	@Autowired
	TestExecutorController testExecutorController;

	public ArrayList<Result> evaluateTimes(TestResult testResult, Result result, Times tt, Times pt) {
		setTimes(result, pt);
		setTimes(result, tt);
		return evaluateResultTimes(testResult, result, tt, pt);
	}

	private ArrayList<Result> evaluateResultTimes( TestResult testResult, Result result, Times tt, Times pt) {
		ArrayList<Result> updatedtTimes = new ArrayList<>();
		ExpectedPerformaceResults exRS = testResult.getMainRequestStepModel().getExpectedPerformaceResults();
		List<TimeSuccessKind> timeSuccessKinds = exRS.getTimeSuccessKind();
		Dates dates = result.getDates();

		boolean needTestEvaluated = needEval(timeSuccessKinds, TimeSuccessKind.TOTAL_TIME);
		boolean needParallEvaluated = needEval(timeSuccessKinds, TimeSuccessKind.PARALLEL_TIME);
		boolean needSingleEvaluated = needEval(timeSuccessKinds, TimeSuccessKind.SINGLE_TIME);

		Long expectedSingleTime = exRS.getExpectedSingleTime();
		Long expectedParallelTime = exRS.getExpectedPararellTime();
		Long expectedTotalTime = exRS.getExpectedTotalTime();

		boolean totalPassed = !needTestEvaluated || timePassed(expectedTotalTime, tt);
		boolean parallPassed = !needParallEvaluated || timePassed(expectedParallelTime, pt);

		long singleDiff = timeDif(dates.getRequestDate(), dates.getResponseDate());
		boolean singlePassed = !needSingleEvaluated || testTimePassed(singleDiff, expectedSingleTime);

		boolean passed = totalPassed && parallPassed && singlePassed;

		if (!needTestEvaluated && !needParallEvaluated) {
			updateTimesPassed(testResult, passed);
			result.getEvaluation().setTimePassed(passed);
		}
		result.getDifferences().setRequestResponseDiff(singleDiff);
		evaluateDiffereces(needTestEvaluated, passed, pt, this::parallDiffSetter, testResult,result,updatedtTimes);
		evaluateDiffereces(false, passed, tt, this::totallDiffSetter, testResult,result, updatedtTimes);
		return updatedtTimes;
	}

	private void evaluateDiffereces(Boolean needMoreEval, Boolean passed, Times t, DiffSetter diffSetter,
			TestResult testResult, Result result, ArrayList<Result> updatedtTimes) {
		if (checkEval(t)) {
			long timeDiff = timeDif(t.getMinRequestDate(), t.getMaxResponseDate());
			ArrayList<Result> rs = t.getTotalResult();
			rs.forEach((r) -> {
				Differences diff = r.getDifferences();
				Dates rd = r.getDates();
				Long requestParallDiff = timeDif(rd.getRequestDate(), t.getMaxResponseDate());
				diffSetter.setDiff(rd, t.getMaxResponseDate(), diff, timeDiff, requestParallDiff);
				if(!r.equals(result)) {
					updatedtTimes.add(r);
				}
				if (!needMoreEval && !r.equals(result)) {
					updateTimesPassed(testResult, passed);
					r.getEvaluation().setTimePassed(passed);
				}
			});
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

	public void updateTimesPassed(TestResult testResult, boolean passed) {
		if (passed) {
			Long ok = testResult.getTimeOK();
			ok++;
			testResult.setTimeOK(ok);
		} else {
			Long ko = testResult.getTimeKO();
			ko++;
			testResult.setTimeKO(ko);
		}
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
