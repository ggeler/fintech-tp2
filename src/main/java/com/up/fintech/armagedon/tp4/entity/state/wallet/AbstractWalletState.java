package com.up.fintech.armagedon.tp4.entity.state.wallet;

import com.up.fintech.armagedon.tp4.entity.Wallet;

public abstract class AbstractWalletState implements IWalletState {

	Wallet wallet;
	
	public AbstractWalletState(Wallet wallet) {
		this.wallet = wallet;
		this.wallet.setStatus(this.getState());
	}
}
