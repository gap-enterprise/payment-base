package io.surati.gap.payment.db;

import io.surati.gap.admin.secure.ConstantSalt;
import io.surati.gap.admin.secure.EncryptedWordImpl;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

public class EncryptedWordImplTest {

	@Test
	public void generateValue() {
		MatcherAssert.assertThat(
			new EncryptedWordImpl(
				"admin",
				new ConstantSalt("PErZHEn0O6IOwUXh8IWRVC40aSol6S")
			).value(),
			new IsEqual<>("bgJf2vB2udmDA3yZ/8Sb14AUJ9YhLXevdTTL31DIncQ=")
		);
	}
}
