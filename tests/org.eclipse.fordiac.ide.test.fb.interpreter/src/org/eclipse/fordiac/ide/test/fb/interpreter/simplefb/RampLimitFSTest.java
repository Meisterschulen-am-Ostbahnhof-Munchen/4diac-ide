/*******************************************************************************
 * Copyright (c) 2026 HR Agrartechnik GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Franz Höpfinger
 *       - initial implementation and/or documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.test.fb.interpreter.simplefb;

import static org.eclipse.fordiac.ide.fb.interpreter.api.TransactionFactory.addTransaction;
import static org.eclipse.fordiac.ide.fb.interpreter.mm.VariableUtils.setVariable;

import org.eclipse.fordiac.ide.fb.interpreter.api.FBTransactionBuilder;
import org.eclipse.fordiac.ide.fb.interpreter.mm.ServiceSequenceUtils;
import org.eclipse.fordiac.ide.model.libraryElement.ServiceSequence;
import org.eclipse.fordiac.ide.model.libraryElement.SimpleFBType;
import org.eclipse.fordiac.ide.test.fb.interpreter.infra.AbstractInterpreterTest;

/**
 * Tests for the RampLimitFS SimpleFB. Covers the INIT/ZERO/FULL/LOAD jump algorithms, the
 * UP_SLOW/UP_FAST/DOWN_SLOW/DOWN_FAST ramp algorithms including clamping against VAL_FULL/VAL_ZERO,
 * and the qAtZero/qAtFull limit-reached indicator outputs added in version 3.2.
 */
public class RampLimitFSTest extends AbstractInterpreterTest {

