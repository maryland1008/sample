package org.bank.demo.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DateUtils {
	final static Logger logger = Logger.getLogger(DateUtils.class);

	public static final SimpleDateFormat SIMPLE_DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm");

	public static boolean validDate(String someDate) {
		return (DateUtils.parseDate(someDate) != null);
	}

	public static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		Date safeDate = new Date(date.getTime());
		LocalDate localDate = safeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return DateUtils.formatDate(localDate);
	}

	public static String formatDateTime(Date date) {
		if (date == null) {
			return null;
		}
		Date safeDate = new Date(date.getTime());
		LocalDateTime localDateTime = safeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return DateUtils.formatDateTime(localDateTime);
	}

	public static String formatDate(LocalDate date) {
		if (date == null) {
			return null;
		}
		return DATE_FORMATTER.format(date);
	}

	public static String formatDateTime(LocalDateTime dateTime) {
		if (dateTime == null) {
			return null;
		}
		return DATE_TIME_FORMATTER.format(dateTime);
	}

	public static Date parseDate(String someDate) {
		try {
			LocalDate localDate = LocalDate.parse(someDate, DATE_FORMATTER);
			return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		} catch (DateTimeParseException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public static long dateDiff(String startDate, String endDate) throws DateTimeParseException {
		LocalDate from = LocalDate.parse(startDate, DATE_FORMATTER);
		LocalDate to = LocalDate.parse(endDate, DATE_FORMATTER);

		if (from.isBefore(to)) {
			return (from.until(to, ChronoUnit.DAYS) + 1);
		} else {
			return (to.until(from, ChronoUnit.DAYS) + 1);
		}
	}

	public static String getCurrentDate() {
		return LocalDate.now().format(DATE_FORMATTER);
	}

	public static String getCurrentDateTime() {
		return LocalDateTime.now().format(DATE_TIME_FORMATTER);
	}

	public static boolean isExpiredAfterYears(String someDate, int year) throws DateTimeParseException {
		LocalDate date1 = LocalDate.parse(someDate, DATE_FORMATTER);
		LocalDate date2 = LocalDate.now();
		return !(date1.isBefore(date2) && date1.plusYears(year).isAfter(date2));
	}

	public static boolean isDateInPast(String someDate) {
		if (StringUtils.isNotBlank(someDate)) {
			try {
				LocalDate date1 = LocalDate.parse(someDate, DATE_FORMATTER);
				LocalDate date2 = LocalDate.now();
				return date1.isBefore(date2);
			} catch (DateTimeParseException e) {
				logger.error(e.getMessage());
			}
		}
		return false;
	}

}
