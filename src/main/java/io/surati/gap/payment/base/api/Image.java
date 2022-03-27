package io.surati.gap.payment.base.api;

import java.io.IOException;
import java.io.InputStream;

public interface Image {
	
	int dpi();

	double width();
	
	double height();
	
	double widthpx();
	
	double heightpx();
	
	InputStream content() throws IOException;
	void update(InputStream content, String ext) throws IOException;
	void update(double width, double height, int dpi);
	
	String contentBase64();
}
