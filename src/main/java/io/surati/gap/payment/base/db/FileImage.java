package io.surati.gap.payment.base.db;

import io.surati.gap.commons.utils.convert.PixelToCentimeter;
import io.surati.gap.payment.base.api.Image;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.poi.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public final class FileImage implements Image {

	private final InputStream content;

	private final ImageInfo info;

	public FileImage(final Image origin) throws ImageReadException, IOException {
		this(origin.content());
	}

	public FileImage(final InputStream content) throws ImageReadException, IOException {
		this.content = content;
		this.info = Imaging.getImageInfo((IOUtils.toByteArray(content)));
	}

	@Override
	public int dpi() {
		return this.info.getPhysicalWidthDpi();
	}

	@Override
	public double width() {
		return new PixelToCentimeter(this.dpi(), this.widthpx()).value();
	}

	@Override
	public double height() {
		return new PixelToCentimeter(this.dpi(), this.heightpx()).value();
	}

	@Override
	public double widthpx() {
		return this.info.getWidth();
	}

	@Override
	public double heightpx() {
		return this.info.getHeight();
	}

	@Override
	public InputStream content() throws IOException {
		return content;
	}

	@Override
	public void update(InputStream content, String ext) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(double width, double height, int dpi) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String contentBase64() {
		throw new UnsupportedOperationException();
	}

}
