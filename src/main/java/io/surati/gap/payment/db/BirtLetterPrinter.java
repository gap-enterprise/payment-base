package io.surati.gap.payment.db;

import io.surati.gap.admin.prop.PropCompany;
import io.surati.gap.payment.api.Bank;
import io.surati.gap.payment.api.BankNote;
import io.surati.gap.payment.api.Printer;
import org.cactoos.iterable.IterableOf;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class BirtLetterPrinter implements Printer {

	private final Iterable<BankNote> notes;

	public BirtLetterPrinter(final BankNote note) {
		this(new IterableOf<>(note));
	}
	
	public BirtLetterPrinter(final Iterable<BankNote> notes) {
		this.notes = notes;
	}

	@Override
	public void print(final OutputStream output) throws Exception {
		final Map<String, Object> context = new HashMap<>();
	    context.put("lines", new ListOf<>(notes));
	    context.put(Locale.class.getSimpleName(), Locale.FRENCH);
	    IReportEngine engine = null;
		try {
			final EngineConfig config = new EngineConfig();
			Platform.startup(config);
			final IReportEngineFactory fact = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			engine = fact.createReportEngine(config);
			final InputStream reportResource = getClass().getClassLoader().getResourceAsStream("report/letter.rptdesign");
			final IReportRunnable runnable = engine.openReportDesign(reportResource);
			final IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);
			final RenderOption pdfOptions = new PDFRenderOption();
			pdfOptions.setOutputFormat("PDF");
			pdfOptions.setOutputStream(output);
			pdfOptions.setOption(PDFRenderOption.PAGE_OVERFLOW, PDFRenderOption.FIT_TO_PAGE_SIZE);
			task.setRenderOption(pdfOptions);
			task.setAppContext(context);
			task.setParameterValue("Today", String.format("%s, le %s", new PropCompany().city(), DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(LocalDate.now())));
			final BankNote note = this.notes.iterator().next();
			final Bank bank = note.book().account().bank();
			task.setParameterValue("Announce", String.format("A l'attention de %s %s", bank.representativeCivility(), bank.representative()));
			task.setParameterValue("Object", String.format("Objet : %s", note.book().meanType().toString()));
			task.setParameterValue("Account", String.format("Compte à débiter : %s - Compte N° %s", new PropCompany().abbreviated(), note.book().account().rib()));
			task.setParameterValue("BankRepresentative", String.format("Monsieur le Directeur Général de %s, %s", bank.abbreviated(), bank.headquarters()));
			task.setParameterValue("BankRepresentativeCivility", bank.representativeCivility());
			task.setParameterValue("Content", "Nous vous prions de bien vouloir régler à l'échéance, l'effet ci-dessous :");
			task.setParameterValue("Representative", new PropCompany().representative());
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
