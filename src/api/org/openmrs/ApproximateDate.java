/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

/**
 *
 */
public class ApproximateDate implements Comparable<ApproximateDate> {
	
	private Integer metadata = null;
	
	private Integer year = null;
	
	private Integer month = null;
	
	private Integer day = null;
	
	public final static int NOT_APPROXIMATED = 0;

	public final static int APPROXIMATE_YEAR = 1;
	
	public final static int APPROXIMATE_MONTH = 2;
	
	public final static int APPROXIMATE_DAY = 4;
	
	public final static int APPROXIMATE_WEEK = 8;
	
	public final static int APPROXIMATE_AGE = 16;
	
	protected final static int UNKNOWN_YEAR = 256;
	
	protected final static int UNKNOWN_MONTH = 512;
	
	protected final static int UNKNOWN_DAY = 1024;

	private final static float DAYS_PER_YEAR = 365.25f;

	/**
	 * default no-arg constructor
	 */
	public ApproximateDate() {
	}
	
	/**
	 * Initializes the partialDate with an exact date
	 * 
	 * @param date the date to be set
	 */
	public ApproximateDate(Date date) {
		setDate(date);
	}
	
	/**
	 * Initializes the partialDate with an exact date and imposes meta-data on the data
	 * 
	 * @param date
	 * @param metadata
	 */
	public ApproximateDate(Date date, Integer metadata) {
		this(date);
		this.metadata = metadata;

	}
	
