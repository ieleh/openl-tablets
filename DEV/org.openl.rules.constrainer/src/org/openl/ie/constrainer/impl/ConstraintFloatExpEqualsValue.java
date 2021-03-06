package org.openl.ie.constrainer.impl;

import org.openl.ie.constrainer.Constraint;
import org.openl.ie.constrainer.ConstraintImpl;
import org.openl.ie.constrainer.EventOfInterest;
import org.openl.ie.constrainer.Failure;
import org.openl.ie.constrainer.FloatEvent;
import org.openl.ie.constrainer.FloatExp;
import org.openl.ie.constrainer.Goal;
import org.openl.ie.constrainer.IntBoolExp;
import org.openl.ie.constrainer.Observer;
import org.openl.ie.constrainer.Subject;

///////////////////////////////////////////////////////////////////////////////
/*
 * Copyright Exigen Group 1998, 1999, 2000
 * 320 Amboy Ave., Metuchen, NJ, 08840, USA, www.exigengroup.com
 *
 * The copyright to the computer program(s) herein
 * is the property of Exigen Group, USA. All rights reserved.
 * The program(s) may be used and/or copied only with
 * the written permission of Exigen Group
 * or in accordance with the terms and conditions
 * stipulated in the agreement/contract under which
 * the program(s) have been supplied.
 */
///////////////////////////////////////////////////////////////////////////////
//
//: ConstraintExpEqualsValue.java
//
/**
 * An implementation of the constraint: <code>FloatExp == value</code>.
 */
public final class ConstraintFloatExpEqualsValue extends ConstraintImpl {
    // PRIVATE MEMBERS
    private FloatExp _exp;
    private double _value;
    private Constraint _opposite;

    public ConstraintFloatExpEqualsValue(FloatExp exp, double value) {
        super(exp.constrainer());
        _exp = exp;
        _value = value;

        if (constrainer().showInternalNames()) {
            _name = "(" + exp.name() + "=" + value + ")";
        }

    }

    public Goal execute() throws Failure {
        class ObserverFloatEqualValue extends Observer {
            @Override
            public Object master() {
                return ConstraintFloatExpEqualsValue.this;
            }

            @Override
            public int subscriberMask() {
                return EventOfInterest.VALUE | EventOfInterest.MINMAX;
            }

            @Override
            public String toString() {
                return "ObserverFloatEqualValue";
            }

            @Override
            public void update(Subject exp, EventOfInterest interest) throws Failure {
                // Debug.on();Debug.print("ObserverFloatEqualValue:
                // "+interest);Debug.off();
                FloatEvent event = (FloatEvent) interest;
                if (FloatCalc.gt(_value, event.max()) || FloatCalc.gt(event.min(), _value)) {
                    exp.constrainer().fail("from ObserverFloatEqualValue");
                }
                _exp.setValue(_value);
            }
        } // ~ ObserverFloatEqualValue
        _exp.setValue(_value); // may fail
        _exp.attachObserver(new ObserverFloatEqualValue());
        return null;
    }

    @Override
    public boolean isLinear() {
        return _exp.isLinear();
    }

    @Override
    public Constraint opposite() {
        if (_opposite == null) {
            _opposite = new ConstraintFloatExpNotValue(_exp, _value);
        }
        return _opposite;
    }

    @Override
    public IntBoolExp toIntBoolExp() {
        return _exp.eq(_value);
    }

    @Override
    public String toString() {
        return _exp + "=" + _value;
    }

} // ~ ConstraintFloatExpEqualsValue
