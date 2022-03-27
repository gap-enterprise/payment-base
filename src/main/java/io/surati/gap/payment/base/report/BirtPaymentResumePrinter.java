package io.surati.gap.payment.base.report;

import io.surati.gap.commons.utils.convert.FrShortDateFormat;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.Printer;
import org.cactoos.list.ListOf;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.core.internal.registry.RegistryProviderFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class BirtPaymentResumePrinter implements Printer {

	private final BankNote note;

	public BirtPaymentResumePrinter(final BankNote note) {
		this.note = note;
	}

	@Override
	public void print(final OutputStream output) throws Exception {
		final Map<String, Object> context = new HashMap<>();
	    context.put("lines", new ListOf<>(note.orders().iterate()));
	    context.put(Locale.class.getSimpleName(), Locale.FRENCH);
	    IReportEngine engine = null;
		try {
			final EngineConfig config = new EngineConfig();
			Platform.startup(config);
			final IReportEngineFactory fact = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			engine = fact.createReportEngine(config);
			final InputStream reportResource = getClass().getClassLoader().getResourceAsStream("io/surati/gap/payment/report/payment_resume.rptdesign");
			final IReportRunnable runnable = engine.openReportDesign(reportResource);
			final IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);
			final RenderOption pdfOptions = new PDFRenderOption();
			pdfOptions.setOutputFormat("PDF");
			pdfOptions.setOutputStream(output);
			pdfOptions.setOption(PDFRenderOption.PAGE_OVERFLOW, PDFRenderOption.FIT_TO_PAGE_SIZE);
			task.setRenderOption(pdfOptions);
			task.setAppContext(context);
			task.setParameterValue("Beneficiary", note.beneficiary().name());
			task.setParameterValue("ReportNumber", note.internalReference());
			task.setParameterValue("BankNoteResume", String.format("%s                  %s                   N°%s                 date %s                  %s", note.book().meanType().toString(), note.book().account().bank().abbreviated(), note.issuerReference(), new FrShortDateFormat().convert(note.date()), note.amountInHuman()));
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
}
