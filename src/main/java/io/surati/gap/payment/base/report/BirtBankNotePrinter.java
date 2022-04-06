package io.surati.gap.payment.base.report;

import io.surati.gap.admin.base.api.Company;
import io.surati.gap.admin.base.prop.PropCompany;
import io.surati.gap.commons.utils.amount.FrThousandSeparatorAmount;
import io.surati.gap.commons.utils.convert.FrShortDateFormat;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.PaymentMean;
import io.surati.gap.payment.base.api.PaymentMeanField;
import io.surati.gap.payment.base.api.PaymentMeanFieldType;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.Point;
import io.surati.gap.payment.base.api.Printer;
import io.surati.gap.payment.base.db.BankNotePrintDirection;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.core.internal.registry.RegistryProviderFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class BirtBankNotePrinter implements Printer {

	private static final int DPI = 300;

	private final BankNote note;
	private final PaymentMean mean;

	public BirtBankNotePrinter(final BankNote note, final PaymentMean mean) {
		this.note = note;
		this.mean = mean;
	}

	@Override
	@SuppressWarnings( "deprecation" )
	public void print(final OutputStream output) throws Exception {
		final Company company = new PropCompany();
		final int fontsize = Integer.parseInt(company.parameter("branknoteprintfontsize"));
		IReportEngine engine = null;
		try {
			final BankNotePrintDirection printdir;// = BankNotePrintDirection.valueOf(company.parameter("banknoteprintdirection"));
			if(note.book().meanType() == PaymentMeanType.CHQ) {
				printdir = BankNotePrintDirection.MIDDLE;
			} else {
				printdir = BankNotePrintDirection.LEFT;
			}
			final SessionHandle session = DesignEngine.newSession(null);
			final ReportDesignHandle design = session.createDesign();
			design.setDefaultUnits("mm");
			design.setImageDPI(DPI);
			final ElementFactory factory = design.getElementFactory();
			final DesignElementHandle element = factory.newSimpleMasterPage("Page Master");
			design.getMasterPages().add(element);
			element.setProperty(MasterPageHandle.TYPE_PROP, "custom");
			element.setProperty(MasterPageHandle.ORIENTATION_PROP, "landscape");
			element.setProperty(MasterPageHandle.WIDTH_PROP, MasterPageHandle.A4_HEIGHT);
			element.setProperty(MasterPageHandle.HEIGHT_PROP, MasterPageHandle.A4_WIDTH);
			element.setProperty("backgroundColor", "#FFFFFF");
			element.setProperty("headerHeight", 0.0);
			element.setProperty("footerHeight", 0.0);
			element.setProperty(MasterPageHandle.BOTTOM_MARGIN_PROP, 0.0);
			element.setProperty(MasterPageHandle.LEFT_MARGIN_PROP, 0.0);
			element.setProperty(MasterPageHandle.RIGHT_MARGIN_PROP, 0.0);
			element.setProperty(MasterPageHandle.TOP_MARGIN_PROP, 0.0);
			final int widthimg = pixelsFromCentimeters(mean.image().width()).intValue();
			final int heightimg = pixelsFromCentimeters(mean.image().height()).intValue();
			final int widthdoc = pixelsFromCentimeters(29.7).intValue();
			final int heigthdoc = pixelsFromCentimeters(21.0).intValue();
			final BufferedImage buffer = new BufferedImage(widthdoc, heigthdoc, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = buffer.createGraphics();
			g2d.setComposite(AlphaComposite.Src);
		    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(Color.black);
			g2d.setBackground(Color.white);
			g2d.clearRect(0, 0, widthdoc, heigthdoc);
			final Font font = new Font(company.parameter("banknoteprintfamilyfont"), Font.PLAIN, (int)Math.round(fontsize * DPI / 72.0));
			g2d.setFont(font);
			final Map<PaymentMeanField, String> fields = new HashMap<>();
			final String amountinletters = note.amountInLetters();
			final double lengthamountligne1inpt = pixelsFromCentimeters(mean.field(PaymentMeanFieldType.MONTANT_EN_LETTRES_LIGNE_1).width()).doubleValue();
			final StringBuilder amountinlettersligne1 = new StringBuilder();
			final StringBuilder amountinlettersligne2 = new StringBuilder();
			final StringBuilder amountinbuilding = new StringBuilder();
			for (String word : StringUtils.split(amountinletters, ' ')) {
				final String ligne = amountinbuilding.append(" ").append(word).toString().trim();
				final double sizeinpt = getStringWidth(g2d, font, ligne);
				if(sizeinpt <= lengthamountligne1inpt) {
					if(StringUtils.isBlank(amountinlettersligne1)) {
						amountinlettersligne1.append(word);
					} else {
						amountinlettersligne1.append(" ").append(word);
					}
				} else {
					if(StringUtils.isBlank(amountinlettersligne2)) {
						amountinlettersligne2.append(word);
					} else {
						amountinlettersligne2.append(" ").append(word);
					}
				}
			}
			for (PaymentMeanField field : mean.fields()) {
				if(!field.visible()) {
					continue;
				}
				switch(field.type()) {
				case BENEFICIAIRE:
					fields.put(field, note.beneficiary().name());
					break;
				case DATE_ECHEANCE:
					fields.put(field, new FrShortDateFormat().convert(note.dueDate()));
					break;
				case DATE_EDITION:
					fields.put(field, new FrShortDateFormat().convert(note.date()));
					break;
				case MENTION_SUPPLEMENTAIRE_1:
					fields.put(field, note.mention1());
					break;
				case MENTION_SUPPLEMENTAIRE_2:
					fields.put(field, note.mention2());
					break;
				case MONTANT_EN_CHIFFRES:
					fields.put(field, String.format("#%s#", new FrThousandSeparatorAmount(note.amount())));
					break;
				case MONTANT_EN_LETTRES_LIGNE_1:
					fields.put(field, amountinlettersligne1.toString().trim());
					break;
				case MONTANT_EN_LETTRES_LIGNE_2:
					fields.put(field, amountinlettersligne2.toString().trim());
					break;
				case VILLE_EDITION:
					fields.put(field, note.place());
					break;
				}
			}
			final int dx = widthdoc - widthimg;
			final int dy;
			switch (printdir) {
				case LEFT:
					dy = 0;
					break;
				case MIDDLE:
					dy = (int)(heigthdoc/2.0 - heightimg/2.0);
					break;
				case RIGHT:
					dy = heigthdoc - heightimg;
					break;
				default:
					throw new IllegalArgumentException("Direction d'impression de formule non prise en charge !");
			}
			for (Entry<PaymentMeanField, String> entry : fields.entrySet()) {
				final Point point = entry.getKey().location();
				g2d.drawString(entry.getValue(), pixelsFromCentimeters(point.x()) + dx, pixelsFromCentimeters(point.y()) + dy);
			}
			g2d.dispose();
			// end build		
			final ByteArrayOutputStream bas = new ByteArrayOutputStream();
			ImageIO.write(buffer, "jpg", bas);
			final EmbeddedImage image = StructureFactory.createEmbeddedImage();
			image.setType(DesignChoiceConstants.IMAGE_TYPE_IMAGE_JPEG);
			image.setData(bas.toByteArray());
			image.setName("note");
			design.addImage(image);
			final ImageHandle printablearea = factory.newImage(null);
			printablearea.setSource(DesignChoiceConstants.IMAGE_REF_TYPE_EMBED);
			printablearea.setImageName("note");
			printablearea.setFitToContainer(true);
			design.getBody().add(printablearea);
			final EngineConfig config = new EngineConfig();
			Platform.startup(config);
			final IReportEngineFactory fact = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			engine = fact.createReportEngine(config);
			final IReportRunnable runnable = engine.openReportDesign(design);
			final IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);
			final RenderOption pdfOptions = new PDFRenderOption();
			pdfOptions.setOutputFormat("PDF");
			pdfOptions.setOutputStream(output);
			pdfOptions.setOption(PDFRenderOption.PAGE_OVERFLOW, PDFRenderOption.FIT_TO_PAGE_SIZE);
			task.setRenderOption(pdfOptions);
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

	private static Float pixelsFromCentimeters(Double centimeters) {
		return (float)Math.round(centimeters * DPI / 2.54);
	}

	private static double getStringWidth(Graphics2D g, Font font, String string) {
        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(string, frc);
        return Math.round(bounds.getWidth());
    }
}
