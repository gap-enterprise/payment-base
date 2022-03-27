package io.surati.gap.payment.base.db;

import io.surati.gap.admin.base.api.Company;
import io.surati.gap.admin.base.prop.PropCompany;
import io.surati.gap.commons.utils.amount.FrThousandSeparatorAmount;
import io.surati.gap.commons.utils.convert.FrShortDateFormat;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.PaymentMean;
import io.surati.gap.payment.base.api.PaymentMeanField;
import io.surati.gap.payment.base.api.PaymentMeanFieldType;
import io.surati.gap.payment.base.api.Point;
import io.surati.gap.payment.base.api.Printer;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class BirtBankNotePreviewPrint implements Printer {

	private static final int DPI = 300;

	private final BankNote note;
	private final PaymentMean mean;

	public BirtBankNotePreviewPrint(final BankNote note, final PaymentMean mean) {
		this.note = note;
		this.mean = mean;
	}

	@Override
	public void print(final OutputStream output) throws Exception {
		final Company company = new PropCompany();
		final int fontsize = Integer.parseInt(company.parameter("branknoteprintfontsize"));
		final Double widthcm = mean.image().width();
		final Double heightcm = mean.image().height();
		final int widthdoc = pixelsFromCentimeters(widthcm).intValue();
		final int heigthdoc = pixelsFromCentimeters(heightcm).intValue();
		final BufferedImage buffer = new BufferedImage(widthdoc, heigthdoc, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = buffer.createGraphics();
		g2d.setComposite(AlphaComposite.Src);
	    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.black);
		g2d.setBackground(Color.white);
		g2d.clearRect(0, 0, widthdoc, heigthdoc);
		final BufferedImage img = ImageIO.read(mean.image().content());
		g2d.drawImage(img, 0, 0, widthdoc, heigthdoc, null);
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
		for (Entry<PaymentMeanField, String> entry : fields.entrySet()) {
			final Point point = entry.getKey().location();
			g2d.drawString(entry.getValue(), pixelsFromCentimeters(point.x()), pixelsFromCentimeters(point.y()));
		}
		g2d.dispose();
		ImageIO.write(buffer, "jpg", output);
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
