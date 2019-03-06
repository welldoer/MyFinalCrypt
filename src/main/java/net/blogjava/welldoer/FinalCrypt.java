package net.blogjava.welldoer;

import java.nio.ByteBuffer;

public class FinalCrypt {

	public byte encryptByte(byte data, byte cipher ) {
		int dim = 0;		// data unfiltered mask
		int dum = 0;		// data filtered mask
		int duim = 0;		// data unfiltered & filtered mask
		
		dum = data & ~cipher;
		dim = ~data & cipher;
		duim = dum + dim;
		
		return ( byte ) duim;
	}

	public ByteBuffer cryptOutputBuffer(ByteBuffer dataBuffer, ByteBuffer cipherBuffer) {
		ByteBuffer outputBuffer = ByteBuffer.allocate( dataBuffer.capacity() );
		for( byte dataByte : dataBuffer.array() ) {
			outputBuffer.put( encryptByte( dataByte, cipherBuffer.get() ) );
		}
		return outputBuffer;
	}

}
