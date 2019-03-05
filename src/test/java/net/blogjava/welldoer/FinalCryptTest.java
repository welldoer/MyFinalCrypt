package net.blogjava.welldoer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class FinalCryptTest {
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	private FinalCrypt finalCrypt;

	@Before
	public void setUp() throws Exception {
		finalCrypt = new FinalCrypt();
	}

	@Test
	public void test() {
		assertThat( true ).isEqualTo( true );
	}

}
