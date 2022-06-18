package com.up.fintech.armagedon.tp4.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.up.fintech.armagedon.tp4.entity.Transaction;
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
	
	@Transactional(label = "WalletTransaction", isolation = Isolation.DEFAULT, readOnly = false)
	public Wallet getWalletByUser(User user) throws UserNotFoundException, WalletNotFoundException  {
		var tmp = userRepository.getUserByUuidOrEmailOrCuit(user.getUuid(), user.getEmail(), user.getCuit()).orElseThrow(() -> new UserNotFoundException("User UUID not found "+user.getUuid()));
		var wallet = repository.getWalletByUser(tmp).orElseThrow(() -> new WalletNotFoundException("Wallet not found for userId "+user.getUuid()));
		wallet.setWalletState();
		return wallet;
	}
	
	@Transactional(label = "WalletTransaction", isolation = Isolation.DEFAULT)
	public Wallet getWallet(UUID uuid) throws UserNotFoundException, WalletNotFoundException  {
		var wallet = repository.getWalletByWalletId(uuid).orElseThrow(() -> new WalletNotFoundException("Wallet not found for walletId "+uuid));
		wallet.setWalletState();
		return wallet;
	}
	
	@Transactional(label = "WalletTransaction", isolation = Isolation.DEFAULT)
	public Wallet getWallet(String toCvu) throws CvuException {
		var cvu = cvuRepository.getCvuByCvu(toCvu).orElseThrow(() -> new CvuException("Cvu not found "+toCvu));
		cvu.getWallet().setWalletState();
		return cvu.getWallet();
	}
	
	@Transactional(label = "WalletTransaction", isolation = Isolation.REPEATABLE_READ)
	public Wallet addWallet(User user) throws WalletAlreadyExistsException {
		try {
			getWalletByUser(user);
			throw new WalletAlreadyExistsException(String.format("Duplicate Wallet for user %s email %s and cuit %s",user.getUuid(), user.getEmail(), user.getCuit()));
		} catch (UserNotFoundException | WalletNotFoundException e) {
			var wallet = new Wallet();
			wallet.getUser().setEmail(user.getEmail());
			wallet.getUser().setUuid(user.getUuid());
			wallet.getUser().setCuit(user.getCuit());
			return repository.save(wallet);
		}
	}
	
	@Transactional(label = "WalletTransaction", isolation = Isolation.REPEATABLE_READ)
	public Wallet addWallet(UUID walletId, User user) throws WalletAlreadyExistsException {
		try {
			getWalletByUser(user);
			throw new WalletAlreadyExistsException(String.format("Wallet %s already exists",user.getUuid()));
		} catch (UserNotFoundException | WalletNotFoundException e) {
			var wallet = new Wallet(walletId);
			wallet.getUser().setEmail(user.getEmail());
			wallet.getUser().setUuid(user.getUuid());
			wallet.getUser().setCuit(user.getCuit());
			return repository.save(wallet);
		}
	}
	/* https://stackoverflow.com/questions/56902108/spring-data-how-to-lock-a-row-in-a-transaction-and-make-other-transactions-wait
	 * Transaction Management to avoid dirty reads
	 */
	@Transactional(label = "WalletTransaction", isolation = Isolation.REPEATABLE_READ)
	public Transaction execute(UUID uuid, Transaction transaction) {
		 return getWallet(uuid).execute(transaction);
	}
	
	@Transactional(label = "WalletTransaction", isolation = Isolation.DEFAULT, readOnly =true)
	public Transaction preview(UUID uuid, Transaction transaction) {
		transaction.getState().preview();
		return getWallet(uuid).execute(transaction);
	}
	
	public Wallet save(Wallet wallet) {
		return repository.save(wallet);
	}
	
	@Transactional(label = "WalletTransaction", isolation = Isolation.REPEATABLE_READ)
	public Transaction execute(String uuid, Transaction transaction) {
		return getWallet(uuid).execute(transaction);
	}

	public Wallet getFeeWallet() {
		Wallet wallet;
		try {
			wallet = getWallet(new UUID(0,0));
		} catch (WalletNotFoundException e) {
			wallet = addWallet(new UUID(0,0), new User(new UUID(0,0),"feewallet@internal.com","0000"));
		}
		return wallet;
	}
}
