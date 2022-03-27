package io.surati.gap.payment.base.report;

import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.Printer;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.core.internal.registry.RegistryProviderFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class PaymentHistoryPrinter implements Printer {

	private final Iterable<BankNote> payments;
	
	public PaymentHistoryPrinter(final Iterable<BankNote> notes) {
		this.payments = notes;
	}
	
	@Override
	public void print(OutputStream output) throws Exception {
		final List<Row> rows = new LinkedList<>();
		int num = 0;
		for (BankNote paym : this.payments) {
			num += 1;
			rows.add(new Row(num, paym));
		}
		final Map<String, Object> context = new HashMap<>();
	    context.put("lines", rows);
	    context.put(Locale.class.getSimpleName(), Locale.FRENCH);
	    IReportEngine engine = null;
		try {
			final EngineConfig config = new EngineConfig();
			Platform.startup(config);
			final IReportEngineFactory fact = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			engine = fact.createReportEngine(config);
			final InputStream reportResource = getClass().getClassLoader().getResourceAsStream("io/surati/gap/payment/report/payment_export_story_format.rptdesign");
			final IReportRunnable runnable = engine.openReportDesign(reportResource);
			final IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);
			final RenderOption options = new EXCELRenderOption();
			options.setOutputFormat("XLSX");
			options.setOutputStream(output);
			task.setRenderOption(options);
			task.setAppContext(context);
			task.run();
			task.close();
		} finally {
			try
			{
				engine.destroy();
				Platform.shutdown();
				//Bugzilla 351052
				RegistryProviderFactory.releaseDefault();
			}catch ( Exception e1 ){
			    e1.printStackTrace();
			}
		}
	}

	public static class Row {
		
		private final int num;
		
		private final BankNote note;
		
		public Row(final int lnum, final BankNote nte) {
			this.num = lnum;
			this.note = nte;
		}
		
		public int num() {
			return this.num;
		}
		
		public LocalDate date() {
			return this.note.date();
		}
		
		public String beneficiary() {
			return this.note.beneficiary().abbreviated();
		}
		
		public String type() {
			final String type;
			if (this.note.book().meanType() == PaymentMeanType.CHEQUE) {
				type = "CHÈQUE";
			} else {
				type = "TRAITE";
			}
			return type;
		}
		
		public double amount() {
			return this.note.amount();
		}
		
		public String bank() {
			return this.note.book().account().bank().abbreviated();
		}
		
		public String noteNumber() {
			return this.note.issuerReference();
		}
	}
}