	@Override
	public void test() {
		final SimpleFBType fb = (SimpleFBType) loadFBType("RampLimitFS"); //$NON-NLS-1$

		// Scenario 1: INIT establishes a deterministic OUT/qAtZero/qAtFull, then ramp up/down through
		// both limits, exercising the overshoot clamps and the indicators exactly at the boundary.
		ServiceSequence seq = fb.getService().getServiceSequence().get(0);
		setVariable(fb, "PV", "42"); //$NON-NLS-1$ //$NON-NLS-2$
		setVariable(fb, "VAL_ZERO", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		setVariable(fb, "SLOW", "10"); //$NON-NLS-1$ //$NON-NLS-2$
		setVariable(fb, "FAST", "30"); //$NON-NLS-1$ //$NON-NLS-2$
		setVariable(fb, "VAL_FULL", "100"); //$NON-NLS-1$ //$NON-NLS-2$

		addTransaction(seq, new FBTransactionBuilder("INIT", "INITO", "OUT:=0;qAtZero:=TRUE;qAtFull:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addTransaction(seq, new FBTransactionBuilder("UP_SLOW", "CNF", "OUT:=10;qAtZero:=FALSE;qAtFull:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addTransaction(seq, new FBTransactionBuilder("UP_FAST", "CNF", "OUT:=40;qAtZero:=FALSE;qAtFull:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addTransaction(seq, new FBTransactionBuilder("UP_FAST", "CNF", "OUT:=70;qAtZero:=FALSE;qAtFull:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		// 70 + 30 lands exactly on VAL_FULL: no overshoot, but qAtFull must already flip via GE.
		addTransaction(seq, new FBTransactionBuilder("UP_FAST", "CNF", "OUT:=100;qAtZero:=FALSE;qAtFull:=TRUE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		// 100 + 10 overshoots VAL_FULL and must be clamped back down to it.
		addTransaction(seq, new FBTransactionBuilder("UP_SLOW", "CNF", "OUT:=100;qAtZero:=FALSE;qAtFull:=TRUE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addTransaction(seq, new FBTransactionBuilder("DOWN_FAST", "CNF", "OUT:=70;qAtZero:=FALSE;qAtFull:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addTransaction(seq, new FBTransactionBuilder("DOWN_SLOW", "CNF", "OUT:=60;qAtZero:=FALSE;qAtFull:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addTransaction(seq, new FBTransactionBuilder("FULL", "CNF", "OUT:=100;qAtZero:=FALSE;qAtFull:=TRUE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addTransaction(seq, new FBTransactionBuilder("ZERO", "CNF", "OUT:=0;qAtZero:=TRUE;qAtFull:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		// 0 - 10 undershoots VAL_ZERO and must be clamped back up to it.
		addTransaction(seq, new FBTransactionBuilder("DOWN_SLOW", "CNF", "OUT:=0;qAtZero:=TRUE;qAtFull:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addTransaction(seq, new FBTransactionBuilder("LOAD", "CNF", "OUT:=42;qAtZero:=FALSE;qAtFull:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		runFBTest(fb, seq);

		// Scenario 2: ZERO and FULL jump directly to the (non-zero) limits without needing INIT
		// first, and the indicators flip exactly at the boundary (LE/GE, not strict LT/GT).
		seq = ServiceSequenceUtils.addServiceSequence(fb.getService());
		setVariable(fb, "VAL_ZERO", "5"); //$NON-NLS-1$ //$NON-NLS-2$
		setVariable(fb, "VAL_FULL", "95"); //$NON-NLS-1$ //$NON-NLS-2$
		addTransaction(seq, new FBTransactionBuilder("ZERO", "CNF", "OUT:=5;qAtZero:=TRUE;qAtFull:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addTransaction(seq, new FBTransactionBuilder("FULL", "CNF", "OUT:=95;qAtZero:=FALSE;qAtFull:=TRUE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		runFBTest(fb, seq);

		// Scenario 3: LOAD is documented as not clamped to VAL_ZERO/VAL_FULL - unlike UP_*/DOWN_*, an
		// out-of-range PV is passed straight through to OUT, only the limit indicators reflect the
		// overrun.
		seq = ServiceSequenceUtils.addServiceSequence(fb.getService());
		setVariable(fb, "VAL_ZERO", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		setVariable(fb, "VAL_FULL", "100"); //$NON-NLS-1$ //$NON-NLS-2$
		setVariable(fb, "PV", "-5"); //$NON-NLS-1$ //$NON-NLS-2$
		addTransaction(seq, new FBTransactionBuilder("LOAD", "CNF", "OUT:=-5;qAtZero:=TRUE;qAtFull:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		runFBTest(fb, seq);

		seq = ServiceSequenceUtils.addServiceSequence(fb.getService());
		setVariable(fb, "VAL_ZERO", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		setVariable(fb, "VAL_FULL", "100"); //$NON-NLS-1$ //$NON-NLS-2$
		setVariable(fb, "PV", "999"); //$NON-NLS-1$ //$NON-NLS-2$
		addTransaction(seq, new FBTransactionBuilder("LOAD", "CNF", "OUT:=999;qAtZero:=FALSE;qAtFull:=TRUE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		runFBTest(fb, seq);

		// Scenario 4: negative-range regression check - VAL_ZERO/VAL_FULL/PV and the ramp arithmetic
		// must work correctly when the whole range sits on/around negative DINT values.
		seq = ServiceSequenceUtils.addServiceSequence(fb.getService());
		setVariable(fb, "PV", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		setVariable(fb, "VAL_ZERO", "-50"); //$NON-NLS-1$ //$NON-NLS-2$
		setVariable(fb, "SLOW", "20"); //$NON-NLS-1$ //$NON-NLS-2$
		setVariable(fb, "FAST", "80"); //$NON-NLS-1$ //$NON-NLS-2$
		setVariable(fb, "VAL_FULL", "50"); //$NON-NLS-1$ //$NON-NLS-2$
		addTransaction(seq, new FBTransactionBuilder("INIT", "INITO", "OUT:=-50;qAtZero:=TRUE;qAtFull:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		// -50 - 80 undershoots VAL_ZERO(-50) and must be clamped back up to it.
		addTransaction(seq, new FBTransactionBuilder("DOWN_FAST", "CNF", "OUT:=-50;qAtZero:=TRUE;qAtFull:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addTransaction(seq, new FBTransactionBuilder("UP_FAST", "CNF", "OUT:=30;qAtZero:=FALSE;qAtFull:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		// 30 + 80 overshoots VAL_FULL(50) and must be clamped back down to it.
		addTransaction(seq, new FBTransactionBuilder("UP_FAST", "CNF", "OUT:=50;qAtZero:=FALSE;qAtFull:=TRUE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		runFBTest(fb, seq);
	}

}