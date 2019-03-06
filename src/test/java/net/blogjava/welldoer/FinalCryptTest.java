package net.blogjava.welldoer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

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
	public void testEncryptByteWithSameParameters() {
		assertThat( String.format( "%8s", Integer.toBinaryString( 0 & 0xFF ) ).replace( ' ', '0' ) ).isEqualTo( "00000000" );
		assertThat( String.format( "%8s", Integer.toBinaryString( 257 & 0xFF ) ).replace( ' ', '0' ) ).isEqualTo( "00000001" );
		assertThat( finalCrypt.encryptByte( ( byte ) 0, ( byte ) 0 ) ).isEqualTo( ( byte ) 0 );
	}

	@Test
	public void testEncryptByteWithTypicalParameters() {
		assertThat( String.format( "%8s", Integer.toBinaryString( 5 & 0xFF ) ).replace( ' ', '0' ) ).isEqualTo( "00000101" );
		assertThat( finalCrypt.encryptByte( ( byte ) 5, ( byte ) 3 ) ).isEqualTo( ( byte ) 6 );
		assertThat( finalCrypt.encryptByte( ( byte ) 6, ( byte ) 3 ) ).isEqualTo( ( byte ) 5 );
	}
	
	@Test
	public void testCryptOutputBuffer() {
		ByteBuffer dataBuffer = ByteBuffer.wrap( new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 } );
		assertThat( dataBuffer.position() ).isEqualTo( 0 );
		assertThat( dataBuffer.limit() ).isEqualTo( 13 );
		ByteBuffer cipherBuffer = ByteBuffer.allocate( 13 );
		cipherBuffer.put( new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 } );
		ByteBuffer outputBuffer = ByteBuffer.allocate( cipherBuffer.capacity() );
		outputBuffer.put( new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } );
		dataBuffer.flip();
		cipherBuffer.flip();
		assertThat( dataBuffer.position() ).isEqualTo( 0 );
		assertThat( dataBuffer.limit() ).isEqualTo( 0 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
		assertThat( cipherBuffer.position() ).isEqualTo( 0 );
		assertThat( cipherBuffer.limit() ).isEqualTo( 13 );
		assertThat( cipherBuffer.capacity() ).isEqualTo( 13 );
		assertThat( finalCrypt.cryptOutputBuffer( dataBuffer, cipherBuffer ) ).isEqualToComparingFieldByField( outputBuffer );
	}

	@Test
	public void testByteBuffer() {
		ByteBuffer dataBuffer = ByteBuffer.wrap( new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 } );
		assertThat( dataBuffer.position() ).isEqualTo( 0 );
		assertThat( dataBuffer.limit() ).isEqualTo( 13 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
		assertThat( dataBuffer.get( 1 ) ).isEqualTo( ( byte ) 1 );
		assertThat( dataBuffer.position() ).isEqualTo( 0 );
		assertThat( dataBuffer.limit() ).isEqualTo( 13 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
		assertThat( dataBuffer.get() ).isEqualTo( ( byte ) 0 );
		assertThat( dataBuffer.get() ).isEqualTo( ( byte ) 1 );
		assertThat( dataBuffer.position() ).isEqualTo( 2 );
		assertThat( dataBuffer.limit() ).isEqualTo( 13 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
		dataBuffer.flip();
		assertThat( dataBuffer.position() ).isEqualTo( 0 );
		assertThat( dataBuffer.limit() ).isEqualTo( 2 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
		assertThat( dataBuffer.get() ).isEqualTo( ( byte ) 0 );
		assertThat( dataBuffer.get() ).isEqualTo( ( byte ) 1 );
		assertThat( dataBuffer.position() ).isEqualTo( 2 );
		assertThat( dataBuffer.limit() ).isEqualTo( 2 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
		dataBuffer.flip();
		assertThat( dataBuffer.position() ).isEqualTo( 0 );
		assertThat( dataBuffer.limit() ).isEqualTo( 2 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
		assertThat( dataBuffer.get() ).isEqualTo( ( byte ) 0 );
		assertThat( dataBuffer.position() ).isEqualTo( 1 );
		assertThat( dataBuffer.limit() ).isEqualTo( 2 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
	}

	@Test( expected = BufferUnderflowException.class )
	public void testByteBufferWrapAndFlip() {
		ByteBuffer dataBuffer = ByteBuffer.wrap( new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 } );
		assertThat( dataBuffer.position() ).isEqualTo( 0 );
		assertThat( dataBuffer.limit() ).isEqualTo( 13 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
		dataBuffer.flip();
		assertThat( dataBuffer.get() ).isEqualTo( ( byte ) 0 );
	}
	
	@Test
	public void testNewByteBuffer() {
		ByteBuffer dataBuffer = ByteBuffer.allocate( 13 );
		assertThat( dataBuffer.position() ).isEqualTo( 0 );
		assertThat( dataBuffer.limit() ).isEqualTo( 13 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
		dataBuffer.put( new byte[] { 0, 1, 2 } );
		assertThat( dataBuffer.position() ).isEqualTo( 3 );
		dataBuffer.put( new byte[] { 3, 4, 5, 6 } );
		assertThat( dataBuffer.position() ).isEqualTo( 7 );
		assertThat( dataBuffer.limit() ).isEqualTo( 13 );
		dataBuffer.put( new byte[] { 7, 8, 9, 10 } );
		assertThat( dataBuffer.position() ).isEqualTo( 11 );
		assertThat( dataBuffer.limit() ).isEqualTo( 13 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
		dataBuffer.flip();
		assertThat( dataBuffer.get() ).isEqualTo( ( byte ) 0 );
		assertThat( dataBuffer.get() ).isEqualTo( ( byte ) 1 );
		assertThat( dataBuffer.get() ).isEqualTo( ( byte ) 2 );
		assertThat( dataBuffer.position() ).isEqualTo( 3 );
		assertThat( dataBuffer.limit() ).isEqualTo( 11 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
		dataBuffer.flip();
		dataBuffer.put( new byte[] { 100, 101 } );
		assertThat( dataBuffer.get() ).isEqualTo( ( byte ) 2 );
		assertThat( dataBuffer.position() ).isEqualTo( 3 );
		assertThat( dataBuffer.limit() ).isEqualTo( 3 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
	}

	@Test
	public void testNewByteBufferWrap() {
		ByteBuffer dataBuffer = ByteBuffer.wrap( new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 } );
		assertThat( dataBuffer.position() ).isEqualTo( 0 );
		assertThat( dataBuffer.limit() ).isEqualTo( 13 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
		dataBuffer.put( new byte[] { 100, 101, 102, 103, 104 } );
		assertThat( dataBuffer.position() ).isEqualTo( 5 );
		dataBuffer.flip();
		assertThat( dataBuffer.get() ).isEqualTo( ( byte ) 100 );
		assertThat( dataBuffer.get() ).isEqualTo( ( byte ) 101 );
		assertThat( dataBuffer.get() ).isEqualTo( ( byte ) 102 );
		assertThat( dataBuffer.position() ).isEqualTo( 3 );
		assertThat( dataBuffer.limit() ).isEqualTo( 5 );
		assertThat( dataBuffer.capacity() ).isEqualTo( 13 );
	}

}
