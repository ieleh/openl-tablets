package org.openl.tablets.tutorial4;

import org.openl.generated.beans.Driver;


/**
 * Web app is supposed to call web service via an instance of this interface.
 */
public interface Tutorial_4Wrapper {
	String[] getCoverage();
	String[] getTheft_rating();
	String driverAgeType(Driver driver);
}