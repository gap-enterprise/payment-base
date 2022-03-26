package io.surati.gap.payment.api;

import java.io.OutputStream;

public interface Printer {
	void print(OutputStream output) throws Exception;
}
