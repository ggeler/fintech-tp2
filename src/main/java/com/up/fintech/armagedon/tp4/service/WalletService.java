package com.up.fintech.armagedon.tp4.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.User;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.CvuException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletAlreadyExistsException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.repository.ICvuRepository;
import com.up.fintech.armagedon.tp4.repository.IUserRepository;
import com.up.fintech.armagedon.tp4.repository.IWalletRepository;

@Service
public class WalletService {

	private final IWalletRepository repository;
	private final IUserRepository userRepository;
	private final ICvuRepository cvuRepository;
	
	@Autowired
	public WalletService(IWalletRepository repository, IUserRepository user, ICvuRepository cvuRepository) {
		this.repository = repository;
		this.userRepository = user;
		this.cvuRepository = cvuRepository;
	}
	
	public Wallet getWalletByUserUuid(UUID uuid) throws UserNotFoundException, WalletNotFoundException  {
		var user = userRepository.getUserByUuid(uuid).orElseThrow(() -> new UserNotFoundException("User UUID not found "+uuid));
		var wallet = repository.getWalletByUser(user).orElseThrow(() -> new WalletNotFoundException("Wallet not found for userId "+user.getUuid()));
		return wallet;
	}
	
	public Wallet getWallet(UUID uuid) throws UserNotFoundException, WalletNotFoundException  {
		var wallet = repository.getWalletByWalletId(uuid).orElseThrow(() -> new WalletNotFoundException("Wallet not found for walletId "+uuid));
		return wallet;
	}
	
	public Wallet getWallet(String toCvu) throws CvuException {
		var cvu = cvuRepository.getCvuByCvu(toCvu).orElseThrow(() -> new CvuException("Cvu not found "+toCvu));
		return cvu.getWallet();
	}
	public Wallet addWallet(User user) throws WalletAlreadyExistsException {
		try {
			getWalletByUserUuid(user.getUuid());
			throw new WalletAlreadyExistsException(String.format("Wallet %s already exists",user.getUuid()));
		} catch (UserNotFoundException | WalletNotFoundException e) {
			var wallet = new Wallet();
			wallet.getUser().setEmail(user.getEmail());
			wallet.getUser().setUuid(user.getUuid());
			return repository.save(wallet);
		}
	}
	
	public Wallet save(Wallet wallet) {
		return repository.save(wallet);
	}
}