	/**
	 * @return the metadata
	 */
	public Integer getMetadata() {
		return metadata;
	}
	
	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(Integer metadata) {
		this.metadata = metadata;
		if (isDayUnknown())
			setDay(null);
		if (isMonthUnknown())
			setMonth(null);
		if (isYearUnknown())
			setYear(null);
	}
	
	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}
	
	/**
	 * @return the month
	 */
	public Integer getMonth() {
		return month;
	}
	
	/**
	 * @return the day
	 */
	public Integer getDay() {
		return day;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
		setFlag(UNKNOWN_YEAR, year == null);
	}
	
	/**
	 * @param month the month to set
	 */
	public void setMonth(Integer month) {
		this.month = month;
		setFlag(UNKNOWN_MONTH, month == null);
	}
	
	/**
	 * @param day the day to set
	 */
	public void setDay(Integer day) {
		this.day = day;
		setFlag(UNKNOWN_DAY, day == null);
	}

	/**
	 * Used internally to check flag status
	 * 
	 * @param flag the flag to be checked for
	 * @return
	 */
	protected boolean isFlag(int flag) {
		if (metadata == null)
			return false;
		return (metadata & flag) == flag;
	}
	
	/**
	 * Used internally to set flag status
	 * 
	 * @param flag the flag to be modified
	 * @param value the value set to the flag
	 */
	protected void setFlag(int flag, boolean value) {
		if (metadata == null)
			metadata = 0;
		if (value) {
			metadata |= flag;
		} else {
			metadata &= ~flag;
		}
	}
	
	/**
	 * Used internally to set the value for any field in the date
	 * 
	 * @param dtFieldType the type of the field to set
	 * @param value value for the
	 * @param approximate
	 * @param approximateFlag
	 * @param unknownFlag
	 **/
	//	private void setAnyField(DateTimeFieldType dtFieldType, Integer value, boolean approximate, int approximateFlag,
	//	                         int unknownFlag) {
	//		if (value != null) {
	//			partialDate = getPartial().with(dtFieldType, value);
	//			setFlag(unknownFlag, false);
	//			setFlag(approximateFlag, approximate);
	//		} else {
	//			setFlag(unknownFlag, true);
	//			setFlag(approximateFlag, false);
	//		}
	//	}
	
	/**
	 * Sets the year value
	 * 
	 * @param year
	 * @param approximate
	 * @should set the year value
	 * @should set the approximate value for year
	 */
	public void setYear(Integer year, boolean approximate) {
		setYear(year);
		setFlag(APPROXIMATE_YEAR, approximate);
	}
	
	/**
	 * Sets the month value
	 * 
	 * @param month
	 * @param approximate
	 * @should set the month value
	 * @should set the approximate value for month
	 */
	public void setMonth(Integer month, boolean approximate) {
		setMonth(month);
		setFlag(APPROXIMATE_MONTH, approximate);
	}
	
	/**
	 * Sets the day value
	 * 
	 * @param day
	 * @param approximate
	 * @should set the day value
	 * @should set the approximate value for day
	 */
	public void setDay(Integer day, boolean approximate) {
		setDay(day);
		setFlag(APPROXIMATE_DAY, approximate);
	}
	
	/**
	 * Used to set an approximation level for this date
	 * 
	 * @param flag
	 * @should set the approximation level
	 */
	public void setApproximated(int flag) {
		metadata = flag;
	}
	
	/**
	 * Checks if any part of the date is approximated
	 * 
	 * @return
	 * @should set the approximation level
	 */
	public boolean isApproximated() {
		if (metadata == null)
			return false;
		return getMetadata() > 0;
	}
	
	/**
	 * Checks if a particular part of the date is approximated
	 * 
	 * @param flag
	 * @return
	 */
	public boolean isApproximated(int approximatedTo) {
		return isFlag(approximatedTo);
	}
	
	/**
	 * Checks if the day value of the date is approximated
	 * 
	 * @return
	 */
	public boolean isDayApproximated() {
		return isFlag(APPROXIMATE_DAY);
	}
	
	/**
	 * Checks if the month value of the date is approximated
	 * 
	 * @return
	 */
	public boolean isMonthApproximated() {
		return isFlag(APPROXIMATE_MONTH);
	}
	
	/**
	 * Checks if the year value of the date is approximated
	 * 
	 * @return
	 */
	public boolean isYearApproximated() {
		return isFlag(APPROXIMATE_YEAR);
	}
	
	/**
	 * Checks if the date is derived using the age
	 * 
	 * @return
	 */
	public boolean isAgeApproximated() {
		return isFlag(APPROXIMATE_AGE);
	}
	
	protected boolean isDayUnknown() {
		return isFlag(UNKNOWN_DAY);
	}
	
	protected boolean isMonthUnknown() {
		return isFlag(UNKNOWN_MONTH);
	}
	
	protected boolean isYearUnknown() {
		return isFlag(UNKNOWN_MONTH);
	}

	/**
	 * Auto generated method comment
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param yearApprox
	 * @param monthApprox
	 * @param dayApprox
	 * @should properly set the date
	 */
	public void setDate(int year, int month, int day, boolean yearApprox, boolean monthApprox, boolean dayApprox) {
		setYear(year, yearApprox);
		setMonth(month, monthApprox);
		setDay(day, dayApprox);
	}
	
	/**
	 * Set partial date to a specific date
	 * 
	 * @param date
	 */
	public void setDate(Calendar calendar) {
		setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), false,
		    false, false);
	}
	
	/**
	 * Set partial date to a specific date
	 * 
	 * @param date
	 */
	public void setDate(Date date) {
		if (date == null) {
			setYear(null);
			setMonth(null);
			setDay(null);
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			setYear(calendar.get(Calendar.YEAR));
			setMonth(calendar.get(Calendar.MONTH) + 1);
			setDay(calendar.get(Calendar.DAY_OF_MONTH));
			if (metadata != null) {
				if (isYearUnknown())
					setYear(null);
				if (isMonthUnknown())
					setMonth(null);
				if (isDayUnknown())
					setDay(null);
			}
		}
	}
	
	/**
	 * Set partial date to an exact date
	 * 
	 * @param date the date to be set
	 */
	public void setExactDate(Date date) {
		setDate(date);
		setMetadata(0);
	}

	/**
	 * Auto generated method comment
	 * 
	 * @param age
	 * @should set the date depending on the age
	 */
	public void setDateFromAge(float age) {
		this.setDateFromAge(age, null);
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param age
	 * @should set the date depending on the age and a date
	 */
	public void setDateFromAge(float age, Date ageOnDate) {
		int days = Math.round(age * DAYS_PER_YEAR);
		Calendar calendar = Calendar.getInstance();
		if (ageOnDate != null)
			calendar.setTime(ageOnDate);
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		setDate(calendar);
		setFlag(APPROXIMATE_AGE, true);
	}
	
	/**
	 * @return Date estimated date based on partial and approximations
	 * @should return properly estimated dates
	 **/
	public Date getDate() {
		//TODO
		Integer year;
		if (isFlag(UNKNOWN_YEAR) || getYear() == null) {
			year = Calendar.getInstance().YEAR;
		} else {
			year = getYear();
		}
		
		Integer month;
		if (isFlag(UNKNOWN_MONTH) || getMonth() == null) {
			month = 7;
		} else {
			month = getMonth();
		}
		
		Integer date;
		if (isFlag(UNKNOWN_DAY) || getDay() == null) {
			date = 15;
		} else {
			date = getDay();
		}
		
		if (isFlag(UNKNOWN_MONTH) && isFlag(UNKNOWN_DAY) || getMonth() == null && getDay() == null) {
			month = 7;
			date = 1;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0L);
		cal.set(year, month - 1, date, 0, 0, 0);
		return cal.getTime();
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @should compare two ApproximateDates
	 */
	@Override
	public int compareTo(ApproximateDate o) {
		//TODO Advanced comparison incorporating Approximate values and Unknowns
		Partial thisDate = getAsPartial(this);
		Partial otherDate = getAsPartial(o);
		
		return thisDate.compareTo(otherDate);
	}
	
	private Partial getAsPartial(ApproximateDate aDate) {
		Partial partial = new Partial();
		if (aDate.getMetadata() != null) {
			if (!aDate.isYearUnknown())
				partial = partial.with(DateTimeFieldType.year(), getYear());
			if (!aDate.isMonthUnknown())
				partial = partial.with(DateTimeFieldType.monthOfYear(), getMonth());
			if (!aDate.isDayUnknown())
				partial = partial.with(DateTimeFieldType.monthOfYear(), getDay());
		}
		return partial;
	}

}