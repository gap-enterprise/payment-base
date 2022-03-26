package io.surati.gap.payment.impl;

import io.surati.gap.payment.api.BankAccount;
import io.surati.gap.payment.api.PaymentBatch;
import io.surati.gap.payment.api.PaymentBatches;
import io.surati.gap.payment.api.PaymentMeanType;
import io.surati.gap.payment.api.PaymentOrderGroup;
import io.surati.gap.payment.api.PaymentOrderGroupsToExecute;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

public class PaymentBatchesImpl implements PaymentBatches {

	private final PaymentOrderGroupsToExecute groups;

	public PaymentBatchesImpl(final PaymentOrderGroupsToExecute groups) {
		this.groups = groups;
	}

	@Override
	public Iterable<PaymentBatch> iterate() {
		Long order = 0L;
		final Collection<PaymentBatch> items = new LinkedList<>();
		for (PaymentOrderGroup group : this.groups.iterate()) {
			final PaymentMeanType meantype = group.meanType();
			final BankAccount account = group.accountToUse();
			final PaymentBatch batch;
			final Optional<PaymentBatch> opt =
				items.stream().filter(
					p -> p.account().equals(account) && p.meanType() == meantype
				).findFirst();
			if(opt.isPresent()) {
				batch = opt.get();
			} else {
				order++;
				batch = new PaymentBatchImpl(order, account, meantype);
				items.add(batch);
			}
			batch.add(group);
		}
		return items;
	}

	@Override
	public Double totalAmount() {
		return this.groups.totalAmount();
	}

	@Override
	public PaymentBatch get(BankAccount account, PaymentMeanType meantype) {
		for (PaymentBatch batch : this.iterate()) {
			if(batch.account().equals(account) && batch.meanType() == meantype) {
				return batch;
			}
		}
		throw new IllegalArgumentException(String.format("Le lot (%s, %s) n'a pas été trouvé !", account.rib(), meantype.toString()));
	}

	@Override
	public PaymentBatch get(Long id) {
		for (PaymentBatch batch : this.iterate()) {
			if(batch.id().equals(id)) {
				return batch;
			}
		}
		throw new IllegalArgumentException(String.format("Le lot %s n'a pas été trouvé !", id));
	}

	@Override
	public PaymentBatch add(LocalDate date, BankAccount account, PaymentMeanType meantype) {
		
		return null;
	}
}
