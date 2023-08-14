package gay.sylv.phonochat.network.s2c;

import gay.sylv.phonochat.Initializable;

/**
 * Registers receivers and handles client-side S2C-related packets.
 */
public final class S2CPackets implements Initializable {
	public static final S2CPackets INSTANCE = new S2CPackets();
	
	private S2CPackets() {}
}
